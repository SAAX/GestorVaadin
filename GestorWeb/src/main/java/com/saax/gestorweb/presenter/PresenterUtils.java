package com.saax.gestorweb.presenter;

import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.UsuarioModel;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.saax.gestorweb.view.PopUpStatusView;
import com.saax.gestorweb.view.TarefaView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Está classe é responsavel pela apresentação de itens comuns à vários outros
 * presenters do projeto. É uma biblioteca de métodos estáticos, cujo objetivo é
 * resolver as duplicações de código, por exemplo, para carregar combos ou fazer
 * configurações comuns de apresentação.
 *
 * @author rodrigo
 */
public class PresenterUtils {


    // Referencia ao recurso das mensagens:
    private static final ResourceBundle mensagensResource = (ResourceBundle.getBundle("ResourceBundles.Messages.Messages", new Locale("pt", "BR")));
    private static final GestorWebImagens imagensResource = new GestorWebImagens();

    private static PresenterUtils instance;

    public static PresenterUtils getInstance() {
        if (instance == null) {
            instance = new PresenterUtils();
        }
        return instance;
    }

    public static ResourceBundle getMensagensResource() {
        return mensagensResource;
    }

    public static GestorWebImagens getImagensResource() {
        return imagensResource;
    }

    public static Usuario getUsuarioLogado() {
        return (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
    }

    /**
     * Carrega o combo box de departamento informado, com todas os departamentos
     * ativos da empresas Ou desabilita se não houver departamentos ativos para
     * esta empresa
     *
     * @param departamentoCombo o combo a ser carregado
     * @param empresa a empresa
     */
    public static void carregaComboDepartamento(ComboBox departamentoCombo, Empresa empresa) {

        if (empresa != null) {

            List<Departamento> departamentoList = EmpresaModel.obterListaDepartamentosAtivos(empresa);

            if (departamentoList.isEmpty()) {

                departamentoCombo.removeAllItems();
                departamentoCombo.setEnabled(false);

            } else {

                for (Departamento depto : departamentoList) {
                    departamentoCombo.addItem(depto);
                    departamentoCombo.setItemCaption(depto, depto.getDepartamento());
                }
                departamentoCombo.setEnabled(true);
            }
        } else {

            departamentoCombo.removeAllItems();
            departamentoCombo.setEnabled(false);

        }

    }

    public static void carregaComboParticipante(ComboBox participante, Empresa empresa) {
        for (Usuario usuario : UsuarioModel.listarUsuariosEmpresa(empresa)) {
            participante.addItem(usuario);
            participante.setItemCaption(usuario, usuario.getNome());
        }
    }
    
    public static void carregaComboResponsavel(ComboBox combo, Empresa empresa) {
        for (Usuario usuario : UsuarioModel.listarUsuariosEmpresa(empresa)) {
            combo.addItem(usuario);
            combo.setItemCaption(usuario, usuario.getNome());
        }
    }


    
    public static void resetaComboDepartamento(ComboBox departamentoCombo) {

        departamentoCombo.select(null);
        departamentoCombo.removeAllItems();

    }

    /**
     * Carrega o combo box de centro de custo informado, com todas os centros de
     * custos ativos da empresa Ou desabilita se não houver centros de custo
     * ativos
     *
     * @param centroCustoCombo o combo a ser carregado
     * @param empresa a empresa
     */
    public static void carregaComboCentroCusto(ComboBox centroCustoCombo, Empresa empresa) {

        // Verify if the company is already set
        if (empresa != null) {

            // Retrieves the list of active departments for this company
            List<CentroCusto> centroCustoList = EmpresaModel.obterListaCentroCustosAtivos(empresa);

            if (centroCustoList.isEmpty()) {

                // if there is not any cost center: disable and empty the combo
                centroCustoCombo.removeAllItems();
                centroCustoCombo.setEnabled(false);

            } else {

                // loads the cost center list into the combo
                for (CentroCusto cc : centroCustoList) {
                    centroCustoCombo.addItem(cc);
                    centroCustoCombo.setItemCaption(cc, cc.getCentroCusto());
                }
                centroCustoCombo.setEnabled(true);
            }
        } else {

            // if there conmpany has not been setted: disable and empty the combo
            centroCustoCombo.removeAllItems();
            centroCustoCombo.setEnabled(false);

        }

    }

    public static void resetaComboCentroCusto(ComboBox centroCustoCombo) {

        centroCustoCombo.select(null);
        centroCustoCombo.removeAllItems();

    }

    
    
    /**
     * Carrega o combo de clientes com todos os clientes ativos de todas as
     * empresas (empresa pricipal + subs ) do usuario logado
     *
     * @param empresaClienteCombo o combo a ser carregado
     */
    public static void carregaComboEmpresaCliente(ComboBox empresaClienteCombo) {

        EmpresaModel empresaModel = new EmpresaModel();
        Usuario usuarioLogado = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        for (EmpresaCliente cliente : empresaModel.listarEmpresasCliente(usuarioLogado)) {
            empresaClienteCombo.addItem(cliente);
            empresaClienteCombo.setItemCaption(cliente, cliente.getNome());
        }

    }

    public static Button buildButtonEditarTarefa(TreeTable tabela, CallBackListener callbackListener, Tarefa subTarefa, String caption) {
        Button link = new Button(caption);
        link.setStyleName("quiet");
        link.addClickListener((Button.ClickEvent event) -> {
            tabela.setValue(subTarefa);
            TarefaPresenter presenter = new TarefaPresenter(new TarefaView());
            for (CallBackListener callBack : callbackListener.getCallbackListeneres()) {
                presenter.addCallBackListener(callBack);

            }
            presenter.addCallBackListener(callbackListener);
            presenter.editar(subTarefa);
        });
        return link;

    }

    /**
     * Configura a coluna da a tabela informadas de modo que ao
     * expandir/colapsar o tamanho seja aumentado ou reduzido em 20px
     *
     * @param treeTable
     * @param columnID
     */
    public static void configuraExpansaoColunaCodigo(TreeTable treeTable, Object columnID) {

        final int INCREMENTO = 20;

        treeTable.addExpandListener((Tree.ExpandEvent event) -> {
            Collection<?> subTarefas = treeTable.getChildren(event.getItemId());

            if (subTarefas != null && !subTarefas.isEmpty()) {
                treeTable.setColumnWidth(columnID, treeTable.getColumnWidth(columnID) + INCREMENTO);
            }
        });
        
        treeTable.addCollapseListener((Tree.CollapseEvent event) -> {
            Collection<?> subTarefas = treeTable.getChildren(event.getItemId());

            if (subTarefas != null && !subTarefas.isEmpty()) {
                treeTable.setColumnWidth(columnID, treeTable.getColumnWidth(columnID) - INCREMENTO);
            }

        });

    }

    public static void resetaSelecaoUsuarioResponsavel(ComboBox responsavel) {
        responsavel.select(null);
        responsavel.removeAllItems();
    }


    public static void resetaSelecaoParticipantes(ComboBox participanteCombo, BeanItemContainer<Participante> participanteContainer) {
        participanteCombo.select(null);
        participanteCombo.removeAllItems();
        participanteContainer.removeAllItems();
    }
    
    /**
     * 
     * Static method that builds a link button, that when clicked open a window
     * with the given task to edit
     *
     * @param callback the callback listener that must be called when the update
     * were done
     * @param table the view table (used to auto select the row)
     * @param task the task that will be openned
     * @param caption the button caption
     * @return
     */
    public static Button buildButtonOpenTask(CallBackListener callback, Table table, Tarefa task, String caption) {
        Button link = new Button(caption);
        link.setStyleName("quiet");
        link.addClickListener((Button.ClickEvent event) -> {
            table.setValue(task);
            TarefaPresenter presenter = new TarefaPresenter(new TarefaView());
            presenter.addCallBackListener(callback);
            presenter.editar(task);
        });
        return link;
    }    
    
    public static PopupButton buildPopUpStatusProgressTask(Table table, Tarefa task, PopUpStatusListener listener) {

        PopUpStatusView viewPopUP = new PopUpStatusView();

        PopUpStatusPresenter presenter = new PopUpStatusPresenter(viewPopUP);

        presenter.load(task, null, listener);

        // Event fired when the pop-up becomes visible:
        presenter.getStatusButton().addPopupVisibilityListener((PopupButton.PopupVisibilityEvent event) -> {
            if (event.isPopupVisible()) {
                Tarefa tarefaEditada = (Tarefa) event.getPopupButton().getData();
                table.setValue(tarefaEditada);
            }
        });

        // Correção: 
        // Só habilita o botão de status em parcelas já gravadas no banco.
        presenter.getStatusButton().setEnabled(task.getId() != null);

        return presenter.getStatusButton();
    }
    
}
