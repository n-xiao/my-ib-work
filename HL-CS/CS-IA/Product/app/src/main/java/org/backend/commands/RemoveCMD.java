package org.backend.commands;

import org.backend.Interpreter;
import org.backend.Rosetta;
import org.backend.Task;
import org.backend.TaskHandler;

public class RemoveCMD extends Command { // removes an existing task
    public RemoveCMD() {
        super();
        setKeywords("remove", "delete", "trash");
        setDescription("Deletes all occurences of a task.");
        setArgTypes("Task");
        setArgHints("Enter name of task to delete");
        setArgOptionals(false);
    }

    @Override
    public boolean[] execute(String[] stringArgs) {
        boolean[] argValids = verifyArgs(stringArgs);
        if (!Interpreter.boolArrayAllTrue(argValids))
            return argValids;

        Rosetta rosetta = Rosetta.getCurrentInstance();
        // handle first arg
        Task targetTask = TaskHandler.findTask(stringArgs[0]);
        if (targetTask == null) {
            argValids[0] = false;
            rosetta.displayError("Could not find requested task.");
            return argValids;
        }
        // execute order 66
        TaskHandler.removeTask(targetTask);

        return argValids;
    }
}
