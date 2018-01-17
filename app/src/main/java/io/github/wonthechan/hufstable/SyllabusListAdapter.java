package io.github.wonthechan.hufstable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by YeChan on 2018-01-16.
 */

public class SyllabusListAdapter extends BaseAdapter{

    private Context context;
    private List<Syllabus> syllabusList;

    public SyllabusListAdapter(Context context, List<Syllabus> syllabusList) {
        this.context = context;
        this.syllabusList = syllabusList;
    }

    @Override
    public int getCount() {
        return syllabusList.size();
    }

    @Override
    public Object getItem(int i) {
        return syllabusList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // 어댑터에서 가장 중요한 메소드인 getView !
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.syllabus, null);

        TextView syllabusKey = (TextView) v.findViewById(R.id.syllabusKey);
        TextView syllabusValue = (TextView) v.findViewById(R.id.syllabusValue);

        syllabusKey.setText(syllabusList.get(i).getSyllabusKey());
        syllabusValue.setText(syllabusList.get(i).getSyllabusValue());

        v.setTag(syllabusList.get(i).getSyllabusKey());

        return v;
    }
}
