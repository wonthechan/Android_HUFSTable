package io.github.wonthechan.hufstable;

import android.util.Log;

/**
 * Created by YeChan on 2018-01-16.
 */

public class Course {

    String courseYear; // 강의 개설 연도
    String courseTerm; // 강의 개설 학기
    char courseOrgSect; // 학부, 대학원 코드
    String courseID; // 강의 고유 번호 (학수번호)
    String courseArea; // 개설 영역
    String courseGrade; // 해당학년
    String courseTitle; // 강의 제목
    String courseTitleEnglish; // 강의 영어 제목
    String courseCredit; // 강의 학점
    String coursePersonnel; // 강의 제한 인원
    String courseProfessor; // 강의 교수
    String courseTimeRoom; // 강의 시간대 와 강의실
    Boolean isSyllabus; // 강의계획서 존재 유무

    public String getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public String getCourseTerm() {
        return courseTerm;
    }

    public void setCourseTerm(String courseTerm) {
        this.courseTerm = courseTerm;
    }

    public char getCourseOrgSect() {
        return courseOrgSect;
    }

    public void setCourseOrgSect(char courseOrgSect) {
        this.courseOrgSect = courseOrgSect;
    }

    public Boolean getSyllabus() {
        return isSyllabus;
    }

    public void setSyllabus(Boolean syllabus) {
        isSyllabus = syllabus;
    }

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

    public Course(String courseYear, String courseTerm, char courseOrgSect, String courseID, String courseArea, String courseGrade, String courseTitle, String courseTitleEnglish, String courseCredit, String coursePersonnel, String courseProfessor, String courseTimeRoom, Boolean isSyllabus) {
        this.courseYear = courseYear;
        this.courseTerm = courseTerm;
        this.courseOrgSect = courseOrgSect;
        this.courseID = courseID;
        this.courseArea = courseArea;
        this.courseGrade = courseGrade;
        this.courseTitle = courseTitle;
        this.courseTitleEnglish = courseTitleEnglish;
        this.courseCredit = courseCredit;
        this.coursePersonnel = coursePersonnel;
        this.courseProfessor = courseProfessor;
        this.courseTimeRoom = courseTimeRoom;
        this.isSyllabus = isSyllabus;
    }
}
