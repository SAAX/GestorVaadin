package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.RecurrencySet;
import com.saax.gestorweb.model.datamodel.Tarefa;

/**
 * Call back listener interface. <br>
 * The implementations must handle the events thrown when and recurrency were created
 * @author rodrigo
 */
public interface RecurrencyDoneCallBackListener {

    /**
     * Handles the event thrown when the sub windows with all recurrency parameters is closed
     * @param recurrencySet
     */
    public void recurrencyCreationDone(RecurrencySet recurrencySet);

    public void recurrencyRemoved(Tarefa task);


}
