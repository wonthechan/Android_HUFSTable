package io.github.wonthechan.hufstable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by YeChan on 2018-01-16.
 */

public class CourseListAdapter extends BaseAdapter{

    private Context context;
    private List<Course> courseList;
    private Fragment parent;
    private String year="";
    private String term="";
    private char orgSect;
    private String id="";

    public CourseListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // 어댑터에서 가장 중요한 메소드인 getView !
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.course, null);
        TextView courseGrade = (TextView) v.findViewById(R.id.courseGrade);
        TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);
        TextView courseTitleEnglish = (TextView) v.findViewById(R.id.courseTitleEnglish);
        TextView courseCredit = (TextView) v.findViewById(R.id.courseCredit);
        TextView courseArea = (TextView) v.findViewById(R.id.courseArea);
        TextView coursePersonnel = (TextView) v.findViewById(R.id.coursePersonnel);
        TextView courseProfessor = (TextView) v.findViewById(R.id.courseProfessor);
        TextView courseTimeRoom = (TextView) v.findViewById(R.id.courseTimeRoom);
        Button syllabusButton = (Button) v.findViewById(R.id.syllabusButton);

        courseGrade.setText(courseList.get(i).getCourseGrade() + "학년");
        courseTitle.setText(courseList.get(i).getCourseTitle());
        if(courseList.get(i).getCourseTitleEnglish().equals(""))
        {
            // 영어이름값이 없는경우 그냥 한글이름과 같게 set 한다
            courseTitleEnglish.setText(courseList.get(i).getCourseTitle());
        }
        else
        {
            courseTitleEnglish.setText(courseList.get(i).getCourseTitleEnglish().substring(1, courseList.get(i).getCourseTitleEnglish().lastIndexOf(')')));
        }
        courseCredit.setText(courseList.get(i).getCourseCredit() + "학점");
        courseArea.setText(courseList.get(i).getCourseArea());

        if(courseList.get(i).getCoursePersonnel().equals("없음"))
        {
            coursePersonnel.setText("제한없음");
        }
        else
        {
            coursePersonnel.setText("제한인원 : " + courseList.get(i).getCoursePersonnel() + "명");
        }
        courseProfessor.setText(courseList.get(i).getCourseProfessor() + "교수님");
        courseTimeRoom.setText(courseList.get(i).getCourseTimeRoom() + "");

        v.setTag(courseList.get(i).getCourseID());

        if(!courseList.get(i).isSyllabus)
        {
            syllabusButton.setVisibility(v.INVISIBLE);
        }
        else
        {
            syllabusButton.setVisibility(v.VISIBLE);
        }

        year = courseList.get(i).getCourseYear();
        term = courseList.get(i).getCourseTerm();
        orgSect = courseList.get(i).getCourseOrgSect();
        id = courseList.get(i).getCourseID();

        syllabusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 데이터를 다이얼로그로 보내는 코드
                Bundle args = new Bundle();
                args.putString("year", year);
                args.putString("term", term);
                args.putString("orgSect", orgSect+"");
                args.putString("id", id);
                //
                Dialog dialog = new Dialog();
                dialog.setArguments(args); //데이터 전달
                dialog.show(parent.getActivity().getSupportFragmentManager(),"tag");
            }
        });

        return v;
    }
}
