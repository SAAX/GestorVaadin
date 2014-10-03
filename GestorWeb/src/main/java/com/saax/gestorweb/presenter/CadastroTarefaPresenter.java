package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.dashboard.PopUpEvolucaoStatusModel;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.dashboard.PopUpEvolucaoStatusPresenter;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.saax.gestorweb.view.CadastroTarefaViewListener;
import com.saax.gestorweb.view.dashboard.PopUpEvolucaoStatusView;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Presenter:
 * <p>
 * Listener de eventos da view do cadastro de tarefas
 *
 * @author rodrigo
 */
public class CadastroTarefaPresenter implements CadastroTarefaViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient CadastroTarefaView view;
    private final transient CadastroTarefaModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

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

    }

    /**
     * Abre o pop window do cadastro de tarefas Se informada uma tarefa abre em
     * modo edição, senão em modo criação
     *
     * @param tarefaToEdit
     */
    @Override
    public final void open(Tarefa tarefaToEdit) {

        Usuario usuarioLogado = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");
        Tarefa tarefa;

        // Verifica se deve criar uma nova tarefa ou editar uma existente
        if (tarefaToEdit == null) {

            // Cria uma nova tarefa com valores default
            tarefa = new Tarefa();
            tarefa.setEmpresa(usuarioLogado.getEmpresaAtiva());
            tarefa.setUsuarioInclusao(usuarioLogado);

            view.ocultaPopUpEvolucaoStatusEAndamento();
            view.exibeTituloCadastro();

        } else {

            view.exibeTituloEdicao();
            tarefa = tarefaToEdit;
        }

        // Carrega os combos de seleção
        carregaComboEmpresa();
        carregaComboTipoRecorrenciaTarefa();
        carregaComboPrioridade();
        carregaComboResponsavel();
        carregaComboParticipante();
        carregaComboEmpresaCliente();
        setPopUpEvolucaoStatusEAndamento(tarefa);

        // Configuras os beans de 1-N
        view.setApontamentoTarefa(new ApontamentoTarefa(tarefa, usuarioLogado));

        view.setAbaControleHorasVisible(tarefa.isApontamentoHoras());
        view.setAbaControleOrcamentoVisible(tarefa.isOrcamentoControlado());

        UI.getCurrent().addWindow(view);

        view.setTarefa(tarefa);

    }

    private void removerParticipante(Button.ClickEvent event) {

        String id = event.getButton().getId();

        // remove o item selecionado
        view.getParticipantesTable().removeItem(id);
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

    @Override
    public void avisoButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addSubButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            model.gravarTarefa(tarefa);
            view.close();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addParticipante() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void anexoAdicionado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imputarHorasClicked() {

        view.getControleHorasContainer().addItem(new ApontamentoTarefa());

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imputarOrcamentoClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Evento disparado quando o upload no anexo foi concluído
     *
     * @param event
     */
    @Override
    public void anexoAdicionado(Upload.FinishedEvent event) {

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

}
