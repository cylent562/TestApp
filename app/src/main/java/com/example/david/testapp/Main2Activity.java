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
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    public static String PACKAGE_NAME;

    public static ArrayList<String> HEROES = new ArrayList<String>();
    public static JSONObject HERO_DATA = new JSONObject();
    public static String inheritable_skills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //check server
        try {
            if (server_validation()) {
                JSONObject hero_obj = new JSONObject(loadJSONFromAsset("hero_dump.txt"));
                HERO_DATA = hero_obj;
                for (Iterator<String> iterator = hero_obj.keys(); iterator.hasNext();) {
                    String key = iterator.next();
                    HEROES.add(key);
                }
                inheritable_skills = loadJSONFromAsset("skill_dump.txt");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private boolean server_validation(){
        return true;
    }

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("feh_dump.txt")));
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void setupViewPager(final ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Tab1Fragment");
        adapter.addFragment(new Tab2Fragment(), "Tab2Fragment");
        adapter.addFragment(new Tab3Fragment(), "Tab3Fragment");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //SectionsPageAdapter mAdapter = new SectionsPageAdapter(getSupportFragmentManager());
                //Fragment frag = mAdapter.getFragment(position);

                //On Collection page, make DB call to refresh collection.
                if (position == 1) {
                    DBHelper mydb;
                    ArrayList<Collection> collections;

                    mydb = new DBHelper(viewPager.getContext());
                    ArrayList<Data> arr = mydb.getAllHeroes();
                    StringBuilder sb  = new StringBuilder();
                    collections = new ArrayList<>();

                    for (Data d : arr){
                        //String str = d.getName().toLowerCase().replaceAll("[ ]", "_").replaceAll("[')(]", "");
                        int image_id = getResources().getIdentifier(PACKAGE_NAME+":drawable/" + d.getFilename(), null, null);
                        String str = "rarity_" + String.valueOf(d.getRarity());
                        int rarity_id = getResources().getIdentifier(PACKAGE_NAME+":drawable/" + str, null, null);
                        collections.add(new Collection(d.getId(), d.getName(), image_id, d.getRarity(), rarity_id));
                    }

                    Tab2Fragment tab2 = new Tab2Fragment();
                    tab2.updateFragment(collections, viewPager);
                    //Toast.makeText(viewPager.getContext(),PACKAGE_NAME,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(viewPager.getContext(),"LOL",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //Setups Fragment, don't change.
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
