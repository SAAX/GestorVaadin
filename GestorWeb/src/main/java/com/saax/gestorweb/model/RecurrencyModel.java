package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.RecurrencyEnums;
import com.saax.gestorweb.model.datamodel.RecurrencySet;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.DateTimeConverters;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
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
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Fernando/Rodrigo
 */
public  class RecurrencyModel {

    /**
     * Creates a user friendly message summarizing the recurrency configuration
     * set
     *
     * @param weekDays
     * @param numberWeeks
     * @param startDate
     * @param endDate
     * @return
     */
    private static String formatRecurrencyWeeklyMessage(Set<Integer> weekDays, Integer numberWeeks, Date startDate, Date endDate) {

        StringBuilder recurrencyMessage = new StringBuilder();

        recurrencyMessage.append("Tarefa recorrente: semanal, a cada ");
        recurrencyMessage.append(numberWeeks);
        recurrencyMessage.append(" semana(s), em toda: ");

        for (Integer weekDay : weekDays) {
            recurrencyMessage.append(weekDay);
            recurrencyMessage.append("a., ");

        }

        recurrencyMessage.append("de: ");
        recurrencyMessage.append(FormatterUtil.formatDate(DateTimeConverters.toLocalDate(startDate)));
        recurrencyMessage.append(" até: ");
        recurrencyMessage.append(FormatterUtil.formatDate(DateTimeConverters.toLocalDate(endDate)));

        return recurrencyMessage.toString();
    }

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
    public static RecurrencySet createWeeklyRecurrence(Set<Integer> weekDays, Integer numberWeeks, Date startDate, Date endDate) {

        RecurrencySet recurrencySet = new RecurrencySet();

        // lista das datas das tarefas recorrentes
        recurrencySet.setRecurrentDates(new ArrayList<>());

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
            recurrencySet.getRecurrentDates().add(DateTimeConverters.toLocalDate(dataIteracao.getTime()));
            if (recurrencySet.getFirstTaskStartDate()==null){
                recurrencySet.setFirstTaskStartDate(DateTimeConverters.toLocalDate(dataIteracao.getTime()));
                
            } else {
                LocalDate newFirstTaskStartDateToSet = DateTimeConverters.toLocalDate(dataIteracao.getTime());
                LocalDate firstTaskStartDateAlreadySet = recurrencySet.getFirstTaskStartDate();
                if (newFirstTaskStartDateToSet.isBefore(firstTaskStartDateAlreadySet)){
                    recurrencySet.setFirstTaskStartDate(newFirstTaskStartDateToSet);
                    
                }
                
            }

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
                    recurrencySet.getRecurrentDates().add(DateTimeConverters.toLocalDate(dataIteracao.getTime()));
                }

            } while (dataIteracao.compareTo(terminoSemanalCalendar) <= 0);

        }

        // ordena pela data
        Collections.sort(recurrencySet.getRecurrentDates());

        recurrencySet.setRecurrencyMessage(formatRecurrencyWeeklyMessage(weekDays, numberWeeks, startDate, endDate));

        return recurrencySet;

    }

    /**
     * Creates a user friendly message summarizing the recurrency configuration
     * set
     *
     * @return
     */
    private static String formatRecurrencyAnnualMessage(RecurrencyEnums.DayType dayType, Integer annualDay, RecurrencyEnums.WorkingDayType workingDayType, String mesAnual, String anoTermino) {

        StringBuilder recurrencyMessage = new StringBuilder();

        recurrencyMessage.append("Tarefa recorrente: anual, todo ");
        
        if (dayType == RecurrencyEnums.DayType.FIRST_WORKING_DAY) {
            recurrencyMessage.append("1o. dia útil");

        } else if (dayType == RecurrencyEnums.DayType.LAST_MONTH_DAY) {
            recurrencyMessage.append("últim dia do mês");

        } else {
            recurrencyMessage.append("dia ").append(annualDay.toString());

        }
        recurrencyMessage.append(" do mês ");
        recurrencyMessage.append(mesAnual);
        
        switch (workingDayType) {
            case BUSINESS_DAY:
                recurrencyMessage.append(", considerando apenas dias úteis,");

                break;
            case BUSINESS_DAY_INCLUDING_SATURDAY:
                recurrencyMessage.append(", considerando dias úteis, incluindo sábados, ");

                break;
            case CALENDAR_DAY:

                break;
            default:
                throw new AssertionError();
        }

        
        recurrencyMessage.append(" até ");
        recurrencyMessage.append(anoTermino);

        return recurrencyMessage.toString();
    }

    public static RecurrencySet createAnnualRecurrence(RecurrencyEnums.DayType dayType, Integer annualDay, RecurrencyEnums.WorkingDayType workingDayType, String mesAnual, String anoTermino) {

        RecurrencySet recurrencySet = new RecurrencySet();

        // lista das datas das tarefas recorrentes
        recurrencySet.setRecurrentDates(new ArrayList<>());

        int anoInicial = GregorianCalendar.getInstance().get(Calendar.YEAR);

        int anoFinal = Integer.parseInt(anoTermino);

        int mesDoAno = Integer.parseInt(mesAnual);
        mesDoAno -= 1; // no java janeiro é o mês Zero

        for (int ano = anoInicial; ano <= anoFinal; ano++) {

            int diaDoMes = tratarDiaDoMes(dayType, annualDay, mesDoAno, ano, workingDayType);

            GregorianCalendar dataObrigacao = new GregorianCalendar(ano, mesDoAno, diaDoMes);

            if (dataObrigacao.getTime().after(new Date())) {
                recurrencySet.getRecurrentDates().add(DateTimeConverters.toLocalDate(dataObrigacao.getTime()));
                if (recurrencySet.getFirstTaskStartDate() == null) {
                    recurrencySet.setFirstTaskStartDate(DateTimeConverters.toLocalDate(dataObrigacao.getTime()));
                }

            }
        }

        // ordena pela data
        Collections.sort(recurrencySet.getRecurrentDates());

        recurrencySet.setRecurrencyMessage(formatRecurrencyAnnualMessage(dayType, annualDay, workingDayType, mesAnual, anoTermino));

        return recurrencySet;

    }

    /**
     * Identifica e trata a variável diaAnual. <br>
     * Esta pode ser: <br>
     * pri: primeiro dia util do mes ult: ultimo dia util dia do mes numero: dia
     * util: se for True, trazer somente dia útil, caso False, dia corrido. NOVO
     * COMENTÁRIO: tive de mudar o método do util, pois não é mais boolean
     * porque temos 3 opções: util, util com sábado e corrido
     *
     * @param dayType
     * @param day
     * @param mes
     * @param ano
     * @param util
     * @return
     */
    public static int tratarDiaDoMes(RecurrencyEnums.DayType dayType, Integer day, int mes, int ano, RecurrencyEnums.WorkingDayType util) {

        // no caso de 1o. dia útil do mês
        if (dayType == RecurrencyEnums.DayType.FIRST_WORKING_DAY) {
            System.out.println("verificou que é primeiro dia");

            // cria a data do 1o. dia do mes
            GregorianCalendar dia = new GregorianCalendar(ano, mes, 1);
            System.out.println("que tipo de dia: " + util);
            // testa se o primeiro dia do mes eh sabado ou domingo
            if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && util == RecurrencyEnums.WorkingDayType.BUSINESS_DAY) {
                System.out.println("verificou que é sabado");
                // coloca para proxima segunda
                dia.roll(Calendar.DAY_OF_MONTH, true);
                dia.roll(Calendar.DAY_OF_MONTH, true);
            }
            if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && util == RecurrencyEnums.WorkingDayType.BUSINESS_DAY_INCLUDING_SATURDAY) {
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
        if (dayType == RecurrencyEnums.DayType.LAST_MONTH_DAY) {

            // cria a data como ultimo dia do mes
            GregorianCalendar dia = new GregorianCalendar(ano, mes, 1);
            dia.set(Calendar.DAY_OF_MONTH,
                    dia.getActualMaximum(Calendar.DAY_OF_MONTH));

            // testa se o ultimo dia do mes eh sabado ou domingo
            if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && util == RecurrencyEnums.WorkingDayType.BUSINESS_DAY) {
                // coloca para sexta anterior
                dia.roll(Calendar.DAY_OF_MONTH, false);
                dia.roll(Calendar.DAY_OF_MONTH, false);
            }
            if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && util == RecurrencyEnums.WorkingDayType.BUSINESS_DAY_INCLUDING_SATURDAY) {
                // coloca para sexta anterior
                dia.roll(Calendar.DAY_OF_MONTH, false);
            } else if (dia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                // coloca para sexta anterior
                dia.roll(Calendar.DAY_OF_MONTH, false);
            }

            return dia.get(Calendar.DAY_OF_MONTH);
        }

        if (day != null) {

            if (util == RecurrencyEnums.WorkingDayType.BUSINESS_DAY) {
                System.out.println("entrou no método para verificar dias normais úteis");
                GregorianCalendar primeiroDia = new GregorianCalendar(ano, mes,
                        1);
                int quantidadeAvancos;
                if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    quantidadeAvancos = day;
                } else {
                    quantidadeAvancos = day - 1;
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

            } else if (util == RecurrencyEnums.WorkingDayType.BUSINESS_DAY_INCLUDING_SATURDAY) {
                System.out.println("entrou no método para verificar dias úteis com sabado");

                GregorianCalendar primeiroDia = new GregorianCalendar(ano, mes, 1);
                int quantidadeAvancos;
                if (primeiroDia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    quantidadeAvancos = day;
                } else {
                    quantidadeAvancos = day - 1;
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
                GregorianCalendar primeiroDia = new GregorianCalendar(ano, mes, day);
                return primeiroDia.get(Calendar.DAY_OF_MONTH);
            }

        }

        return 0;
    }

    /**
     * Creates a user friendly message summarizing the recurrency configuration
     * set
     *
     * @return
     */
    private static String formatRecurrencyMonthlyMessage(RecurrencyEnums.DayType dayType, Integer monthDay, Integer numberOfMonths, RecurrencyEnums.WorkingDayType workingDayType, Date startDate, Date endDate) {

        StringBuilder recurrencyMessage = new StringBuilder();
        recurrencyMessage.append("Tarefa recorrente: mensal, todo ");

        if (dayType == RecurrencyEnums.DayType.FIRST_WORKING_DAY) {
            recurrencyMessage.append("1o. dia útil");

        } else if (dayType == RecurrencyEnums.DayType.LAST_MONTH_DAY) {
            recurrencyMessage.append("últim dia do mês");

        } else {
            recurrencyMessage.append("dia ").append(monthDay.toString());

        }

        recurrencyMessage.append(" a cada ");
        recurrencyMessage.append(numberOfMonths);
        recurrencyMessage.append(" mes(es), ");
        switch (workingDayType) {
            case BUSINESS_DAY:
                recurrencyMessage.append("considerando apenas dias úteis,");

                break;
            case BUSINESS_DAY_INCLUDING_SATURDAY:
                recurrencyMessage.append("considerando dias úteis, incluindo sábados, ");

                break;
            case CALENDAR_DAY:

                break;
            default:
                throw new AssertionError();
        }

        recurrencyMessage.append(" de: ");
        recurrencyMessage.append(FormatterUtil.formatDate(DateTimeConverters.toLocalDate(startDate)));
        recurrencyMessage.append(" até: ");
        recurrencyMessage.append(FormatterUtil.formatDate(DateTimeConverters.toLocalDate(endDate)));

        return recurrencyMessage.toString();
    }

    public static RecurrencySet createMonthlyRecurrence(RecurrencyEnums.DayType dayType, Integer monthDay, Integer numberOfMonths, RecurrencyEnums.WorkingDayType workingDayType, Date startDate, Date endDate) {

        RecurrencySet recurrencySet = new RecurrencySet();

        // lista das datas das tarefas recorrentes
        recurrencySet.setRecurrentDates(new ArrayList<>());

        // Calendarios para a data final
        Calendar terminoMensalCalendar = GregorianCalendar.getInstance();
        terminoMensalCalendar.setTime(endDate);

        // Calendario que irá avançando (iterando) da data inicial até a final
        Calendar dataIteracao = GregorianCalendar.getInstance();
        dataIteracao.setTime(startDate);

        // Tratar dia do Mês
        int diaDoMes = tratarDiaDoMes(dayType, monthDay,
                dataIteracao.get(Calendar.MONTH),
                dataIteracao.get(Calendar.YEAR), workingDayType);

        // avança dia-a-dia até p 1o. dia apos (ou igual) a da inicial indicada
        // pelo usuario que corresponda a este dia da semana
        while (dataIteracao.get(Calendar.DAY_OF_MONTH) != diaDoMes) {
            System.out.println(dataIteracao.getTime());
            System.out.println(diaDoMes);

            dataIteracao.roll(Calendar.DAY_OF_YEAR, true);

            diaDoMes = tratarDiaDoMes(dayType,
                    monthDay,
                    dataIteracao.get(Calendar.MONTH),
                    dataIteracao.get(Calendar.YEAR), workingDayType);

            if ((dataIteracao.get(Calendar.DAY_OF_YEAR) == dataIteracao
                    .getActualMinimum(Calendar.DAY_OF_YEAR))) {
                dataIteracao.roll(Calendar.YEAR, true);
            }

        }

        // este será o 1o. dia da obrigação
        recurrencySet.getRecurrentDates().add(DateTimeConverters.toLocalDate(dataIteracao.getTime()));
        recurrencySet.setFirstTaskStartDate(DateTimeConverters.toLocalDate(dataIteracao.getTime()));

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
                diaDoMes = tratarDiaDoMes(dayType,
                        monthDay,
                        dataIteracao.get(Calendar.MONTH),
                        dataIteracao.get(Calendar.YEAR), workingDayType);
                dataIteracao.set(Calendar.DAY_OF_MONTH, diaDoMes);

                recurrencySet.getRecurrentDates().add(DateTimeConverters.toLocalDate(dataIteracao.getTime()));
            }

        } while (dataIteracao.compareTo(terminoMensalCalendar) <= 0);

        // ordena pela data
        Collections.sort(recurrencySet.getRecurrentDates());

        recurrencySet.setRecurrencyMessage(formatRecurrencyMonthlyMessage(dayType, monthDay, numberOfMonths, workingDayType, startDate, endDate));

        return recurrencySet;
    }

    private static int getRecurrencySequenceNextValue() {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        Query query = em.createNativeQuery("SELECT NEXTVAL('RecurrencySequence')");
        List<Long> results = query.getResultList();
        Long nextValue = results.get(0);

        if (nextValue == 0) {
            throw new IllegalStateException("Recurrency sequency not set");
        }

        return nextValue.intValue();

    }

    /**
     * Creates a list of tasks based on a main task an a list of dates
     *
     * @param task the base (main) task
     * @param recurrentDates the list of dates
     * @return the first task of the recuurrency set
     */
    public static Tarefa createRecurrentTasks(Tarefa task, List<LocalDate> recurrentDates, String recurrencyMessage) {

        List<Tarefa> recurrentTasks = new ArrayList<>();

        Collections.sort(recurrentDates);

        final int FIRST_INDEX = 0;
        final int RECURRENCY_ID = getRecurrencySequenceNextValue();
        Tarefa firstTaskOfTheRecurrentSet = null;

        for (LocalDate taskDate : recurrentDates) {

            try {

                Tarefa recurrentTask = task.clone();

                recurrentTask.setRecurrencyMessage(recurrencyMessage);
                recurrentTask.setTipoRecorrencia(TipoTarefa.RECORRENTE);
                recurrentTask.setUsuarioInclusao(task.getUsuarioInclusao());
                recurrentTask.setUsuarioSolicitante(task.getUsuarioSolicitante());
                recurrentTask.setDataHoraInclusao(LocalDateTime.now());

                // if it is the first task of a recurrent set
                if (taskDate.equals(recurrentDates.get(FIRST_INDEX))) {

                    // configures the first task var return later
                    firstTaskOfTheRecurrentSet = recurrentTask;

                }
                recurrentTask.setRecurrencyID(RECURRENCY_ID);
                recurrentTask.setDataInicio(taskDate);

                if (task.getDataFim() != null) {
                    Period p = Period.between(task.getDataInicio(), task.getDataFim());
                    recurrentTask.setDataFim(taskDate.plus(p));
                }

                recurrentTasks.add(recurrentTask);

            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RecurrencyModel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // configures each next task attribute:
        for (int i = 0; i < recurrentTasks.size() - 1; i++) {
            Tarefa previousTask = recurrentTasks.get(i);
            Tarefa nextTask = recurrentTasks.get(i + 1);
            previousTask.setProximaTarefa(nextTask);
        }

        return firstTaskOfTheRecurrentSet;
    }

    private static void removeTask(Tarefa task, Usuario loggedUser) {
        TarefaModel.removerTarefa(task, loggedUser);

    }

    /**
     * Removes all recurrent tasks, given any task of the set
     *
     * @param task any task of the set
     * @param loggedUser the logged user
     * @return
     */
    public static Tarefa removeAllRecurrency(Tarefa task, Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        Tarefa firstTaskOfRecurrentSet = null;
        List<Tarefa> recurrenTasks;

        try {
            em.getTransaction().begin();

            // retrieves all the tasks in the set
            recurrenTasks = em.createNamedQuery("Tarefa.findByRecurrencyID").setParameter("recurrencyID", task.getRecurrencyID()).getResultList();

            // iterates over all the tasks in the recurrent set, marking as removed
            for (Tarefa recurrentTask : recurrenTasks) {

                if (firstTaskOfRecurrentSet == null) {
                    firstTaskOfRecurrentSet = recurrentTask;
                }

                removeTask(recurrentTask, loggedUser);
                em.merge(recurrentTask);
            }

            firstTaskOfRecurrentSet = em.find(Tarefa.class, firstTaskOfRecurrentSet.getId());

            em.getTransaction().commit();

            return firstTaskOfRecurrentSet;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            // propaga a exceção pra cima
            throw ex;

        }

    }

    /**
     * Removes all next recurrent task, given any task of the recurrent set
     *
     * @param task
     * @param loggedUser
     * @return
     */
    public static Tarefa removeAllNextRecurrency(Tarefa task, Usuario loggedUser) {

        Tarefa editingTask = task;

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            // verify if there is a next recurrent task to remove
            if (task.getProximaTarefa() == null) {
                return editingTask;
            }

            em.getTransaction().begin();

            Tarefa taskIterator = em.find(Tarefa.class, task.getProximaTarefa().getId());
            while (taskIterator != null) {

                removeTask(taskIterator, loggedUser);
                em.merge(taskIterator);
                if (taskIterator.getProximaTarefa() != null) {
                    taskIterator = em.find(Tarefa.class, taskIterator.getProximaTarefa().getId());
                } else {
                    taskIterator = null;

                }

            }

            em.getTransaction().commit();

            editingTask = em.find(Tarefa.class, editingTask.getId());

            return editingTask;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            // propaga a exceção pra cima
            throw ex;

        }

    }

}
