package com.saax.gestorweb.view;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.presenter.LixeiraPresenter;
import com.saax.gestorweb.presenter.PresenterUtils;
import com.saax.gestorweb.util.FormatterUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.List;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author rodrigo
 */
public class LixeiraView {

    private LixeiraPresenter listener;
    private Table tarefaRemovidasTable;
    private Window popUpLixeira;

    public void setListener(LixeiraPresenter listener) {
        this.listener = listener;
    }

    /**
     * Apresenta um pop up de confirmação para o usuário certificar que deseja mesmo proceder com a remoção
     * @param tarefa a ser removida
     */
    public void apresentaConfirmacaoRemocaoTarefa(Tarefa tarefa) {

        ConfirmDialog.show(UI.getCurrent(), PresenterUtils.getMensagensResource().getString("LixeiraView.removerTarefa.title"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.removerTarefa.text"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.removerTarefa.OKButton"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.removerTarefa.CancelButton"), (ConfirmDialog dialog) -> {
                    if (dialog.isConfirmed()) {
                        listener.remocaoTarefaConfirmada(tarefa);

                    }
                });
    }
    
    private Window constroiPopUpLixeira() {

        Window popup = new Window();
        popup.setModal(true);
        popup.setWidth("710px");
        popup.setHeight("220px");

        // Container main, which will store all other containers
        VerticalLayout containerPrincipal = new VerticalLayout();

        tarefaRemovidasTable = new Table();
        tarefaRemovidasTable.addGeneratedColumn(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button();
            removeButton.addClickListener((Button.ClickEvent event) -> {
                listener.restaurarTarefaClicked((Tarefa) itemId);
            });

            removeButton.setIcon(FontAwesome.RECYCLE);

            return removeButton;
        });
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.cod"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.cod"), 100);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.title"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.title"), 100);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.name"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.name"), 250);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.company"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.company"), 200);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.requestor"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.requestor"), 80);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.assingee"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.assingee"), 80);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.startDate"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.startDate"), 80);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.endDate"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.endDate"), 80);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.state"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.state"), 200);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.forecast"), Character.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.forecast"), 30);
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.dataHoraRemocao"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.dataHoraRemocao"), 100);


        tarefaRemovidasTable.setPageLength(0);
        tarefaRemovidasTable.setSelectable(true);
        tarefaRemovidasTable.setImmediate(true);

        containerPrincipal.addComponent(tarefaRemovidasTable);
        containerPrincipal.setSpacing(true);
        popup.setContent(containerPrincipal);

        return popup;

    }

    public void apresentarLixeira(List<Tarefa> tarefas) {

        popUpLixeira = constroiPopUpLixeira();
        
        popUpLixeira.addCloseListener((Window.CloseEvent e) -> {
            listener.janelaFechada();
        });
        
        popUpLixeira.center();

        for (Tarefa tarefa : tarefas) {

            Object[] linha = new Object[]{
                tarefa.getGlobalID(),
                tarefa.getHierarquia().getCategoria(),
                tarefa.getNome(),
                tarefa.getEmpresa().getNome()
                + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""),
                tarefa.getUsuarioSolicitante().getNome(),
                tarefa.getUsuarioResponsavel().getNome(),
                FormatterUtil.formatDate(tarefa.getDataInicio()),
                FormatterUtil.formatDate(tarefa.getDataFim()),
                tarefa.getStatus().toString(),
                tarefa.getProjecao().toString().charAt(0),
                FormatterUtil.formatDateTime(tarefa.getDataHoraRemocao())
            };

            tarefaRemovidasTable.addItem(linha, tarefa);

        }

        UI.getCurrent().addWindow(popUpLixeira);

    }
    
    public void apresentaConfirmacaoRestauracaoTarefa(Tarefa tarefa) {
        ConfirmDialog.show(UI.getCurrent(), PresenterUtils.getMensagensResource().getString("LixeiraView.restaurarTarefa.title"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.restaurarTarefa.text"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.restaurarTarefa.OKButton"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.restaurarTarefa.CancelButton"), (ConfirmDialog dialog) -> {
                    if (dialog.isConfirmed()) {
                        listener.restaurarTarefaConfirmada(tarefa);
                        tarefaRemovidasTable.removeItem(tarefa);
                    }
                });
    }

    public Table getTarefaRemovidasTable() {
        return tarefaRemovidasTable;
    }


    
}
