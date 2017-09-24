package com.example.david.testapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.david.testapp.Main2Activity.PACKAGE_NAME;
import static com.example.david.testapp.R.id.container;

/**
 * Created by David on 6/11/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";
    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

        //button
        btnTEST = (Button) view.findViewById(R.id.btnTEST2);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 2",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void updateFragment(ArrayList<Collection> collections, ViewPager v) {
        final Context mContext = v.getContext();

        GridView gridView = (GridView) v.findViewById(R.id.gridview);
        CollectionAdapter collectionAdapter = new CollectionAdapter(mContext, collections);
        gridView.setAdapter(collectionAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View convertView = layoutInflater.inflate(R.layout.tab2_fragment, null, false);

                DBHelper mydb;
                Data d;
                mydb = new DBHelper(convertView.getContext());
                d = mydb.getHeroDataById((int)id);

                ImageView actionImage = (ImageView) convertView.findViewById(R.id.image_hero_action);
                try {
                    String filename_special = d.getFilename() + "_special";
                    int image_id = convertView.getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + filename_special, null, null);
                    actionImage.setImageResource(image_id);
                    Toast.makeText(convertView.getContext(), "SUCCESS: "+filename_special, Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    String filename_special = d.getFilename() + "_special";
                    Toast.makeText(view.getContext(), "FAILED: "+e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
