package io.github.wonthechan.hufstable;

/**
 * Created by YeChan on 2018-01-16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        return rootView;
    }
}
