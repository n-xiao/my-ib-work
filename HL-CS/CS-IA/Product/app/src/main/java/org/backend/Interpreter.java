package org.backend;

import java.time.LocalTime;
import org.backend.commands.AddCMD;
import org.backend.commands.Command;
import org.backend.commands.ModifyCMD;
import org.backend.commands.RemoveCMD;

public class Interpreter { // utility class

    private Interpreter() {}

    /**
     * Checks if a boolean array contains values that are all true.
     *
     * @param bools
     * @return returns true if there are no false elements in bools, false if there is at least one
     *     false element.
     */
    public static boolean boolArrayAllTrue(boolean[] bools) {
        for (boolean b : bools) {
            if (!b)
                return false;
        }
        return true;
    }

    /**
     * This method attempts to parse common short-hand expressions of time, such as "12pm" and
     * "2am". It converts these shortened time expressions from String to a new LocalTime object.
     *
     * @param rawStringTime the <code>String</code> to be converted into a LocalTime object
     * @return a new <code>LocalTime</code> object
     *     <li><b>null</b> if input could not be parsed
     */
    public static LocalTime parseTime(String rawStringTime) {
        if (rawStringTime.isBlank())
            return null;

        String stringInput = rawStringTime.toLowerCase();
        boolean isPM = stringInput.contains("pm");
        boolean isAM = stringInput.contains("am");
        StringBuilder numerics = new StringBuilder();
        char[] chars = stringInput.toCharArray();
        // copy all digits to numerics
        for (char c : chars) {
            if (Character.isDigit(c))
                numerics.append(c);
        }
        // make numerics length of 4
        if (numerics.length() == 1) {
            numerics.insert(0, "0");
            numerics.append("00");
        } else if (numerics.length() == 3) {
            numerics.insert(0, "0");
        } else if (numerics.length() == 2) {
            numerics.append("00");
        }

        if (numerics.length() != 4)
            return null;
        // get hours
        int hour = Integer.parseInt(numerics.substring(0, 2));
        int minute = Integer.parseInt(numerics.substring(2));
        // conversions to 24 hour format
        if (hour != 12 && isPM)
            hour += 12;
        else if (hour == 12 && isAM) {
            hour = 0;
        }

        return LocalTime.of(hour, minute);
    }

    /**
     * Converts a string format of a duration to minutes as an int. For example, 2h 30m returns 150
     * (minutes). 2 hours and 30 minutes also returns 150 (minutes).
     *
     * @param stringDuration input <code>String</code> to convert into a duration
     * @return <b>duration in minutes</b> as an <code>int</code> value greater than 0
     *     <li><b>-1</b> if the parsed duration is less than or equal to 0
     */
    public static int parseDuration(String stringDuration) {
        stringDuration = stringDuration.toLowerCase();
        char[] characters = stringDuration.toCharArray();
        int minutes = 0;
        int hours = 0;

        StringBuilder currentNum = new StringBuilder();
        for (char c : characters) { // extract digit
            if (Character.isDigit(c)) {
                currentNum.append(c);
            } else if (c == 'h') { // identify if currentNum is in hours
                hours = Integer.parseInt(currentNum.toString());
                currentNum = new StringBuilder();
            } else if (c == 'm') { // identify if currentNum is in minutes
                minutes = Integer.parseInt(currentNum.toString());
                currentNum = new StringBuilder();
            }
        }

        if (minutes + hours <= 0) // if no amount of time is collected
            return -1;

        return minutes + 60 * hours; // return duration in minutes
    }

    /**
     * Retrieves the best three commands as a sorted array of <code>Command</code>objects, starting
     * with the closest matches, for a given <code>userInput</code>. If there is an exact match
     * between the <code>userInput</code> and a <code>Command</code> object, an array consisting of
     * that single <code>Command</code> will be returned.
     *
     * @param userInput the command that is being typed out by the user
     * @return an <b>array</b> of <code>Command</code> elements
     */
    public static Command[] getPotentialCommands(String userInput) {
        Command[] commands = {new AddCMD(), new ModifyCMD(), new RemoveCMD()};
        Command[] potentialCommands = new Command[3];

        if (userInput.length() == 0) { // if the user input is empty, just return first three
                                       // without processing
            for (int i = 0; i < potentialCommands.length; i++) potentialCommands[i] = commands[i];

            return potentialCommands;
        }

        // selection sort. closest matching cmd is at the front
        for (int i = 0; i < commands.length; i++) {
            int shortestDistPos = 0;
            int shortestDis = Integer.MAX_VALUE;

            for (int j = i; j < commands.length; j++) {
                Command cmd = commands[j];
                int dist = matchCommandNames(userInput, cmd);

                if (dist < shortestDis) {
                    shortestDis = dist;
                    shortestDistPos = j;
                }
            }
            // swap
            Command temp = commands[i];
            commands[i] = commands[shortestDistPos];
            commands[shortestDistPos] = temp;
        }

        if (commands[0].getKeywords()[0].equals(userInput)) { // if there is an exact match
            potentialCommands = new Command[1];
            potentialCommands[0] = commands[0];

            return potentialCommands; // return the exact match only
        }

        for (int i = 0; i < potentialCommands.length; i++) // else, get best 3 cmds
            potentialCommands[i] = commands[i];

        return potentialCommands; // return best 3 cmds
    }

    /**
     * Adjusts the given <code>Command</code> to better match the user input. Since commands accept
     * multiple different keywords, such as "create" and "add" for adding a new task to the day,
     * this method will allow the user interface to display the best matching keyword for a given
     * <code>String</code> and <code>Command</code>.
     *
     * @param input the user input
     * @param cmd the command that should try and match the user input
     * @return <b>int</b> value of the levenshtein distance between the best matching <code>cmd
     *     </code> keyword to the <code>input</code>
     */
    private static int matchCommandNames(String input, Command cmd) {
        String[] names = cmd.getKeywords();
        int bestDist = Integer.MAX_VALUE;
        int posOfBestDist = -1;

        // get the levenshtein distances of each cmd keyword
        for (int i = 0; i < names.length; i++) {
            int currentDist = getLevenshteinDistance(input.toLowerCase(), names[i].toLowerCase());

            if (currentDist < bestDist) { // store best distance
                bestDist = currentDist;
                posOfBestDist = i;
            }
        }

        // swap best matching keyword with default keyword
        String temp = names[posOfBestDist];
        names[posOfBestDist] = names[0];
        names[0] = temp;

        return bestDist; // returns best distance for the provided cmd
    }

    // method below adapted from
    // https://www.geeksforgeeks.org/java-program-to-implement-levenshtein-distance-computing-algorithm/
    /**
     * Edit distance algorithm which measures how closely two strings match. The smaller the
     * distance, the closer the match of the two provided strings. Method has a bias, where having
     * to append characters for a match of the two strings is penalised less than replacement or
     * removal since this method is often called when the user is in the process of typing
     * something.
     *
     * @param input first <code>String</code>
     * @param target second <code>String</code>
     * @return <b>int</b> the levenshtein distance distance between the two strings provided in the
     *     parameters
     */
    public static int getLevenshteinDistance(String input, String target) {
        int[][] dp = new int[input.length() + 1][target.length() + 1];

        for (int i = 0; i < dp.length; i++) // scenario where second str is empty
            dp[i][0] = i;

        for (int i = 0; i < dp[0].length; i++) // scenario where first str is empty
            dp[0][i] = i;

        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {
                int append = dp[i][j - 1] + 1; // append to input
                int remove = dp[i - 1][j] + 2; // remove from input (double penalty)
                int replace = dp[i - 1][j - 1]; // assume no replacement

                if (input.charAt(i - 1) != target.charAt(j - 1)) // if chars at pos i dont match
                    replace += 2; // add replacement penalty (double penalty)

                dp[i][j] = Math.min(append,
                    Math.min(remove,
                        replace)); // find min of append, remove and replace ops
            }
        }

        return dp[input.length()][target.length()]; // returns the last element of the 2d array
                                                    // (bottom right element)
    }
}
