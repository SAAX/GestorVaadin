package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetaModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroMetaCallBackListener;
import com.saax.gestorweb.view.CadastroMetaView;
import com.saax.gestorweb.view.CadastroMetaViewListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Rodrigo
 */
public class CadastroMetaPresenter implements CadastroMetaViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final CadastroMetaView view;
    private final CadastroMetaModel model;

    // recursos de aplicação
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    
    // usuario logado
    private final Usuario usuarioLogado;
    private CadastroMetaCallBackListener callbackListener;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public CadastroMetaPresenter(CadastroMetaModel model,
            CadastroMetaView view) {

        this.model = model;
        this.view = view;

        usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");

        view.setListener(this);

    }

    /**
     * Cria uma nova meta na categoria informada
     *
     * @param categoria
     */
    public void criarNovaMeta(HierarquiaProjetoDetalhe categoria) {

        Meta meta = model.criarNovaMeta(categoria, usuarioLogado);

        // configura a categoria
        ComboBox combo = view.getHierarquiaCombo();
        combo.addItem(meta.getHierarquia());
        combo.setItemCaption(meta.getHierarquia(), meta.getHierarquia().getCategoria());

        init(meta);

        view.getHierarquiaCombo().setEnabled(false);

    }

    /**
     * Inicializa o formulário carregando a meta informada
     */
    private void init(Meta meta) {

        carregaComboEmpresa();
        carregaComboResponsavel();
        carregaComboEmpresaCliente();

        // Abre o formulário
        UI.getCurrent().addWindow(view);

        // liga (bind) o form com a meta
        view.setMeta(meta);

    }

    /**
     * Carrega o combo de seleção da empresa com todas as empresas relacionadas
     * ao usuário logado
     */
    private void carregaComboEmpresa() {
        ComboBox empresaCombo = view.getEmpresaCombo();

        EmpresaModel empresaModel = new EmpresaModel();

        List<Empresa> empresas = empresaModel.listarEmpresasParaSelecao(usuarioLogado);
        for (Empresa empresa : empresas) {

            empresaCombo.addItem(empresa);
            empresaCombo.setItemCaption(empresa, empresa.getNome());

        }

    }

    /**
     * Carrega o combo de departamentos com os departamentos ativos da empresa
     * logada
     */
    private void carregaComboDepartamento(Empresa empresa) {

        if (empresa != null) {
            ComboBox departamento = view.getDepartamentoCombo();
            for (Departamento depto : model.obterListaDepartamentosAtivos(empresa)) {
                departamento.addItem(depto);
                departamento.setItemCaption(depto, depto.getDepartamento());
            }
        } else {
            view.getDepartamentoCombo().setInputPrompt(mensagens.getString("CadastroMetaPresenter.departamentoCombo.avisoSelecionarEmpresa"));
        }

    }

    /**
     * Carrega o combo de responsáveis com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    private void carregaComboResponsavel() {
        ComboBox responsavel = view.getResponsavelCombo();
        for (Usuario usuario : model.listarUsuariosEmpresa()) {
            responsavel.addItem(usuario);
            responsavel.setItemCaption(usuario, usuario.getNome());

        }
    }

    /**
     * Carrega o combo de clientes com todos os clientes ativos de todas as
     * empresas (empresa pricipal + subs ) do usuario logado
     */
    private void carregaComboEmpresaCliente() {
        ComboBox empresaCliente = view.getEmpresaClienteCombo();
        for (EmpresaCliente cliente : model.listarEmpresasCliente(usuarioLogado)) {
            empresaCliente.addItem(cliente);
            empresaCliente.setItemCaption(cliente, cliente.getNome());
        }

    }

    /**
     * Trata o evento disparado ao soelecionar uma empresa e carrega a lista de departamentos
     * @param empresa 
     */
    @Override
    public void empresaSelecionada(Empresa empresa) {
        carregaComboDepartamento(empresa);
    }

    /**
     * Configura um listener para ser chamado quando o cadastro for concluido
     *
     * @param callback
     */
    @Override
    public void setCallBackListener(CadastroMetaCallBackListener callback) {
        this.callbackListener = callback;
    }

    @Override
    public void gravarButtonClicked() {
        Meta meta = (Meta) view.getMeta();

        if (meta.getUsuarioResponsavel() == null) {
            meta.setUsuarioResponsavel(meta.getUsuarioInclusao());
        }
        boolean novaMeta = meta.getId() == null;
        meta = model.gravarMeta(meta);

        // notica (se existir) algum listener interessado em saber que o cadastro foi finalizado.
        if (callbackListener != null) {
            if (novaMeta) {
                callbackListener.cadastroMetaConcluido(meta);
            } else {
                callbackListener.edicaoMetaConcluida(meta);
            }
        }
        view.close();
        Notification.show(meta.getHierarquia().getCategoria() + mensagens.getString("CadastroMetaPresenter.mensagem.gravadoComSucesso"), Notification.Type.HUMANIZED_MESSAGE);
    }

    @Override
    public void cancelarButtonClicked() {
        UI.getCurrent().removeWindow(view);
    }

    @Override
    public void hierarquiaSelecionada(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
