/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.vaadin.ui.Notification;
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
 *
 * @author rodrigo
 */
public class CadastroTarefaModel {

    // Classes do modelo acessórias acessadas por este model
    private final UsuarioModel usuarioModel;
    private final EmpresaModel empresaModel;

    // Reference to the use of the messages:
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();
    private Tarefa firstRecurrentTask;

    public CadastroTarefaModel() {
        usuarioModel = new UsuarioModel();
        empresaModel = new EmpresaModel();

    }

    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado
     *
     * @return
     */
    public List<Usuario> listarUsuariosEmpresa() {
        return usuarioModel.listarUsuariosEmpresa();
    }

    /**
     * Lista e retorna todos os clientes de todas as empresas de usuario logado
     *
     * @param loggedUser
     * @return
     */
    public List<EmpresaCliente> listarEmpresasCliente(Usuario loggedUser) {
        return empresaModel.listarEmpresasCliente(loggedUser);
    }

    /**
     * Save (persist) a Task
     *
     * @param task
     * @return the saved task if success, null otherwise
     *
     */
    public Tarefa saveTask(Tarefa task) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        if (task == null) {
            throw new IllegalArgumentException("Tarefa NULA para persistencia");
        }

        try {

            // so abre transação na gravacao da tarefa pai
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            // TODO: Colocar projecao calculada
            task.setProjecao(ProjecaoTarefa.NORMAL);

            // persiste as tarefas recorrentes:
            if (task.getTipoRecorrencia() == TipoTarefa.RECORRENTE) {
                if (firstRecurrentTask == null){
                    firstRecurrentTask = task; // save the first of the recurrency set
                }
                if (task.getProximaTarefa() != null){
                    saveTask(task.getProximaTarefa());
                }
            }

            if (task.getId() == null) {
                em.persist(task);
            } else {
                em.merge(task);
            }

            // so comita na gravação da tarefa pai e na 1a. ( no caso de uma recorrencia ) 
            if (task.getTarefaPai() == null) {
                // verify if it is a recurrent set of tasks
                if (firstRecurrentTask != null) {
                    // if it is a recurrent set, only commit on the first 
                    if (task == firstRecurrentTask){
                    em.getTransaction().commit();
                    }
                } else {
                    em.getTransaction().commit();
                }
            }

            // mover anexos das pastas temporarias para as oficiais
            task.getAnexos().stream().filter((anexo) -> (anexo.getArquivoTemporario() != null)).forEach((anexo) -> {
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

        return task;
    }

    /**
     * Move os arquivos anexos temporários para a pasta oficial, dentro do CNPJ
     * e do ID Tarefa.
     *
     * @param anexoTarefa anexo a ser movido
     */
    public void moverAnexoTemporario(AnexoTarefa anexoTarefa) {

        if (anexoTarefa.getTarefa() == null) {
            throw new IllegalArgumentException("Parametro inválido: AnexoTarefa");
        }

        Tarefa tarefa = anexoTarefa.getTarefa();

        String relativePath = ((GestorMDI) UI.getCurrent()).getApplication().getProperty("anexos.relative.path");

        if (relativePath == null) {
            throw new IllegalStateException("Não encontrada propriedade: 'anexos.relative.path'");
        }

        String path = System.getProperty("user.dir") + "/" + relativePath;

        String cnpj = FormatterUtil.removeNonDigitChars(tarefa.getEmpresa().getCnpj());

        if (cnpj == null) {
            throw new IllegalStateException("Empresa não possui CNPJ");
        }

        File folder = new File(path + "/" + cnpj + "/" + tarefa.getId());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (!folder.exists() || !folder.canWrite()) {
            throw new IllegalStateException("Não é possível gravar no destino: '" + folder.getAbsolutePath() + "'.");
        }

        File arquivoTMP = anexoTarefa.getArquivoTemporario();

        if (!arquivoTMP.exists() || !arquivoTMP.canWrite()) {
            throw new IllegalStateException("Não é possível ler arquivo de origem: '" + arquivoTMP.getAbsolutePath() + "'.");
        }

        File arquivoOficial = new File(folder, arquivoTMP.getName());

        boolean sucess = arquivoTMP.renameTo(arquivoOficial);

        if (!sucess) {
            throw new RuntimeException("Falha ao gravar anexo: " + arquivoOficial.getAbsolutePath());
        }
        anexoTarefa.setArquivo(arquivoOficial);
        anexoTarefa.setArquivoTemporario(null);
        anexoTarefa.setCaminhoCompleto(arquivoOficial.getAbsolutePath());

        GestorEntityManagerProvider.getEntityManager().merge(anexoTarefa);

    }

    /**
     * Valida o arquivo que será enviado ao servidor. <br>
     * Regras: 1. O arquivo deve existir 2. O arquivo deve ter menos que 10 mb
     * 3. O arquivo nao pode ser executavel por risco de virus
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
            throw new RuntimeException(messages.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoInsuficiente"));
        }

        // obtem o ultimo registro de apontamento inserido (registro anterior), para verificar o saldo disponivel
        List<ApontamentoTarefa> apontamentos = apontamentoTarefa.getTarefa().getApontamentos();
        int indiceUltimoApontamento = apontamentos.size() - 1;
        ApontamentoTarefa ultimoApontamentoTarefa = apontamentos.get(indiceUltimoApontamento);

        // Inicialmente verifica se o saldo em Horas é suficiente para receber um débito de InputHoras
        if (ultimoApontamentoTarefa.getSaldoHoras().compareTo(inputHoras) < 0) {
            throw new RuntimeException(messages.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoInsuficiente"));
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
            throw new RuntimeException(messages.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoInsuficiente"));
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
            throw new RuntimeException(messages.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoInsuficiente"));
        }

        // obtem o ultimo registro de apontamento inserido (registro anterior), para verificar o saldo disponivel
        List<OrcamentoTarefa> orcamentos = orcamentoTarefa.getTarefa().getOrcamentos();
        int indiceUltimoApontamento = orcamentos.size() - 1;
        OrcamentoTarefa ultimoApontamentoTarefa = orcamentos.get(indiceUltimoApontamento);

        // obtem Saldo disponível no último apontamento
        BigDecimal saldoDisponivel = ultimoApontamentoTarefa.getSaldo();

        if ((saldoDisponivel.compareTo(orcamentoTarefa.getDebito())) < 0) {
            throw new RuntimeException(messages.getString("CadastroTarefaModel.validaSaldoSuficiente.erroSaldoValorInsuficiente"));
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
            inputValor = new BigDecimal(orcamentoTarefa.getInputValor());
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
     * Delega chamada ao model responsavel (EmpresaModel)
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

}
