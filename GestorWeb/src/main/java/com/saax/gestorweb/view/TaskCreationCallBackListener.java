package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Task;

/**
 * Call back listener interface. <br>
 * The implementations must handle the events thrown when an creation or update of task is done
 * @author rodrigo
 */
public interface TaskCreationCallBackListener {

    /**
     * Handles the event thrown when the sub window is done with a Task creation 
     * @param createdTask 
     */
    public void taskCreationDone(Task createdTask);

    /**
     * Handles the event thrown when the sub window is done with a Task update
     * @param updatedTask 
     */
    public void taskUpdateDone(Task updatedTask);

}
