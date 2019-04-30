package io.github.wonthechan.hufstable;

/**
 * Created by YeChan on 2018-01-16.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScheduleFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
//    private static final String ARG_SECTION_NUMBER = "section_number";
//
//    public CourseFragment() {
//    }
//
//    /**
//     * Returns a new instance of this fragment for the given section
//     * number.
//     */
//    public static CourseFragment newInstance(int sectionNumber) {
//        Log.e("TAG", "CourseFragment");
//        CourseFragment fragment = new CourseFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
//        return fragment;
//    }

    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;
    private CourseListRecyclerAdapter courseListRecyclerAdapter;
    private ArrayList<Course> courseLIst;
    List<Element> myLectures = new ArrayList<Element>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        rv.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        courseLIst = new ArrayList<Course>();
        courseListRecyclerAdapter = new CourseListRecyclerAdapter(courseLIst);
//        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(mLinearLayoutManager);
        rv.setAdapter(courseListRecyclerAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());

        new BackgroundParseTask2().execute(2);

        return rootView;
    }

    // 서브클래스 작성
    class BackgroundParseTask2 extends AsyncTask<Integer, Integer, Integer> {

        ProgressDialog asyncDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        @Override
        protected void onPreExecute()
        {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("잠시만 기다리세요...");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            // 백그라운드 작업이 진행되는 곳.
            try{
                Log.e("TAG", "BackgroundParseTask @ doInBackground started");
                switch (integers[0])
                {
                    case 1:
                    {
                        Log.e("TAG", "case1로 들어옴");
                        break;
                    }
                    case 2:
                    {
                        Log.e("TAG", "case 2 시작됨");
                        // case 2는 검색 버튼을 누르고 강의정보를 파싱 : 전공 선택
                        // 리스트 초기화
                        courseLIst.clear();

                        String ccourseYear = "2019";
                        String ccourseTerm = "1";
                        char ccourseOrg = 'A';
                        String ccourseCampus = "서울";
                        String ccourseID; // 강의 고유 번호 (학수번호)
                        String ccourseArea; // 개설 영역
                        String ccourseGrade; // 해당학년
                        String ccourseTitle; // 강의 제목
                        String ccourseTitleEnglish; // 강의 영어 제목
                        String ccourseCredit; // 강의 학점
                        String ccoursePersonnel; // 강의 제한 인원
                        String ccourseProfessor; // 강의 교수
                        String ccourseTimeRoom; // 강의 시간대
                        Boolean ccourseSyllabus; // 강의계획서 유무

                        // 컴전학부 강의 리스트 페이지
                        String URL = "http://wis.hufs.ac.kr:8989/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&type=&ag_ledg_year=2018&ag_ledg_sessn=3&ag_org_sect=A&campus_sect=H2&gubun=1&ag_crs_strct_cd=ATIA1_H2&ag_compt_fld_cd=301_H2";
                        Document sampleDoc = Jsoup.connect(URL).get();
                        Elements elementsLecture = sampleDoc.select("[id=premier1]").select("tbody tr"); // 강의s 파싱

                        myLectures.add(elementsLecture.get(20)); // 데이터베이스
                        myLectures.add(elementsLecture.get(23)); // 소프트웨어공학
                        myLectures.add(elementsLecture.get(24)); // 운영체제
                        myLectures.add(elementsLecture.get(28)); // 컴파일러구성론
                        myLectures.add(elementsLecture.get(29)); // 컴퓨터네트워크

                        // 영어통번역학부 강의 리스트 페이지
                        URL = "http://wis.hufs.ac.kr:8989/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&type=&ag_ledg_year=2018&ag_ledg_sessn=3&ag_org_sect=A&campus_sect=H2&gubun=1&ag_crs_strct_cd=AO1_H2&ag_compt_fld_cd=301_H2";
                        sampleDoc = Jsoup.connect(URL).get();
                        elementsLecture = sampleDoc.select("[id=premier1]").select("tbody tr"); // 강의s 파싱

                        myLectures.add(elementsLecture.get(64)); // 미디어영어청취2
                        myLectures.add(elementsLecture.get(68)); // 영어글쓰기의기초

                        // 교양(과기) 강의 리스트 페이지
                        URL = "http://wis.hufs.ac.kr:8989/src08/jsp/lecture/LECTURE2020L.jsp?tab_lang=K&type=&ag_ledg_year=2018&ag_ledg_sessn=3&ag_org_sect=A&campus_sect=H2&gubun=2&ag_compt_fld_cd=334_H2";
                        sampleDoc = Jsoup.connect(URL).get();
                        elementsLecture = sampleDoc.select("[id=premier1]").select("tbody tr"); // 강의s 파싱

                        myLectures.add(elementsLecture.get(15)); // 생명과화학

                        for(int i = 0; i < myLectures.size(); i++)
                        {
                            Elements elementsAttrLecture = myLectures.get(i).select("td"); // 강의의 세부 <td> 속성들 파싱

                            ccourseID = elementsAttrLecture.get(3).text();
                            ccourseArea = elementsAttrLecture.get(1).text();
                            ccourseGrade = elementsAttrLecture.get(2).text();
                            ccourseTitle = elementsAttrLecture.get(4).select(".txt_navy").toString();

                            // gray8을 포함한다는것은 강의의 영어제목이 따로 있는것.
                            if(ccourseTitle.contains("gray8"))
                            {
                                ccourseTitleEnglish = elementsAttrLecture.get(4).select(".txt_gray8").text();
                            }
                            else
                            {
                                // 영어제목이 따로 없다면 그냥 빈값을 넘겨준다
                                ccourseTitleEnglish =  "";
                            }

                            ccourseTitle = ccourseTitle.substring(ccourseTitle.indexOf('>')+1, ccourseTitle.indexOf("<br>"));

                            if(ccourseTitle.contains("&amp;"))
                            {
                                ccourseTitle = ccourseTitle.replace("&amp;","&");
                            }

                            ccourseCredit = elementsAttrLecture.get(12).text();
                            ccoursePersonnel = elementsAttrLecture.get(15).text();
                            if(elementsAttrLecture.get(11).text().contains("("))
                            {
                                ccourseProfessor = elementsAttrLecture.get(11).text().substring(0,elementsAttrLecture.get(11).text().indexOf('(')); // 한국인 교수일 경우
                            }
                            else
                            {
                                ccourseProfessor = elementsAttrLecture.get(11).text()+" "; // 외국인 교수일 경우

                            }
                            ccourseTimeRoom = elementsAttrLecture.get(14).text().substring(0, elementsAttrLecture.get(14).text().indexOf(')')+1);
                            if(ccourseTimeRoom.length() == 2){
                                // 강의실이 정해지지 않은 경우 => 온라인강의
                                ccourseTimeRoom = "온라인강의";
                            }
                            ccourseSyllabus = elementsAttrLecture.get(4).select("div").toString().contains("onclick");
                            // for test
                            Course course = new Course(ccourseCampus, ccourseYear, ccourseTerm, ccourseOrg, ccourseID,ccourseArea,ccourseGrade,ccourseTitle,ccourseTitleEnglish,ccourseCredit,ccoursePersonnel,ccourseProfessor,ccourseTimeRoom,ccourseSyllabus);

                            courseLIst.add(course);
                        }
                        break;
                    }
                    case 3:
                    {
                        break;
                    }
                }
                Log.e("TAG", "BackgroundParseTask @ doInBackground finished");
            } catch(Exception e){
                e.printStackTrace();
            }
            return integers[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.e("TAG", "BackgroundParseTask @ onPostExecute started");

            switch (integer){
                case 1:
                {
                    // major 스피너 업데이트
                    break;
                }
                case 2:
                {
                    Log.e("TAG", "Post2에서 courseList size값은 : " + courseLIst.size());
                    //  업데이트
                    courseListRecyclerAdapter.notifyDataSetChanged();
                    break;
                }
                case 3:
                {
                    Log.e("TAG", "Post3에서 courseList size값은 : " + courseLIst.size());
                    //  업데이트
                    courseListRecyclerAdapter.notifyDataSetChanged();
                }
                break;
            }
            Log.e("TAG", "BackgroundParseTask @ onPostExecute finished");
            asyncDialog.dismiss();
            cancel(true);
        }
    }
}
