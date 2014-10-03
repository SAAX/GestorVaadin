package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorException;
import com.vaadin.server.VaadinSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rodrigo
 */
public class EmpresaModel {

    
    /**
     * Listar as coligadas (se existirem)
     *
     * @return
     * @throws com.saax.gestorweb.util.GestorException
     */
    public List<Empresa> listarEmpresasRelacionadas() throws GestorException {

        Usuario usuarioLogado = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");
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
