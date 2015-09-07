package com.saax.gestorweb;

import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.presenter.LoginPresenter;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.saax.gestorweb.view.LoginView;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável por tratar os parametros de entrada da aplicação
 *
 * @author Rodrigo
 */
public class TrataParametros {

    private final GestorMDI gestorMDI;
    private static final String IDTAREFA_PARAM = "task";
    
    public TrataParametros(GestorMDI gestorMDI) {
        this.gestorMDI = gestorMDI;
    }

    public void trataParametrosDeEntrada(VaadinRequest request) {

        String openTask = request.getParameter(IDTAREFA_PARAM);

        boolean tratamentoBemSucedido = trataParametroIDTarefa(openTask);

        // caso existam outros parametros no futuro, criar um método para cada um aqui
        if (!tratamentoBemSucedido) {

            gestorMDI.loadstartPage();

        }

    }

    private boolean trataParametroIDTarefa(String parametroIdTarefa) {

        // verifica se foi passado o parametro "Tarefa"
        if (parametroIdTarefa == null) {
            return false;
        }

        // valida se o valor passado é inteiro
        Integer taskID;
        try {
            taskID = Integer.parseInt(parametroIdTarefa);
        }
        catch (NumberFormatException ex) {
            Logger.getLogger(GestorMDI.class.getName()).log(Level.WARNING, "Trying to open an invallid task (" + parametroIdTarefa + ")", ex);
            return false;
        }

        // valida se o valor passado é mesmo uma task válida na base de dados
        Tarefa task = TarefaModel.findByID(taskID);

        if (task == null) {
            return false;
        }

        // valida se o usuário está logado 
        if (GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO) == null) {

            // se o usuario nao estiver logado, chama da tela de login
            LoginView loginView = new LoginView();

            // O presenter liga model e view
            LoginPresenter presenter = new LoginPresenter(loginView);
            presenter.openTaskOnSucessLogin(task);

            // adiciona a visualização à UI
            UI.getCurrent().addWindow(loginView);

            return true;
        }

        gestorMDI.carregarDashBoard(task);

        return true;
    }

}
