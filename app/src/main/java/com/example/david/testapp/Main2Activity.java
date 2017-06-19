package com.example.david.testapp;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");
        PACKAGE_NAME = getApplicationContext().getPackageName();

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(final ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Add");
        adapter.addFragment(new Tab2Fragment(), "Collection");
        adapter.addFragment(new Tab3Fragment(), "TAB3");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //SectionsPageAdapter mAdapter = new SectionsPageAdapter(getSupportFragmentManager());
                //Fragment frag = mAdapter.getFragment(position);
                if (position == 1) {
                    DBHelper mydb;
                    ArrayList<Collection> collections;

                    mydb = new DBHelper(viewPager.getContext());
                    ArrayList<Data> arr = mydb.getAllHeroes();
                    StringBuilder sb  = new StringBuilder();
                    collections = new ArrayList<>();

                    for (Data d : arr){
                        String str = d.getName().toLowerCase().replaceAll("[( ]", "_").replaceAll("[')]", "");
                        int image_id = getResources().getIdentifier(PACKAGE_NAME+":drawable/" + str, null, null);
                        str = "rarity_" + String.valueOf(d.getRarity());
                        int rarity_id = getResources().getIdentifier(PACKAGE_NAME+":drawable/" + str, null, null);
                        collections.add(new Collection(d.getId(), d.getName(), image_id, d.getRarity(), rarity_id));
                    }

                    Tab2Fragment tab2 = new Tab2Fragment();
                    tab2.updateFragment(collections, viewPager);
                    Toast.makeText(viewPager.getContext(),PACKAGE_NAME,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(viewPager.getContext(),"LOL",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        SectionsPageAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {return mFragmentList.get(position);}

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
