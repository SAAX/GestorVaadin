/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.util;

import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author rodrigo
 */
public class DAOAleatorio {

    public static CentroCusto getCentroCustoAleatorio(EntityManager em) {
        List<CentroCusto> centroCustos = em.createNamedQuery("CentroCusto.findAll").getResultList();
        return centroCustos.get(new Random().nextInt(centroCustos.size() - 1));
    }

    public static Departamento getDepartamentoAleatorio(EntityManager em, Empresa e) {
        List<Departamento> departamentos = em.createNamedQuery("Departamento.findByEmpresa")
                .setParameter("empresa", e)
                .getResultList();
        return departamentos.get(new Random().nextInt(departamentos.size() - 1));
    }

    public static EmpresaCliente getEmpresaClienteAleatoria(EntityManager em, Empresa e) {
        List<EmpresaCliente> empresaClientes = em.createNamedQuery("EmpresaCliente.findByEmpresa")
                .setParameter("empresa", e)
                .getResultList();
        return empresaClientes.get(new Random().nextInt(empresaClientes.size() - 1));
    }

    
    public static Date getDataByOffset(int offsetDataAtual, boolean up){
        GregorianCalendar gc = new GregorianCalendar();
        Date hoje = DateUtils.truncate(new Date(), Calendar.DATE);
        gc.setTime(hoje); // hoje truncando as horas

        for (int i = 0; i < offsetDataAtual; i++) {
            gc.roll(GregorianCalendar.DAY_OF_MONTH, up); 
        }
        return DateUtils.truncate(new Date(gc.getTimeInMillis()), Calendar.DATE);
        
    }
}
