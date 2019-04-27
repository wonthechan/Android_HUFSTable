package io.github.wonthechan.hufstable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YeChan on 2018-01-16.
 */

public class CourseListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Course> courseList = new ArrayList<>();

    public CourseListRecyclerAdapter(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결 (XML 가져오는 부분)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course,parent,false);
        return new RowCell(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        // 데이터를 넣어주는 부분

        if(courseList.get(i).getCourseGrade().equals(""))
        {
            ((RowCell)holder).courseGrade.setText("");
        }
        else
        {
            ((RowCell)holder).courseGrade.setText(courseList.get(i).getCourseGrade() + "학년");
        }
        ((RowCell)holder).courseTitle.setText(courseList.get(i).getCourseTitle());
        if(courseList.get(i).getCourseTitleEnglish().equals(""))
        {
            // 영어이름값이 없는경우 그냥 한글이름과 같게 set 한다
            ((RowCell)holder).courseTitleEnglish.setText(courseList.get(i).getCourseTitle());
        }
        else
        {
            ((RowCell)holder).courseTitleEnglish.setText(courseList.get(i).getCourseTitleEnglish().substring(1, courseList.get(i).getCourseTitleEnglish().lastIndexOf(')')));
        }
        ((RowCell)holder).courseCredit.setText(courseList.get(i).getCourseCredit() + "학점");
        ((RowCell)holder).courseArea.setText(courseList.get(i).getCourseArea());

        if(courseList.get(i).getCoursePersonnel().equals("없음"))
        {
            ((RowCell)holder).coursePersonnel.setText("제한없음");
        }
        else
        {
            ((RowCell)holder).coursePersonnel.setText(courseList.get(i).getCoursePersonnel());
        }
        ((RowCell)holder).courseProfessor.setText(courseList.get(i).getCourseProfessor() + "교수님");
        ((RowCell)holder).courseTimeRoom.setText(courseList.get(i).getCourseTimeRoom() + "");

    }

    @Override
    public int getItemCount() {
        // 카운터
        return courseList.size();
    }

    // 소스코드 절약해주는 부분
    private static class RowCell extends RecyclerView.ViewHolder {
        TextView courseGrade;
        TextView courseTitle;
        TextView courseTitleEnglish;
        TextView courseCredit;
        TextView courseArea;
        TextView coursePersonnel;
        TextView courseProfessor;
        TextView courseTimeRoom;
        final Button syllabusButton;

        public RowCell(View v) {
            super(v);
            courseGrade = (TextView) v.findViewById(R.id.courseGrade);
            courseTitle = (TextView) v.findViewById(R.id.courseTitle);
            courseTitleEnglish = (TextView) v.findViewById(R.id.courseTitleEnglish);
            courseCredit = (TextView) v.findViewById(R.id.courseCredit);
            courseArea = (TextView) v.findViewById(R.id.courseArea);
            coursePersonnel = (TextView) v.findViewById(R.id.coursePersonnel);
            courseProfessor = (TextView) v.findViewById(R.id.courseProfessor);
            courseTimeRoom = (TextView) v.findViewById(R.id.courseTimeRoom);
            syllabusButton = (Button) v.findViewById(R.id.syllabusButton);
        }
    }
}
