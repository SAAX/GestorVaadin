package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.GestorSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rodrigo
 */
public class EmpresaModel {

    /**
     * Obtem a lista de empresas possiveis de seleção para o usuário logado
     *
     * @param usuarioLogado
     * @return a empresa principal do usuário + as coligadas (se existirem)
     */
    public List<Empresa> listarEmpresasParaSelecao(Usuario usuarioLogado) {

        Empresa empresa = usuarioLogado.getEmpresaAtiva();

        List<Empresa> empresas = new ArrayList<>();

        empresas.add(empresa);

        for (Empresa subempresa : empresa.getSubEmpresas()) {
            if (subempresa.getAtiva()) {
                empresas.add(subempresa);
            }

        }

        return empresas;
    }

}
