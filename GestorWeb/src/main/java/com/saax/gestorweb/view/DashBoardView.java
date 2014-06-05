package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Rodrigo
 */
public class DashBoardView extends Panel {

    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getUserData().getImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private DashboardViewListenter listener;

    public void setListener(DashboardViewListenter listener) {
        this.listener = listener;
    }

    private final VerticalLayout main;
    private Table metasTable;

    public DashBoardView() {

        setSizeFull();

        main = new VerticalLayout();
        main.setSpacing(true);
        main.setSizeFull();

        setContent(main);

        main.addComponent(new Label("TESTE: usuario logado = " + ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado().getNome()));

        main.addComponent(buildMetasTable());
        
        main.addComponent(buildAddMetasButton());
        
    }
    
    private Table buildMetasTable(){
        
        metasTable = new Table();
        
        
        metasTable.setWidth("750px");
        metasTable.setHeight("300px");
        
        metasTable.addContainerProperty("Cod", Integer.class, null);
        metasTable.addContainerProperty("Nome", String.class, null);
        
        return metasTable;

    }

    private Button buildAddMetasButton() {
        final Button previewMetas = new Button("Add", new Button.ClickListener() {

            // notifica o listener que o botão foi acionado para que este dê o devido tratamento
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.addMeta();
            }
        });

        return previewMetas;

    }

    public void exibirMetas(List<Meta> metas) {
     
        metasTable.removeAllItems();
        
        for (Meta meta : metas) {
            metasTable.addItem(
                    new Object[]{
                        meta.getIdMeta(),
                        meta.getNome()
                    },
                    meta.getIdMeta());
            
        }
    }

}
