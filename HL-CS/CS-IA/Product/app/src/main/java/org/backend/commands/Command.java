package org.backend.commands;

import java.util.Arrays;

public abstract class Command { // command object should not be created. only objects that inherit
                                // command
    private String[] keywords; // first element is default keyword. all others are alternate
    private String description; // description of command
    private String[] argTypes; // argument names (ui)
    private String[] argHints; // argument hints (ui)
    private boolean[] argOptionals; // indicates which arguments are optional. true if optional.

    public Command() {}

    public abstract boolean[] execute(String[] stringArgs);

    /**
     * Before carrying out command execution, this method should be run to ensure the provided
     * arguments are processable by commands. Command execution should only be done if this method
     * returns a boolean array of all true values.
     *
     * @param args user input
     * @return array of boolean values. false values indicate an invalid argument
     */
    protected boolean[] verifyArgs(String[] args) {
        boolean[] valids = new boolean[getArgOptionals().length];
        Arrays.fill(valids, true); // assume everything is valid first

        boolean[] argOptionals = getArgOptionals(); // indicates which args are optional

        for (int i = 0; i < args.length; i++) { // for all strings
            char[] characters = args[i].toCharArray(); // convert input to chars
            valids[i] = false; // assume invalid
            for (char c : characters) { // for each character of arg input
                if (!argOptionals[i] && (Character.isLetter(c) || Character.isDigit(c)))
                    valids[i] = true; // required arg must contain at least one letter or number
            }

            if (argOptionals[i]) // if optional, letter or number requirements do not apply
                valids[i] = true;
        }

        return valids;
    }

    public void setKeywords(String... names) {
        this.keywords = names;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public void setArgTypes(String... argNames) {
        this.argTypes = argNames;
    }

    protected void setArgHints(String... argHints) {
        this.argHints = argHints;
    }

    protected void setArgOptionals(boolean... argOptionals) {
        this.argOptionals = argOptionals;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String getDescription() {
        return description;
    }

    public String[] getArgTypes() {
        return argTypes;
    }

    public String[] getArgHints() {
        return argHints;
    }

    public boolean[] getArgOptionals() {
        return argOptionals;
    }
}
