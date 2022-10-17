package com.schedulemaster.server;

import com.schedulemaster.model.LectureTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LectureTimeTest {
    @Test
    public void testLectureTime() {
        LectureTime a = new LectureTime();
        LectureTime.Time start = LectureTime.Time.parseTime("13:00");
        LectureTime.Time end = LectureTime.Time.parseTime("15:00");
        a.addTimeSet(0, start, end);
        a.addTimeSet(1, start, end);

        LectureTime b = new LectureTime();
        start = LectureTime.Time.parseTime("12:00");
        end = LectureTime.Time.parseTime("14:00");
        b.addTimeSet(0, start, end);
        b.addTimeSet(1, start, end);

        Assertions.assertTrue(a.conflictWith(b));
    }
}
