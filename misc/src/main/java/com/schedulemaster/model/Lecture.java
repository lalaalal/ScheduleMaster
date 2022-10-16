package com.schedulemaster.model;

import java.io.Serializable;

public class Lecture implements Serializable {
    public static final long serialVersionUID = 1L;
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
    public String completion;	// 이수구분

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
                "grade=" + grade +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", time=" + time +
                ", professor='" + professor + '\'' +
                ", lectureNum='" + lectureNum + '\'' +
                ", max=" + max +
                ", enrolled=" + enrolled +
                ", classRoom='" + classRoom + '\'' +
                ", major='" + major + '\'' +
                ", completion='" + completion + '\'' +
                '}';
    }
}
