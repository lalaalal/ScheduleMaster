package com.schedulemaster.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Lecture implements Serializable {
    public static final long serialVersionUID = 12L;
    public int grade;		    // 학년
    public String name;         // 강의명
    public int score; 		    // 학점
    public LectureTime time; 	// 요일 및 강의시간
    public String professor;    // 담당교수
    public String lectureNum;   // 강좌번호
    public int max;             // 최대신청인원
    public int enrolled;        // 신청된 인원
    public String classRoom; 	// 강의실

    public String major;		// 개설학과 전공

    public static Lecture createLecture(String[] tuple) {
        Lecture lecture = new Lecture();
        lecture.grade = parseGrade(tuple[0]);
        lecture.name = tuple[1];
        lecture.major = tuple[2];
        lecture.score = Integer.parseInt(tuple[3]);
        lecture.professor = tuple[4];
        lecture.lectureNum = tuple[5];
        lecture.max = Integer.parseInt(tuple[6]);
        int dayOfWeek = LectureTime.findDayOfWeek(tuple[7]);
        LectureTime.Time start = LectureTime.Time.parseTime(tuple[8]);
        LectureTime.Time end = LectureTime.Time.parseTime(tuple[9]);
        lecture.time = new LectureTime();
        lecture.time.addTimeSet(dayOfWeek, start, end);
        lecture.classRoom = tuple[10];

        return lecture;
    }

    public static int parseGrade(String grade) {
        try {
            return Integer.parseInt(grade);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "lectureNum='" + lectureNum + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", score=" + score +
                ", time=" + time +
                ", professor='" + professor + '\'' +
                ", max=" + max +
                ", enrolled=" + enrolled +
                ", classRoom='" + classRoom + '\'' +
                ", major='" + major + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lecture lecture = (Lecture) o;

        if (grade != lecture.grade) return false;
        if (score != lecture.score) return false;
        if (max != lecture.max) return false;
        if (enrolled != lecture.enrolled) return false;
        if (!Objects.equals(name, lecture.name)) return false;
        if (!Objects.equals(time, lecture.time)) return false;
        if (!Objects.equals(professor, lecture.professor)) return false;
        if (!Objects.equals(lectureNum, lecture.lectureNum)) return false;
        if (!Objects.equals(classRoom, lecture.classRoom)) return false;
        return Objects.equals(major, lecture.major);
    }

    @Override
    public int hashCode() {
        int result = grade;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + score;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (professor != null ? professor.hashCode() : 0);
        result = 31 * result + (lectureNum != null ? lectureNum.hashCode() : 0);
        result = 31 * result + max;
        result = 31 * result + enrolled;
        result = 31 * result + (classRoom != null ? classRoom.hashCode() : 0);
        result = 31 * result + (major != null ? major.hashCode() : 0);
        return result;
    }
}
