/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.vaadin.server.VaadinSession;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodrigo
 */
public class ProcessoDemoradoModel {

    public void executarProcessoDemoradoNoServidor() {

        System.out.println("***************** executando passo 1 ***********************");
        simulaProcessoQueDemora10Segundos();

        // verifica se o usuário nao pediu para parar:
        if (getInterromperProcesso()) {

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! ops. usuario mandou parar!");
            return;
        }

        System.out.println("***************** executando passo 2 ***********************");
        simulaProcessoQueDemora10Segundos();

        // verifica se o usuário nao pediu para parar:
        if (getInterromperProcesso()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! ops. usuario mandou parar!");
            return;
        }

        System.out.println("***************** executando passo 3 ***********************");
        simulaProcessoQueDemora10Segundos();

        // verifica se o usuário nao pediu para parar:
        if (getInterromperProcesso()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! ops. usuario mandou parar!");
            return;
        }

        System.out.println("***************** executando passo 4 ***********************");
        simulaProcessoQueDemora10Segundos();

        // verifica se o usuário nao pediu para parar:
        if (getInterromperProcesso()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! ops. usuario mandou parar!");
            return;
        }

        System.out.println("***************** executando passo 5 ***********************");
        simulaProcessoQueDemora10Segundos();

        // verifica se o usuário nao pediu para parar:
        if (getInterromperProcesso()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! ops. usuario mandou parar!");
            return;
        }

        System.out.println("***************** executando passo 6 ***********************");
        simulaProcessoQueDemora10Segundos();

    }

    private boolean getInterromperProcesso() {
        boolean interromper = (Boolean) VaadinSession.getCurrent().getAttribute("interromper");
        return interromper;
    }

    private synchronized void simulaProcessoQueDemora10Segundos() {
        try {
            wait(1000 * 10);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessoDemoradoModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
