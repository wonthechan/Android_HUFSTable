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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseFragment extends Fragment implements AdapterView.OnItemSelectedListener{
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

    private ArrayAdapter yearAdapter;
    private Spinner yearSpinner;
    private ArrayAdapter termAdapter;
    private Spinner termSpinner;
    private ArrayAdapter areaAdapter;
    private Spinner areaSpinner;
    private ArrayAdapter studiesAdapter;
    private Spinner studiesSpinner;

    private String campusGubun = "";
    private String studiesGubun = "";
    private String courseYear = "";

    // tab_lang=K&type=&ag_ledg_year=2017&ag_ledg_sessn=1&ag_org_sect=T&gubun=1

    // tab_lang=K&type=&ag_ledg_year=2018&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H2&gubun=1

    // tab_lang=K&type=&ag_ledg_year=2018&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H1&gubun=1&ag_crs_strct_cd=AAR01_H1&ag_compt_fld_cd=301_H1

    private String paramYear = "2018";
    private String paramTerm = "1"; // 1부터 시작하며 1학기, 여름계절학기, 2학기, 겨울계절학기 순
    private char paramOrgSect = 'A';
    private String paramCamSect = "H1"; // H1 은 서울캠퍼스, H2 는 글로벌 캠퍼스
    private char paramGubun = '1'; // 1은 전공/부전공, 2는 교양 영역
    private String paramStudies = "";

    final char[] orgSectList = {'A','B','D','E','G','H','I','J','L','M','T'}; // 학부, 대학원 코드 리스트
    final ArrayList<String> majorList = new ArrayList<>(); // 전공 과목 리스트
    final ArrayList<String> notMajorList = new ArrayList<>(); // 교양 과목 리스트

    final ArrayList<String> paramMajorList = new ArrayList<>();
    final ArrayList<String> paramNotMajorList = new ArrayList<>();

    final ArrayList<String> codeStudiesList = new ArrayList<>();

    private ListView courseListView;
    private CourseListAdapter courseListAdapter;
    private List<Course> courseLIst;


    RadioGroup campusGroup;
    RadioGroup studiesGroup;
    RadioButton seoulRButton;
    RadioButton globalRButton;
    RadioButton majorRButton;
    RadioButton cultRButton;

    Button searchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        new BackgroundParseTask().execute(1);

        campusGroup = (RadioGroup) rootView.findViewById(R.id.campusGroup);
        studiesGroup = (RadioGroup) rootView.findViewById(R.id.studiesGroup);
        seoulRButton = (RadioButton) rootView.findViewById(R.id.seoulRButton);

        yearSpinner = (Spinner) rootView.findViewById(R.id.yearSpinner);
        termSpinner = (Spinner) rootView.findViewById(R.id.termSpinner);
        areaSpinner = (Spinner) rootView.findViewById(R.id.areaSpinner); // 학부,대학원
        studiesSpinner = (Spinner) rootView.findViewById(R.id.studiesSpinner);

        searchButton = (Button) rootView.findViewById(R.id.searchButton);

        // 초기화면 기본 스피너 어댑터 설정
        // year 스피너
        yearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.year, android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // term 스피너
        termAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.univTerm, android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(termAdapter);
        // area 스피너
        areaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.affiliation, android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);

        //yearSpinner.setAdapter(termAdapter);

        // 기본적으로 4개의 스피너에 대하여 리스너를 달아준다.
        yearSpinner.setOnItemSelectedListener(this);
        termSpinner.setOnItemSelectedListener(this);
        areaSpinner.setOnItemSelectedListener(this);
        studiesSpinner.setOnItemSelectedListener(this);

        campusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton campusButton = (RadioButton) getView().findViewById(i);
                campusGubun = campusButton.getText().toString();

                // 캠퍼스 구분에 따라 달라지는 부분
                if(campusGubun.equals("서울"))
                {
                    paramCamSect = "H1";
                    new BackgroundParseTask().execute(1);
                }
                else if(campusGubun.equals("글로벌"))
                {
                    paramCamSect = "H2";
                    new BackgroundParseTask().execute(1);
                }
            }
        });

        studiesGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton studiesButton = (RadioButton) getView().findViewById(i);
                studiesGubun = studiesButton.getText().toString();

                // 캠퍼스 구분에 따라 달라지는 부분
                if(studiesGubun.equals("전공/부전공"))
                {
                    paramGubun = '1';
                    // 전공으로 교체
                    studiesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, majorList);
                    studiesSpinner.setAdapter(studiesAdapter);
                }
                else if(studiesGubun.equals("실용외국어/교양과목"))
                {
                    paramGubun = '2';
                    // 교양으로 교체
                    studiesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, notMajorList);
                    studiesSpinner.setAdapter(studiesAdapter);
                }
            }
        });

        courseListView = (ListView) rootView.findViewById(R.id.courseListView);
        courseLIst = new ArrayList<Course>();
        courseListAdapter = new CourseListAdapter(getContext().getApplicationContext(), courseLIst, this);
        courseListView.setAdapter(courseListAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(paramGubun == '1')
                {
                    new BackgroundParseTask().execute(2);
                }
                else
                {
                    new BackgroundParseTask().execute(3);
                }
            }
        });

        return rootView;
    }

    // 다중 스피너 리스너 구현 (switch)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()){
            case R.id.yearSpinner:
            {
                courseYear = yearSpinner.getSelectedItem().toString().substring(0,4); // 뒤에 붙은 "년도" 떼어내기
                if(!courseYear.equals(paramYear))
                {
                    paramYear = courseYear;
                    new BackgroundParseTask().execute(1);
                }
                break;
            }
            case R.id.termSpinner:
            {
                if(!(""+(termSpinner.getSelectedItemPosition()+1)).equals(paramTerm))
                {
                    paramTerm = "" + (termSpinner.getSelectedItemPosition()+1);
                    new BackgroundParseTask().execute(1);
                }
                break;
            }
            case R.id.areaSpinner:
            {
                // 이 스피너 값이 바뀔때에는 secOrg가 다시 파싱되서는 안된다. 기존 선택값이 초기화 되기 때문..
                // 따라서 이곳에서 스레드를 실행시킬때는 areaSpinner를 업데이트하지않도록 주의 한다
                int areaPos = areaSpinner.getSelectedItemPosition();
                if(paramOrgSect != orgSectList[areaPos])
                {
                    paramOrgSect = orgSectList[areaPos];
                    if(paramOrgSect != 'A')
                    {
                        campusGroup.check(R.id.seoulRButton);
                        studiesGroup.check(R.id.majorRButton);
                        switchEnabled(false);
                        // 학부가 아니면 계절학기가 없으므로 term 스피너 업데이트.
                        termAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gradTerm, android.R.layout.simple_spinner_dropdown_item);
                        termSpinner.setAdapter(termAdapter);
                    }
                    else
                    {
                        switchEnabled(true);
                        // 원상태로 복귀
                        termAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.univTerm, android.R.layout.simple_spinner_dropdown_item);
                        termSpinner.setAdapter(termAdapter);
                    }
                    new BackgroundParseTask().execute(1);
                }
                break;
            }
            case R.id.studiesSpinner:
            {
                if(paramGubun == '1')
                {
                    paramStudies = paramMajorList.get(studiesSpinner.getSelectedItemPosition());
                }
                else
                {
                    paramStudies = paramNotMajorList.get(studiesSpinner.getSelectedItemPosition());
                }
                Log.e("TAG", "paramStudies 값은 => " + paramStudies);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Switch isEnabled() for RadioGroup
    public void switchEnabled(Boolean b){
        campusGroup.setEnabled(b);
        for (int i = 0; i < campusGroup.getChildCount(); i++) {
            campusGroup.getChildAt(i).setEnabled(b);
        }
        for (int i = 0; i < studiesGroup.getChildCount(); i++) {
            studiesGroup.getChildAt(i).setEnabled(b);
        }
    }

    // 서브클래스 작성
    class BackgroundParseTask extends AsyncTask<Integer, Integer, Integer> {

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
                        // case 1은 메뉴 파싱
                        // 리스트 초기화
                        majorList.clear();
                        paramMajorList.clear();
                        notMajorList.clear();
                        paramNotMajorList.clear();

                        // tab_lang=K&type=&ag_ledg_year=2018&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H2&gubun=1
                        String parameter = "?tab_lang=K"+"&type=&ag_ledg_year="+paramYear+"&ag_ledg_sessn="+paramTerm+"&ag_org_sect="+paramOrgSect+"&campus_sect="+paramCamSect+"&gubun="+paramGubun;
                        String URL = "http://wis.hufs.ac.kr:8989/src08/jsp/lecture/LECTURE2020L.jsp";
                        Document sampleDoc = Jsoup.connect(URL+parameter).get();
                        Elements elementsMajor = sampleDoc.select(".selectBox").select("[name=ag_crs_strct_cd]").select("option"); // 전공 파싱
                        Elements elementsNotMajor = sampleDoc.select(".selectBox").select("[name=ag_compt_fld_cd]").select("option"); // 교양 파싱

                        for(Element e: elementsMajor){
                            majorList.add(e.text());
                            paramMajorList.add(e.attr("value"));
                        }

                        for(Element e: elementsNotMajor){
                            notMajorList.add(e.text());
                            paramNotMajorList.add(e.attr("value"));
                        }
                        break;
                    }
                    case 2:
                    {
                        Log.e("TAG", "case 2 시작됨");
                        // case 2는 검색 버튼을 누르고 강의정보를 파싱 : 전공 선택
                        // 리스트 초기화
                        courseLIst.clear();
                        // test data
                        //String test = "#23dwf(sdfsf)";
                        Log.e("TAG", "1");
                        // tab_lang=K&type=&ag_ledg_year=2018&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H1&gubun=1&ag_crs_strct_cd=AAR01_H1    ..전공선택시
                        String parameter = "?tab_lang=K"+"&type=&ag_ledg_year="+paramYear+"&ag_ledg_sessn="+paramTerm+"&ag_org_sect="+paramOrgSect+"&campus_sect="+paramCamSect+"&gubun="+paramGubun+"&ag_crs_strct_cd="+paramStudies;
                        String URL = "http://wis.hufs.ac.kr:8989/src08/jsp/lecture/LECTURE2020L.jsp";
                        Document sampleDoc = Jsoup.connect(URL+parameter).get();
                        Log.e("TAG", "2");
                        Elements elementsLecture = sampleDoc.select("[id=premier1]").select("tbody tr"); // 강의s 파싱
                        Elements elementsAttrLecture; // 강의의 세부 <td> 속성들 파싱
                        Log.e("TAG", "3");

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

                        String ccourseYear = paramYear;
                        String ccourseTerm = paramTerm;
                        char ccourseOrg = paramOrgSect;

                        for(int i = 1; i < elementsLecture.size(); i++)
                        {
                            elementsAttrLecture = elementsLecture.get(i).select("td");

                            ccourseID = elementsAttrLecture.get(3).text();
                            //ccourseID = elementsAttrLecture.get(4).select("div").attr("onclick").toString();
                            ccourseArea = elementsAttrLecture.get(1).text();
                            ccourseGrade = elementsAttrLecture.get(2).text();
                            // ccourseTitle = elementsAttrLecture.get(4).text().substring(0, elementsAttrLecture.get(4).text().lastIndexOf('('));

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

                            ccourseCredit = elementsAttrLecture.get(11).text();
                            ccoursePersonnel = elementsAttrLecture.get(14).text().substring(elementsAttrLecture.get(14).text().indexOf('/')+2, elementsAttrLecture.get(14).text().length());
                            if(elementsAttrLecture.get(10).text().contains("("))
                            {
                                ccourseProfessor = elementsAttrLecture.get(10).text().substring(0,elementsAttrLecture.get(10).text().indexOf('(')); // 한국인 교수일 경우
                            }
                            else
                            {
                                ccourseProfessor = elementsAttrLecture.get(10).text()+" "; // 외국인 교수일 경우

                            }
                            ccourseTimeRoom = elementsAttrLecture.get(13).text().substring(0, elementsAttrLecture.get(13).text().indexOf(')')+1);

                            ccourseSyllabus = elementsAttrLecture.get(4).select("div").toString().contains("onclick");
                            // for test
                            Course course = new Course(ccourseYear, ccourseTerm, ccourseOrg, ccourseID,ccourseArea,ccourseGrade,ccourseTitle,ccourseTitleEnglish,ccourseCredit,ccoursePersonnel,ccourseProfessor,ccourseTimeRoom,ccourseSyllabus);

                            courseLIst.add(course);
                        }
                        Log.e("TAG", "courseList size값은 : " + courseLIst.size());
                        Log.e("TAG", "case 2 끝남");
                        break;
                    }
                    case 3:
                    {
                        Log.e("TAG", "case 3 시작됨");

                        // case 3은 검색 버튼을 누르고 강의정보를 파싱 : 교양 선택
                        // 리스트 초기화
                        courseLIst.clear();
                        Log.e("TAG","ag_compt_fld_cd : " + paramStudies);
                        // tab_lang=K&type=&ag_ledg_year=2018&ag_ledg_sessn=1&ag_org_sect=A&campus_sect=H1&gubun=2&ag_compt_fld_cd=301_H1    ..교양선택시
                        String parameter = "?tab_lang=K"+"&type=&ag_ledg_year="+paramYear+"&ag_ledg_sessn="+paramTerm+"&ag_org_sect="+paramOrgSect+"&campus_sect="+paramCamSect+"&gubun="+paramGubun+"&ag_compt_fld_cd="+paramStudies;
                        String URL = "http://wis.hufs.ac.kr:8989/src08/jsp/lecture/LECTURE2020L.jsp";
                        Document sampleDoc = Jsoup.connect(URL+parameter).get();

                        Elements elementsLecture = sampleDoc.select("[id=premier1]").select("tbody tr"); // 강의s 파싱
                        Elements elementsAttrLecture; // 강의의 세부 <td> 속성들 파싱
                        Log.e("TAG","elementsLecture size : " + elementsLecture.size());

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

                        String ccourseYear = paramYear;
                        String ccourseTerm = paramTerm;
                        char ccourseOrg = paramOrgSect;

                        for(int i = 1; i < elementsLecture.size(); i++)
                        {
                            elementsAttrLecture = elementsLecture.get(i).select("td");

                            ccourseID = elementsAttrLecture.get(3).text();
                            ccourseArea = elementsAttrLecture.get(1).text();
                            ccourseGrade = elementsAttrLecture.get(2).text();
                            // ccourseTitle = elementsAttrLecture.get(4).text().substring(0, elementsAttrLecture.get(4).text().lastIndexOf('('));

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
                            if(ccourseTitle.contains("&amp;"))
                            {
                                ccourseTitle = ccourseTitle.replace("&amp;","&");
                                if(!ccourseTitleEnglish.equals(""))
                                {
                                    ccourseTitleEnglish = ccourseTitleEnglish.replace("&amp;","&");
                                }
                            }
                            ccourseTitle = ccourseTitle.substring(ccourseTitle.indexOf('>')+1, ccourseTitle.indexOf("<br>"));

                            ccourseCredit = elementsAttrLecture.get(11).text();
                            ccoursePersonnel = elementsAttrLecture.get(14).text().substring(elementsAttrLecture.get(14).text().indexOf('/')+2, elementsAttrLecture.get(14).text().length());
                            if(elementsAttrLecture.get(10).text().contains("("))
                            {
                                ccourseProfessor = elementsAttrLecture.get(10).text().substring(0,elementsAttrLecture.get(10).text().indexOf('(')); // 한국인 교수일 경우
                            }
                            else
                            {
                                ccourseProfessor = elementsAttrLecture.get(10).text()+" "; // 외국인 교수일 경우

                            }
                            ccourseTimeRoom = elementsAttrLecture.get(13).text().substring(0, elementsAttrLecture.get(13).text().indexOf(')')+1);

                            ccourseSyllabus = elementsAttrLecture.get(4).select("div").toString().contains("onclick");
                            Log.e("TAG2", ccourseSyllabus.toString());
                            // for test
                            Course course = new Course(ccourseYear, ccourseTerm, ccourseOrg, ccourseID,ccourseArea,ccourseGrade,ccourseTitle,ccourseTitleEnglish,ccourseCredit,ccoursePersonnel,ccourseProfessor,ccourseTimeRoom,ccourseSyllabus);

                            courseLIst.add(course);
                        }
                        Log.e("TAG", "case 3 끝남");
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

                    if(paramGubun == '1') // 전공과목에 체크되 있는 경우
                    {
                        studiesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, majorList);
                        studiesSpinner.setAdapter(studiesAdapter);
                    }
                    else if(paramGubun == '2') // 교양과목에 체크 되어있는 경우
                    {
                        studiesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, notMajorList);
                        studiesSpinner.setAdapter(studiesAdapter);
                    }

                    if(majorList.isEmpty())
                    {
                        Log.e("TAG", "나비었어요!");
                        studiesSpinner.setEnabled(false); // 표시할 요소가 없으면 아예 비활성화한다.
                    }
                    else if(!majorList.isEmpty())
                    {
                        Log.e("TAG", "나 안비었어요!");
                        studiesSpinner.setEnabled(true);
                    }
                    break;
                }
                case 2:
                {
                    Log.e("TAG", "Post2에서 courseList size값은 : " + courseLIst.size());
                    //  업데이트
                    courseListAdapter.notifyDataSetChanged();
                    break;
                }
                case 3:
                {
                    Log.e("TAG", "Post3에서 courseList size값은 : " + courseLIst.size());
                    //  업데이트
                    courseListAdapter.notifyDataSetChanged();
                }
                break;
            }
            Log.e("TAG", "BackgroundParseTask @ onPostExecute finished");
            asyncDialog.dismiss();
            cancel(true);
        }
    }
}
