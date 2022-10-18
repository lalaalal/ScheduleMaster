package com.schedulemaster.model;

import com.schedulemaster.misc.LinkedList;

import java.io.Serializable;

public class LectureTime implements Serializable {
    public static final long serialVersionUID = 10L;
    public record Time(int hour, int minute) implements Serializable {
        public static final long serialVersionUID = 10L;

        public boolean isAfter(Time time) {
            return this.hour >= time.hour && this.minute >= time.minute;
        }

        public boolean isBefore(Time time) {
            return this.hour <= time.hour && this.minute <= time.minute;
        }

        public static Time parseTime(String time) {
            String[] hourAndMinute = time.split(":");
            int hour = Integer.parseInt(hourAndMinute[0]);
            int minute = Integer.parseInt(hourAndMinute[1]);

            return new Time(hour, minute);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Time time = (Time) o;

            if (hour != time.hour) return false;
            return minute == time.minute;
        }

        @Override
        public int hashCode() {
            int result = hour;
            result = 31 * result + minute;
            return result;
        }
    }

    public record TimeSet(int dayOfWeek, Time start, Time end) implements Serializable {
        public static final long serialVersionUID = 10L;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TimeSet timeSet = (TimeSet) o;

            if (dayOfWeek != timeSet.dayOfWeek) return false;
            if (!start.equals(timeSet.start)) return false;
            return end.equals(timeSet.end);
        }

        @Override
        public int hashCode() {
            int result = dayOfWeek;
            result = 31 * result + start.hashCode();
            result = 31 * result + end.hashCode();
            return result;
        }
    }

    public static final String[] DAY_OF_WEEK = { "월", "화", "수", "목", "금" };

    private final LinkedList<TimeSet> timeSets = new LinkedList<>();

    public static int findDayOfWeek(String dayOfWeek) {
        for (int i = 0; i < DAY_OF_WEEK.length; i++) {
            if (dayOfWeek.equals(DAY_OF_WEEK[i]))
                return i;
        }

        return -1;
    }

    public LinkedList<TimeSet> getTimeSets() {
        return timeSets;
    }

    public void addTimeSet(int dayOfWeek, Time start, Time end) {
        if (!(0 <= dayOfWeek && dayOfWeek < 5))
            throw new RuntimeException("Invalid value; dayOfWeek is " + dayOfWeek);
        timeSets.push(new TimeSet(dayOfWeek, start, end));
    }

    public void addTimeSets(LinkedList<TimeSet> timeSets) {
        for (TimeSet timeSet : timeSets) {
            this.timeSets.push(timeSet);
        }
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
                if (timeSet.dayOfWeek == compare.dayOfWeek && !timeSet.include(compare))
                    return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LectureTime that = (LectureTime) o;

        return timeSets.equals(that.timeSets);
    }

    @Override
    public int hashCode() {
        return timeSets.hashCode();
    }
}
