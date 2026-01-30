package org.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.backend.commands.Command;

// singleton class
public class Rosetta { // allows communication between Java and JavaScript
    private static Rosetta instance = null;
    private WebView wv = null;
    private Command[] cmds = null;
    private int selectedCmdPos = 0;

    private Rosetta(WebView wv) {
        this.wv = wv;
    }

    public static void initialise(WebView wv) {
        if (instance == null)
            instance = new Rosetta(wv);

        wv.getEngine().documentProperty().addListener((newDoc) -> { // prevents garbage collection
            if (newDoc != null) { // on document load
                instance.enableJavaScriptComms();
                instance.populateTasks();
            }
        });
    }

    public static Rosetta getCurrentInstance() {
        return instance;
    }

    /**
     * Monitors for hotkeys such as Enter, Tab, Space and Escape so that they can be forwarded to
     * the frontend.
     *
     * @param oceania the main javafx scene (the app window)
     */
    public static void startBigBrother(Scene oceania) {
        // alt. solution as js keydown, keypress and keyup events dont work here
        Rosetta rosetta = Rosetta.getCurrentInstance();
        if (rosetta == null)
            return;
        /*
         * ChatGPT was used to generate the proper syntax for "oceania.addEventFilter
         * (KeyEvent.Key_PRESSED, event -> {})" Code within the lambda function was NOT generated,
         * in any way, by AI
         */
        oceania.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            String selectedIndexParam = String.valueOf(rosetta.getSelectedCmdPos());
            String key = "";
            KeyCode keyCode = event.getCode();

            switch (keyCode) { // converts recognised hotkeys into string
                case KeyCode.ENTER:
                    key = "Enter";
                    break;
                case KeyCode.TAB:
                    key = "Tab";
                    break;
                case KeyCode.SPACE:
                    key = "Space";
                    break;
                case KeyCode.ESCAPE:
                    key = "Escape";
                    break;
                default:
                    break;
            }

            if (!key.isEmpty()) { // sends detected key to the front end
                rosetta.executeJS("handleCommandSubmitKey", key, selectedIndexParam);
                event.consume();
            }
        });
    }

    public void enableJavaScriptComms() {
        JSObject window = (JSObject) wv.getEngine().executeScript("window");
        window.setMember("rosetta", instance); // this (instance) is exposed to javascript frontend
    }

    public void populateTasks() { // adds tasks to the front end
        ArrayList<Task> tasks = TaskHandler.scheduledTasks;
        for (Task task : tasks) {
            String taskTitle = task.getName();
            String taskTimeStart = task.getTimeStart().toString();
            String taskTimeEnd = task.getTimeEnd().toString();
            String fixedStatus = (task.isFixed()) ? "manual" : "auto-allocated";
            executeJS("addTask", taskTitle, taskTimeStart, taskTimeEnd, fixedStatus);
        }
    }

    public void populateArgs() { // calls frontend methods to create input boxes
        executeJS("initArgsContainer"); // starts a container to hold the inputs

        String[] argTypes = cmds[selectedCmdPos].getArgTypes(); // gets arg types

        for (int i = 0; i < argTypes.length; i++) {
            executeJS("createArg", argTypes[i], String.valueOf(i));
        }

        executeJS("updateSelectedArg");
    }

    /**
     * Called when the user types in the command bar. Calls JavaScript methods to display frontend
     * information.
     *
     * @param input the user input
     */
    public void handleCommandInput(String input) {
        executeJS("adjustInputWidth"); // frontend ui adjustment to fit text being typed

        Command[] commands = Interpreter.getPotentialCommands(input); // gets best matching cmds for
                                                                      // input

        Collections.reverse(Arrays.asList(commands)); // the best match will be displayed at the
                                                      // bottom of the list

        cmds = commands.clone(); // store a copy of the cmds that are being displayed to the user
        selectedCmdPos = cmds.length - 1; // updates the value of the current selected cmd position

        executeJS("prepareCommandView"); // adds a (list) container for the commands to the UI
        for (int i = 0; i < commands.length; i++) {
            String cmdName = commands[i].getKeywords()[0];
            String cmdDesc = commands[i].getDescription();

            executeJS("addCommand", cmdName, cmdDesc); // command is added and displayed
        }

        executeJS("selectCommandAtPos",
            String.valueOf(selectedCmdPos)); // indicate current selected cmd to user
    }

    public void handleCommandunfocus() { // called from index.html
        // deletes list of cmds in the frontend when the cmd bar is not selected
        executeJS("deleteCommandView");
    }

    /**
     * Called by the JavaScript frontend when the user presses enter to execute a command after
     * setting input values. It cleans up frontend ui elements and advises the user if there are
     * required inputs that are left blank.
     *
     * @param jsObject Javascript array of input values as strings
     */
    public void executeCommand(Object jsObject) {
        String[] args = jsToJavaStringArrayConvert(jsObject);

        if (args == null)
            return;

        boolean[] valids = cmds[selectedCmdPos].execute(args); // executes the command
        // cleans up the user interface
        if (Interpreter.boolArrayAllTrue(valids)) {
            executeJS("resetCommandView");
            executeJS("clearTaskList");
            executeJS("resetArgInputs");
            populateTasks(); // refreshes the task list
            return;
        }
        // indicate any errors to the user
        boolean[] argOptionals = cmds[selectedCmdPos].getArgOptionals();
        for (int i = 0; i < valids.length; i++) {
            if (valids[i])
                executeJS("setArgAsNormal", String.valueOf(i));
            else {
                executeJS("setArgAsInvalid", String.valueOf(i));
            }

            if (!argOptionals[i] && args[i].isBlank())
                displayError("Please enter required input(s)");
        }
    }

    /**
     * Display the argument hint at a certain index of the currently selected command to the
     * frontend.
     *
     * @param index the index of the argument
     */
    public void requestArgHint(int index) {
        Command selCommand = cmds[selectedCmdPos];
        String argTitle = selCommand.getKeywords()[0];
        String argBody = selCommand.getArgHints()[index];
        executeJS("showArgHint", argTitle, argBody);
    }

    public void displayError(String msg) { // exposes the javascript method to other classes
        executeJS("showError", msg);
    }

    public int getSelectedCmdPos() {
        return selectedCmdPos;
    }

    public void setSelectedCmdPos(int selectedCmdPos) {
        this.selectedCmdPos = selectedCmdPos;
    }

    /**
     * Converts a string Javascript array to a Java <code>String</code> array
     *
     * @param jsObject the Javascript string array to convert
     * @return <b>String[]</b> the converted <code>String</code> array
     */
    private String[] jsToJavaStringArrayConvert(Object jsObject) {
        // sourced from chatgpt
        if (jsObject instanceof JSObject jsStringArray) {
            int len = (int) jsStringArray.getMember("length");
            String[] javaArray = new String[len];
            // copies string values to new array
            for (int i = 0; i < len; i++) {
                javaArray[i] = (String) jsStringArray.getSlot(i);
            }

            return javaArray;
        }

        return null;
    }

    /**
     * This method executes a javascript method from java. Do not call if methodName is from a
     * userinput as it is unsanitised. args are sanitised. Only string args are allowed.
     *
     * @param methodName name of the method, without the () at the end
     * @param args any arguments that should be included
     */
    private void executeJS(String methodName, String... args) {
        StringBuilder methodCall = new StringBuilder(methodName);
        methodCall.append("("); // open bracket to start adding args

        for (int i = 0; i < args.length; i++) {
            methodCall.append("String.raw`" + args[i] + "`"); // appends args and sanitises

            if (i < args.length - 1)
                methodCall.append(", "); // specify that there is next arg
        }

        methodCall.append(");"); // close bracket to complete the method call
        wv.getEngine().executeScript(methodCall.toString()); // executes javascript
    }
}
