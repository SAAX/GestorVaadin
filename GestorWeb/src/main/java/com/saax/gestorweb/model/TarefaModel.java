package com.saax.gestorweb.model;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.Anexo;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.presenter.PresenterUtils;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorEntityManagerProvider;

import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Classe de modelo da tarefa <br><br>
 *
 * Esta classe é responsável pela aplicação das regras de negócios do elemento
 * "Tarefa" do sistema <br>
 * Todas as regras de negócio que envolvam outros elementos do sistema são
 * executadas com auxílio do model destes elementos <br>
 *
 * @author Rodrigo Moreira
 * @author Fernando Stávale
 */
public class TarefaModel {

    // Reference to the use of the messages:
    private static final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    // Controle da primeira tafarefa da lista de tarefas recorrentes, usado na gravação de tarefas
    private static final ThreadLocal<Tarefa> primeiraTarefaRecorrenteThreadLocal = new ThreadLocal<>();

    /**
     * Lista e retorna todos os clientes de todas as empresas de usuario logado
     *
     * @param usuarioLogado
     * @return a lista de empresas cliente
     */
    public static List<EmpresaCliente> listarEmpresasCliente(Usuario usuarioLogado) {
        return EmpresaModel.listarEmpresasCliente(usuarioLogado);
    }

    /**
     * Grava a tarefa na base de dados. <br>
     * No caso de tarefas recursivas percorre toda a lista de tarefas
     * recusivamente (da última para a primeira) persistindo só na primeira <br>
     * No caso das tarefas não recursivas: <br>
     * Quando for uma nova tarefa só persiste na tarefa pai ou na meta <br>
     * Quando for uma tarefa já existente (edição) grava individualmente <br>
     *
     * @param tarefa
     * @return a tarefa gravada com sucesso, ou null se não gravar
     * @throws IllegalStateException se a tarefa passada por parâmetro for nula
     * @throws RuntimeException se ocorrer algum problema na persistencia
     */
    public static Tarefa gravarTarefa(Tarefa tarefa) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        if (tarefa == null) {
            throw new IllegalArgumentException(mensagens.getString("TarefaModel.gravarTarefa.tarefanula"));
        }

        try {

            // abre a transação, caso ainda não esteja aberta
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            // TODO: Colocar projecao calculada
            tarefa.setProjecao(ProjecaoTarefa.NORMAL);

            // persiste as tarefas recorrentes:
            if (tarefa.getTipoRecorrencia() == TipoTarefa.RECORRENTE) {
                if (primeiraTarefaRecorrenteThreadLocal.get() == null) {
                    primeiraTarefaRecorrenteThreadLocal.set(tarefa); // save the first of the recurrency set
                }
                if (tarefa.getProximaTarefa() != null) {
                    gravarTarefa(tarefa.getProximaTarefa());
                }
            }

            if (tarefa.getId() == null) {
                em.persist(tarefa);
            } else {
                em.merge(tarefa);
            }

            boolean novaTarefa = tarefa.getId() == null;
            boolean tarefaPai = tarefa.getMeta() == null && tarefa.getTarefaPai() == null;

            if (novaTarefa && tarefaPai || !novaTarefa) {

                // verify if it is a recurrent set of tasks
                if (primeiraTarefaRecorrenteThreadLocal.get() != null) {
                    // if it is a recurrent set, only commit on the first 
                    if (tarefa == primeiraTarefaRecorrenteThreadLocal.get()) {
                        em.getTransaction().commit();
                    }
                } else {
                    em.getTransaction().commit();
                    primeiraTarefaRecorrenteThreadLocal.remove();
                }
            }

            moverAnexos(tarefa);

        }
        catch (RuntimeException ex) {
            // Caso a persistencia falhe, efetua rollback no banco
            if (GestorEntityManagerProvider.getEntityManager().getTransaction().isActive()) {
                GestorEntityManagerProvider.getEntityManager().getTransaction().rollback();
            }
            // propaga a exceção pra cima
            throw ex;
        }

        return tarefa;
    }

    private static void moverAnexos(Tarefa tarefa) {
        for (Anexo anexo : tarefa.getAnexos()) {
            if (anexo.getArquivoTemporario() != null) {
                moverAnexoTemporario(anexo);
            }
        }
        if (tarefa.getSubTarefas() != null && !tarefa.getSubTarefas().isEmpty()) {
            for (Tarefa sub : tarefa.getSubTarefas()) {
                moverAnexos(sub);
            }
        }
    }

    /**
     * Move os arquivos anexos temporários para a pasta oficial, dentro do CNPJ,
     * e do ID Tarefa. <br> <br>
     * Este método é chamado na gravação das tarefas para mover os anexos
     * enviados pelo usuário para pasta oficial de anexos <br>
     * Considerando a pasta base para gravação dos anexos parametrizada na
     * aplicação em : <br>
     * "anexos.relative.path"
     *
     * @param anexoTarefa anexo a ser movido
     * @throws IllegalArgumentException se o anexo enviado for nulo
     * @throws IllegalStateException se o parametro "anexos.relative.path" nao
     * estiver adequadamente configurado
     * @throws IllegalStateException se a empresa não possuir CNPJ (já que este
     * é usado como pasta)
     * @throws IllegalStateException se a pasta base nao existir ou ser somente
     * leitura
     * @throws IllegalStateException se o arquivo anexo nao existir
     * @throws RuntimeException se ocorrer algum erro na gravação do anexo
     */
    public static void moverAnexoTemporario(Anexo anexoTarefa) {

        if (anexoTarefa.getTarefa() == null) {
            throw new IllegalArgumentException(mensagens.getString("TarefaModel.moverAnexoTemporario.anexoNulo"));
        }

        Tarefa tarefa = anexoTarefa.getTarefa();

        String relativePath = ((GestorMDI) UI.getCurrent()).getApplication().getProperty("anexos.relative.path");

        if (relativePath == null) {
            throw new IllegalStateException(mensagens.getString("TarefaModel.moverAnexoTemporario.pastaBaseNaoEncontrada"));
        }

        String path = System.getProperty("user.home") + System.getProperty("file.separator") + relativePath;

        String cnpj = FormatterUtil.removeNonDigitChars(tarefa.getEmpresa().getCnpj());

        if (cnpj == null) {
            throw new IllegalStateException(mensagens.getString("TarefaModel.moverAnexoTemporario.empresaSemCNPJ"));
        }

        File folder = new File(path + "/" + cnpj + "/" + tarefa.getId());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (!folder.exists() || !folder.canWrite()) {
            throw new IllegalStateException(mensagens.getString("TarefaModel.moverAnexoTemporario.impossivelGravarPastaBase"));
        }

        File arquivoTMP = anexoTarefa.getArquivoTemporario();

        if (!arquivoTMP.exists() || !arquivoTMP.canWrite()) {
            throw new IllegalStateException(mensagens.getString("TarefaModel.moverAnexoTemporario.impossivelLerAnexoTemporario"));
        }

        File arquivoOficial = new File(folder, arquivoTMP.getName());

        boolean sucess = arquivoTMP.renameTo(arquivoOficial);

        if (!sucess) {
            throw new RuntimeException(mensagens.getString("TarefaModel.moverAnexoTemporario.falhaAoGravarAnexoTemporario"));
        }
        anexoTarefa.setArquivo(arquivoOficial);
        anexoTarefa.setArquivoTemporario(null);
        anexoTarefa.setCaminhoCompleto(arquivoOficial.getAbsolutePath());

        GestorEntityManagerProvider.getEntityManager().merge(anexoTarefa);

    }

    /**
     * Valida o arquivo que será enviado ao servidor. <br>
     * Regras: <br>
     * 1. O arquivo deve existir <br>
     * 2. O arquivo deve ter menos que 10 mb <br>
     * 3. O arquivo nao pode ser executavel por risco de virus <br>
     *
     *
     * @throws java.io.FileNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @param event
     */
    public static void validarArquivo(Upload.StartedEvent event) throws FileNotFoundException {

        if (event == null) {
            throw new IllegalArgumentException("Parâmetro inválido: Event");
        }

        if (!new File(event.getFilename()).exists()) {
            throw new FileNotFoundException("Arquivo não encontrado");
        }

        double tamanho = event.getContentLength();

        // tamanho em KB
        tamanho = tamanho / 1024;
        // tamanho em MB
        tamanho = tamanho / 1024;

        if (tamanho > 10D) {
            throw new IllegalArgumentException("Arquivo deve ter menos que 10mb");
        }

        String extensao = event.getFilename().substring(event.getFilename().lastIndexOf('.'));

        Set<String> extensoesProibidas = new HashSet<>();
        extensoesProibidas.add("exe");
        extensoesProibidas.add("bat");
        extensoesProibidas.add("sh");

        if (extensoesProibidas.contains(extensao)) {
            throw new SecurityException("Arquivo não permitido: " + extensao);
        }

    }

    private static ApontamentoTarefa obtemUltimoApontamento(Tarefa tarefa) {

        if (tarefa.getApontamentos().size() == 1) {
            return tarefa.getApontamentos().get(0);
        }
        ArrayList<ApontamentoTarefa> apontamentos = new ArrayList<>(tarefa.getApontamentos());
        Collections.sort(apontamentos, (ApontamentoTarefa o1, ApontamentoTarefa o2) -> o1.getDataHoraInclusao().compareTo(o2.getDataHoraInclusao()));
        int indiceUltimoApontamento = apontamentos.size() - 1;
        ApontamentoTarefa ultimoApontamentoTarefa = apontamentos.get(indiceUltimoApontamento);
        return ultimoApontamentoTarefa;

    }

    /**
     * Verifica se o apontamento é de DEBITO e neste caso, verifica se há saldo
     * suficiente para registrar este debito. <br>
     * São verificados os saldos em Horas e em Valores
     *
     * @param usuarioApontamento usuário logado que realizou o apontamento
     * @param usuarioSolicitante usuário solicitante da tarefa
     * @param apontamentoTarefa registro de apontamento que está sendo inserido
     * @param inputHoras quantidade de horas (Duração) que está sendo inputada
     */
    private static void validaSaldoSuficiente(Usuario usuarioApontamento, Usuario usuarioSolicitante, ApontamentoTarefa apontamentoTarefa, Duration inputHoras) {

        // se o usuário que criou o apontamento (logado) for o solicitante, o apontamento é de CREDITO e 
        // não há verificação a ser feita
        if (usuarioApontamento.equals(usuarioSolicitante)) {
            return;
        }

        // Para verificar se haverá saldo, é necessário obter o último registro de apontamento.
        // Porém de a lista de apontamentos estiver vazia significa que este é o primeiro apontamento.
        // E sendo o primeiro apontamento um de DEBTIO o sistema reporta um aviso:
        if (apontamentoTarefa.getTarefa().getApontamentos().isEmpty()) {
            throw new RuntimeException(mensagens.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoInsuficiente"));
        }

        // obtem o ultimo registro de apontamento inserido (registro anterior), para verificar o saldo disponivel
        ApontamentoTarefa ultimoApontamentoTarefa = obtemUltimoApontamento(apontamentoTarefa.getTarefa());

        // Inicialmente verifica se o saldo em Horas é suficiente para receber um débito de InputHoras
        if (ultimoApontamentoTarefa.getSaldoHoras().compareTo(inputHoras) < 0) {
            throw new RuntimeException(mensagens.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoInsuficiente"));
        }

        // Passada a verificação do saldo em horas, verifica o saldo em valores.
        // somente faz sentido esta verificação se houver custo de horas.
        if (apontamentoTarefa.getCustoHora() == null || apontamentoTarefa.getCustoHora().equals(BigDecimal.ZERO)) {
            return;
        }

        // obtem Saldo disponível no último apontamento
        BigDecimal saldoDisponivel = ultimoApontamentoTarefa.getSaldoValor();
        // calcula o custo (Valor) das horas sendo inputadas
        BigDecimal custoInputHoras = calculaCustoTotalHora(apontamentoTarefa.getCustoHora(), inputHoras);

        // realiza a comparação
        if (saldoDisponivel.compareTo(custoInputHoras) < 0) {
            throw new RuntimeException(mensagens.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoInsuficiente"));
        }

        // Fim da verificação de saldo em valor
    }

    private static OrcamentoTarefa obtemUltimoOrcamento(Tarefa tarefa) {

        if (tarefa.getOrcamentos().size() == 1) {
            return tarefa.getOrcamentos().get(0);
        }

        ArrayList<OrcamentoTarefa> orcamentos = new ArrayList<>(tarefa.getOrcamentos());
        Collections.sort(orcamentos, (OrcamentoTarefa o1, OrcamentoTarefa o2) -> o1.getDataHoraInclusao().compareTo(o2.getDataHoraInclusao()));
        int indiceUltimoApontamento = orcamentos.size() - 1;
        OrcamentoTarefa ultimoOrcamentoTarefa = orcamentos.get(indiceUltimoApontamento);

        return ultimoOrcamentoTarefa;

    }

    /**
     * Verifica se o apontamento é de DEBITO e neste caso, verifica se há saldo
     * suficiente para registrar este debito. <br>
     * São verificados os saldos em Horas e em Valores
     *
     * @param usuarioApontamento usuário logado que realizou o apontamento
     * @param usuarioSolicitante usuário solicitante da tarefa
     * @param apontamentoOrcamento registro de apontamento que está sendo
     * inserido
     * @param inputHoras quantidade de horas (Duração) que está sendo inputada
     */
    private static void validaSaldoSuficienteOrcamento(Usuario usuarioApontamento, Usuario usuarioSolicitante, OrcamentoTarefa orcamentoTarefa) {

        // se o usuário que criou o apontamento (logado) for o solicitante, o apontamento é de CREDITO e 
        // não há verificação a ser feita
        if (usuarioApontamento.equals(usuarioSolicitante)) {
            return;
        }

        // Para verificar se haverá saldo, é necessário obter o último registro de apontamento.
        // Porém de a lista de apontamentos estiver vazia significa que este é o primeiro apontamento.
        // E sendo o primeiro apontamento um de DEBTIO o sistema reporta um aviso:
        if (orcamentoTarefa.getTarefa().getOrcamentos().isEmpty()) {
            throw new RuntimeException(mensagens.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoInsuficiente"));
        }

        // obtem o ultimo registro de apontamento inserido (registro anterior), para verificar o saldo disponivel
        OrcamentoTarefa ultimoOrcamentoTarefa = obtemUltimoOrcamento(orcamentoTarefa.getTarefa());

        // obtem Saldo disponível no último apontamento
        BigDecimal saldoDisponivel = ultimoOrcamentoTarefa.getSaldo();

        if ((saldoDisponivel.compareTo(orcamentoTarefa.getDebito())) < 0) {
            throw new RuntimeException(mensagens.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoValorInsuficiente"));
        }

        // Fim da verificação de saldo em valor
    }

    /**
     * Configura os campos de credito e débido do apontamento de acordo com o
     * usuário logado.
     *
     * @param apontamentoTarefa
     * @return
     */
    public static ApontamentoTarefa configuraApontamento(ApontamentoTarefa apontamentoTarefa) {

        // Identifica os usuários relacionados ao apontamento e a tarefa
        Usuario usuarioApontamento = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
        Usuario usuarioResponsavel = apontamentoTarefa.getTarefa().getUsuarioResponsavel();
        Usuario usuarioSolicitante = apontamentoTarefa.getTarefa().getUsuarioSolicitante();

        Duration inputHoras = null;
        try {
            String[] input = apontamentoTarefa.getInputHoras().split(":");
            long hour = Long.parseLong(input[0]);
            long minutes = Long.parseLong(input[1]);
            inputHoras = Duration.ofHours(hour).plus(minutes, ChronoUnit.MINUTES);
        }
        catch (RuntimeException e) {
            throw new RuntimeException("Hora deve ser informada como HH:MM");
        }

        // se o usuário for o responsavel as horas inputadas são "debito"
        if (usuarioApontamento.equals(usuarioResponsavel)) {
            apontamentoTarefa.setDebitoHoras(inputHoras);
            apontamentoTarefa.setDebitoValor(calculaCustoTotalHora(apontamentoTarefa.getCustoHora(), inputHoras));

        } else if (usuarioApontamento.equals(usuarioSolicitante)) {
            // se o usuário for o solicitante as horas inputadas são "credito"
            apontamentoTarefa.setCreditoHoras(inputHoras);
            apontamentoTarefa.setCreditoValor(calculaCustoTotalHora(apontamentoTarefa.getCustoHora(), inputHoras));
        } else {
            throw new IllegalStateException("Usuário não deveria ter acesso aos apontamentos.");
        }

        // No caso de apontamentos de débito, verifica se existe saldo suficiente para lançar o débito
        validaSaldoSuficiente(usuarioApontamento, usuarioSolicitante, apontamentoTarefa, inputHoras);

        // Adiciona o apontamento na tarefa e ordena os apontamento por data/hora de inclusao
        apontamentoTarefa.getTarefa().addApontamento(apontamentoTarefa);

        recalculaSaldoApontamentoHoras(apontamentoTarefa.getTarefa().getApontamentos());

        return apontamentoTarefa;

    }

    public static void recalculaSaldoApontamentoHoras(List<ApontamentoTarefa> apontamentos) {

        Collections.sort(apontamentos, (ApontamentoTarefa o1, ApontamentoTarefa o2) -> o1.getDataHoraInclusao().compareTo(o2.getDataHoraInclusao()));

        // calcula o saldo:
        Duration saldoAnterior = Duration.of(0, ChronoUnit.HOURS);
        BigDecimal saldoAnteriorValor = BigDecimal.ZERO;
        for (ApontamentoTarefa apontamentoElement : apontamentos) {

            // ----------------------------------------------------------------
            // Calculo das horas
            // ----------------------------------------------------------------
            Duration credito = apontamentoElement.getCreditoHoras();
            Duration debito = apontamentoElement.getDebitoHoras();

            // Calcula saldo = saldoAnterior + Credito - Debito
            //      1. Saldo = Saldo Anterior
            Duration saldo = saldoAnterior;

            //      2. Saldo = Saldo + Credito
            if (credito != null) {
                saldo = saldo.plus(credito);
            }
            //      3. Saldo = Saldo - Debito
            if (debito != null) {
                saldo = saldo.minus(debito);
            }

            apontamentoElement.setSaldoHoras(saldo);

            // configura o saldo alterior a ser usado na proxima iteraçao
            saldoAnterior = saldo;

            // ----------------------------------------------------------------
            // Calculo dos valores
            // ----------------------------------------------------------------
            BigDecimal creditoValor = apontamentoElement.getCreditoValor();
            BigDecimal debitoValor = apontamentoElement.getDebitoValor();

            // Calcula saldo = saldoAnterior + Credito - Debito
            //      1. Saldo = Saldo Anterior
            BigDecimal saldoValor = saldoAnteriorValor.setScale(2, RoundingMode.HALF_UP);

            //      2. Saldo = Saldo + Credito
            if (creditoValor != null) {
                saldoValor = saldoValor.add(creditoValor);
            }
            //      3. Saldo = Saldo - Debito
            if (debitoValor != null) {
                saldoValor = saldoValor.subtract(debitoValor);
            }

            apontamentoElement.setSaldoValor(saldoValor);

            // configura o saldo alterior a ser usado na proxima iteraçao
            saldoAnteriorValor = saldoValor.setScale(2, RoundingMode.HALF_UP);
        }

    }

    /**
     * Calcula o custo total como: custo / hora * quantidade de horas
     *
     * @param custoHora
     * @param tempo
     * @return
     */
    private static BigDecimal calculaCustoTotalHora(BigDecimal custoHora, Duration tempo) {
        double tempoEmHoras = tempo.toMinutes() / 60D;
        if (custoHora == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(tempoEmHoras).multiply(custoHora);
    }

    /**
     * Configura um input de valor para o orçamento da tarefa de acordo com o
     * perfil do usuário
     *
     * @param orcamentoTarefa
     * @return
     */
    public static OrcamentoTarefa configuraInputOrcamento(OrcamentoTarefa orcamentoTarefa) {

        // Identifica os usuários relacionados ao apontamento e a tarefa
        Usuario usuarioApontamento = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
        Usuario usuarioResponsavel = orcamentoTarefa.getTarefa().getUsuarioResponsavel();
        Usuario usuarioSolicitante = orcamentoTarefa.getTarefa().getUsuarioSolicitante();

        BigDecimal inputValor = null;
        try {
            inputValor = new BigDecimal(orcamentoTarefa.getInputValor().replaceAll(",", "."));
            inputValor.setScale(2, RoundingMode.HALF_UP);
            inputValor.doubleValue();

            if (inputValor.doubleValue() <= 0) {
                throw new IllegalArgumentException("Valor negativo para orçamento.");
            }
        }

        catch (Exception e) {
            throw new RuntimeException("Valor inválido para Orçamento: " + orcamentoTarefa.getInputValor());
        }

        // se o usuário for o responsavel os valores inputadas são "debito"
        if (usuarioApontamento.equals(usuarioResponsavel)) {
            orcamentoTarefa.setDebito(inputValor);
        } else if (usuarioApontamento.equals(usuarioSolicitante)) {
            // se o usuário for o solicitante as horas inputadas são "credito"
            orcamentoTarefa.setCredito(inputValor);
        } else {
            throw new IllegalStateException("Usuário não deveria ter acesso ao orçamento.");
        }

        // No caso de apontamentos de débito, verifica se existe saldo suficiente para lançar o débito
        validaSaldoSuficienteOrcamento(usuarioApontamento, usuarioSolicitante, orcamentoTarefa);

        // Adiciona o valor de orçamento na tarefa e ordena os registros de orçamento por data/hora de inclusao
        orcamentoTarefa.getTarefa().addOrcamento(orcamentoTarefa);

        recalculaSaldoOrcamento(orcamentoTarefa.getTarefa().getOrcamentos());

        return orcamentoTarefa;
    }

    public static void recalculaSaldoOrcamento(List<OrcamentoTarefa> orcamentos) {

        Collections.sort(orcamentos, (OrcamentoTarefa o1, OrcamentoTarefa o2) -> o1.getDataHoraInclusao().compareTo(o2.getDataHoraInclusao()));

        // calcula o saldo:
        BigDecimal saldoAnterior = BigDecimal.ZERO;
        for (OrcamentoTarefa orcamentoElement : orcamentos) {

            BigDecimal credito = orcamentoElement.getCredito();
            BigDecimal debito = orcamentoElement.getDebito();

            // Calcula saldo = saldoAnterior + Credito - Debito
            //      1. Saldo = Saldo Anterior
            BigDecimal saldo = saldoAnterior.setScale(2, RoundingMode.HALF_UP);
            //      2. Saldo = Saldo + Credito
            if (credito != null) {
                saldo = saldo.add(credito);
            }
            //      3. Saldo = Saldo - Debito
            if (debito != null) {
                saldo = saldo.subtract(debito);
            }

            orcamentoElement.setSaldo(saldo);

            // configura o saldo alterior a ser usado na proxima iteraçao
            saldoAnterior = saldo.setScale(2, RoundingMode.HALF_UP);

        }

    }

    public static void removerApontamentoHoras(ApontamentoTarefa apontamentoTarefa) {

        // Adiciona o apontamento na tarefa e ordena os apontamento por data/hora de inclusao
        List<ApontamentoTarefa> apontamentos = apontamentoTarefa.getTarefa().getApontamentos();
        apontamentos.remove(apontamentoTarefa);

        recalculaSaldoApontamentoHoras(apontamentos);

    }

    public static void removerOrcamentoTarefa(OrcamentoTarefa orcamentoTarefa) {
        // Adiciona o apontamento na tarefa e ordena os apontamento por data/hora de inclusao
        List<OrcamentoTarefa> orcamentos = orcamentoTarefa.getTarefa().getOrcamentos();
        orcamentos.remove(orcamentoTarefa);

        recalculaSaldoOrcamento(orcamentos);
    }

    public static Participante criarParticipante(Usuario usuario, Tarefa tarefa) {

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        Participante participanteTarefa = new Participante();
        participanteTarefa.setTarefa(tarefa);
        participanteTarefa.setUsuarioInclusao(loggedUser);
        participanteTarefa.setUsuarioParticipante(usuario);
        participanteTarefa.setDataHoraInclusao(LocalDateTime.now());

        return participanteTarefa;
    }

    public static List<HierarquiaProjetoDetalhe> listaCategorias() {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        return em.createNamedQuery("HierarquiaProjetoDetalhe.findAll")
                .getResultList();
    }

    public static List<HierarquiaProjetoDetalhe> listaCategoriasNivelTarefa() {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        return em.createNamedQuery("HierarquiaProjetoDetalhe.findByNivel")
                .setParameter("nivel", 2)
                .getResultList();

    }

    public static HierarquiaProjetoDetalhe getCategoriaDefaultTarefa() {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        HierarquiaProjeto hierarquiaProjetoDefault = (HierarquiaProjeto) em.createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Meta")
                .getSingleResult();

        for (HierarquiaProjetoDetalhe categoria : hierarquiaProjetoDefault.getCategorias()) {
            if (categoria.getNivel() == 2) {
                return categoria;
            }
        }

        return null;
    }

    public static List<HierarquiaProjetoDetalhe> getProximasCategorias(Tarefa tarefaPai) {

        List<HierarquiaProjetoDetalhe> categoriasPossiveis = new ArrayList<>();

        int proximoNivel = tarefaPai.getHierarquia().getNivel() + 1;

        List<HierarquiaProjetoDetalhe> categorias = tarefaPai.getHierarquia().getHierarquia().getCategorias();
        for (HierarquiaProjetoDetalhe categoria : categorias) {
            if (categoria.getNivel() == proximoNivel) {
                categoriasPossiveis.add(categoria);
            }
        }

        if (categoriasPossiveis.isEmpty()) {
            throw new IllegalStateException("Não encontradas categorias para o próximo nível: " + tarefaPai.getHierarquia().getCategoria() + "/ Nivel: " + proximoNivel);
        }
        return categoriasPossiveis;
    }

    public static List<HierarquiaProjetoDetalhe> getProximasCategorias(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe) {

        List<HierarquiaProjetoDetalhe> categoriasPossiveis = new ArrayList<>();

        int proximoNivel = hierarquiaProjetoDetalhe.getNivel() + 1;

        List<HierarquiaProjetoDetalhe> categorias = hierarquiaProjetoDetalhe.getHierarquia().getCategorias();
        for (HierarquiaProjetoDetalhe categoria : categorias) {
            if (categoria.getNivel() == proximoNivel) {
                categoriasPossiveis.add(categoria);
            }
        }

        return categoriasPossiveis;
    }

    public static List<Departamento> obterListaDepartamentosAtivos(Empresa empresa) {
        return EmpresaModel.obterListaDepartamentosAtivos(empresa);
    }

    /**
     * Delega chamada ao model responsavel (EmpresaModel)
     *
     * @param empresa
     * @return
     */
    public static List<CentroCusto> obterListaCentroCustosAtivos(Empresa empresa) {
        return EmpresaModel.obterListaCentroCustosAtivos(empresa);
    }

    /**
     * Attachs (binds) a task under a target
     *
     * @param task
     * @param target
     */
    public static void attachTaskToTarget(Tarefa task, Meta target) {
        if (task != null && target != null) {
            task.setMeta(target);
            target.addTask(task);
        }
    }

    public static boolean verificaAcesso(Usuario loggedUser, Tarefa tarefaToEdit) {

        if (tarefaToEdit.getUsuarioRemocao() != null) {
            return false;
        }

        // verifica se o usuário ainda está ativo na empresa da tarefa
        // se ele tiver sido desativado, não pode ver a tarefa
        UsuarioEmpresa empresaUsuario = UsuarioModel.getRelacionamentoUsuarioEmpresa(loggedUser, tarefaToEdit.getEmpresa());
        if (empresaUsuario == null || !empresaUsuario.getAtivo()) {
            return false;
        }

        if (tarefaToEdit.getUsuarioInclusao().equals(loggedUser)) {
            return true;
        }
        if (tarefaToEdit.getUsuarioResponsavel().equals(loggedUser)) {
            return true;
        }
        if (tarefaToEdit.getUsuarioSolicitante().equals(loggedUser)) {
            return true;
        }

        List<Usuario> followers = new ArrayList<>();

        for (Participante pt : tarefaToEdit.getParticipantes()) {
            followers.add(pt.getUsuarioParticipante());
        }
        if (followers.contains(loggedUser)) {
            return true;
        }

        return false;

    }

    public static Tarefa refresh(Tarefa taskToEdit) {

        if (taskToEdit.getId() != null) {
            EntityManager em = GestorEntityManagerProvider.getEntityManager();
            Tarefa t = em.find(Tarefa.class, taskToEdit.getId());
            t.setSubTarefas(LixeiraModel.filtrarTarefasRemovidas(t.getSubTarefas()));
            return t;
        } else {
            return taskToEdit;
        }

    }

    public static Tarefa findByID(Integer taskID) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        Tarefa t = em.find(Tarefa.class, taskID);
        t.setSubTarefas(LixeiraModel.filtrarTarefasRemovidas(t.getSubTarefas()));
        return t;
    }

    /**
     * Creates an acessible link to the task
     *
     * @param task
     * @return the HTML created link
     */
    public static String buildTaskLink(Tarefa task) {
        StringBuilder html = new StringBuilder();

        html.append("<a href=\"");
        html.append(((GestorMDI) UI.getCurrent()).getLocation());
        html.append("?task=");
        html.append(task.getId());
        html.append("\">");
        html.append(task.getNome());
        html.append("</a>");

        return html.toString();

    }

    /**
     * Obtém as tarefas sob responsabilidade do usuário logado
     *
     * @param usuarioLogado
     * @return
     */
    public static List<Tarefa> listarTarefas(Usuario usuarioLogado) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Tarefa> tarefas = em.createNamedQuery("Tarefa.findByUsuarioResponsavelDashboard")
                .setParameter("usuarioResponsavel", usuarioLogado)
                //.setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getResultList();

        return filtrarTarefas(tarefas, usuarioLogado);

    }

    private static List<Tarefa> obtemTarefasPorUsuarioResponsavel(List<Usuario> usuariosResponsaveis) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<Tarefa> tarefasUsuarioResponsavel = new ArrayList<>();
        for (Usuario usuarioResponsavel : usuariosResponsaveis) {

            usuarioResponsavel = em.find(Usuario.class, usuarioResponsavel.getId());
            tarefasUsuarioResponsavel.addAll(usuarioResponsavel.getTarefasSobResponsabilidade());

        }
        return tarefasUsuarioResponsavel;
    }

    /**
     * Lista as tarefas que correspondam aos filtros informados
     *
     * @param tipoPesquisa
     * @param usuariosResponsaveis
     * @param usuariosSolicitantes
     * @param usuariosParticipantes
     * @param empresas
     * @param filiais
     * @param dataFim
     * @param projecoes
     * @param usuarioLogado
     * @return
     */
    public static List<Tarefa> pesquisarTarefas(DashboardPresenter.TipoPesquisa tipoPesquisa,
            List<Usuario> usuariosResponsaveis,
            List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas,
            List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes, Usuario usuarioLogado) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Tarefa> tarefasUsuarioResponsavel = obtemTarefasPorUsuarioResponsavel(usuariosResponsaveis);

        List<Tarefa> tarefasUsuarioSolicitante = new ArrayList<>();
        for (Usuario usuariosSolicitante : usuariosSolicitantes) {
            usuariosSolicitante = em.find(Usuario.class, usuariosSolicitante.getId());
            tarefasUsuarioSolicitante.addAll(usuariosSolicitante.getTarefasSolicitadas());
        }

        List<Tarefa> tarefasUsuariosParticipantes = new ArrayList<>();
        for (Usuario usuarioParticipante : usuariosParticipantes) {
            usuarioParticipante = em.find(Usuario.class, usuarioParticipante.getId());

            for (Participante participanteTarefa : usuarioParticipante.getTarefasParticipantes()) {
                if (participanteTarefa.getTarefa() != null) {
                    tarefasUsuariosParticipantes.add(participanteTarefa.getTarefa());
                }
            }
        }

        List<Tarefa> tarefasEmpresa = new ArrayList<>();
        for (Empresa empresa : empresas) {
            empresa = em.find(Empresa.class, empresa.getId());
            tarefasEmpresa.addAll(empresa.getTarefas());
        }

        List<Tarefa> tarefasFiliais = new ArrayList<>();
        for (FilialEmpresa filial : filiais) {
            filial = em.find(FilialEmpresa.class, filial.getId());
            tarefasFiliais.addAll(filial.getTarefas());
        }

        List<Tarefa> tarefasDataFim = new ArrayList<>();
        if (dataFim != null) {

            for (UsuarioEmpresa usuarioEmpresa : usuarioLogado.getEmpresas()) {
                if (usuarioEmpresa.getAtivo()) {
                    Empresa empresa = usuarioEmpresa.getEmpresa();
                    tarefasDataFim.addAll(em.createNamedQuery("Tarefa.findByDataFim")
                            .setParameter("dataFim", dataFim)
                            .setParameter("empresa", empresa)
                            .getResultList());

                    for (Empresa subEmpresa : empresa.getSubEmpresas()) {
                        tarefasDataFim.addAll(em.createNamedQuery("Tarefa.findByDataFim")
                                .setParameter("dataFim", dataFim)
                                .setParameter("empresa", subEmpresa)
                                .getResultList());
                    }

                }

            }
        }

        List<Tarefa> tarefasProjecao = new ArrayList<>();
        for (ProjecaoTarefa projecao : projecoes) {

            for (UsuarioEmpresa usuarioEmpresa : usuarioLogado.getEmpresas()) {
                if (usuarioEmpresa.getAtivo()) {
                    Empresa empresa = usuarioEmpresa.getEmpresa();
                    tarefasProjecao.addAll(
                            em.createNamedQuery("Tarefa.findByProjecao")
                            .setParameter("empresa", empresa)
                            .setParameter("projecao", projecao)
                            .getResultList());

                    for (Empresa subEmpresa : empresa.getSubEmpresas()) {
                        tarefasProjecao.addAll(
                                em.createNamedQuery("Tarefa.findByProjecao")
                                .setParameter("empresa", subEmpresa)
                                .setParameter("projecao", projecao)
                                .getResultList());
                    }

                }

            }
        }

        List<Tarefa> tarefas = new ArrayList<>();
        if (tipoPesquisa == DashboardPresenter.TipoPesquisa.INCLUSIVA_OU) {

            tarefas.addAll(tarefasUsuarioResponsavel);

            tarefas.addAll(tarefasUsuarioSolicitante);

            tarefas.addAll(tarefasUsuariosParticipantes);

            tarefas.addAll(tarefasEmpresa);

            tarefas.addAll(tarefasFiliais);

            tarefas.addAll(tarefasDataFim);

            tarefas.addAll(tarefasProjecao);

        } else if (tipoPesquisa == DashboardPresenter.TipoPesquisa.EXCLUSIVA_E) {
            for (UsuarioEmpresa usuarioEmpresa : usuarioLogado.getEmpresas()) {
                if (usuarioEmpresa.getAtivo()) {
                    Empresa empresa = usuarioEmpresa.getEmpresa();
                    tarefas.addAll(em.createNamedQuery("Tarefa.findAll")
                            .setParameter("empresa", empresa)
                            .getResultList());

                    for (Empresa subEmpresa : empresa.getSubEmpresas()) {
                        tarefas.addAll(em.createNamedQuery("Tarefa.findAll")
                                .setParameter("empresa", subEmpresa)
                                .getResultList());
                    }

                }
            }

            if (!tarefasUsuarioResponsavel.isEmpty()) {
                tarefas.retainAll(tarefasUsuarioResponsavel);
            }
            if (!tarefasUsuarioSolicitante.isEmpty()) {
                tarefas.retainAll(tarefasUsuarioSolicitante);
            }
            if (!tarefasUsuariosParticipantes.isEmpty()) {
                tarefas.retainAll(tarefasUsuariosParticipantes);
            }
            if (!tarefasEmpresa.isEmpty()) {
                tarefas.retainAll(tarefasEmpresa);
            }
            if (!tarefasFiliais.isEmpty()) {
                tarefas.retainAll(tarefasFiliais);
            }
            if (!tarefasDataFim.isEmpty()) {
                tarefas.retainAll(tarefasDataFim);
            }
            if (!tarefasProjecao.isEmpty()) {
                tarefas.retainAll(tarefasProjecao);
            }

        }

        return filtrarTarefas(tarefas, usuarioLogado);
    }

    private static List<Tarefa> filtrarTarefas(List<Tarefa> tarefas, Usuario usuarioLogado) {

        List<Tarefa> result = new ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            if (verificaAcesso(usuarioLogado, tarefa)) {
                result.add(tarefa);
            }

            tarefa.setSubTarefas(LixeiraModel.filtrarTarefasRemovidas(tarefa.getSubTarefas()));
        }

        return result;

    }

    /**
     * Obtém as tarefas solicitadas pelo usuário logado, ordenadas por data FIM
     *
     * @param loggedUser
     * @return
     */
    public static List<Tarefa> listarTarefasPrincipais(Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<Tarefa> tarefas = new ArrayList<>();

        for (UsuarioEmpresa usuarioEmpresa : loggedUser.getEmpresas()) {
            if (usuarioEmpresa.getAtivo()) {
                Empresa empresa = usuarioEmpresa.getEmpresa();
                Query q = em.createNamedQuery("Tarefa.findTarefasPrincipais")
                        .setParameter("empresa", empresa)
                        .setParameter("usuarioSolicitante", loggedUser);

                tarefas.addAll(q.getResultList());

                for (Empresa subEmpresa : empresa.getSubEmpresas()) {
                    q = em.createNamedQuery("Tarefa.findTarefasPrincipais")
                            .setParameter("empresa", subEmpresa)
                            .setParameter("usuarioSolicitante", loggedUser);
                    tarefas.addAll(q.getResultList());
                }

            }
        }

        return tarefas;

    }

    /**
     * Obtém as tarefas não aceitas pelo usuário logado, ordenadas por data FIM
     *
     * @param loggedUser
     * @return
     */
    public static List<Tarefa> listarTarefasAguardandoAceite(Usuario loggedUser) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<Tarefa> tarefas = new ArrayList<>();

        for (UsuarioEmpresa usuarioEmpresa : loggedUser.getEmpresas()) {
            if (usuarioEmpresa.getAtivo()) {
                Empresa empresa = usuarioEmpresa.getEmpresa();
                Query q = em.createNamedQuery("Tarefa.findTarefasAguardandoAceite")
                        .setParameter("empresa", empresa)
                        .setParameter("usuarioSolicitante", loggedUser)
                        .setParameter("status", StatusTarefa.NAO_INICIADA);

                tarefas.addAll(q.getResultList());

                for (Empresa subEmpresa : empresa.getSubEmpresas()) {
                    q = em.createNamedQuery("Tarefa.findTarefasAguardandoAceite")
                            .setParameter("empresa", subEmpresa)
                            .setParameter("usuarioSolicitante", loggedUser)
                            .setParameter("status", StatusTarefa.NAO_INICIADA);
                    tarefas.addAll(q.getResultList());
                }
            }
        }
        return tarefas;
    }

    /**
     * Aceita automaticamente uma tarefa cujo solicitante é o responsavel
     *
     * @param tarefa
     */
    public static void aceitarAutomaticamenteTarefaPropria(Tarefa tarefa) {

        if (tarefa != null) {

            if (tarefa.getUsuarioResponsavel() != null && tarefa.getUsuarioSolicitante() != null && tarefa.getUsuarioResponsavel().equals(tarefa.getUsuarioSolicitante())) {

                // if it is an own task auto accept the task (switches from NOT ACCEPTED to NOT STARTED)
                if (tarefa.getStatus() == StatusTarefa.NAO_ACEITA) {
                    tarefa.setStatus(StatusTarefa.NAO_INICIADA);
                }
            }

            if (tarefa.getSubTarefas() != null) {
                for (Tarefa sub : tarefa.getSubTarefas()) {
                    if (sub != null) {
                        aceitarAutomaticamenteTarefaPropria(sub);
                    }
                }
            }

        }
    }

    public static Tarefa criarNovaTarefaPeloTemplate(Tarefa template) {
        try {
            Tarefa novaTarefa = template.clone();
            novaTarefa.setTarefaPai(null);
            novaTarefa.setTemplate(false);

            return novaTarefa;
        }
        catch (CloneNotSupportedException ex) {
            Logger.getLogger(TarefaModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

    }

    /**
     * Remove uma tarefa
     *
     * @param tarefa
     * @param loggedUser
     */
    public static void removerTarefa(Tarefa tarefa, Usuario loggedUser) {

        tarefa.setUsuarioRemocao(loggedUser);
        tarefa.setDataHoraRemocao(LocalDateTime.now());
        tarefa.addHistorico(new HistoricoTarefa("Tarefa removida.", null, loggedUser, tarefa, LocalDateTime.now()));

        if (tarefa.getSubTarefas() != null) {
            for (Tarefa subtarefa : tarefa.getSubTarefas()) {
                removerTarefa(subtarefa, loggedUser);
            }
        }
    }

    public static void restaurarTarefa(Tarefa tarefa, Usuario usuarioLogado) {
        tarefa.setUsuarioRemocao(null);
        tarefa.setDataHoraRemocao(null);
        tarefa.addHistorico(new HistoricoTarefa("Tarefa restaurada.", null, usuarioLogado, tarefa, LocalDateTime.now()));

        if (tarefa.getSubTarefas() != null) {
            for (Tarefa subtarefa : tarefa.getSubTarefas()) {
                restaurarTarefa(subtarefa, usuarioLogado);
            }
        }
    }

    public static List<Tarefa> listarTarefasRemovidas(Usuario usuarioLogado) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        Query q = em.createNamedQuery("Tarefa.findTarefasRemovidas")
                .setParameter("usuarioRemocao", usuarioLogado);

        List<Tarefa> tarefas = new ArrayList<>();

        // filtro:
        for (Object tarefaObject : q.getResultList()) {
            Tarefa tarefa = (Tarefa) tarefaObject;

            // so apresenta as tarefas pai removidas
            if (tarefa.getTarefaPai() == null) {
                tarefas.add(tarefa);
            } else if (tarefa.getTarefaPai().getDataHoraRemocao() == null) {
                // ou filhas: se a pai nao tiver sido removida
                tarefas.add(tarefa);
            }

        }

        return tarefas;
    }

}
