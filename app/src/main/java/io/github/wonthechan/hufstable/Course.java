package io.github.wonthechan.hufstable;

import android.util.Log;

/**
 * Created by YeChan on 2018-01-16.
 */

public class Course {

    String courseID; // 강의 고유 번호 (학수번호)
    String courseArea; // 개설 영역
    String courseGrade; // 해당학년
    String courseTitle; // 강의 제목
    String courseTitleEnglish; // 강의 영어 제목
    String courseCredit; // 강의 학점
    String coursePersonnel; // 강의 제한 인원
    String courseProfessor; // 강의 교수
    String courseTimeRoom; // 강의 시간대 와 강의실

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseArea() {
        return courseArea;
    }

    public void setCourseArea(String courseArea) {
        this.courseArea = courseArea;
    }

    public String getCourseGrade() {
        return courseGrade;
    }

    public void setCourseGrade(String courseGrade) {
        this.courseGrade = courseGrade;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getCoursePersonnel() {
        return coursePersonnel;
    }

    public void setCoursePersonnel(String coursePersonnel) {
        this.coursePersonnel = coursePersonnel;
    }

    public String getCourseProfessor() {
        return courseProfessor;
    }

    public void setCourseProfessor(String courseProfessor) {
        this.courseProfessor = courseProfessor;
    }

    public String getCourseTimeRoom() {
        return courseTimeRoom;
    }

    public void setCourseTimeRoom(String courseTimeRoom) {
        this.courseTimeRoom = courseTimeRoom;
    }

    public String getCourseTitleEnglish() {
        return courseTitleEnglish;
    }

    public void setCourseTitleEnglish(String courseTitleEnglish) {
        this.courseTitleEnglish = courseTitleEnglish;
    }

    public Course(String courseID, String courseArea, String courseGrade, String courseTitle, String courseTitleEnglish, String courseCredit, String coursePersonnel, String courseProfessor, String courseTimeRoom) {
        this.courseID = courseID;
        this.courseArea = courseArea;
        this.courseGrade = courseGrade;
        this.courseTitle = courseTitle;
        this.courseTitleEnglish = courseTitleEnglish;
        this.courseCredit = courseCredit;
        this.coursePersonnel = coursePersonnel;
        this.courseProfessor = courseProfessor;
        this.courseTimeRoom = courseTimeRoom;
    }
}
