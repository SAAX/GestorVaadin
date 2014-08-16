package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.view.DashBoardView;
import com.saax.gestorweb.view.DashboardViewListenter;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

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
 * Presenter do Dashboard Esta classe é responsável captar todos os eventos que
 * ocorrem na View e dar o devido tratamento, utilizando para isto o modelo
 *
 *
 * @author Rodrigo
 */
public class DashboardPresenter implements DashboardViewListenter {

    // Todo presenter mantem acesso à view e ao model
    private final transient DashBoardView view;
    private final transient DashboardModel model;

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

        carregarListaTarefasUsuarioLogado();
        carregarFiltrosPesquisa();
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

            List<Empresa> empresas = model.listarEmpresasRelacionadas();
            for (Empresa empresa : empresas) {

                view.getFiltroEmpresaOptionGroup().addItem(empresa);
                view.getFiltroEmpresaOptionGroup().setItemCaption(empresa, empresa.getNome());

                for (FilialEmpresa filial : empresa.getFiliais()) {
                    view.getFiltroEmpresaOptionGroup().addItem(filial);
                    view.getFiltroEmpresaOptionGroup().setItemCaption(filial, "Filial: " + filial.getNome());

                }
            }

            for (ProjecaoTarefa projecao : ProjecaoTarefa.values()) {
                view.getFiltroProjecaoOptionGroup().addItem(projecao.toString());
            }

        } catch (GestorException ex) {
            Logger.getLogger(DashboardPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carrega a lista de tarefas sob responsabilidade do usuario logado
     */
    public void carregarListaTarefasUsuarioLogado() {

        // Usuario logado
        Usuario usuarioLogado = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();

        List<Tarefa> listaTarefas = model.listarTarefas(usuarioLogado);

        exibirListaTarefas(listaTarefas);

    }

    /**
     * Constrói o pop up de alteração de status e/ou andamento de tarefas neste
     * PopUP o usuario poderá alterar (evoluir ou regredir) um status de tarefa
     * ou indicar seu andamento.
     *
     * @param tarefa
     * @return
     */
    private PopupButton buildBotaoStatus(Tarefa tarefa) {

        // criar o botão com o status atual tarefa em seu caption
        PopupButton statusButton = new PopupButton(getStatusTarefaDescription(tarefa));

        // vincula o botão a tarefa
        statusButton.setId(tarefa.getId().toString());

        // evento disparado quando o pop-up se torna visivel:
        // seleciona a linha correta na tabela
        statusButton.addPopupVisibilityListener((PopupButton.PopupVisibilityEvent event) -> {
            if (event.isPopupVisible()) {
                // selecionar a linha clicada:
                Integer idTarefa = Integer.parseInt(event.getPopupButton().getId());
                view.getTarefasTable().setValue(idTarefa);
            }
        });

        // Se a tarefa estiver em andamento o pop-up será com um 
        // combo para seleção do andamento
        if (tarefa.getStatus().equals(StatusTarefa.EM_ANDAMENTO)) {

            statusButton.setCaption(getStatusTarefaDescription(tarefa));

            HorizontalLayout popupContainer = new HorizontalLayout();
            popupContainer.setSpacing(true);

            ComboBox andamentoTarefaCombo = new ComboBox("Indique o andamento da Tarefa:");

            andamentoTarefaCombo.addItem(0);
            andamentoTarefaCombo.setItemCaption(0, "0%");

            andamentoTarefaCombo.addItem(25);
            andamentoTarefaCombo.setItemCaption(25, "25%");

            andamentoTarefaCombo.addItem(50);
            andamentoTarefaCombo.setItemCaption(50, "50%");

            andamentoTarefaCombo.addItem(75);
            andamentoTarefaCombo.setItemCaption(75, "75%");

            andamentoTarefaCombo.addItem(100);
            andamentoTarefaCombo.setItemCaption(100, "100%");

            andamentoTarefaCombo.setWidth("100px");

            popupContainer.addComponent(andamentoTarefaCombo);

            /**
             * Metodo executado quando o botao de confirmaçao (OK) do pop up de
             * andamento da tarefa eh acionado
             */
            Button okButton = new Button("OK", (Button.ClickEvent event) -> {

                Integer idTarefa = (Integer) view.getTarefasTable().getValue();

                // obtem o pop up button
                PopupButton statusButtonSelecioado = (PopupButton) view.getTarefasTable().getItem(idTarefa).getItemProperty("Status").getValue();

                Integer andamento = (Integer) andamentoTarefaCombo.getValue();

                Tarefa tarefaSelecionada = model.atualizarAndamentoTarefa(idTarefa, andamento);

                statusButtonSelecioado.setPopupVisible(false);

                statusButtonSelecioado.setCaption(getStatusTarefaDescription(tarefaSelecionada));

            });

            popupContainer.addComponent(okButton);
            popupContainer.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);

            Button cancelButton = new Button("Cancel", (Button.ClickEvent event) -> {

                andamentoTarefaCombo.select(tarefa.getAndamento());

                statusButton.setPopupVisible(false);
            });

            popupContainer.addComponent(cancelButton);
            popupContainer.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);

            statusButton.setContent(popupContainer);

            andamentoTarefaCombo.select(tarefa.getAndamento());

        } else {
            // Colocar tratamentos futuros no caso de outras alteraçoes de status
            // como bloquear, cancelar, etc.
        }
        return statusButton;
    }

    /*
     private ComboBox buildComboStatus(Tarefa tarefa) {

     ComboBox statusCombo = new ComboBox();

     statusCombo.addItem(StatusTarefa.NAO_ACEITA);
     statusCombo.setItemCaption(StatusTarefa.NAO_ACEITA, "Não Aceita");
        
     statusCombo.addItem(StatusTarefa.NAO_INICIADA);
     statusCombo.setItemCaption(StatusTarefa.NAO_INICIADA, "Não Iniciada");
        
     statusCombo.addItem(StatusTarefa.EM_ANDAMENTO+"_0");
     statusCombo.setItemCaption(StatusTarefa.EM_ANDAMENTO+"_0", "0% Concluída");

     statusCombo.addItem(StatusTarefa.EM_ANDAMENTO+"_25");
     statusCombo.setItemCaption(StatusTarefa.EM_ANDAMENTO+"_25", "0% Concluída");
        
        
     statusCombo.addItems("0%", "25%", "50%", "75%", "100%");
     statusButton.setContent(statusCombo);

     switch (tarefa.getAndamento()) {
     case 0:
     statusCombo.select("0%");
     break;
     case 25:
     statusCombo.select("25%");
     break;
     case 50:
     statusCombo.select("50%");
     break;
     case 75:
     statusCombo.select("75%");
     break;
     case 100:
     statusCombo.select("100%");
     break;

     default:
     statusCombo.select("0%");
     }


     statusCombo.setWidth("60px");
     return statusCombo;
     }
     */
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
                buildBotaoStatus(tarefa),
                tarefa.getProjecao().toString().charAt(0),
                new Button("E"),
                new Button("C")
            };

            view.getTarefasTable().addItem(linha, tarefa.getId());

        }

        for (Tarefa tarefa : listaTarefas) {
            if (tarefa.getTarefaPai() != null) {
                view.getTarefasTable().setParent(tarefa.getGlobalID(), tarefa.getTarefaPai().getGlobalID());

            }

        }

    }

    /**
     * Aplicar filtros de pesquisa selecionado
     */
    @Override
    public void aplicarFiltroPesquisa() {

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
        List<ProjecaoTarefa> projecoes = new ArrayList<>();

        // recarrega a visualizacao
        List<Tarefa> listaTarefas = model.listarTarefas(usuariosResponsaveis, usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes);

        exibirListaTarefas(listaTarefas);

        view.getRemoverFiltroPesquisa().setVisible(true);

    }

    /**
     * Remove todos os filtros de pesquisa e recarrega a visualização
     */
    @Override
    public void removerFiltrosPesquisa() {

        view.getRemoverFiltroPesquisa().setVisible(false);

        view.getFiltroUsuarioParticipanteOptionGroup().setValue(null);
        view.getFiltroUsuarioSolicitanteOptionGroup().setValue(null);
        view.getFiltroUsuarioResponsavelOptionGroup().setValue(null);
        view.getFiltroEmpresaOptionGroup().setValue(null);
        view.getFiltroDataFimDateField().setValue(null);
        view.getFiltroProjecaoOptionGroup().setValue(null);

        carregarListaTarefasUsuarioLogado();

    }

    private String getStatusTarefaDescription(Tarefa tarefa) {

        StatusTarefa statusTarefa = tarefa.getStatus();

        switch (statusTarefa) {
            case NAO_ACEITA:
                return mensagens.getString("StatusTarefa.NAO_ACEITA");
            case NAO_INICIADA:
                return mensagens.getString("StatusTarefa.NAO_INICIADA");
            case EM_ANDAMENTO:
                return mensagens.getString("StatusTarefa.EM_ANDAMENTO") + tarefa.getAndamento() + "%";
            case ADIADA:
                return mensagens.getString("StatusTarefa.ADIADA");
            case BLOQUEADA:
                return mensagens.getString("StatusTarefa.BLOQUEADA");
            case CONCLUIDA:
                return mensagens.getString("StatusTarefa.CONCLUIDA");
            case CANCELADA:
                return mensagens.getString("StatusTarefa.CANCELADA");
            default:
                throw new AssertionError();
        }

    }
}
