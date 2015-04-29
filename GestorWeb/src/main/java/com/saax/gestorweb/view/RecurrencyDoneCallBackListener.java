package com.saax.gestorweb.view;

import java.time.LocalDate;
import java.util.List;

/**
 * Call back listener interface. <br>
 * The implementations must handle the events thrown when and recurrency were created
 * @author rodrigo
 */
public interface RecurrencyDoneCallBackListener {

    /**
     * Handles the event thrown when the sub windows with all recurrency parameters is closed
     * @param tarefasRecorrentes
     */
    public void recurrencyCreationDone(List<LocalDate> tarefasRecorrentes);


}
