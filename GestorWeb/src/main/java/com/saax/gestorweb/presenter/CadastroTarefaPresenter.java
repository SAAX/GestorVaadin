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

        
        Tarefa tarefa;
        // Cria uma nova tarefa com valores default
        tarefa = new Tarefa();
        tarefa.setEmpresa(usuarioLogado.getEmpresaAtiva());
        tarefa.setUsuarioInclusao(usuarioLogado);
        tarefa.setUsuarioSolicitante(usuarioLogado);
        tarefa.setDataHoraInclusao(LocalDateTime.now());

        view.ocultaPopUpEvolucaoStatusEAndamento();
        view.exibeTituloCadastro();

        init(tarefa);
    }

    /**
     * Abre o pop window do cadastro de tarefas para criação de uma sub tarefa
     *
     * @param tarefaPai
     */
    @Override
    public void criarSubTarefa(Tarefa tarefaPai) {
        Tarefa tarefa;
        // Cria uma nova tarefa com valores default
        tarefa = new Tarefa();
        tarefa.setEmpresa(usuarioLogado.getEmpresaAtiva());
        tarefa.setUsuarioInclusao(usuarioLogado);
        tarefa.setUsuarioSolicitante(usuarioLogado);
        tarefa.setTarefaPai(tarefaPai);

        view.ocultaPopUpEvolucaoStatusEAndamento();
        view.exibeTituloCadastro();

        init(tarefa);

    }

    /**
     * Abre o pop window do cadastro de tarefas para edição da tarefa informada
     *
     * @param tarefaToEdit
     */
    @Override
    public void editar(Tarefa tarefaToEdit) {

        view.exibeTituloEdicao();

        init(tarefaToEdit);
        
        
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
        presenter.criarSubTarefa(view.getTarefa());
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

            
//            // tratamento especial para o caso de cadastro de uma sub tarefa, para uma tarefa que
//            // ainda não está persistida
//            // neste caso o sistema não deve persistir a sub tarefa
//            // e sim aguardar a tarefa pai ser gravada
//            if (tarefa.getTarefaPai() != null && tarefa.getTarefaPai().getId() == null){
//                
//            } else {
//                tarefa = model.gravarTarefa(tarefa);
//                
//            }
             
            tarefa = model.gravarTarefa(tarefa);
 

            // notica (se existir) algum listener interessado em saber que o cadastro foi finalizado.
            if (callbackListener != null) {
                callbackListener.cadastroNovaTarefaConcluido(tarefa);
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
     * Tratamento para o evento disparado ao acionar o comando para imputar horas no controle de horas da tarefa
     */
    @Override
    public void imputarHorasClicked() {

        try {
            ApontamentoTarefa apontamentoTarefa = view.getApontamentoTarefa();
            apontamentoTarefa = model.configuraApontamento(apontamentoTarefa);
            view.getControleHorasContainer().addItem(apontamentoTarefa);
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
     * Tratamento para o evento disparado ao acionar o comando para imputar valores no controle de orçamento da tarefa
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

    /**
     * metodo chamado quando uma subtarefa foi criada 
     * @param tarefa
     */
    @Override
    public void cadastroNovaTarefaConcluido(Tarefa tarefa) {
        // monta os dados para adicionar na grid
        Object[] linha = new Object[]{
                
                tarefa.getGlobalID(),
                tarefa.getTitulo(),
                tarefa.getNome(),
                tarefa.getEmpresa().getNome()
                + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""),
                tarefa.getUsuarioSolicitante().getNome(),
                tarefa.getUsuarioResponsavel().getNome(),
                FormatterUtil.formatDate(tarefa.getDataInicio()),
                FormatterUtil.formatDate(tarefa.getDataFim()),
                buildPopUpEvolucaoStatusEAndamento(tarefa),
                tarefa.getProjecao().toString().charAt(0),
                new Button("E"),
                new Button("C")
                
            
        };
        view.getSubTarefasTable().addItem(linha, tarefa);
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
                String idTarefa = event.getPopupButton().getId();
                this.view.getSubTarefasTable().setValue(idTarefa);
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
        
        if (tarefa.getParticipantes()==null){
            tarefa.setParticipantes(new ArrayList<>());
        }
        
        tarefa.getParticipantes().add(participanteTarefa);
        
    }



    

}
