package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetaModel;
import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.PopUpEvolucaoStatusModel;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroMetaCallBackListener;
import com.saax.gestorweb.view.CadastroMetaView;
import com.saax.gestorweb.view.CadastroMetaViewListener;
import com.saax.gestorweb.view.TaskCreationCallBackListener;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.saax.gestorweb.view.PopUpEvolucaoStatusView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 *
 * @author Rodrigo
 */
public class CadastroMetaPresenter implements CadastroMetaViewListener, TaskCreationCallBackListener {

    // Todo presenter mantem acesso à view e ao model
    private final CadastroMetaView view;
    private final CadastroMetaModel model;

    // recursos de aplicação
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    
    // usuario logado
    private final Usuario usuarioLogado;
    private CadastroMetaCallBackListener callbackListener;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public CadastroMetaPresenter(CadastroMetaModel model,
            CadastroMetaView view) {

        this.model = model;
        this.view = view;

        usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");

        view.setListener(this);

    }

    /**
     * Cria uma nova meta na categoria informada
     *
     * @param categoria
     */
    public void criarNovaMeta(HierarquiaProjetoDetalhe categoria) {

        Meta meta = model.criarNovaMeta(categoria, usuarioLogado);

        // configura a categoria
        ComboBox combo = view.getHierarquiaCombo();
        combo.addItem(meta.getCategoria());
        combo.setItemCaption(meta.getCategoria(), meta.getCategoria().getCategoria());

        init(meta);

        view.getHierarquiaCombo().setEnabled(false);
        view.setCaption(mensagens.getString("CadastroMetaView.tituloBase") + categoria.getCategoria());
        
    }

    /**
     * Inicializa o formulário carregando a meta informada
     */
    private void init(Meta meta) {

        carregaComboEmpresa();
        carregaComboResponsavel();
        carregaComboEmpresaCliente();

        // Abre o formulário
        UI.getCurrent().addWindow(view);

        // liga (bind) o form com a meta
        view.setMeta(meta);

    }

    /**
     * Carrega o combo de seleção da empresa com todas as empresas relacionadas
     * ao usuário logado
     */
    private void carregaComboEmpresa() {
        ComboBox empresaCombo = view.getEmpresaCombo();

        EmpresaModel empresaModel = new EmpresaModel();

        List<Empresa> empresas = empresaModel.listarEmpresasParaSelecao(usuarioLogado);
        for (Empresa empresa : empresas) {

            empresaCombo.addItem(empresa);
            empresaCombo.setItemCaption(empresa, empresa.getNome());

        }

    }

    /**
     * Carrega o combo de departamentos com os departamentos ativos da empresa
     * logada
     */
    private void carregaComboDepartamento(Empresa empresa) {

        if (empresa != null) {
            ComboBox departamento = view.getDepartamentoCombo();
            for (Departamento depto : model.obterListaDepartamentosAtivos(empresa)) {
                departamento.addItem(depto);
                departamento.setItemCaption(depto, depto.getDepartamento());
            }
        } else {
            view.getDepartamentoCombo().setInputPrompt(mensagens.getString("CadastroMetaPresenter.departamentoCombo.avisoSelecionarEmpresa"));
        }

    }
    
    /**
     * Carrega o combo de departamentos com os departamentos ativos da empresa
     * logada
     */
    private void carregaComboCentroCusto(Empresa empresa) {

        if (empresa != null) {
            ComboBox centroCusto = view.getCentroCustoCombo();
            for (CentroCusto cc : model.obterListaCentroCustosAtivos(empresa)) {
                centroCusto.addItem(cc);
                centroCusto.setItemCaption(cc, cc.getCentroCusto());
            }
        } else {
            view.getCentroCustoCombo().setInputPrompt(mensagens.getString("CadastroMetaPresenter.departamentoCombo.avisoSelecionarEmpresa"));
        }

    }

    /**
     * Carrega o combo de responsáveis com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    private void carregaComboResponsavel() {
        ComboBox responsavel = view.getResponsavelCombo();
        for (Usuario usuario : model.listarUsuariosEmpresa()) {
            responsavel.addItem(usuario);
            responsavel.setItemCaption(usuario, usuario.getNome());

        }
    }

    /**
     * Carrega o combo de clientes com todos os clientes ativos de todas as
     * empresas (empresa pricipal + subs ) do usuario logado
     */
    private void carregaComboEmpresaCliente() {
        ComboBox empresaCliente = view.getEmpresaClienteCombo();
        for (EmpresaCliente cliente : model.listarEmpresasCliente(usuarioLogado)) {
            empresaCliente.addItem(cliente);
            empresaCliente.setItemCaption(cliente, cliente.getNome());
        }

    }

    /**
     * Trata o evento disparado ao soelecionar uma empresa e carrega a lista de departamentos
     * @param empresa 
     */
    @Override
    public void empresaSelecionada(Empresa empresa) {
        carregaComboDepartamento(empresa);
        carregaComboCentroCusto(empresa);
    }

    /**
     * Configura um listener para ser chamado quando o cadastro for concluido
     *
     * @param callback
     */
    @Override
    public void setCallBackListener(CadastroMetaCallBackListener callback) {
        this.callbackListener = callback;
    }

    @Override
    public void gravarButtonClicked() {
        Meta meta = (Meta) view.getMeta();

        if (meta.getUsuarioResponsavel() == null) {
            meta.setUsuarioResponsavel(meta.getUsuarioInclusao());
        }
        boolean novaMeta = meta.getId() == null;
        meta = model.gravarMeta(meta);

        // notica (se existir) algum listener interessado em saber que o cadastro foi finalizado.
        if (callbackListener != null) {
            if (novaMeta) {
                callbackListener.cadastroMetaConcluido(meta);
            } else {
                callbackListener.edicaoMetaConcluida(meta);
            }
        }
        view.close();
        Notification.show(meta.getCategoria().getCategoria() + mensagens.getString("CadastroMetaPresenter.mensagem.gravadoComSucesso"), Notification.Type.HUMANIZED_MESSAGE);
    }

    @Override
    public void cancelarButtonClicked() {
        UI.getCurrent().removeWindow(view);
    }

    @Override
    public void hierarquiaSelecionada(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Abre o pop window do cadastro de metas para edição da meta informada
     *
     * @param tarefaToEdit
     */
    @Override
    public void editar(Meta metaToEdit) {

        view.exibeTituloEdicao(metaToEdit);

        init(metaToEdit);
        
        view.setCaption(mensagens.getString("CadastroMetaView.tituloBase") + metaToEdit.getCategoria().getCategoria());

        //view.getParticipantesContainer().addAll(metaToEdit.getParticipantes());
        //view.getAnexoTarefaContainer().addAll(metaToEdit.getAnexos());
        //view.getControleHorasContainer().addAll(metaToEdit.getApontamentos());
        //view.getOrcamentoContainer().addAll(metaToEdit.getOrcamentos());

    }

    /**
     * Event thrown when the "addTask" button is clicked, indicating that the user wants a new task to the goal
     */
    @Override
    public void addTaskButtonClicked() {
            
        try {
            // commit to run validators
            view.getMetaFieldGroup().commit();
            
            // Creates the presenter that will handle the new task creation
            CadastroTarefaPresenter presenter = new CadastroTarefaPresenter(new CadastroTarefaModel(), new CadastroTarefaView());
            
            // Configure this as the object to be called when the task creation was done
            presenter.setCallBackListener(this);
            
            // Gets the tasks categories from the goal category
            List<HierarquiaProjetoDetalhe> tasksCategories = model.getFirstsTaskCategories(view.getMeta().getCategoria());
            
            // Tells the presenter which is gonna be the Task's category
            presenter.createTask(tasksCategories);
            
        } catch (FieldGroup.CommitException ex) {
            Notification.show(mensagens.getString("CadastroMetaPresenter.addTaskButtonClicked.commitException"), Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    /**
     * Event thrown when the "chat" button is clicked to start or continue a conversation between the stackholders of the goal
     */
    @Override
    public void chatButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Event thrown when the "chat" button is clicked to start or continue a conversation between the stackholders of the goal
     */
    @Override
    public void trendButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Handles the event thrown when the sub window is done with a Task creation 
     * @param createdTask 
     * @see TaskCreationCallBackListener
     */
    @Override
    public void taskCreationDone(Tarefa createdTask) {
        
        // monta os dados para adicionar na grid
        Object[] gridRow = new Object[]{
            CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), createdTask, createdTask.getGlobalID()),
            CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), createdTask, createdTask.getHierarquia().getCategoria()),
            CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), createdTask, createdTask.getNome()),
            createdTask.getEmpresa().getNome()
            + (createdTask.getFilialEmpresa() != null ? "/" + createdTask.getFilialEmpresa().getNome() : ""),
            createdTask.getUsuarioSolicitante().getNome(),
            createdTask.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(createdTask.getDataInicio()),
            FormatterUtil.formatDate(createdTask.getDataFim()),
            CadastroTarefaView.buildPopUpStatusProgressTask(view.getTarefasTable(), createdTask),
            createdTask.getProjecao().toString().charAt(0),
            new Button("E"),
            new Button("C")

        };
        view.getTarefasTable().addItem(gridRow, createdTask);

    }

    

    /**
     * @see TaskCreationCallBackListener
     */
    @Override
    public void taskUpdateDone(Tarefa updatedTask) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
