package org.backend;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Task {
    private String name;
    private int duration; // in minutes
    private LocalTime timeStart;
    private Task fragmentOf;
    private boolean fixedTask;

    public Task(String title, int duration) {
        this.name = title;
        this.duration = duration;
        this.fragmentOf = null;
        this.fixedTask = false;
    }

    public Task(String title, int duration, LocalTime timeStart) {
        this.name = title;
        this.duration = duration;
        this.timeStart = timeStart;
        this.fixedTask = true;
    }

    @Override public String toString() { // mainly for debugging purposes
        String type = (fixedTask) ? "[FIXED]" : "[AUTO]";
        return type + " " + name + " "
            + " TIMING: " + timeStart.toString() + " to " + getTimeEnd().toString();
    }

    public boolean isAfter(LocalTime time) {
        return timeStart.isAfter(time) && getTimeEnd().isAfter(time) || time.equals(timeStart);
    }

    public void bringForward(int minutes) {
        timeStart = timeStart.minusMinutes(minutes);
    }

    public int minsUntil(Task otherTask) {
        return (int) timeStart.plus(duration, ChronoUnit.MINUTES)
            .until(otherTask.getTimeStart(), ChronoUnit.MINUTES);
    }

    public boolean overlaps(Task otherTask) {
        int totalDuration = otherTask.getDuration() + duration;
        boolean occursBefore =
            minsUntil(otherTask) < 0 && Math.abs(minsUntil(otherTask)) >= totalDuration;
        boolean occursAfter = minsUntil(otherTask) >= 0;
        return !(occursBefore ^ occursAfter);
    }

    public boolean isOngoing(LocalTime time) {
        return timeStart.isBefore(time) && getTimeEnd().isAfter(time);
    }

    public LocalTime getTimeEnd() {
        return timeStart.plus(duration, ChronoUnit.MINUTES);
    }

    public int getDuration() {
        return duration;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public String getName() {
        return name;
    }

    public boolean isFixed() {
        return fixedTask;
    }

    public Task getFragmentOf() {
        return fragmentOf;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setName(String title) {
        this.name = title;
    }

    public void setFragmentOf(Task fragmentOf) {
        this.fragmentOf = fragmentOf;
    }
}
