package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.util.DateTimeConverters;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fernando/Rodrigo
 */
public class RecorrenciaModel {

    /**
     * Compute the parameters for a weekly recurrence to obtain a list of its
     * dates
     *
     * @param weekDays
     * @param numberWeeks
     * @param startDate
     * @param endDate
     * @return a list of dates
     */
    public List<LocalDate> createWeeklyRecurrence(Set<Integer> weekDays, Integer numberWeeks, Date startDate, Date endDate) {

        // lista das datas das tarefas recorrentes
        List<LocalDate> dataTarefas = new ArrayList<>();

        // percorre todos os dias da semana marcados pelo usuario para geraçao
        // da obrigaçao
        for (Integer diaSemanaConsiderado : weekDays) {

            // Calendario para a data final
            Calendar terminoSemanalCalendar = GregorianCalendar.getInstance();
            terminoSemanalCalendar.setTime(endDate);

            // Calendario que irá avançando (iterando) da data inicial até a
            // final
            Calendar dataIteracao = GregorianCalendar.getInstance();
            dataIteracao.setTime(startDate);

            // avança dia-a-dia até p 1o. dia apos (ou igual) a da inicial
            // indicada
            // pelo usuario que corresponda a este dia da semana
            while (dataIteracao.get(Calendar.DAY_OF_WEEK) != diaSemanaConsiderado) {
                dataIteracao.roll(Calendar.DAY_OF_YEAR, true);
            }

            // este será o 1o. dia da obrigação
            dataTarefas.add(DateTimeConverters.toLocalDate(dataIteracao.getTime()));

            // vai avancando, semana a semana, até a data final
            do {

                int avanco = 7 * (numberWeeks);

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
                    dataTarefas.add(DateTimeConverters.toLocalDate(dataIteracao.getTime()));
                }

            } while (dataIteracao.compareTo(terminoSemanalCalendar) <= 0);

        }

        // ordena pela data
        Collections.sort(dataTarefas);

        return dataTarefas;

    }

    public List<LocalDate>  createAnnualRecurrence(String diaAnual, String util, String mesAnual, String anoTermino) {
        
        int anoInicial = GregorianCalendar.getInstance().get(Calendar.YEAR);

        // lista das datas das tarefas recorrentes
        List<LocalDate> dataTarefas = new ArrayList<>();

        int anoFinal = Integer.parseInt(anoTermino);

        int mesDoAno = Integer.parseInt(mesAnual);
        mesDoAno -= 1; // no java janeiro é o mês Zero

        System.out.println("util: " + util);
        for (int ano = anoInicial; ano <= anoFinal; ano++) {

            int diaDoMes = tratarDiaDoMes(diaAnual, mesDoAno, ano, util);
            
            GregorianCalendar dataObrigacao = new GregorianCalendar(ano,mesDoAno, diaDoMes);

            if (dataObrigacao.getTime().after(new Date())) {
                dataTarefas.add(DateTimeConverters.toLocalDate(dataObrigacao.getTime()));

            }
        }

        // ordena pela data
        Collections.sort(dataTarefas);

        return dataTarefas;

    }

    /**
     * Identifica e trata a variável diaAnual. <br>
     * Esta pode ser: <br>
     * pri: primeiro dia util do mes ult: ultimo dia util dia do mes numero: dia
     * util: se for True, trazer somente dia útil, caso False, dia corrido. NOVO
     * COMENTÁRIO: tive de mudar o método do util, pois não é mais boolean
     * porque temos 3 opções: util, util com sábado e corrido
     *
     * @param diaAnual
     * @param mes
     * @param ano
     * @param util
     * @return
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

    public List<LocalDate> createMonthlyRecurrence(String dayMonth, Integer numberOfMonths, String typeMonth, Date startDate, Date endDate) {

        // lista das datas das tarefas recorrentes
        List<LocalDate> datasObrigacoes = new ArrayList<>();

        // Calendarios para a data final
        Calendar terminoMensalCalendar = GregorianCalendar.getInstance();
        terminoMensalCalendar.setTime(endDate);

        // Calendario que irá avançando (iterando) da data inicial até a final
        Calendar dataIteracao = GregorianCalendar.getInstance();
        dataIteracao.setTime(startDate);

        // Tratar dia do Mês
        int diaDoMes = tratarDiaDoMes(dayMonth,
                dataIteracao.get(Calendar.MONTH),
                dataIteracao.get(Calendar.YEAR), typeMonth);

        // avança dia-a-dia até p 1o. dia apos (ou igual) a da inicial indicada
        // pelo usuario que corresponda a este dia da semana
        while (dataIteracao.get(Calendar.DAY_OF_MONTH) != diaDoMes) {
            System.out.println(dataIteracao.getTime());
            System.out.println(diaDoMes);

            dataIteracao.roll(Calendar.DAY_OF_YEAR, true);

            diaDoMes = tratarDiaDoMes(dayMonth,
                    dataIteracao.get(Calendar.MONTH),
                    dataIteracao.get(Calendar.YEAR), typeMonth);

            if ((dataIteracao.get(Calendar.DAY_OF_YEAR) == dataIteracao
                    .getActualMinimum(Calendar.DAY_OF_YEAR))) {
                dataIteracao.roll(Calendar.YEAR, true);
            }

        }

        // este será o 1o. dia da obrigação
        datasObrigacoes.add(DateTimeConverters.toLocalDate(dataIteracao.getTime()));

        // vai avancando, semana a semana, até a data final
        do {

            int avanco = 1 * (numberOfMonths);

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
                diaDoMes = tratarDiaDoMes(dayMonth,
                        dataIteracao.get(Calendar.MONTH),
                        dataIteracao.get(Calendar.YEAR), typeMonth);
                dataIteracao.set(Calendar.DAY_OF_MONTH, diaDoMes);

                datasObrigacoes.add(DateTimeConverters.toLocalDate(dataIteracao.getTime()));
            }

        } while (dataIteracao.compareTo(terminoMensalCalendar) <= 0);

        // ordena pela data
        Collections.sort(datasObrigacoes);

        return datasObrigacoes;
    }

    /**
     * Creates a list of tasks based on a main task an a list of dates
     *
     * @param task the base (main) task
     * @param recurrentDates the list of dates
     * @return the first task of the recuurrency set
     */
    public Tarefa createRecurrentTasks(Tarefa task, List<LocalDate> recurrentDates) {

        List<Tarefa> recurrentTasks = new ArrayList<>();

        Collections.sort(recurrentDates);

        for (LocalDate taskDate : recurrentDates) {

            try {

                Tarefa recurrentTask = task.clone();

                recurrentTask.setTipoRecorrencia(TipoTarefa.RECORRENTE);
                recurrentTask.setUsuarioInclusao(task.getUsuarioInclusao());
                recurrentTask.setUsuarioSolicitante(task.getUsuarioSolicitante());
                recurrentTask.setDataHoraInclusao(LocalDateTime.now());

                recurrentTask.setDataInicio(taskDate);
                if (task.getDataFim() != null) {
                    Period p = Period.between(task.getDataInicio(), task.getDataFim());
                    task.setDataFim(taskDate.plus(p));
                }

                recurrentTasks.add(recurrentTask);

            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RecorrenciaModel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // configures each next task attribute:
        for (int i = 0; i < recurrentTasks.size() - 1; i++) {
            Tarefa previousTask = recurrentTasks.get(i);
            Tarefa nextTask = recurrentTasks.get(i + 1);
            previousTask.setProximaTarefa(nextTask);
        }

        return recurrentTasks.get(0);
    }

    public void removeAllRecurrency(Tarefa tarefa) {

        // TODO: remove all predecent
        // remove all next
        removeAllNextRecurrency(tarefa);

    }

    public void removeAllNextRecurrency(Tarefa tarefa) {
        Tarefa tarefaIterator = tarefa.getProximaTarefa();
        while (tarefaIterator != null) {
            tarefaIterator.setRemovida(true);
            tarefaIterator = tarefaIterator.getProximaTarefa();
        }
    }

}
