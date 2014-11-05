/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetaModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroMetaView;
import com.saax.gestorweb.view.CadastroMetaViewListener;
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

    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final Usuario usuarioLogado;

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

        init();

    }

    /**
     * Cria uma nova meta na categoria informada
     * @param categoria 
     */
    public void criarNovaMeta(HierarquiaProjetoDetalhe categoria){
    
        model.criarNovaMeta(categoria);
    }
    
    /**
     * Evento disparado ao ser aberta a tela de cadastro de metas Seu objetivo e
     * configurar o estado inicial do form, com os combos carregados, mensagens
     * configuradas etc.
     */
    private void init() {

        // 1. Configura o combo de empresas
        // caso o usuário esteja relacionado com várias empresas, exibe um 
        // combo para seleção de empresa para a qual ele quer selecionar a meta
        // caso contrário exibe um combo desabilitado, com a empresa já indicada.
        EmpresaModel empresaModel = new EmpresaModel();
        List<Empresa> empresas = empresaModel.listarEmpresasParaSelecao(usuarioLogado);

        view.carregarComboEmpresas(empresas);
        if (empresas.size() == 1) {
            view.selecionarEmpresa(empresas.get(0));
            view.desabilitarSelecaoEmpresas();
        }

        // 2. Configura o combo de seleção de departamentos:
        // caso exista apenas uma empresa relacionada ao usuario,
        // já carrega o combo com os depatamentos da empresa
        // caso contrário exibe uma mensagem no combo e deixa desabilitado
        // departamentos da empresa
        List<Departamento> departamentos = model.obterListaDepartamentosAtivos(empresas.get(0));

        view.carregarComboDepartamentos(departamentos);
    }

    @Override
    public void empresaSelecionada(Empresa empresa) {
        List<Departamento> departamentos = model.obterListaDepartamentosAtivos(empresa);
        view.carregarComboDepartamentos(departamentos);
    }

}
