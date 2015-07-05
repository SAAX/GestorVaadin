package com.saax.gestorweb.model;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.persistence.EntityManager;

/**
 * Classe de modelo da tarefa <br><br>
 * 
 * Esta classe é responsável pela aplicação das regras de negócios do elemento "Tarefa" do sistema <br>
 * Todas as regras de negócio que envolvam outros elementos do sistema são executadas com auxílio do model destes elementos <br>
 * @author Rodrigo Moreira
 * @author Fernando Stávale
 */
public class TarefaModel {

    // Classes do modelo acessórias utilizadas por este model
    private final UsuarioModel usuarioModel;
    private final CompanyModel empresaModel;

    // Reference to the use of the messages:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    // Controle da primeira tafarefa da lista de tarefas recorrentes, usado na gravação de tarefas
    private Tarefa primeiraTarefaRecorrente;

    /**
     * Cria um novo model para Tarefa e instancia todos os models acessórios necessários
     */
    public TarefaModel() {
        usuarioModel = new UsuarioModel();
        empresaModel = new CompanyModel();

    }

    /**
     * Lista todos os usuários ativos da mesma empresa do usuário logado <br>
     * 
     * @return a lista de usuários
     */
    public List<Usuario> listarUsuariosEmpresa() {
        return usuarioModel.listarUsuariosEmpresa();
    }

    /**
     * Lista e retorna todos os clientes de todas as empresas de usuario logado
     *
     * @param usuarioLogado
     * @return a lista de empresas cliente
     */
    public List<EmpresaCliente> listarEmpresasCliente(Usuario usuarioLogado) {
        return empresaModel.listarEmpresasCliente(usuarioLogado);
    }

    /**
     * Grava a tarefa na base de dados. <br>
     * No caso de tarefas recursivas percorre toda a lista de tarefas recusivamente (da última para a primeira) persistindo só na primeira <br>
     * No caso das tarefas não recursivas: <br>
     * Quando for uma nova tarefa só persiste na tarefa pai ou na meta <br>
     * Quando for uma tarefa já existente (edição) grava individualmente <br>
     *
     * @param tarefa
     * @return a tarefa gravada com sucesso, ou null se não gravar
     * @throws IllegalStateException se a tarefa passada por parâmetro for nula
     * @throws RuntimeException se ocorrer algum problema na persistencia 
     */
    public Tarefa gravarTarefa(Tarefa tarefa) {

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
                if (primeiraTarefaRecorrente == null) {
                    primeiraTarefaRecorrente = tarefa; // save the first of the recurrency set
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
                if (primeiraTarefaRecorrente != null) {
                    // if it is a recurrent set, only commit on the first 
                    if (tarefa == primeiraTarefaRecorrente) {
                        em.getTransaction().commit();
                    }
                } else {
                    em.getTransaction().commit();
                }
            }

            // mover anexos das pastas temporarias para as oficiais
            tarefa.getAnexos().stream().filter((anexo) -> (anexo.getArquivoTemporario() != null)).forEach((anexo) -> {
                moverAnexoTemporario(anexo);
            });

        } catch (RuntimeException ex) {
            // Caso a persistencia falhe, efetua rollback no banco
            if (GestorEntityManagerProvider.getEntityManager().getTransaction().isActive()) {
                GestorEntityManagerProvider.getEntityManager().getTransaction().rollback();
            }
            // propaga a exceção pra cima
            throw ex;
        }

        return tarefa;
    }

    /**
     * Move os arquivos anexos temporários para a pasta oficial, dentro do CNPJ,  e do ID Tarefa. <br> <br>
     * Este método é chamado na gravação das tarefas para mover os anexos enviados pelo usuário para pasta oficial de anexos <br>
     * Considerando a pasta base para gravação dos anexos parametrizada na aplicação em : <br>
     * "anexos.relative.path"
     * @param anexoTarefa anexo a ser movido
     * @throws IllegalArgumentException se o anexo enviado for nulo
     * @throws IllegalStateException se o parametro "anexos.relative.path" nao estiver adequadamente configurado 
     * @throws IllegalStateException se a empresa não possuir CNPJ (já que este é usado como pasta) 
     * @throws IllegalStateException se a pasta base nao existir ou ser somente leitura
     * @throws IllegalStateException se o arquivo anexo nao existir
     * @throws RuntimeException se ocorrer algum erro na gravação do anexo
     */
    public void moverAnexoTemporario(AnexoTarefa anexoTarefa) {

        if (anexoTarefa.getTarefa() == null) {
            throw new IllegalArgumentException(mensagens.getString("TarefaModel.moverAnexoTemporario.anexoNulo"));
        }

        Tarefa tarefa = anexoTarefa.getTarefa();

        String relativePath = ((GestorMDI) UI.getCurrent()).getApplication().getProperty("anexos.relative.path");

        if (relativePath == null) {
            throw new IllegalStateException(mensagens.getString("TarefaModel.moverAnexoTemporario.pastaBaseNaoEncontrada"));
        }

        String path = System.getProperty("user.dir") + "/" + relativePath;

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
    public void validarArquivo(Upload.StartedEvent event) throws FileNotFoundException {

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
    private void validaSaldoSuficiente(Usuario usuarioApontamento, Usuario usuarioSolicitante, ApontamentoTarefa apontamentoTarefa, Duration inputHoras) {

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
        List<ApontamentoTarefa> apontamentos = apontamentoTarefa.getTarefa().getApontamentos();
        int indiceUltimoApontamento = apontamentos.size() - 1;
        ApontamentoTarefa ultimoApontamentoTarefa = apontamentos.get(indiceUltimoApontamento);

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
    private void validaSaldoSuficienteOrcamento(Usuario usuarioApontamento, Usuario usuarioSolicitante, OrcamentoTarefa orcamentoTarefa) {

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
        List<OrcamentoTarefa> orcamentos = orcamentoTarefa.getTarefa().getOrcamentos();
        int indiceUltimoApontamento = orcamentos.size() - 1;
        OrcamentoTarefa ultimoApontamentoTarefa = orcamentos.get(indiceUltimoApontamento);

        // obtem Saldo disponível no último apontamento
        BigDecimal saldoDisponivel = ultimoApontamentoTarefa.getSaldo();

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
    public ApontamentoTarefa configuraApontamento(ApontamentoTarefa apontamentoTarefa) {

        // Identifica os usuários relacionados ao apontamento e a tarefa
        Usuario usuarioApontamento = (Usuario) GestorSession.getAttribute("loggedUser");
        Usuario usuarioResponsavel = apontamentoTarefa.getTarefa().getUsuarioResponsavel();
        Usuario usuarioSolicitante = apontamentoTarefa.getTarefa().getUsuarioSolicitante();

        Duration inputHoras = null;
        try {
            String[] input = apontamentoTarefa.getInputHoras().split(":");
            long hour = Long.parseLong(input[0]);
            long minutes = Long.parseLong(input[1]);
            inputHoras = Duration.ofHours(hour).plus(minutes, ChronoUnit.MINUTES);
        } catch (RuntimeException e) {
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

    public void recalculaSaldoApontamentoHoras(List<ApontamentoTarefa> apontamentos) {

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
            BigDecimal saldoValor = saldoAnteriorValor.setScale(2);

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
            saldoAnteriorValor = saldoValor.setScale(2);
        }

    }

    /**
     * Calcula o custo total como: custo / hora * quantidade de horas
     *
     * @param custoHora
     * @param tempo
     * @return
     */
    private BigDecimal calculaCustoTotalHora(BigDecimal custoHora, Duration tempo) {
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
    public OrcamentoTarefa configuraInputOrcamento(OrcamentoTarefa orcamentoTarefa) {

        // Identifica os usuários relacionados ao apontamento e a tarefa
        Usuario usuarioApontamento = (Usuario) GestorSession.getAttribute("loggedUser");
        Usuario usuarioResponsavel = orcamentoTarefa.getTarefa().getUsuarioResponsavel();
        Usuario usuarioSolicitante = orcamentoTarefa.getTarefa().getUsuarioSolicitante();

        BigDecimal inputValor = null;
        try {
            inputValor = new BigDecimal(orcamentoTarefa.getInputValor().replaceAll(",", "."));
            inputValor.setScale(2);
            inputValor.doubleValue();

        } catch (Exception e) {
            throw new RuntimeException("Não foi possível identificar o valor informado: " + orcamentoTarefa.getInputValor());
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

    public void recalculaSaldoOrcamento(List<OrcamentoTarefa> orcamentos) {

        Collections.sort(orcamentos, (OrcamentoTarefa o1, OrcamentoTarefa o2) -> o1.getDataHoraInclusao().compareTo(o2.getDataHoraInclusao()));

        // calcula o saldo:
        BigDecimal saldoAnterior = BigDecimal.ZERO;
        for (OrcamentoTarefa orcamentoElement : orcamentos) {

            BigDecimal credito = orcamentoElement.getCredito();
            BigDecimal debito = orcamentoElement.getDebito();

            // Calcula saldo = saldoAnterior + Credito - Debito
            //      1. Saldo = Saldo Anterior
            BigDecimal saldo = saldoAnterior.setScale(2);
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
            saldoAnterior = saldo.setScale(2);

        }

    }

    public void removerApontamentoHoras(ApontamentoTarefa apontamentoTarefa) {

        // Adiciona o apontamento na tarefa e ordena os apontamento por data/hora de inclusao
        List<ApontamentoTarefa> apontamentos = apontamentoTarefa.getTarefa().getApontamentos();
        apontamentos.remove(apontamentoTarefa);

        recalculaSaldoApontamentoHoras(apontamentos);

    }

    public void removerOrcamentoTarefa(OrcamentoTarefa orcamentoTarefa) {
        // Adiciona o apontamento na tarefa e ordena os apontamento por data/hora de inclusao
        List<OrcamentoTarefa> orcamentos = orcamentoTarefa.getTarefa().getOrcamentos();
        orcamentos.remove(orcamentoTarefa);

        recalculaSaldoOrcamento(orcamentos);
    }

    public ParticipanteTarefa criarParticipante(Usuario usuario, Tarefa tarefa) {

        Usuario loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");

        ParticipanteTarefa participanteTarefa = new ParticipanteTarefa();
        participanteTarefa.setTarefa(tarefa);
        participanteTarefa.setUsuarioInclusao(loggedUser);
        participanteTarefa.setUsuarioParticipante(usuario);
        participanteTarefa.setDataHoraInclusao(LocalDateTime.now());

        return participanteTarefa;
    }

    public List<HierarquiaProjetoDetalhe> listaCategorias() {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        return em.createNamedQuery("HierarquiaProjetoDetalhe.findAll")
                .getResultList();
    }

    public List<HierarquiaProjetoDetalhe> listaCategoriasNivelTarefa() {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        return em.createNamedQuery("HierarquiaProjetoDetalhe.findByNivel")
                .setParameter("nivel", 2)
                .getResultList();

    }

    public HierarquiaProjetoDetalhe getCategoriaDefaultTarefa() {
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

    public List<HierarquiaProjetoDetalhe> getProximasCategorias(Tarefa tarefaPai) {

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

    public List<HierarquiaProjetoDetalhe> getProximasCategorias(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe) {

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

    public List<Departamento> obterListaDepartamentosAtivos(Empresa empresa) {
        return empresaModel.obterListaDepartamentosAtivos(empresa);
    }

    /**
     * Delega chamada ao model responsavel (CompanyModel)
     *
     * @param empresa
     * @return
     */
    public List<CentroCusto> obterListaCentroCustosAtivos(Empresa empresa) {
        return empresaModel.obterListaCentroCustosAtivos(empresa);
    }

    /**
     * Attachs (binds) a task under a target
     *
     * @param task
     * @param target
     */
    public void attachTaskToTarget(Tarefa task, Meta target) {
        if (task != null && target != null) {
            task.setMeta(target);
            target.addTask(task);
        }
    }

    public boolean userHasAccessToTask(Usuario loggedUser, Tarefa tarefaToEdit) {

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

        for (ParticipanteTarefa pt : tarefaToEdit.getParticipantes()) {
            followers.add(pt.getUsuarioParticipante());
        }
        if (followers.contains(loggedUser)) {
            return true;
        }

        return false;

    }

    public Tarefa refresh(Tarefa taskToEdit) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        return em.find(Tarefa.class, taskToEdit.getId());

    }

    public Tarefa findByID(Integer taskID) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        return em.find(Tarefa.class, taskID);
    }

    
    /**
     * Creates an acessible link to the task
     *
     * @param task
     * @return the HTML created link
     */
    public String buildTaskLink(Tarefa task) {
        StringBuilder html = new StringBuilder();

        html.append("<a href=\"");
        html.append(UI.getCurrent().getPage().getLocation());
        html.append("?task=");
        html.append(task.getId());
        html.append("\">");
        html.append(task.getNome());
        html.append("</a>");

        return html.toString();

    }
    
}
