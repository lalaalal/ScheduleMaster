package com.schedulemaster;

import com.schedulemaster.model.Lecture;
import org.junit.jupiter.api.Test;

public class LectureTest {
    @Test
    public void testParseGrade() {
        System.out.println(Lecture.parseGrade("1학년"));
    }
}
