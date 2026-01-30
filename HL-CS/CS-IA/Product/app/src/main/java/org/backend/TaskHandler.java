package org.backend;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TaskHandler { // utility class
    public static ArrayList<Task> scheduledTasks;

    private TaskHandler() {}

    static {
        scheduledTasks = new ArrayList<>();
    }

    public static LocalTime getCurrentTime() {
        return LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * Adds a <code>FixedTask</code> to the <code>scheduledTasks</code>
     * ArrayList. Returns ints depending on the method outcome.
     *
     * @param task the <code>FixedTask</code> that should be added
     * @return <b>-2</b> a <code>task</code> with the same name already exists
     * <li><b>-1</b> the provided <code>task</code> has an incorrect duration,
     * such as if it was
     * 0
     * <li><b>0</b> the provided <code>task</code> was successfully added
     * <li><b>1</b> there is not enough free time for the <code>task</code> to
     * be added <li><b>2</b> the provided <code>task</code> is conflicting with
     * an existing scheduled task
     */
    private static int addFixedTask(Task task) {
        final LocalTime CURRENT_TIME = getCurrentTime();
        // error checking
        if (task.getDuration() == 0)
            return -1;
        if (taskAlreadyExist(task))
            return -2;
        // a task to midnight (24:00) will be auto corrected to 23:59
        if (task.getTimeStart().until(task.getTimeEnd(), ChronoUnit.HOURS)
            == -1 * task.getTimeStart().getHour())
            task.setDuration(task.getDuration() - 1);
        // prevent task from going to next day
        else if (task.getTimeStart().isAfter(task.getTimeEnd()))
            return 1;
        if (scheduledTasks.isEmpty()) { // if no existing tasks, just add the task
            scheduledTasks.add(task);
            return 0;
        }

        int totalFreeTime = 0;
        ArrayList<Integer> freeTimes = getFreeTime(CURRENT_TIME);

        for (Integer integer : freeTimes) // gets total free time in day
            totalFreeTime += integer.intValue();

        // checks to make sure there is enough free time
        if (task.getDuration() > totalFreeTime)
            return 1;

        int pos = 0;
        for (int i = 0; i < scheduledTasks.size(); i++) {
            Task current = scheduledTasks.get(i);

            if (task.overlaps(current)) // tasks must not overlap
                return 2;

            if (current.getTimeStart().isBefore(task.getTimeStart()))
                pos++; // finds position in scheduledTasks to add task to
        }

        scheduledTasks.add(pos, task); // adds task

        return 0; // success code
    }

    /**
     * Gets every duration of uninterrupted time in which there are no ongoing
     * tasks. In other words, it gets the durations in between tasks. All free
     * time in the past is ignored. It is retrieved in an
     * <code>ArrayList</code> of <code>Integers</code>, with durations in
     * minutes. The returned <code>ArrayList</code> is ordered relative to the
     * order of existing tasks in the day.
     *
     * @param currentTime the current time according to the user's system
     * @return <b>ArrayList</b> collection of free time in minutes
     */
    private static ArrayList<Integer> getFreeTime(LocalTime currentTime) {
        if (scheduledTasks.isEmpty())
            return null;

        ArrayList<Integer> freeTimes = new ArrayList<>();
        Task nextTask = getNextOccuringTask(currentTime);
        int posOfNext = scheduledTasks.indexOf(nextTask);
        int minsFromNowToNext = Integer.MAX_VALUE;
        int minsFromPrevToNext = Integer.MAX_VALUE;

        minsFromNowToNext = (nextTask == null) // adds the free time at the start
            ? (int) currentTime.until(LocalTime.MAX, ChronoUnit.MINUTES) // if no next task
            : (int) currentTime.until(nextTask.getTimeStart(), ChronoUnit.MINUTES); // if got next

        // if on-going task may end closer to start of nextTask compared to current time
        Task currentTask = getCurrentOccuringTask(currentTime);

        if (currentTask != null && nextTask != null)
            minsFromPrevToNext = (int) currentTask.minsUntil(nextTask);

        int minsFromNext = Math.min(minsFromNowToNext, minsFromPrevToNext); // gets the first
                                                                            // occuring free time
        freeTimes.add(minsFromNext);

        if (nextTask == null)
            return freeTimes; // returns duration from current time to 23:59

        // adds the free times found between existing tasks
        for (int i = posOfNext; i < scheduledTasks.size() - 1; i++) {
            Task current = scheduledTasks.get(i);
            Task next = scheduledTasks.get(i + 1);
            freeTimes.add(current.minsUntil(next));
        }

        // adds the free time from the end of last task to 23:59
        Task lastTask = scheduledTasks.getLast();
        freeTimes.add((int) lastTask.getTimeEnd().until(LocalTime.MAX, ChronoUnit.MINUTES));

        return freeTimes;
    }

    private static Task getCurrentOccuringTask(LocalTime currentTime) {
        for (Task task : scheduledTasks) {
            if (task.isOngoing(currentTime))
                return task;
        }
        return null;
    }

    /**
     * Returns the next task that occurs from the present time. If there is an
     * ongoing task, it will be ignored. An ongoing task is classified as a task
     * with a start time before the current time, and an end time after the
     * current time.
     *
     * @param currentTime the current time of the user
     * @return the next <code>Task</code> that has not yet started
     */
    public static Task getNextOccuringTask(LocalTime currentTime) {
        if (scheduledTasks.isEmpty())
            return null;

        int pastPos = 0;
        int futurePos = scheduledTasks.size() - 1;
        // binary search for the next task
        while (pastPos < futurePos) {
            int currentPos = (pastPos + futurePos) / 2;
            Task task = scheduledTasks.get(currentPos);
            if (!task.isAfter(currentTime)) {
                pastPos = currentPos + 1; // immediate next. not on-going task
            } else {
                futurePos = currentPos;
            }
        }
        // if there is no next task, return null
        Task nextTask = scheduledTasks.get(pastPos);
        if (!nextTask.isAfter(currentTime)) {
            nextTask = null;
        }

        return nextTask;
    }

    /**
     * Attempts to automatically allocate tasks as early as possible, relative
     * to the user's system time. It is able to add tasks in between other
     * existing tasks. Returns integers, based on the outcome of the
     * allocation.
     *
     * @param task the <code>task</code> to be automatically allocated
     * @return <b>-2</b> a <code>task</code> with the same name already exists
     * <li><b>-1</b> the <code>task</code> in parameter has an unprocessable
     * duration. For example, if the <code>task</code> duration is 0.
     * <li><b>0</b> successful allocation of <code>task</code> provided
     * <li><b>1</b> there is not enough free time to allocate the
     * <code>task</code>
     */
    private static int autoAllocate(Task task) {
        final LocalTime CURRENT_TIME = getCurrentTime();

        if (task.getDuration() == 0)
            return -1;
        if (task.getFragmentOf() == null && taskAlreadyExist(task))
            return -2;

        // get all free times
        ArrayList<Integer> freeTimes = getFreeTime(CURRENT_TIME);

        int nextTaskPos = scheduledTasks.indexOf(getNextOccuringTask(CURRENT_TIME));
        int taskDuration = task.getDuration();

        // if scheduledTasks is empty or if there is no next task
        if (nextTaskPos < 0
            && CURRENT_TIME.until(LocalTime.MAX, ChronoUnit.MINUTES) >= taskDuration) {
            task.setTimeStart(CURRENT_TIME);
            scheduledTasks.add(task);
            return 0;
        } else if (nextTaskPos < 0) // if no next task but too close to end of day
            return 1;

        // allocating tasks in between existing tasks. think of each free time
        // as being found in front of an existing task. this is why the last
        // free time has to be handled separately (delete me and put in explanation)

        // allocate tasks in between tasks
        for (int i = nextTaskPos; i < scheduledTasks.size(); i++) {
            int freeTime = freeTimes.get(i - nextTaskPos).intValue();
            if (freeTime >= taskDuration) {
                task.setTimeStart(
                    scheduledTasks.get(i).getTimeStart().minus(freeTime, ChronoUnit.MINUTES));
                scheduledTasks.add(i, task);
                return 0;
            }
        }

        // allocating a task at the end of the day
        int freeTime = freeTimes.getLast().intValue();
        if (freeTime >= taskDuration) {
            task.setTimeStart(scheduledTasks.getLast().getTimeEnd());
            scheduledTasks.add(task);
            return 0;
        }

        // if taskDuration is too big for any free time
        if (task.getFragmentOf() != null) // do not allow frags of frags
            return 1;
        // sorts in descending order
        Collections.sort(freeTimes, Collections.reverseOrder());

        int[] fragFormat = fragsFormatter(taskDuration, freeTimes); // get frags duration
        if (fragFormat.length == 0) { // not enough free time / task is too small to be frag
            return -1;
        }
        for (int i = 0; i < fragFormat.length; i++) {
            Task fragTask = new Task(task.getName(), fragFormat[i]);
            fragTask.setFragmentOf(task);
            autoAllocate(fragTask);
        }

        return 0;
    }

    /**
     * This method first assigns a maximum number of times a task can be split
     * from 2 to 4 depending on the duration of the task. It tries to apply
     * smaller fragmentations than the maximum to prevent unnecessary
     * splitting.
     *
     * @param taskDuration
     * @param freeTimes
     * @return returns an array of integers that dictate how the original task
     *     will be split up. An
     * array of length 0 indicates that the original task should not be
     * fragmented.
     */
    private static int[] fragsFormatter(int taskDuration, ArrayList<Integer> freeTimes) {
        if (taskDuration <= 55)
            return new int[0];

        int maxFrags = -1;
        // client defined these constants to decide max no. of frags for
        // certain durations
        final int[] TIME_LIMITS = {55, 150, 210};
        final int[] MAX_FRAGS = {2, 3, 4};
        for (int i = 0; i < TIME_LIMITS.length - 1; i++) {
            if (maxFrags == -1 && TIME_LIMITS[i] < taskDuration
                && taskDuration <= TIME_LIMITS[i + 1])
                maxFrags = MAX_FRAGS[i];
        }
        if (maxFrags == -1)
            maxFrags = MAX_FRAGS[MAX_FRAGS.length - 1];

        // get minimum no. of possible frags
        int duration = taskDuration;
        int minFrags = 0;
        while (duration > 0 && minFrags < freeTimes.size()) {
            duration -= freeTimes.get(minFrags++).intValue();
        }

        if (duration > 0 || minFrags > maxFrags) // not enough time in each free time
            return new int[0];

        int[] fragTemplate = new int[minFrags];
        final int MIN_FRAG_DURATION = 30; // client defined this
        duration = taskDuration; // reset the duration
        // populate fragTemplate greedily
        int freeTimePos = 0;
        for (int i = 0; i < fragTemplate.length && freeTimePos < freeTimes.size(); i++) {
            int freeTime = freeTimes.get(freeTimePos++).intValue();
            if (freeTime >= MIN_FRAG_DURATION && duration > freeTime) {
                fragTemplate[i] = freeTime;
                duration -= freeTime;
            } else if (freeTime >= MIN_FRAG_DURATION) {
                fragTemplate[i] = duration;
                duration = 0;
            } else {
                --i;
            }
        }

        if (duration == 0)
            return fragTemplate;

        // attempt to balance out fragTemplate values
        for (int i = minFrags; i <= maxFrags; i++) { // from min frags to max frags allowed
            ArrayList<Integer> modFreeTimes = new ArrayList<>();
            // make a shallow copy of free
            freeTimes.forEach((d) -> modFreeTimes.add(Integer.valueOf(d.intValue())));
            // make all durations a multiple of 5 or 10
            fragTemplate = makeFiveTenMultiple(i, taskDuration);

            int templatePos = 0;

            for (int j = 0; j < modFreeTimes.size() && templatePos < fragTemplate.length; j++) {
                // get free time at j
                int currentFree = modFreeTimes.get(j).intValue();
                if (currentFree >= fragTemplate[templatePos]) { // if free time more than frag time
                    // occupy free time and repeat without
                    // incrementing j. increment templatePos
                    modFreeTimes.set(
                        j--, Integer.valueOf(currentFree - fragTemplate[templatePos++]));
                }
            }

            if (templatePos == fragTemplate.length)
                return fragTemplate;
        }

        // if all methods of task fragmentation fails
        return new int[0];
    }

    /**
     * Creates and returns an integer array with a specified length,
     * <code>arrLen</code> with values at position 1 and greater being
     * multiples of 5 or 10 and having all its values in the array sum up to a
     * specified integer, <code>sum</code>. The value at position 0 can still
     * be a multiple of 5 or 10. However, it is not guaranteed.
     *
     * @param arrLen length of array to create
     * @param sum    the sum of all elements in the new array
     * @return <b>int[]</b> array of integers that are all definitely multiples
     *     of 5 or 10, except for the integer at position 0.
     */
    private static int[] makeFiveTenMultiple(int arrLen, int sum) {
        int[] returnArray = new int[arrLen]; // array to return
        // if array length is even, values of array strictly after pos 0 will
        // be multiples of 10. else, 5
        final int MULTIPLE = (arrLen % 2 == 0) ? 10 : 5;
        int remainder = sum % MULTIPLE; // get the remainder
        // fills array with multiples of 5 or 10
        Arrays.fill(returnArray, (sum - remainder) / arrLen);
        returnArray[0] += remainder; // adds remainder to value at pos 0

        return returnArray;
    }

    public static int addTask(Task taskToAdd) { // wrapper method
        return (taskToAdd.isFixed()) ? addFixedTask(taskToAdd) : autoAllocate(taskToAdd);
    }

    /**
     * Finds a task based on a provided name. Only returns an exact match.
     *
     * @param taskName name of task to return
     * @return <b>Task</b> with the exact matching name
     */
    public static Task findTask(String taskName) {
        for (Task task : scheduledTasks)
            if (task.getName().equals(taskName))
                return task;
        return null;
    }

    /**
     * Checks if a task with the same name already exists.
     *
     * @param task task with a name
     * @return <b>boolean</b> <code>true</code> if a task with the same name as
     *     the provided task
     * exists
     */
    private static boolean taskAlreadyExist(Task task) {
        String taskName = task.getName();
        for (Task t : scheduledTasks) {
            String currentTaskName = t.getName();
            if (taskName.equals(currentTaskName))
                return true;
        }
        return false;
    }

    /**
     * Removes an existing task. If the task is a fragment, its other fragments
     * will also be deleted.
     *
     * @param unTask erased from the past and present. the task... never
     *     existed
     */
    public static void removeTask(Task unTask) {
        scheduledTasks.removeIf((task)
                                    -> task.equals(unTask)
                || task.getFragmentOf() != null && unTask.getFragmentOf() != null
                    && task.getFragmentOf().equals(unTask.getFragmentOf()));
    }

    /**
     * Replaces an old <code>Task</code> with a new <code>Task</code>.
     *
     * @param oldTask the <code>Task</code> to be replaced
     * @param newTask the <code>Task</code> that will replace
     *     <code>oldTask</code>
     * @return <b>int</b> success codes of {@link #addFixedTask(FixedTask)}
     */
    public static int replaceTask(Task oldTask, Task newTask) {
        ArrayList<Task> backupScheduledTasks = new ArrayList<>();

        for (Task task : scheduledTasks) // soft copy scheduledTasks
            backupScheduledTasks.add(task); // into a backup collection

        removeTask(oldTask); // removes the old task from the schedule

        int successCode = addTask(newTask); // add new task

        if (successCode != 0) // if failure, restore from backup
            scheduledTasks = backupScheduledTasks;

        return successCode; // return success code
    }
}
