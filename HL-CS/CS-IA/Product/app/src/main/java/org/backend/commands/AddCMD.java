package org.backend.commands;

import java.time.LocalTime;
import java.util.Arrays;
import org.backend.Interpreter;
import org.backend.Rosetta;
import org.backend.Task;
import org.backend.TaskHandler;

public class AddCMD extends Command {
    public AddCMD() {
        super();
        setKeywords("add", "create", "new", "make");
        setDescription("Adds a new task to the day");
        setArgTypes("Text", "Duration", "Time");
        setArgHints("Set task title", "Set task duration", "(Optional) Set task time");
        setArgOptionals(false, false, true);
    }

    @Override public boolean[] execute(String[] stringArgs) { // adds a new task to the day
        boolean[] argValids = verifyArgs(stringArgs);
        if (!Interpreter.boolArrayAllTrue(argValids))
            return argValids;
        // handle first arg
        String name = null;
        if (!stringArgs[0].isBlank())
            name = stringArgs[0];
        else
            argValids[0] = false;
        // handle second arg
        int durationInMins = Interpreter.parseDuration(stringArgs[1]);
        if (durationInMins <= 0)
            argValids[1] = false;
        // handle third arg
        LocalTime parsedTime = Interpreter.parseTime(stringArgs[2]);
        if (parsedTime == null && !stringArgs[2].isBlank())
            argValids[2] = false;

        Rosetta rosetta = Rosetta.getCurrentInstance();

        if (!Interpreter.boolArrayAllTrue(argValids))
            return argValids;

        // execute
        Task newTask = (stringArgs[2].isBlank()) ? new Task(name, durationInMins)
                                                 : new Task(name, durationInMins, parsedTime);
        int statusCode = TaskHandler.addTask(newTask);

        // error handling
        switch (statusCode) {
            case -2:
                rosetta.displayError("A task with the same name already exists.");
                argValids[0] = false;
                break;
            case -1:
                rosetta.displayError("Could not fragment task! Check your input value.");
                argValids[1] = false;
                break;
            case 0: // do nothing and be happy
                break;
            case 1:
                rosetta.displayError("Insufficient free time to add the specificied task.");
                argValids[1] = false;
                break;
            case 2:
                rosetta.displayError("You cannot add a task that overlaps existing tasks.");
                argValids[2] = false;
                break;
            default:
                rosetta.displayError(
                    "An error occurred. Code: " + statusCode + ". Contact the developer.");
                Arrays.fill(argValids, false);
                break;
        }

        return argValids; // bool array with all true values means command was successfully executed
    }
}
