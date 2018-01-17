package io.github.wonthechan.hufstable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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

                syllabusList.add(new Syllabus("학수번호", paramYear));
                syllabusList.add(new Syllabus("교수", elementsLecture.get(6).text()));

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
