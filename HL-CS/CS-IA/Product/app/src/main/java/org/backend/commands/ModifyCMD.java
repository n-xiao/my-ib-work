package org.backend.commands;

import java.time.LocalTime;
import java.util.Arrays;
import org.backend.Interpreter;
import org.backend.Rosetta;
import org.backend.Task;
import org.backend.TaskHandler;

public class ModifyCMD extends Command {
    public ModifyCMD() {
        super();
        setKeywords("edit", "modify", "change", "alter");
        setDescription("Modifies an existing task");
        setArgTypes("Task", "Text", "Duration", "Time");
        setArgHints("Enter the name of the task to modify", "(Optional) Set new name",
            "(Optional) Set new duration", "(Optional) Set new start time");
        setArgOptionals(false, true, true, true);
    }

    @Override public boolean[] execute(String[] stringArgs) { // edits an existing task
        boolean[] argValids = verifyArgs(stringArgs);
        if (!Interpreter.boolArrayAllTrue(argValids))
            return argValids;

        Rosetta rosetta = Rosetta.getCurrentInstance();
        String newName = null;
        int newDuration = 0;
        LocalTime newTimeStart = null;

        // get first arg
        Task oldTask = TaskHandler.findTask(stringArgs[0]);
        if (oldTask == null) {
            argValids[0] = false;
            rosetta.displayError("Could not find requested task.");
            return argValids;
        }

        // get second arg
        newName = (!stringArgs[1].isBlank()) ? stringArgs[1] : oldTask.getName();
        // get third arg
        newDuration = (!stringArgs[2].isBlank()) ? Interpreter.parseDuration(stringArgs[2])
                                                 : oldTask.getDuration();
        // get fourth arg
        newTimeStart = (!stringArgs[3].isBlank()) ? Interpreter.parseTime(stringArgs[3])
                                                  : oldTask.getTimeStart();

        Task newTask = (newTimeStart != null) ? new Task(newName, newDuration, newTimeStart)
                                              : new Task(newName, newDuration);

        int statusCode = TaskHandler.replaceTask(oldTask, newTask);

        // error handling
        switch (statusCode) {
            case -2:
                rosetta.displayError("A task with the same name already exists.");
                argValids[1] = false;
                break;
            case -1:
                rosetta.displayError(
                    "Invalid duration / start time provided. Check your input values.");
                argValids[2] = false;
                argValids[3] = false;
                break;
            case 0: // do nothing and be happy
                break;
            case 1:
                rosetta.displayError(
                    "There is not enough free time to accomodate the new duration.");
                argValids[2] = false;
                break;
            case 2:
                rosetta.displayError("New start time will cause tasks to overlap.");
                argValids[3] = false;
                break;

            default:
                rosetta.displayError(
                    "An error occurred. Code: " + statusCode + ". Contact the developer.");
                Arrays.fill(argValids, false);
                break;
        }

        return argValids;
    }
}
