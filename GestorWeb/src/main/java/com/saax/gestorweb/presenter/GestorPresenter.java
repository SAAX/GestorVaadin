package com.saax.gestorweb.presenter;

import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.GestorModel;
import com.saax.gestorweb.model.UsuarioModel;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
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
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Está classe é responsavel pela apresentação de itens comuns à vários outros
 * presenters do projeto. <br>
 * É uma biblioteca de métodos estáticos, cujo objetivo é
 * resolver as duplicações de código, por exemplo, para carregar combos ou fazer
 * configurações comuns de apresentação.
 *
 * @author rodrigo
 */
public class GestorPresenter {

    private static final ResourceBundle MENSAGENS = (ResourceBundle.getBundle("ResourceBundles.Messages.Messages", new Locale("pt", "BR")));

    public static ResourceBundle getMENSAGENS() {
        return MENSAGENS;
    }

    public static Usuario getUsuarioLogado() {
        return (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
    }

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

    public static void carregaComboUsuarios(ComboBox comboResponsavel, Empresa empresa) {

        if (empresa != null) {

            List<Usuario> usuariosList = UsuarioModel.listarUsuariosEmpresa(empresa);

            if (usuariosList.isEmpty()) {

                comboResponsavel.removeAllItems();
                comboResponsavel.setEnabled(false);

            } else {
                for (Usuario usuario : usuariosList) {
                    comboResponsavel.addItem(usuario);
                    comboResponsavel.setItemCaption(usuario, usuario.getNome());
                }

                comboResponsavel.setEnabled(true);
            }
        } else {

            comboResponsavel.removeAllItems();
            comboResponsavel.setEnabled(false);

        }

    }

    public static void carregaComboCategoria(ComboBox categoriaCombo, Empresa empresa, int nivel) {

        if (empresa != null) {

            List<HierarquiaProjetoDetalhe> categoriasList = GestorModel.getCategoriasPorNivel(empresa, nivel);

            if (categoriasList.isEmpty()) {

                categoriaCombo.removeAllItems();
                categoriaCombo.setEnabled(false);

            } else {
                for (HierarquiaProjetoDetalhe categoria : categoriasList) {
                    categoriaCombo.addItem(categoria);
                    categoriaCombo.setItemCaption(categoria, categoria.getCategoria());

                }

                categoriaCombo.setEnabled(true);
            }
        } else {

            categoriaCombo.removeAllItems();
            categoriaCombo.setEnabled(false);

        }

    }

    public static void carregaComboCentroCusto(ComboBox centroCustoCombo, Empresa empresa) {

        if (empresa != null) {

            List<CentroCusto> centroCustoList = EmpresaModel.obterListaCentroCustosAtivos(empresa);

            if (centroCustoList.isEmpty()) {

                centroCustoCombo.removeAllItems();
                centroCustoCombo.setEnabled(false);

            } else {

                for (CentroCusto cc : centroCustoList) {
                    centroCustoCombo.addItem(cc);
                    centroCustoCombo.setItemCaption(cc, cc.getCentroCusto());
                }
                centroCustoCombo.setEnabled(true);
            }
        } else {

            centroCustoCombo.removeAllItems();
            centroCustoCombo.setEnabled(false);

        }

    }

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
    
    public static void resetaCombo(ComboBox responsavelCombo) {
        resetaCombo(responsavelCombo, null);
    }

    public static void resetaCombo(ComboBox combo, BeanItemContainer container) {

        if (combo != null) {
            combo.select(null);
            combo.removeAllItems();
        }
        if (container != null) {
            container.removeAllItems();

        }
    }

    public static Button buildButtonEditarTarefa(CallBackListener callback, Table table, Tarefa task, String caption) {
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

    public static PopupButton buildPopUpStatusTarefa(Table table, Tarefa task, PopUpStatusListener listener) {

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
