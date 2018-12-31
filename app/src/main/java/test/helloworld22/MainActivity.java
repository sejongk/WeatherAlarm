package test.helloworld22;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static test.helloworld22.PlusPerson.MY_PERMISSIONS_REQUEST_READ_CONTACTS;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
            tabs.addTab(tabs.newTab().setText("Tab 1"));
            tabs.addTab(tabs.newTab().setText("Tab 2"));
            tabs.addTab(tabs.newTab().setText("Tab 3"));
            tabs.setTabGravity(tabs.GRAVITY_FILL);

            //어답터설정
            final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 3);
            viewPager.setAdapter(myPagerAdapter);

            //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
            tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        }

}


    class MyPagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs; //tab의 갯수

        public MyPagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.mNumOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    MainFragment tab1 = new MainFragment();
                    return tab1;
                case 1:
                    Gallery tab2 = new Gallery();
                    return tab2;
                case 2:
                    //FreeTab tab3 = new FreeTab();
                   // return tab3;
                    WeatherAlarm tab3 = new WeatherAlarm();
                    return tab3;
                default:
                    return null;
            }
            //return null;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }


    }




