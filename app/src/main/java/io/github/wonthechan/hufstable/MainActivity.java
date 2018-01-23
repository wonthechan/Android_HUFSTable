package io.github.wonthechan.hufstable;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provid e
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //FragmentPagerAdapter는 메모리에 프래그먼트를 로드한 상태로 유지하지만(3개 프래그먼트 유지하는게 적당함)
    //메모리를 많이 차지하는경우 FragmentStatePagerAdapter를 사용하여 화면에 보이지 않는 프래그먼트는 메모리에서 제거한다.
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FirebaseAuth auth; // 싱글톤 패턴으로 어느 액티비티를 가든 참조할 수 있다.

    private TextView nameTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 화면 방향을 항상 세로로 고정
        auth = FirebaseAuth.getInstance(); // 현재 auth 를 가져온다.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_content);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        nameTextView = (TextView) view.findViewById(R.id.header_name_textView);
        emailTextView = (TextView) view.findViewById(R.id.header_email_textView);

        String userName = auth.getCurrentUser().getDisplayName();
        String userEmail = auth.getCurrentUser().getEmail();
        if(userName.equals(""))
        {
            nameTextView.setText(userEmail.substring(0, userEmail.indexOf('@')));
        }
        else
        {
            nameTextView.setText(userName);
        }
        emailTextView.setText(auth.getCurrentUser().getEmail());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "설정 버튼을 누르셨습니다.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                auth.signOut();
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private long lastTimeBackPressed;

    @Override
    public void onBackPressed(){
        // Main 화면에서 뒤로가기 버튼을 한번 누른 후 1.5초 이내로 또 버튼을 누르면 종료한다.
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500)
        {
            //auth.signOut();
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis(); // 버튼을 처음 한번 눌렀을때의 시간을 저장
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.test1) {
            Toast.makeText(this, "test1 버튼을 누르셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.test2) {
            Toast.makeText(this, "test2 버튼을 누르셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.test3) {
            Toast.makeText(this, "test3 버튼을 누르셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "share 버튼을 누르셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            auth.signOut();
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_content);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
