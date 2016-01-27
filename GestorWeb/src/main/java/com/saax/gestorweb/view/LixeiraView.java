package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.presenter.LixeiraPresenter;
import com.saax.gestorweb.presenter.PresenterUtils;
import com.saax.gestorweb.util.FormatterUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
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
    private Window popUpLixeira;
    private TabSheet tabSheet;
    private Table tarefaRemovidasTable;
    private Table metasRemovidasTable;

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
    
    /**
     * Apresenta um pop up de confirmação para o usuário certificar que deseja mesmo proceder com a remoção
     * @param meta a ser removida
     */
    public void apresentaConfirmacaoRemocaoMeta(Meta meta) {

        ConfirmDialog.show(UI.getCurrent(), PresenterUtils.getMensagensResource().getString("LixeiraView.removerMeta.title"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.removerMeta.text"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.removerMeta.OKButton"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.removerMeta.CancelButton"), (ConfirmDialog dialog) -> {
                    if (dialog.isConfirmed()) {
                        listener.remocaoMetaConfirmada(meta);

                    }
                });
    }
    
    private Table constroiTabelaTarefasRemovidas(){

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
        tarefaRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.dataHoraRemocao"), String.class, "");
        tarefaRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.tarefaRemovidasTable.dataHoraRemocao"), 100);


        tarefaRemovidasTable.setPageLength(0);
        tarefaRemovidasTable.setSelectable(true);
        tarefaRemovidasTable.setImmediate(true);
        
        return tarefaRemovidasTable;
        
    }
    
    private Table constroiTabelaMetasRemovidas(){

        metasRemovidasTable = new Table();
        metasRemovidasTable.addGeneratedColumn(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button();
            removeButton.addClickListener((Button.ClickEvent event) -> {
                listener.restaurarMetaClicked((Meta) itemId);
            });

            removeButton.setIcon(FontAwesome.RECYCLE);

            return removeButton;
        });
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.cod"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.cod"), 100);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.title"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.title"), 100);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.name"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.name"), 250);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.company"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.company"), 200);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.requestor"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.requestor"), 80);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.assingee"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.assingee"), 80);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.startDate"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.startDate"), 80);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.endDate"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.endDate"), 80);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.state"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.state"), 200);
        metasRemovidasTable.addContainerProperty(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.dataHoraRemocao"), String.class, "");
        metasRemovidasTable.setColumnWidth(PresenterUtils.getMensagensResource().getString("LixeiraView.metasRemovidasTable.dataHoraRemocao"), 100);


        metasRemovidasTable.setPageLength(0);
        metasRemovidasTable.setSelectable(true);
        metasRemovidasTable.setImmediate(true);
        
        return metasRemovidasTable;
        
    }
    
    private Window constroiPopUpLixeira() {

        Window popup = new Window();
        popup.setModal(true);
        popup.setWidth("710px");
        popup.setHeight("220px");

        // Container main, which will store all other containers
        VerticalLayout containerPrincipal = new VerticalLayout();

        
        tabSheet = new TabSheet();
        tabSheet.setWidth("100%");
        tabSheet.setHeight("100%");
        

        tabSheet.addTab(constroiTabelaTarefasRemovidas(), "Tarefas");
        tabSheet.addTab(constroiTabelaMetasRemovidas(), "Metas");
        
        containerPrincipal.addComponent(tabSheet);
        containerPrincipal.setSpacing(true);
        popup.setContent(containerPrincipal);

        return popup;

    }

    private void adicionarTarefa(Tarefa tarefa){
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
                FormatterUtil.formatDateTime(tarefa.getDataHoraRemocao())
            };

            tarefaRemovidasTable.addItem(linha, tarefa);
        
    }
    private void adicionarMeta(Meta meta){
        
            Object[] linha = new Object[]{
                meta.getGlobalID(),
                meta.getCategoria().getCategoria(),
                meta.getNome(),
                meta.getEmpresa().getNome()
                + (meta.getFilialEmpresa() != null ? "/" + meta.getFilialEmpresa().getNome() : ""),
                meta.getUsuarioSolicitante().getNome(),
                meta.getUsuarioResponsavel().getNome(),
                FormatterUtil.formatDate(meta.getDataInicio()),
                FormatterUtil.formatDate(meta.getDataFim()),
                meta.getStatus().toString(),
                FormatterUtil.formatDateTime(meta.getDataHoraRemocao())
            };

            metasRemovidasTable.addItem(linha, meta);
    }
    public void apresentarLixeira(List<Tarefa> tarefas, List<Meta> metas) {

        popUpLixeira = constroiPopUpLixeira();
        
        popUpLixeira.addCloseListener((Window.CloseEvent e) -> {
            listener.janelaFechada();
        });
        
        popUpLixeira.center();

        for (Tarefa tarefa : tarefas) {
            adicionarTarefa(tarefa);
        }
        for (Meta meta : metas) {
            adicionarMeta(meta);
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

    public void apresentaConfirmacaoRestauracaoMeta(Meta meta) {
        ConfirmDialog.show(UI.getCurrent(), PresenterUtils.getMensagensResource().getString("LixeiraView.restaurarMeta.title"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.restaurarMeta.text"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.restaurarMeta.OKButton"),
                PresenterUtils.getMensagensResource().getString("LixeiraView.restaurarMeta.CancelButton"), (ConfirmDialog dialog) -> {
                    if (dialog.isConfirmed()) {
                        listener.restaurarMetaConfirmada(meta);
                        metasRemovidasTable.removeItem(meta);
                    }
                });
    }

    public Table getTarefaRemovidasTable() {
        return tarefaRemovidasTable;
    }



    
}
