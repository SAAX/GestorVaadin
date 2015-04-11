package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.util.DateTimeConverters;
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
     * Compute the parameters for a weekly recurrence to obtain a list of its dates
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

    public void createAnnualRecurrence(Object value, Object value0, Object value1, Object value2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void createMonthlyRecurrence(Object value, Object value0, Object value1, Date value2, Date value3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Creates a list of tasks based on a main task an a list of dates
     * @param task the base (main) task
     * @param recurrentDates the list of dates
     * @return a list of tasks (each one in one date)
     */
    public List<Tarefa> createRecurrentTasks(Tarefa task, List<LocalDate> recurrentDates) {
        
        // TODO: calcular a data final como: data da tarefa + periodo
        
        List<Tarefa> recurrentTasks = new ArrayList<>();
        
        Collections.sort(recurrentDates);
        
        for (LocalDate taskDate : recurrentDates) {
            
            try {
                
                Tarefa recurrentTask = task.clone();
                
                recurrentTask.setUsuarioInclusao(task.getUsuarioInclusao());
                recurrentTask.setUsuarioSolicitante(task.getUsuarioSolicitante());
                recurrentTask.setDataHoraInclusao(LocalDateTime.now());
                
                recurrentTask.setDataInicio(taskDate);
                if (task.getDataFim()!=null){
                    Period p = Period.between(task.getDataInicio(), task.getDataFim());
                    task.setDataFim(taskDate.plus(p));
                }
                
                recurrentTasks.add(recurrentTask);
                
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RecorrenciaModel.class.getName()).log(Level.SEVERE, null, ex);
            }
                
        }

        // configures each next task attribute:
        task.setProximaTarefa(recurrentTasks.get(0));
        for (int i = 0; i < recurrentTasks.size()-1; i++) {
            Tarefa recurrentTask = recurrentTasks.get(i);
            Tarefa nextTask = recurrentTasks.get(i+1);
            recurrentTask.setProximaTarefa(nextTask);
        }
        
        return recurrentTasks;
    }

    
}
