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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    public static String PACKAGE_NAME;

    public static List<String> HEROES = new ArrayList<>();
    public static JSONObject HERO_DATA = new JSONObject();
    public static JSONObject SKILL_DATA = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //check server
        try {
            if (server_validation()) {
                HERO_DATA = new JSONObject(loadJSONFromAsset("hero_dump.txt"));
                for (Iterator<String> iterator = HERO_DATA.keys(); iterator.hasNext();) {
                    String key = iterator.next();
                    HEROES.add(key);
                }
                SKILL_DATA = new JSONObject(loadJSONFromAsset("inheritable_skill_dump.txt"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        DBHelper d;
        d = new DBHelper(this);

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
        adapter.addFragment(new Tab1Fragment(), "Add");
        adapter.addFragment(new Tab2Fragment(), "Collection");
        adapter.addFragment(new Tab3Fragment(), "Overview");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
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
                    Tab2Fragment tab2 = new Tab2Fragment();
                    tab2.updateFragment(getCollection(viewPager), viewPager);
                    //Toast.makeText(viewPager.getContext(),PACKAGE_NAME,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(viewPager.getContext(),"LOL",Toast.LENGTH_SHORT).show();
                }

                //On Overview page, make DB call to refresh skill chain.
                if (position == 2) {
                    Tab3Fragment tab3 = new Tab3Fragment();
                    tab3.updateFragment(getCollection(viewPager), viewPager);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public ArrayList<Collection> getCollection(ViewPager viewPager) {
        DBHelper mydb;
        ArrayList<Collection> collections;
        mydb = new DBHelper(viewPager.getContext());
        ArrayList<Data> list = mydb.getAllHeroes();
        collections = new ArrayList<>();

        for (Data d : list){
            String filename = d.getName().toLowerCase().replaceAll("[ ]", "_").replaceAll("[')(+]", "");
            int image_id = getResources().getIdentifier(PACKAGE_NAME+":drawable/" + filename, null, null);
            String str = "rarity_" + String.valueOf(d.getRarity());
            int rarity_id = getResources().getIdentifier(PACKAGE_NAME+":drawable/" + str, null, null);
            collections.add(new Collection(d.getId(), d.getName(), image_id, d.getRarity(), rarity_id, d.getBoon(), d.getBane()));
        }
        return collections;
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
