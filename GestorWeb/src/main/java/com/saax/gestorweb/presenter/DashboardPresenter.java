package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.view.DashBoardView;
import com.saax.gestorweb.view.DashboardViewListenter;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Presenter do Dashboard
 * Esta classe é responsável captar todos os eventos
 * que ocorrem na View e dar o devido tratamento, utilizando para isto o modelo
 *
 *
 * @author Rodrigo
 */
public final class DashboardPresenter implements DashboardViewListenter {

    // Todo presenter mantem acesso à view e ao model
    private final DashBoardView view;
    private final DashboardModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

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
        
    }
   
    /**
     * Logout geral
     */
    @Override
    public void logout() {

        ((GestorMDI) UI.getCurrent()).setUserData(null);
        
        ((GestorMDI) UI.getCurrent()).getPage().setLocation("/GestorWeb");
        
        // Close the VaadinServiceSession
        ((GestorMDI) UI.getCurrent()).getSession().close();

    }

    /**
     * Configura todos os eventos ao abrir a visualização
     */
    @Override
    public void carregaVisualizacaoInicial() {
        
        carregarListaTarefas();
        carregarFiltrosPesquisa();
    }

    /**
     * Carregar os filtros de pesquisa
     */
    public void carregarFiltrosPesquisa(){
        
        try {
            List<Usuario> usuarioEmpresa = model.listarUsuariosEmpresa();
            for (Usuario usuario : usuarioEmpresa) {
                view.getFiltroUsuarioOptionGroup().addItem(usuario.getNome());
            }
        } catch (GestorException ex) {
            Logger.getLogger(DashboardPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Carrega a lista de tarefas sob responsabilidade
     * do usuario logado
     */
    public void carregarListaTarefas(){
        
        // Usuario logado
        Usuario usuarioLogado = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
        
        List<Tarefa> listaTarefas = model.listarTarefas(usuarioLogado);
        
        exibirListaTarefas(listaTarefas);

        
        
    }
            

    /**
     * Cria o combo para apontamento do andamento
     * @param tarefa
     * @return 
     */
    private ComboBox buildComboApontamento(Tarefa tarefa){
        ComboBox apontamento = new ComboBox();
        apontamento.addItems("0%", "25%", "50%", "75%", "100%");
        
        
        switch (tarefa.getAndamento()) {
            case 0:
                apontamento.select("0%");
                break;
            case 25:
                apontamento.select("25%");
                break;
            case 50:
                apontamento.select("50%");
                break;
            case 75:
                apontamento.select("75%");
                break;
            case 100:
                apontamento.select("100%");
                break;
                
            default:
                apontamento.select("0%");
        }
        
        if (tarefa.getStatus()!=StatusTarefa.EM_ANDAMENTO){
            apontamento.setEnabled(false);
        }
        
        return apontamento;
    }
    
    /**
     * Carrega a lista de tarefas na tabela
     * @param listaTarefas 
     */
    public void exibirListaTarefas(List<Tarefa> listaTarefas) {

        
        view.getTarefasTable().removeAllItems();
        
        Object[] linha;
        for (Tarefa tarefa : listaTarefas) {
            linha = new Object[]{
                
                tarefa.getGlobalID(),
                tarefa.getTitulo(),
                tarefa.getNome(),
                tarefa.getEmpresa().getNome(),
                tarefa.getUsuarioSolicitante().getNome(),
                tarefa.getUsuarioResponsavel().getNome(),
                FormatterUtil.formatDate(tarefa.getDataInicio()),
                FormatterUtil.formatDate(tarefa.getDataFim()),
                tarefa.getStatus().toString(),
                buildComboApontamento(tarefa),
                null,
                new Button("E"), 
                new Button("C")
            };
            
            view.getTarefasTable().addItem(linha, tarefa.getGlobalID());

            
        }
        
        for (Tarefa tarefa : listaTarefas) {
            if (tarefa.getTarefaPai()!=null){
                view.getTarefasTable().setParent(tarefa.getGlobalID(),tarefa.getTarefaPai().getGlobalID());
                
            } 
            
        }
        

    }

    /**
     * Aplicar filtros de pesquisa selecionado
     */
    @Override
    public void aplicarFiltroPesquisa() {
       
    }


}
