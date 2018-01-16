package io.github.wonthechan.hufstable;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by YeChan on 2018-01-16.
 */

public class CourseListAdapter extends BaseAdapter{

    private Context context;
    private List<Course> courseList;

    public CourseListAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
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
        return v;
    }
}
