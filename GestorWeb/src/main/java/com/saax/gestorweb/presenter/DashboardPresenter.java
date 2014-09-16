package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.dashboard.PopUpEvolucaoStatusModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.dashboard.PopUpEvolucaoStatusPresenter;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.DashBoardView;
import com.saax.gestorweb.view.DashboardViewListenter;
import com.saax.gestorweb.view.dashboard.PopUpEvolucaoStatusView;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Presenter do Dashboard <br>
 * Esta classe é responsável captar todos os eventos que ocorrem na View e dar o
 * devido tratamento, utilizando para isto o modelo.
 *
 *
 * @author Rodrigo
 */
public class DashboardPresenter implements DashboardViewListenter, Serializable {

    // Todo presenter mantem acesso à view e ao model
    private final transient DashBoardView view;
    private final transient DashboardModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // enumeracao do tipo de pesquisa
    public enum TipoPesquisa {

        INCLUSIVA_OU, EXCLUSIVA_E
    };

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
     * Chama o Logout geral do MDI
     */
    @Override
    public void logout() {

        ((GestorMDI) UI.getCurrent()).logout();
    }

    /**
     * Carrega os campos necessarios ao abrir a visualização
     */
    @Override
    public void carregaVisualizacaoInicial() {

        carregarListaTarefasUsuarioLogado();
        carregarFiltrosPesquisa();
        carregarListaTarefasPrincipais();
    }

    /**
     * Carregar os filtros de pesquisa
     */
    public void carregarFiltrosPesquisa() {

        try {

            List<Usuario> usuarios = model.listarUsuariosEmpresa();
            for (Usuario usuario : usuarios) {
                view.getFiltroUsuarioResponsavelOptionGroup().addItem(usuario);
                view.getFiltroUsuarioResponsavelOptionGroup().setItemCaption(usuario, usuario.getNome());

                view.getFiltroUsuarioSolicitanteOptionGroup().addItem(usuario);
                view.getFiltroUsuarioSolicitanteOptionGroup().setItemCaption(usuario, usuario.getNome());

                view.getFiltroUsuarioParticipanteOptionGroup().addItem(usuario);
                view.getFiltroUsuarioParticipanteOptionGroup().setItemCaption(usuario, usuario.getNome());

            }

            EmpresaModel empresaModel = new EmpresaModel();
            List<Empresa> empresas = empresaModel.listarEmpresasRelacionadas();
            for (Empresa empresa : empresas) {

                view.getFiltroEmpresaOptionGroup().addItem(empresa);
                view.getFiltroEmpresaOptionGroup().setItemCaption(empresa, empresa.getNome());

                for (FilialEmpresa filial : empresa.getFiliais()) {
                    view.getFiltroEmpresaOptionGroup().addItem(filial);
                    view.getFiltroEmpresaOptionGroup().setItemCaption(filial, "Filial: " + filial.getNome());

                }
            }

            for (ProjecaoTarefa projecao : ProjecaoTarefa.values()) {
                view.getFiltroProjecaoOptionGroup().addItem(projecao);
                view.getFiltroProjecaoOptionGroup().setItemCaption(projecao, projecao.toString());
            }

            view.getPermutacaoPesquisaOptionGroup().addItem(TipoPesquisa.EXCLUSIVA_E);
            view.getPermutacaoPesquisaOptionGroup().setItemCaption(TipoPesquisa.EXCLUSIVA_E, "Todos os filtros");
            view.getPermutacaoPesquisaOptionGroup().addItem(TipoPesquisa.INCLUSIVA_OU);
            view.getPermutacaoPesquisaOptionGroup().setItemCaption(TipoPesquisa.INCLUSIVA_OU, "Pelo menos um filtro");

        } catch (GestorException ex) {
            Logger.getLogger(DashboardPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carrega a lista de tarefas sob responsabilidade do usuario logado
     */
    @Override
    public void carregarListaTarefasUsuarioLogado() {

        // Usuario logado
        Usuario usuarioLogado = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");

        List<Tarefa> listaTarefas = model.listarTarefas(usuarioLogado);

        exibirListaTarefas(listaTarefas);

    }

    /**
     * Carrega o box inferior esquerdo com as tarefas principais
     */
    private void carregarListaTarefasPrincipais() {

        Usuario usuarioLogado = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");

        List<Tarefa> tarefasPrincipais = model.listarTarefasPrincipais(usuarioLogado);

        view.setListaTarefasPrincipais(tarefasPrincipais);
    }

    /**
     * Constrói o pop up de alteração de status e/ou andamento de tarefas neste
     * PopUP o usuario poderá alterar (evoluir ou regredir) um status de tarefa
     * ou indicar seu andamento.
     *
     * @param tarefa
     * @return
     */
    private PopupButton buildPopUpEvolucaoStatusEAndamento(Tarefa tarefa) {

        // comportmento e regras:
        PopUpEvolucaoStatusView viewPopUP = new PopUpEvolucaoStatusView();
        PopUpEvolucaoStatusModel modelPopUP = new PopUpEvolucaoStatusModel();

        PopUpEvolucaoStatusPresenter presenter = new PopUpEvolucaoStatusPresenter(viewPopUP, modelPopUP);

        presenter.load(tarefa);

        // evento disparado quando o pop-up se torna visivel:
        // seleciona a linha correta na tabela
        presenter.getStatusButton().addPopupVisibilityListener((PopupButton.PopupVisibilityEvent event) -> {
            if (event.isPopupVisible()) {
                // selecionar a linha clicada:
                String idTarefa = event.getPopupButton().getId();
                this.view.getTarefasTable().setValue(idTarefa);
            }
        });

        return presenter.getStatusButton();
    }

    /**
     * Carrega a lista de tarefas na tabela
     *
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
                tarefa.getEmpresa().getNome()
                + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""),
                tarefa.getUsuarioSolicitante().getNome(),
                tarefa.getUsuarioResponsavel().getNome(),
                FormatterUtil.formatDate(tarefa.getDataInicio()),
                FormatterUtil.formatDate(tarefa.getDataFim()),
                buildPopUpEvolucaoStatusEAndamento(tarefa),
                tarefa.getProjecao().toString().charAt(0),
                new Button("E"),
                new Button("C")
            };

            view.getTarefasTable().addItem(linha, tarefa.getGlobalID());

        }

        for (Tarefa tarefa : listaTarefas) {
            if (tarefa.getTarefaPai() != null) {
                view.getTarefasTable().setParent(tarefa.getGlobalID(), tarefa.getTarefaPai().getGlobalID());

            }

        }

    }


    /**
     * Aplicar filtros de pesquisa selecionado
     *
     */
    @Override
    public void aplicarFiltroPesquisa() {
        
        TipoPesquisa tipoPesquisa = (TipoPesquisa) view.getPermutacaoPesquisaOptionGroup().getValue();

        if (tipoPesquisa==null){
            view.getPermutacaoPesquisaOptionGroup().setValue(TipoPesquisa.INCLUSIVA_OU);
        } else {
            aplicarFiltroPesquisa(tipoPesquisa);
        }
    }
    
    /**
     * Aplicar filtros de pesquisa selecionado
     *
     * @param tipoPesquisa
     */
    public void aplicarFiltroPesquisa(TipoPesquisa tipoPesquisa) {

        // Obtem os filtros selecionados pelo usuario
        // usuarios selecionados
        List<Usuario> usuariosResponsaveis = new ArrayList<>((Collection<Usuario>) view.getFiltroUsuarioResponsavelOptionGroup().getValue());

        List<Usuario> usuariosSolicitantes = new ArrayList<>((Collection<Usuario>) view.getFiltroUsuarioSolicitanteOptionGroup().getValue());

        List<Usuario> usuariosParticipantes = new ArrayList<>((Collection<Usuario>) view.getFiltroUsuarioParticipanteOptionGroup().getValue());

        // Empresas selecionadas
        List<Empresa> empresas = new ArrayList<>();
        List<FilialEmpresa> filiais = new ArrayList<>();

        for (Object empresaFilial : (Collection<Object>) view.getFiltroEmpresaOptionGroup().getValue()) {
            if (empresaFilial instanceof Empresa) {
                Empresa e = (Empresa) empresaFilial;
                empresas.add(e);

            } else if (empresaFilial instanceof FilialEmpresa) {
                FilialEmpresa f = (FilialEmpresa) empresaFilial;
                filiais.add(f);

            }
        }

        LocalDate dataFim = null;

        // Data Fim
        Date dataFimDate = view.getFiltroDataFimDateField().getValue();
        if (dataFimDate != null) {
            Instant instant = Instant.ofEpochMilli(dataFimDate.getTime());
            dataFim = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        }

        // Projecoes
        List<ProjecaoTarefa> projecoes = new ArrayList<>((Collection<ProjecaoTarefa>) view.getFiltroProjecaoOptionGroup().getValue());

        // recarrega a visualizacao
        List<Tarefa> listaTarefas = model.filtrarTarefas(tipoPesquisa, usuariosResponsaveis, usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes);

        exibirListaTarefas(listaTarefas);

        view.getRemoverFiltroPesquisa().setVisible(true);
        view.getPermutacaoPesquisaOptionGroup().setVisible(true);

    }

    /**
     * Remove todos os filtros de pesquisa e recarrega a visualização
     */
    @Override
    public void removerFiltrosPesquisa() {

        view.getFiltroUsuarioParticipanteOptionGroup().setValue(null);
        view.getFiltroUsuarioSolicitanteOptionGroup().setValue(null);
        view.getFiltroUsuarioResponsavelOptionGroup().setValue(null);
        view.getFiltroEmpresaOptionGroup().setValue(null);
        view.getFiltroDataFimDateField().setValue(null);
        view.getFiltroProjecaoOptionGroup().setValue(null);

        carregarListaTarefasUsuarioLogado();

        view.getRemoverFiltroPesquisa().setVisible(false);
        view.getPermutacaoPesquisaOptionGroup().setVisible(false);
        view.getPermutacaoPesquisaOptionGroup().setValue(null);

    }


}
