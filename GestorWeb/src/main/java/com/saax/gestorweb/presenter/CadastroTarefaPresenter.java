package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.dashboard.PopUpEvolucaoStatusModel;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.dashboard.PopUpEvolucaoStatusPresenter;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroTarefaCallBackListener;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.saax.gestorweb.view.CadastroTarefaViewListener;
import com.saax.gestorweb.view.dashboard.PopUpEvolucaoStatusView;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Presenter:
 * <p>
 * Listener de eventos da view do cadastro de tarefas
 *
 * @author rodrigo
 */
public class CadastroTarefaPresenter implements CadastroTarefaViewListener, CadastroTarefaCallBackListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient CadastroTarefaView view;
    private final transient CadastroTarefaModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private CadastroTarefaCallBackListener callbackListener;
    private final Usuario usuarioLogado;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public CadastroTarefaPresenter(CadastroTarefaModel model,
            CadastroTarefaView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);

        usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");

    }

    /**
     * Abre o pop window do cadastro de tarefas para criação de uma nova tarefa
     */
    @Override
    public void criarNovaTarefa() {

        criarNovaTarefa(null);
    }


    private enum Modo { EDICAO, CRIACAO };
    private Modo modo = null;
    
    /**
     * Abre o pop window do cadastro de tarefas para criação de uma sub tarefa
     *
     * @param tarefaPai
     */
    @Override
    public void criarNovaTarefa(Tarefa tarefaPai) {
        
        modo = Modo.CRIACAO;
        
        Tarefa tarefa;
        // Cria uma nova tarefa com valores default
        tarefa = new Tarefa();
        tarefa.setEmpresa(usuarioLogado.getEmpresaAtiva());
        tarefa.setUsuarioInclusao(usuarioLogado);
        tarefa.setUsuarioSolicitante(usuarioLogado);
    
        // ajuste ate a projecao ser implementada
        tarefa.setProjecao(ProjecaoTarefa.NORMAL);
        
        if (tarefaPai!=null){
            view.apresentarCorFundoSubTarefa();
            tarefa.setTarefaPai(tarefaPai);
        }

        view.ocultaPopUpEvolucaoStatusEAndamento();
        view.exibeTituloCadastro(tarefaPai);

        init(tarefa);

    }

    /**
     * Abre o pop window do cadastro de tarefas para edição da tarefa informada
     *
     * @param tarefaToEdit
     */
    @Override
    public void editar(Tarefa tarefaToEdit) {

        modo = Modo.EDICAO;
        
        view.exibeTituloEdicao(tarefaToEdit.getTarefaPai());

        init(tarefaToEdit);
        
        view.getControleHorasContainer().addAll(tarefaToEdit.getApontamentos());
        view.getOrcamentoContainer().addAll(tarefaToEdit.getOrcamentos());
        
        for (Tarefa sub : tarefaToEdit.getSubTarefas()) {
            adicionarSubTarefa(sub);
        }

    }

    /**
     * inicializa a gui
     */
    private void init(Tarefa tarefa) {
        // Carrega os combos de seleção
        carregaComboEmpresa();
        carregaComboTipoRecorrenciaTarefa();
        carregaComboPrioridade();
        carregaComboResponsavel();
        carregaComboParticipante();
        carregaComboEmpresaCliente();
        carregaComboDepartamento();
        carregaComboCentroCusto();
        setPopUpEvolucaoStatusEAndamento(tarefa);

        // Configuras os beans de 1-N
        view.setApontamentoTarefa(new ApontamentoTarefa(tarefa, usuarioLogado));
        view.setOrcamentoTarefa(new OrcamentoTarefa(tarefa, usuarioLogado));

        view.setAbaControleHorasVisible(tarefa.isApontamentoHoras());
        view.setAbaControleOrcamentoVisible(tarefa.isOrcamentoControlado());

        UI.getCurrent().addWindow(view);

        view.setTarefa(tarefa);

    }

    /**
     * Carrega o combo de seleçao com os status possiveis para a tarefa
     */
    private void carregaComboTipoRecorrenciaTarefa() {

        ComboBox tipo = view.getTipoRecorrenciaCombo();
        for (TipoTarefa tipoTarefaValue : TipoTarefa.values()) {
            tipo.addItem(tipoTarefaValue);
            tipo.setItemCaption(tipoTarefaValue, tipoTarefaValue.getLocalizedString());
        }
    }

    /**
     * Carrega o combo de seleção da empresa com todas as empresas relacionadas
     * ao usuário logado
     */
    private void carregaComboEmpresa() {
        try {
            ComboBox empresaCombo = view.getEmpresaCombo();

            EmpresaModel empresaModel = new EmpresaModel();

            List<Empresa> empresas = empresaModel.listarEmpresasRelacionadas();
            for (Empresa empresa : empresas) {

                empresaCombo.addItem(empresa);
                empresaCombo.setItemCaption(empresa, empresa.getNome());

            }
        } catch (GestorException ex) {
            Logger.getLogger(CadastroTarefaPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carrega o combo de prioridades (alta, normal, baixa) com os valores da
     * enumeração
     */
    private void carregaComboPrioridade() {
        ComboBox prioridade = view.getPrioridadeCombo();
        for (PrioridadeTarefa prioridadeValue : PrioridadeTarefa.values()) {
            prioridade.addItem(prioridadeValue);
            prioridade.setItemCaption(prioridadeValue, prioridadeValue.getLocalizedString());
        }
    }

    /**
     * Carrega o combo de responsáveis com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    private void carregaComboResponsavel() {
        ComboBox responsavel = view.getUsuarioResponsavelCombo();
        try {
            for (Usuario usuario : model.listarUsuariosEmpresa()) {
                responsavel.addItem(usuario);
                responsavel.setItemCaption(usuario, usuario.getNome());

            }
        } catch (GestorException ex) {
            Logger.getLogger(CadastroTarefaPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carrega o combo de participante com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    private void carregaComboParticipante() {
        ComboBox participante = view.getParticipantesCombo();
        try {
            for (Usuario usuario : model.listarUsuariosEmpresa()) {
                participante.addItem(usuario);
                participante.setItemCaption(usuario, usuario.getNome());

            }
        } catch (GestorException ex) {
            Logger.getLogger(CadastroTarefaPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constrói o pop up de alteração de status e/ou andamento de tarefas neste
     * PopUP o usuario poderá alterar (evoluir ou regredir) um status de tarefa
     * ou indicar seu andamento.
     *
     * @return
     */
    private void setPopUpEvolucaoStatusEAndamento(Tarefa tarefa) {

        // comportmento e regras:
        PopUpEvolucaoStatusView viewPopUP = new PopUpEvolucaoStatusView();
        PopUpEvolucaoStatusModel modelPopUP = new PopUpEvolucaoStatusModel();

        PopUpEvolucaoStatusPresenter presenter = new PopUpEvolucaoStatusPresenter(viewPopUP, modelPopUP);

        presenter.load(tarefa, view.getStatusTarefaPopUpButton());

    }

    /**
     * Carrega o combo de clientes com todos os clientes ativos de todas as
     * empresas (empresa pricipal + subs ) do usuario logado
     */
    private void carregaComboEmpresaCliente() {
        ComboBox empresaCliente = view.getEmpresaClienteCombo();
        for (EmpresaCliente cliente : model.listarEmpresasCliente()) {
            empresaCliente.addItem(cliente);
            empresaCliente.setItemCaption(cliente, cliente.getNome());
        }

    }

    /**
     * Carrega o combo de departamentos
     */
    private void carregaComboDepartamento() {

        ComboBox departamento = view.getDepartamentoCombo();
        for (Departamento depto : model.listDepartamentos()) {
            departamento.addItem(depto);
            departamento.setItemCaption(depto, depto.getDepartamento());
        }

    }

    /**
     * Carrega o combo de centros de custo
     */
    private void carregaComboCentroCusto() {

        ComboBox centrocusto = view.getCentroCustoCombo();
        for (CentroCusto cc : model.listCentroCusto()) {
            centrocusto.addItem(cc);
            centrocusto.setItemCaption(cc, cc.getCentroCusto());
        }

    }

    @Override
    public void avisoButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * abre um novo presenter para o cadastro de uma sub tarefa desta tarefa
     */
    @Override
    public void addSubButtonClicked() {

        
        CadastroTarefaPresenter presenter = new CadastroTarefaPresenter(new CadastroTarefaModel(), new CadastroTarefaView());
        presenter.setCallBackListener(this);
        presenter.criarNovaTarefa(view.getTarefa());
    }

    @Override
    public void chatButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void projecaoButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gravarButtonClicked() {
        Tarefa tarefa = (Tarefa) view.getTarefa();
        try {
            
            if (tarefa.getUsuarioResponsavel()==null){
                tarefa.setUsuarioResponsavel(tarefa.getUsuarioInclusao());
            }
            
            if (tarefa.getTarefaPai() == null) {
                tarefa = model.gravarTarefa(tarefa);
            }

            // notica (se existir) algum listener interessado em saber que o cadastro foi finalizado.
            if (callbackListener != null) {
                if (modo == Modo.CRIACAO) {
                    callbackListener.cadastroNovaTarefaConcluido(tarefa);
                } else {
                    callbackListener.edicaoTarefaConcluida(tarefa);
                }
            }
            view.close();
            Notification.show("Tarefa criada!", Notification.Type.HUMANIZED_MESSAGE);
        } catch (RuntimeException e) {
            // caso ocorra alguma exceçao ao gravar, nao adianta mostrar o erro ao
            // usuario pois nao ha nada que o coitado possa fazer
            // ao inves disto mostrar um erro generico e loga
            Logger.getLogger(CadastroTarefaPresenter.class.getName()).log(Level.SEVERE, null, e);
            Notification.show(mensagens.getString("Gestor.mensagemErroGenerica"), Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public void cancelarButtonClicked() {
        UI.getCurrent().removeWindow(view);
    }

    @Override
    public void anexoAdicionado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Tratamento para o evento disparado ao acionar o comando para imputar
     * horas no controle de horas da tarefa
     */
    @Override
    public void imputarHorasClicked() {

        try {
            ApontamentoTarefa apontamentoTarefa = view.getApontamentoTarefa();
            apontamentoTarefa = model.configuraApontamento(apontamentoTarefa);
            view.getControleHorasContainer().addItem(apontamentoTarefa);
            // se o usuário informou um custo / hora, congela este custo para todos os futuros apontamentos
            if (apontamentoTarefa.getCustoHora() != null) {
                view.getTarefa().setCustoHoraApontamento(apontamentoTarefa.getCustoHora());
            }
            // criar um novo apontamento em branco para o usuario adicionar um novo:
            view.setApontamentoTarefa(new ApontamentoTarefa(view.getTarefa(), usuarioLogado));

        } catch (Exception ex) {
            Notification.show(ex.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE);
            Logger.getLogger(CadastroTarefaPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void removerApontamentoHoras(ApontamentoTarefa apontamentoTarefa) {

        view.getControleHorasTable().removeItem(apontamentoTarefa);
        model.removerApontamentoHoras(apontamentoTarefa);

    }

    /**
     * Tratamento para o evento disparado ao acionar o comando para imputar
     * valores no controle de orçamento da tarefa
     */
    @Override
    public void imputarOrcamentoClicked() {

        try {
            OrcamentoTarefa orcamentoTarefa = view.getOrcamentoTarefa();
            orcamentoTarefa = model.configuraInputOrcamento(orcamentoTarefa);
            view.getOrcamentoContainer().addItem(orcamentoTarefa);

            // criar um novo apontamento de orçamento em branco para o usuario adicionar um novo:
            view.setOrcamentoTarefa(new OrcamentoTarefa(view.getTarefa(), usuarioLogado));

        } catch (Exception ex) {
            Notification.show(ex.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE);
            Logger.getLogger(CadastroTarefaPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removerRegistroOrcamento(OrcamentoTarefa orcamentoTarefa) {
        view.getControleOrcamentoTable().removeItem(orcamentoTarefa);
        model.removerOrcamentoTarefa(orcamentoTarefa);
    }

    /**
     * Evento disparado quando o upload no anexo foi concluído
     *
     * @param event
     */
    @Override
    public void anexoAdicionado(Upload.FinishedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removerAnexo(AnexoTarefa anexoTarefa) {
        view.getAnexosAdicionadosTable().removeItem(anexoTarefa);
        Tarefa tarefa = view.getTarefa();
        tarefa.getAnexos().remove(anexoTarefa);
    }

    /**
     * Evento disparado quando o usuário seleciona o arquivo para upload Visa
     * validar o arquivo, aceitando ou regeitando-o.
     *
     * NOTA AO FERNANDAO: Este é o metodo do client. É ele que deve pegar a
     * exceção e tratar.
     *
     * @param event
     */
    @Override
    public void solicitacaoParaAdicionarAnexo(Upload.StartedEvent event) {

        try {
            // chama o metodo de validação de arquivo 
            // que pode lançar duas exceções não verificadas (filhas de RuntimeException )
            // ou uma verificada: FileNotFoundException
            model.validarArquivo(event);

        } catch (FileNotFoundException | RuntimeException ex) {
            // caso alguma exceção ocorra, loga:
            Logger.getLogger(CadastroTarefaPresenter.class.getName()).log(Level.SEVERE, null, ex);
            // e exibe ao usuario:
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }

    }

    @Override
    public void apontamentoHorasSwitched(Property.ValueChangeEvent event) {
        view.setAbaControleHorasVisible((boolean) event.getProperty().getValue());
    }

    @Override
    public void controleOrcamentoSwitched(Property.ValueChangeEvent event) {
        view.setAbaControleOrcamentoVisible((boolean) event.getProperty().getValue());
    }

    /**
     * Configura um listener para ser chamado quando o cadastro for concluido
     *
     * @param callback
     */
    @Override
    public void setCallBackListener(CadastroTarefaCallBackListener callback) {
        this.callbackListener = callback;
    }

    private Button buildButtonEditarTarefa(Tarefa subTarefa, String caption) {
        Button link = new Button(caption);
        link.setStyleName("link");
        CadastroTarefaCallBackListener callback = this;
        link.addClickListener((Button.ClickEvent event) -> {
            view.getSubTarefasTable().setValue(subTarefa);
            CadastroTarefaPresenter presenter = new CadastroTarefaPresenter(new CadastroTarefaModel(), new CadastroTarefaView());
            presenter.setCallBackListener(callback);
            presenter.editar(subTarefa);
        });
        return link;
    }
    
    private void adicionarSubTarefa(Tarefa sub){
        
        // monta os dados para adicionar na grid
        Object[] linha = new Object[]{
            buildButtonEditarTarefa(sub, sub.getGlobalID()),
            buildButtonEditarTarefa(sub, sub.getTitulo()),
            buildButtonEditarTarefa(sub, sub.getNome()),
            sub.getEmpresa().getNome()
            + (sub.getFilialEmpresa() != null ? "/" + sub.getFilialEmpresa().getNome() : ""),
            sub.getUsuarioSolicitante().getNome(),
            sub.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(sub.getDataInicio()),
            FormatterUtil.formatDate(sub.getDataFim()),
            buildPopUpEvolucaoStatusEAndamento(sub),
            sub.getProjecao().toString().charAt(0),
            new Button("E"),
            new Button("C")

        };
        view.getSubTarefasTable().addItem(linha, sub);
    }

    /**
     * metodo chamado quando uma subtarefa foi criada
     *
     * @param tarefa
     */
    @Override
    public void cadastroNovaTarefaConcluido(Tarefa tarefa) {

        adicionarSubTarefa(tarefa);
        
    }

    @Override
    public void edicaoTarefaConcluida(Tarefa tarefa) {
        
        
        Item it = view.getSubTarefasTable().getItem(tarefa);

        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaCod")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getGlobalID()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaTitulo")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getTitulo()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaNome")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getNome()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaEmpresaFilial")).setValue(tarefa.getEmpresa().getNome()
                + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaSolicitante")).setValue(tarefa.getUsuarioSolicitante().getNome());
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaResponsavel")).setValue(tarefa.getUsuarioResponsavel().getNome());
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaDataInicio")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaDataFim")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaStatus")).setValue(buildPopUpEvolucaoStatusEAndamento(tarefa));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaProjecao")).setValue(tarefa.getProjecao().toString().charAt(0));
        it.getItemProperty("[E]").setValue(new Button("E"));
        it.getItemProperty("[C]").setValue(new Button("C"));

    }

    /**
     * Constrói o pop up de alteração de status e/ou andamento de tarefas neste
     * PopUP o usuario poderá alterar (evoluir ou regredir) um status de tarefa
     * ou indicar seu andamento.
     *
     * @param tarefa
     * @return
     */
    private PopupButton buildPopUpEvolucaoStatusEAndamento(Tarefa tarefa) {

        // comportmento e regras:
        PopUpEvolucaoStatusView viewPopUP = new PopUpEvolucaoStatusView();
        PopUpEvolucaoStatusModel modelPopUP = new PopUpEvolucaoStatusModel();

        PopUpEvolucaoStatusPresenter presenter = new PopUpEvolucaoStatusPresenter(viewPopUP, modelPopUP);

        presenter.load(tarefa);

        // evento disparado quando o pop-up se torna visivel:
        // seleciona a linha correta na tabela
        presenter.getStatusButton().addPopupVisibilityListener((PopupButton.PopupVisibilityEvent event) -> {
            if (event.isPopupVisible()) {
                // selecionar a linha clicada:
                Tarefa tarefaEditada = (Tarefa) event.getPopupButton().getData();
                this.view.getSubTarefasTable().setValue(tarefaEditada);
            }
        });

        return presenter.getStatusButton();
    }

    @Override
    public void removerParticipante(ParticipanteTarefa participanteTarefa) {
        view.getParticipantesTable().removeItem(participanteTarefa);
        Tarefa tarefa = view.getTarefa();
        tarefa.getParticipantes().remove(participanteTarefa);
    }

    @Override
    public void adicionarParticipante(Usuario usuario) {
        ParticipanteTarefa participanteTarefa = model.criarParticipante(usuario, view.getTarefa());
        view.getParticipantesContainer().addBean(participanteTarefa);
        Tarefa tarefa = view.getTarefa();

        if (tarefa.getParticipantes() == null) {
            tarefa.setParticipantes(new ArrayList<>());
        }

        tarefa.getParticipantes().add(participanteTarefa);

    }

}
