package com.schedulemaster;

import java.io.Serializable;
import java.util.ArrayList;

public class LectureTime implements Serializable {
    private record Time(int hour, int minute) implements Serializable {

        public boolean isAfter(Time time) {
            return this.hour > time.hour && this.minute > time.minute;
        }

        public boolean isBefore(Time time) {
            return this.hour < time.hour && this.minute < time.minute;
        }
    }

    public record TimeSet(int dayOfWeek, Time start, Time end) implements Serializable {
        public boolean conflictWith(TimeSet timeSet) {
            return this.dayOfWeek == timeSet.dayOfWeek
                    && (!start.isAfter(timeSet.end)
                    || !end.isBefore(timeSet.start));
        }

        public boolean include(TimeSet timeSet) {
            return this.dayOfWeek == timeSet.dayOfWeek
                    && start.isBefore(timeSet.start)
                    && end.isAfter(timeSet.end);
        }
    }

    public static final String[] DAY_OF_WEEK = { "월", "화", "수", "목", "금" };

    private final ArrayList<TimeSet> timeSets = new ArrayList<>();

    public ArrayList<TimeSet> getTimeSets() {
        return timeSets;
    }

    public void addTimeSet(TimeSet timeSet) {
        if (!(0 < timeSet.dayOfWeek && timeSet.dayOfWeek < 5))
            throw new RuntimeException("Invalid value; dayOfWeek is " + timeSet.dayOfWeek);
        timeSets.add(timeSet);
    }

    public boolean conflictWith(LectureTime lectureTime) {
        for (TimeSet timeSet : timeSets) {
            for (TimeSet compare : lectureTime.timeSets) {
                if (timeSet.conflictWith(compare))
                    return true;
            }
        }

        return false;
    }

    public boolean include(LectureTime lectureTime) {
        for (TimeSet timeSet : timeSets) {
            for (TimeSet compare : lectureTime.timeSets) {
                if (!timeSet.include(compare))
                    return false;
            }
        }

        return true;
    }

}
