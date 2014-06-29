package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetasModel;
import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.view.CadastroMetasView;
import com.saax.gestorweb.view.DashBoardView;
import com.saax.gestorweb.view.DashboardViewListenter;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Presenter do Dashboard
 * Esta classe é responsável captar todos os eventos
 * que ocorrem na View e dar o devido tratamento, utilizando para isto o modelo
 *
 *
 * @author Rodrigo
 */
public class DashboardPresenter implements DashboardViewListenter {

    // Todo presenter mantem acesso à view e ao model
    private final DashBoardView view;
    private final DashboardModel model;

    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public DashboardPresenter(DashboardModel model,
            DashBoardView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);
        
        loadMetasTable();
    }

    /**
     * Evento disparado ao ser acionado o botão "Login"
     */
    @Override
    public void addMeta() {

        // Cria o pop up de login (model e view)
        CadastroMetasModel cadastroMetasModel = new CadastroMetasModel();
        CadastroMetasView cadastroMetasView = new CadastroMetasView();

        // O presenter liga model e view
        new CadastroMetasPresenter(cadastroMetasModel, cadastroMetasView);

        // adiciona a visualização à UI
        UI.getCurrent().addWindow(cadastroMetasView);
        
        

    }

    
    private void loadMetasTable(){
        
//        List<Meta> metas = model.getMetas();
  //      view.exibirMetas(metas);
        
    }
            
            
}
