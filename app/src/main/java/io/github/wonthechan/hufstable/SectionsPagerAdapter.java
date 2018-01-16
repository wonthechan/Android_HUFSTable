package io.github.wonthechan.hufstable;

/**
 * Created by YeChan on 2018-01-16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // return CourseFragment.newInstance(position + 1);
        // Returning the current tab
        switch (position){
            case 0:
            {
                CourseFragment tab1 = new CourseFragment();
                return tab1;
            }
            case 1:
            {
                ScheduleFragment tab2 = new ScheduleFragment();
                return tab2;
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "강의목록";
            case 1:
                return "시간표";
            default:
                return  null;
        }
    }

}
