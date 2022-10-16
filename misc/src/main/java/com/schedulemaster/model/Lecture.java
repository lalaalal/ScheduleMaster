package com.schedulemaster.model;

import java.io.Serializable;

public class Lecture implements Serializable {
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

}
