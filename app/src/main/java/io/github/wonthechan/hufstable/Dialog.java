package io.github.wonthechan.hufstable;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by YeChan on 2018-01-16.
 */
// Fragment Dialog
public class Dialog extends DialogFragment {

    private Fragment fragment;

    public Dialog(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    }

    private ListView syllabusListView;
    private List<Syllabus> syllabusList;
    private SyllabusListAdapter syllabusListAdapter;

    private String paramYear = "";
    private String paramTerm = "";
    private String paramOrgSect = "";
    private String paramID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog, container, false);

        Bundle args = getArguments();

        paramYear = args.getString("year");
        paramTerm = args.getString("term");
        paramOrgSect = args.getString("orgSect");
        paramID = args.getString("id");

        //TEST
//        Log.e("TAG4",paramYear);
//        Log.e("TAG4",paramTerm);
//        Log.e("TAG4",paramOrgSect);
//        Log.e("TAG4",paramID);

        /*
         * DialogFragment를 종료시키려면? 물론 다이얼로그 바깥쪽을 터치하면 되지만
         * 종료하기 버튼으로도 종료시킬 수 있어야한다.
         */
        // 먼저 부모 Fragment를 받아온다.
        // findFragmentByTag안의 문자열 값은 Fragment1.java에서 있던 문자열과 같아야 한다.
        //dialog.show(getActivity().getSupportFragmentManager(),"tag");
        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");

        syllabusListView = (ListView) view.findViewById(R.id.syllabusListView);
        syllabusList = new ArrayList<Syllabus>();
        syllabusListAdapter = new SyllabusListAdapter(getContext().getApplicationContext(), syllabusList);
        syllabusListView.setAdapter(syllabusListAdapter);

        new BackgroundParseTask2().execute();

        // 닫기 버튼 누를시 다이얼로그 종료
        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment != null){
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//        int height = getResources().getDimensionPixelSize(R.dimen.popup_width);
//        getDialog().getWindow().setLayout(width, height);

        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    public String brToEnter(Element raw)
    {
        String result = raw.toString();
        // <br>태그를 가지고 있는 경우 해당 태그만 뽑아서 줄바꿈으로 바꿔준다.
        if(result.contains("<br>"))
        {
            result = replaceHTMLSpecial(result,"br"); //br태그만 허용한다.
            result = result.replaceAll("<br>", "\r\n");
        }
        else
        {
            result = raw.text();
        }
        return result;
    }

    public  String replaceHTMLSpecial(String str, String strAllowTag) {
        String pattern = "<(\\/?)(?!\\/####)([^<|>]+)?>";
        String[] allowTags = strAllowTag.split(","); // 쉼표를 기준으로 허용 태그를 구분한다.
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < allowTags.length; i++) {
            buffer.append("|" + allowTags[i].trim() + "(?!\\w)");
        }
        pattern = pattern.replace("####",buffer.toString()); // 허용할 태그를 적용한 패턴

        String msg = str.replaceAll(pattern,""); // 허용된 태그를 제외하고 전부 교체
        return msg;
    }

    // 서브클래스 작성
    class BackgroundParseTask2 extends AsyncTask<Void, Void, Void> {

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
        protected Void doInBackground(Void... v) {
            // 백그라운드 작업이 진행되는 곳.
            try{
                // 강의계획표 파싱
                // 리스트 초기화
                syllabusList.clear();
                // &ledg_year=2018&ledg_sessn=1&org_sect=A&lssn_cd=A01699101
                String parameter = "&ledg_year="+paramYear+"&ledg_sessn="+paramTerm+"&org_sect="+paramOrgSect+"&lssn_cd="+paramID;
                String URL = "http://wis.hufs.ac.kr:8989/src08/jsp/lecture/syllabus.jsp?mode=print";
                Document sampleDoc = Jsoup.connect(URL+parameter).get();
                Elements elementsLecture = sampleDoc.select("div").select(".align_center tr td");
                Elements elementsAttrLecture; // 강의의 세부 <td> 속성들 파싱

                syllabusList.add(new Syllabus("학수번호", elementsLecture.get(0).text()));
                syllabusList.add(new Syllabus("교과목개요및학습목표", elementsLecture.get(13).text()));
                syllabusList.add(new Syllabus("교수", elementsLecture.get(6).text()));
                syllabusList.add(new Syllabus("연구실", elementsLecture.get(7).text()));
                syllabusList.add(new Syllabus("면담가능시간", elementsLecture.get(12).text()));
                syllabusList.add(new Syllabus("Tel/E-mail", "종합정보시스템 로그인을 통해 확인하시기 바랍니다,"));

                // 학습평가방법 별도 파싱
                Elements elementsTest = elementsLecture.get(17).select(".align_center tr td");
                //Elements elementsTest = sampleDoc.select("div div table").get(1);
                String parsedTest = "* 중간시험 : "+elementsTest.get(0).text()+"\r\n* 기말시험 : "+elementsTest.get(1).text()+"\r\n* 출석 : "+elementsTest.get(2).text()
                        +"\r\n* 과제물 : "+elementsTest.get(3).text()+"\r\n* 기타(발표및토론,프로젝트,수업참여도 등) : "+elementsTest.get(4).text()+"\r\n"+elementsLecture.get(17).select(".margin_top15").text();
                syllabusList.add(new Syllabus("학습평가방법", parsedTest));

                syllabusList.add(new Syllabus("교재", brToEnter(elementsLecture.get(14))));
                syllabusList.add(new Syllabus("참고문헌", brToEnter(elementsLecture.get(15))));
                syllabusList.add(new Syllabus("수업운영방식", brToEnter(elementsLecture.get(16))));

                syllabusList.add(new Syllabus("기타안내 및유의사항", brToEnter(elementsLecture.get(25))));

                // 주차 수업 계획 파싱
                Elements elementsWeek = sampleDoc.select("div div table").get(2).select("td");

                syllabusList.add(new Syllabus("Week1", brToEnter(elementsWeek.get(0))));
                syllabusList.add(new Syllabus("Week2", brToEnter(elementsWeek.get(1))));
                syllabusList.add(new Syllabus("Week3", brToEnter(elementsWeek.get(2))));
                syllabusList.add(new Syllabus("Week4", brToEnter(elementsWeek.get(3))));
                syllabusList.add(new Syllabus("Week5", brToEnter(elementsWeek.get(4))));
                syllabusList.add(new Syllabus("Week6", brToEnter(elementsWeek.get(5))));
                syllabusList.add(new Syllabus("Week7", brToEnter(elementsWeek.get(6))));
                syllabusList.add(new Syllabus("Week8", brToEnter(elementsWeek.get(7))));
                syllabusList.add(new Syllabus("Week9", brToEnter(elementsWeek.get(8))));
                syllabusList.add(new Syllabus("Week10", brToEnter(elementsWeek.get(9))));
                syllabusList.add(new Syllabus("Week11", brToEnter(elementsWeek.get(10))));
                syllabusList.add(new Syllabus("Week12", brToEnter(elementsWeek.get(11))));
                syllabusList.add(new Syllabus("Week13", brToEnter(elementsWeek.get(12))));
                syllabusList.add(new Syllabus("Week14", brToEnter(elementsWeek.get(13))));
                syllabusList.add(new Syllabus("Week15", brToEnter(elementsWeek.get(14))));
                syllabusList.add(new Syllabus("Week16", brToEnter(elementsWeek.get(15))));

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {
            Log.e("TAG3", "BackgroundParseTask @ onPostExecute started");

            Log.e("TAG3", "syllabusList size값은 : " + syllabusList.size());
            //  업데이트
            syllabusListAdapter.notifyDataSetChanged();
            Log.e("TAG3", "BackgroundParseTask @ onPostExecute finished");
            asyncDialog.dismiss();
            cancel(true);
        }
    }
}
