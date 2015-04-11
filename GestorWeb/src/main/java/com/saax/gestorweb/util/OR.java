/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author rodrigo
 */
public class OR {

    /**
     * Metodo que gera as obrigações anuais com base nos parametros
     *
     * @param diaAnual : dia do ano / pri / ult
     * @param mesAnual : mes do ano
     * @param anoTermino : ano final
     */

    final SimpleDateFormat dateFormatDDMMYYYY = new SimpleDateFormat(
            "dd/MM/yyyy");
//Alterei a variável util de boolean para String

    public void gerarObrigacoesAnuais(Object diaAnual, Object mesAnual,
            Object anoTermino, Object idEmpresa, Object codigoReceita,
            Object descricao, Object tipoArquivo, String util, String utilCorrido, String recorrencia, Object avisoAnual) {

        int anoInicial = GregorianCalendar.getInstance().get(Calendar.YEAR);

        int anoFinal = Integer.parseInt(anoTermino.toString());

        int mesDoAno = Integer.parseInt(mesAnual.toString());
        mesDoAno -= 1; // no java janeiro é o mês Zero

        System.out.println("util: " + util);
        for (int ano = anoInicial; ano <= anoFinal; ano++) {

            int diaDoMes = tratarDiaDoMes(diaAnual, mesDoAno, ano, util);
            GregorianCalendar dataObrigacao = new GregorianCalendar(ano,
                    mesDoAno, diaDoMes);

            String diaExtenso = diaAnual.toString();
            String mesExtenso = mesAnual.toString();
            String anoExtenso = anoTermino.toString();

            if (dataObrigacao.getTime().after(new Date())) {
                System.out.println("Criar obrigacao para "
                        + new SimpleDateFormat("dd/MM/yyyy")
                        .format(dataObrigacao.getTime()));


            }
        }
    }

    //Alterei o boolean util para String 

    public void gerarObrigacoesMensais(Object diaMensal, int qtdeMes,
            String util, Date inicioMensal, Date terminoMensal,
            Object idEmpresa, Object codigoReceita, Object descricao,
            Object tipoArquivo, String utilCorrido, String recorrencia, String qtdeRecorrencia, Object avisoMensal) {
        System.out.println("entrou no metodo");

        String diaExtenso = diaMensal.toString();

        // lista das datas que devem ter obrigação gerada
        List<Date> datasObrigacoes = new ArrayList<>();

        // Calendarios para a data final
        Calendar terminoMensalCalendar = GregorianCalendar.getInstance();
        terminoMensalCalendar.setTime(terminoMensal);

        // Calendario que irá avançando (iterando) da data inicial até a final
        Calendar dataIteracao = GregorianCalendar.getInstance();
        dataIteracao.setTime(inicioMensal);

        // Tratar dia do Mês
        int diaDoMes = tratarDiaDoMes(diaMensal,
                dataIteracao.get(Calendar.MONTH),
                dataIteracao.get(Calendar.YEAR), util);

		// avança dia-a-dia até p 1o. dia apos (ou igual) a da inicial indicada
        // pelo usuario que corresponda a este dia da semana
        while (dataIteracao.get(Calendar.DAY_OF_MONTH) != diaDoMes) {
            System.out.println(dataIteracao.getTime());
            System.out.println(diaDoMes);

            dataIteracao.roll(Calendar.DAY_OF_YEAR, true);

            diaDoMes = tratarDiaDoMes(diaMensal,
                    dataIteracao.get(Calendar.MONTH),
                    dataIteracao.get(Calendar.YEAR), util);

            if ((dataIteracao.get(Calendar.DAY_OF_YEAR) == dataIteracao
                    .getActualMinimum(Calendar.DAY_OF_YEAR))) {
                dataIteracao.roll(Calendar.YEAR, true);
            }

        }

        // este será o 1o. dia da obrigação
        datasObrigacoes.add(dataIteracao.getTime());

        System.out.println("antes do While");

        // vai avancando, semana a semana, até a data final
        do {

            int avanco = 1 * (qtdeMes);

            // verifica se o avanço nao vai passar o ano
            boolean avacarAno = ((dataIteracao.get(Calendar.MONTH) + avanco) > 11);

            // avança 1 mês * a quantidade de Meses indicada pelo usuario
            dataIteracao.roll(Calendar.MONTH, (avanco));

            if (avacarAno) {
                dataIteracao.roll(Calendar.YEAR, true);
            }

            // verifica se ainda é menor ou igual que a data de termino
            if (dataIteracao.compareTo(terminoMensalCalendar) <= 0) {
				// configura a data como dia da orbigação
                // Tratar dia do Mês
                diaDoMes = tratarDiaDoMes(diaMensal,
                        dataIteracao.get(Calendar.MONTH),
                        dataIteracao.get(Calendar.YEAR), util);
                dataIteracao.set(Calendar.DAY_OF_MONTH, diaDoMes);

                datasObrigacoes.add(dataIteracao.getTime());
            }

        } while (dataIteracao.compareTo(terminoMensalCalendar) <= 0);

        System.out.println("final do While");

        for (Date date : datasObrigacoes) {

            GregorianCalendar dataObrigacao = new GregorianCalendar();

            dataObrigacao.setTime(date);

        }

    }

    private List<Date> calcularDatasObrigacoesSemanais(Date inicioSemanal,
            Date terminoSemanal, int qtdeSemanas, List<Integer> diaSemana) {

        // lista das datas que devem ter obrigação gerada
        List<Date> datasObrigacoes = new ArrayList<>();

		// percorre todos os dias da semana marcados pelo usuario para geraçao
        // da obrigaçao
        for (Integer diaSemanaConsiderado : diaSemana) {

            // Calendarios para a data final
            Calendar terminoSemanalCalendar = GregorianCalendar.getInstance();
            terminoSemanalCalendar.setTime(terminoSemanal);

			// Calendario que irá avançando (iterando) da data inicial até a
            // final
            Calendar dataIteracao = GregorianCalendar.getInstance();
            dataIteracao.setTime(inicioSemanal);

			// avança dia-a-dia até p 1o. dia apos (ou igual) a da inicial
            // indicada
            // pelo usuario que corresponda a este dia da semana
            while (dataIteracao.get(Calendar.DAY_OF_WEEK) != diaSemanaConsiderado) {
                dataIteracao.roll(Calendar.DAY_OF_YEAR, true);
            }

            // este será o 1o. dia da obrigação
            datasObrigacoes.add(dataIteracao.getTime());

            // vai avancando, semana a semana, até a data final
            do {

                int avanco = 7 * (qtdeSemanas);

                // verifica se o avanço nao vai passar o ano
                boolean avacarAno = ((dataIteracao.get(Calendar.DAY_OF_YEAR) + avanco) > 365);

                // avança 7 dias * a quantidade de semanas indicada pelo usuario
                dataIteracao.roll(Calendar.DAY_OF_YEAR, (avanco));

                if (avacarAno) {
                    dataIteracao.roll(Calendar.YEAR, true);
                }

                // verifica se ainda é menor ou igual que a data de termino
                if (dataIteracao.compareTo(terminoSemanalCalendar) <= 0) {
                    // configura a data como dia da orbigação
                    datasObrigacoes.add(dataIteracao.getTime());
                }

            } while (dataIteracao.compareTo(terminoSemanalCalendar) <= 0);

        }

        // ordena pela data
        Collections.sort(datasObrigacoes);

        return datasObrigacoes;

    }

    /**
     * Gera obrigacoes semanais de acordo com os parametros
     *
     * @param inicioSemanal
     * @param terminoSemanal
     * @param qtdeSemanas
     * @param diaSemana
     */
    public void gerarObrigacoesSemanais(Date inicioSemanal,
            Date terminoSemanal, int qtdeSemanas, List<Integer> diaSemana,
            Object codigoReceita, Object descricao, Object tipoArquivo,
            Object idEmpresa, String recorrencia, String domingo, String segunda, String terca, String quarta, String quinta, String sexta, String sabado, String qtdeRecorrencia, Object avisoSemanal) {

        // lista das datas que devem ter obrigação gerada
        List<Date> datasObrigacoes = calcularDatasObrigacoesSemanais(
                inicioSemanal, terminoSemanal, qtdeSemanas, diaSemana);

        // Percorre a lista de datas de obrigação
        for (Date date : datasObrigacoes) {
            System.out.println("Gerar obrigação para: "
                    + dateFormatDDMMYYYY.format(date));
            System.out
                    .println("Criar obrigacao para "
                            + new SimpleDateFormat("dd/MM/yyyy").format(date
                                    .getTime()));

            GregorianCalendar dataObrigacao = new GregorianCalendar();

            dataObrigacao.setTime(date);


        }
    }

    /**
     * Identifica e trata a variável diaAnual. <br>
     * Esta pode ser: <br>
     * pri: primeiro dia util do mes ult: ultimo dia util dia do mes numero: dia
     * util: se for True, trazer somente dia útil, caso False, dia corrido. NOVO
     * COMENTÁRIO: tive de mudar o método do util, pois não é mais boolean
     * porque temos 3 opções: util, util com sábado e corrido
     */
    public int tratarDiaDoMes(Object diaAnual, int mes, int ano, String util) {

        // no caso de 1o. dia útil do mês
        if (diaAnual instanceof String && diaAnual.equals("pri")) {
            System.out.println("verificou que é primeiro dia");

            // cria a data do 1o. dia do mes
            GregorianCalendar dia = new GregorianCalendar(ano, mes, 1);
            System.out.println("que tipo de dia: " + util);
            // testa se o primeiro dia do mes eh sabado ou domingo
            if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && util.equals("util")) {
                System.out.println("verificou que é sabado");
                // coloca para proxima segunda
                dia.roll(Calendar.DAY_OF_MONTH, true);
                dia.roll(Calendar.DAY_OF_MONTH, true);
            }
            if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && util.equals("utilSabado")) {
                System.out.println("verificou que é sabado");
				// coloca para proxima segunda
                //dia.roll(Calendar.DAY_OF_MONTH, true);
                //dia.roll(Calendar.DAY_OF_MONTH, true);
            } else if ((dia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                // coloca para proxima segunda
                dia.roll(Calendar.DAY_OF_MONTH, true);
            }

            return dia.get(Calendar.DAY_OF_MONTH);
        }

        // no caso de ultimo dia útil do mês
        if (diaAnual instanceof String && diaAnual.equals("ult")) {

            // cria a data como ultimo dia do mes
            GregorianCalendar dia = new GregorianCalendar(ano, mes, 1);
            dia.set(Calendar.DAY_OF_MONTH,
                    dia.getActualMaximum(Calendar.DAY_OF_MONTH));

            // testa se o ultimo dia do mes eh sabado ou domingo
            if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && util.equals("util")) {
                // coloca para sexta anterior
                dia.roll(Calendar.DAY_OF_MONTH, false);
                dia.roll(Calendar.DAY_OF_MONTH, false);
            }
            if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && util.equals("utilSabado")) {
                // coloca para sexta anterior
                dia.roll(Calendar.DAY_OF_MONTH, false);
            } else if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                // coloca para sexta anterior
                dia.roll(Calendar.DAY_OF_MONTH, false);
            }

            return dia.get(Calendar.DAY_OF_MONTH);
        }

        boolean ehInteiro = true;

        int diaAnualInt = 0;
        try {
            diaAnualInt = Integer.parseInt((String) diaAnual);
        } catch (NumberFormatException numberFormatException) {
            ehInteiro = false;
        }

        // se não é nem o 1o. nem o último, é um dia simples
        if (diaAnual instanceof Integer || ehInteiro) {

            if (util.equals("util")) {
                System.out.println("entrou no método para verificar dias normais úteis");
                GregorianCalendar primeiroDia = new GregorianCalendar(ano, mes,
                        1);
                int quantidadeAvancos;
                if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    quantidadeAvancos = diaAnualInt;
                } else {
                    quantidadeAvancos = diaAnualInt - 1;
                }
                for (int a = 0; a < quantidadeAvancos; a++) {

                    // testa se o primeiro dia do mes eh sabado ou domingo
                    if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        // coloca para proxima segunda
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                    } else if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        // coloca para proxima segunda
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                    } else {
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                    }
                }

                return primeiroDia.get(Calendar.DAY_OF_MONTH);

            } //precisa retir
            else if (util.equals("utilSabado")) {
                System.out.println("entrou no método para verificar dias úteis com sabado");

                GregorianCalendar primeiroDia = new GregorianCalendar(ano, mes,
                        1);
                int quantidadeAvancos;
                if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    quantidadeAvancos = diaAnualInt;
                } // else if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                //	quantidadeAvancos = diaAnualInt -1;
                //} 
                //else if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
                //	quantidadeAvancos = diaAnualInt -1;
                //}
                else {
                    quantidadeAvancos = diaAnualInt - 1;
                }
                for (int a = 0; a < quantidadeAvancos; a++) {

                    // testa se o primeiro dia do mes eh sabado ou domingo
                    if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
						// coloca para proxima segunda
                        //primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                    } else if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						// coloca para proxima segunda
                        //primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                    } else {
                        primeiroDia.roll(Calendar.DAY_OF_MONTH, true);
                    }
                }

                return primeiroDia.get(Calendar.DAY_OF_MONTH);

            } //precisa retirar se der errado
            else {
                System.out.println("entrou no método para verificar dias normais corridos");
                GregorianCalendar primeiroDia = new GregorianCalendar(ano, mes,
                        diaAnualInt);
                if ((primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                    primeiroDia.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                }
                return primeiroDia.get(Calendar.DAY_OF_MONTH);
            }

        }

        return 0;
    }
}
