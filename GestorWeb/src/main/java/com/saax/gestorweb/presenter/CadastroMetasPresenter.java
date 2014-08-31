/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetasModel;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroMetasView;
import com.saax.gestorweb.view.CadastroMetasViewListener;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo
 */
public class CadastroMetasPresenter implements CadastroMetasViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final CadastroMetasView view;
    private final CadastroMetasModel model;

    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public CadastroMetasPresenter(CadastroMetasModel model,
            CadastroMetasView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);
        
        configurarEstadoInicial();
        
    }

    /**
     * Evento disparado ao ser aberta a tela de cadastro de metas Seu objetivo e
     * configurar o estado inicial do form, com os combos carregados, mensagens
     * configuradas etc.
     */
    @Override
    public final void configurarEstadoInicial() {

        try {
        
            // 1. Configura o combo de empresas

            // caso o usuário esteja relacionado com várias empresas, exibe um 
            // combo para seleção de empresa para a qual ele quer selecionar a meta
            // caso contrário exibe um combo desabilitado, com a empresa já indicada.
            List<Empresa> empresas = model.obterListaEmpresasUsuarioLogado();

            view.carregarComboEmpresas(empresas);
            if (empresas.size() == 1) {
                view.selecionarEmpresa(empresas.get(0));
                view.desabilitarSelecaoEmpresas();
            }


            // 2. Configura o combo de seleção de departamentos:
        
            // caso exista apenas uma empresa relacionada ao usuario,
            // já carrega o combo com os depatamentos da empresa
            // caso contrário exibe uma mensagem no combo e deixa desabilitado
            if (empresas.size() == 1) {

                // departamentos da empresa
                List<Departamento> departamentos = model.obterListaDepartamentosAtivos(empresas.get(0));

                view.carregarComboDepartamentos(departamentos);
            } else {

                view.exibirMensagemComboDepartamentos("Selecione a empresa");
                view.desabilitarSelecaoDepartamentos();
            }

        } catch (GestorException ex) {
            Logger.getLogger(CadastroMetasPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
