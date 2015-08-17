package com.saax.gestorweb.presenter;

import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.vaadin.ui.ComboBox;
import java.util.List;

/**
 * Está classe é responsavel pela apresentação de itens comuns à vários outros presenters do projeto.
 * É uma biblioteca de métodos estáticos, cujo objetivo é resolver as duplicações de código, por exemplo, 
 * para carregar combos ou fazer configurações comuns de apresentação.
 * @author rodrigo
 */
public class PresenterUtils {

    
    /**
     * Carrega o combo box de departamento informado, com todas os departamentos ativos da empresas 
     * Ou desabilita se não houver departamentos ativos para esta empresa
     * 
     * @param departamentoCombo o combo a ser carregado
     * @param empresa a empresa
     */
    public static void carregaComboDepartamento(ComboBox departamentoCombo, Empresa empresa) {

        if (empresa != null) {

            EmpresaModel empresaModel = new EmpresaModel();
            List<Departamento> departamentoList = empresaModel.obterListaDepartamentosAtivos(empresa);

            if (departamentoList.isEmpty()) {

                departamentoCombo.removeAllItems();
                departamentoCombo.setEnabled(false);

            } else {

                for (Departamento depto : departamentoList) {
                    departamentoCombo.addItem(depto);
                    departamentoCombo.setItemCaption(depto, depto.getDepartamento());
                }
                departamentoCombo.setEnabled(true);
            }
        } else {

            departamentoCombo.removeAllItems();
            departamentoCombo.setEnabled(false);

        }

    }
    
    
    /**
     * Carrega o combo box de centro de custo informado, com todas os centros de custos ativos da empresa
     * Ou desabilita se não houver centros de custo ativos 
     * 
     * @param centroCustoCombo o combo a ser carregado
     * @param empresa a empresa
     */
    public static void carregaComboCentroCusto(ComboBox centroCustoCombo, Empresa empresa) {
        
        // Verify if the company is already set
        if (empresa != null) {

            // Retrieves the list of active departments for this company
            EmpresaModel empresaModel = new EmpresaModel();            
            List<CentroCusto> centroCustoList = empresaModel.obterListaCentroCustosAtivos(empresa);

            if (centroCustoList.isEmpty()) {

                // if there is not any cost center: disable and empty the combo
                centroCustoCombo.removeAllItems();
                centroCustoCombo.setEnabled(false);

            } else {

                // loads the cost center list into the combo
                for (CentroCusto cc : centroCustoList) {
                    centroCustoCombo.addItem(cc);
                    centroCustoCombo.setItemCaption(cc, cc.getCentroCusto());
                }
            }
        } else {

            // if there conmpany has not been setted: disable and empty the combo
            centroCustoCombo.removeAllItems();
            centroCustoCombo.setEnabled(false);

        }

    }

    
    /**
     * Carrega o combo de clientes com todos os clientes ativos de todas as
     * empresas (empresa pricipal + subs ) do usuario logado
     * @param empresaClienteCombo o combo a ser carregado
     */
    public static void carregaComboEmpresaCliente(ComboBox empresaClienteCombo) {
        
        EmpresaModel empresaModel = new EmpresaModel();            
        Usuario usuarioLogado = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());
        
        for (EmpresaCliente cliente : empresaModel.listarEmpresasCliente(usuarioLogado)) {
            empresaClienteCombo.addItem(cliente);
            empresaClienteCombo.setItemCaption(cliente, cliente.getNome());
        }

    }
    
    
    
}
