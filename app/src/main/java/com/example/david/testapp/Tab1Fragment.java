package com.example.david.testapp;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by David on 6/11/2017.
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    String[] Heroes = {"Alfonse","Anna","Sharena","Virion","Tiki(Adult)","Tiki(Young)","Alm","Chrom","Corrin(M)","Eirika","Fir","Hana","Hinata","Ike","Karel","Laslow","Lloyd","Lon'qu","Lucina","Lyn","Marth","Navarre","Ogma","Olivia","Roy","Ryoma","Selena","Seliph","Caeda","Palla","Cain","Eldigan","Eliwood","Stahl","Xander","Draug","Zephiel","Henry","Lilina","Raigh","Sanaki","Sophia","Tharja","Leo","Arthur","Barst","Bartre","Chrom(Spring Festival)","Hawkeye","Raven","Beruka","Camilla","Cherche","Michalis","Minerva","Narcian","Frederick","Gunter","Titania","Hector","Sheena","Fae","Julia","Merric","Nino","Robin(F)","Soren","Veronica","Camilla(Spring Festival)","Cecilia","Corrin(F)","Ninian","Nowi","Azura","Donnel","Ephraim","Lukas","Oboro","Catria","Clair","Cordelia","Est","Florina","Hinoka","Shanna","Subaki","Abel","Camus","Jagen","Peri","Sully","Xander(Spring Festival)","Effie","Gwendolyn","Linde","Lucina(Spring Festival)","Odin","Robin(M)","Bruno","Olwen","Reinhardt","Ursula","Faye","Gordin","Jeorge","Klein","Niles","Rebecca","Setsuna","Takumi","Felicia","Gaius","Jaffar","Jakob","Kagero","Matthew","Saizo","Azama","Lachesis","Lissa","Lucius","Maria","Mist","Sakura","Serra","Wrys","Clarine","Elise","Priscilla"};
    Integer[] Rarities = {R.drawable.rarity_3, R.drawable.rarity_4, R.drawable.rarity_5};

    DropdownAdapter adapter = null;
    ArrayList<Dropdown> items = null;
    ArrayList<Dropdown> items2 = null;
    DBHelper mydb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        items = new ArrayList<>();
        items = populateItemData(items);
        items2 = new ArrayList<>();
        items2.add(new Dropdown("5 Stars",Rarities[2]));
        items2.add(new Dropdown("4 Stars",Rarities[1]));
        items2.add(new Dropdown("3 Stars",Rarities[0]));

        //addItemsToSpinner(view);

        //set hero dropdown
        final Spinner spin_hero = (Spinner) view.findViewById(R.id.spinner);
        spin_hero.getBackground().setColorFilter(ContextCompat.getColor(getContext(),android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        final DropdownAdapter adapter = new DropdownAdapter(getContext(), items);
        spin_hero.setAdapter(adapter);

        //set rarities dropdown
        final Spinner spin_rarity = (Spinner) view.findViewById(R.id.spinner2);
        spin_rarity.getBackground().setColorFilter(ContextCompat.getColor(getContext(),android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        final DropdownAdapter adapter2 = new DropdownAdapter(getContext(), items2);
        spin_rarity.setAdapter(adapter2);

        //Add button
        FloatingActionButton btn =  (FloatingActionButton) view.findViewById(R.id.floatingActionButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = adapter.getItem(spin_hero.getSelectedItemPosition()).getName();
                String rarity = adapter2.getItem(spin_rarity.getSelectedItemPosition()).getName();
                int value = Character.getNumericValue(rarity.charAt(0));
                mydb = new DBHelper(getContext());
                mydb.insertHero(name, value);
                Toast.makeText(getContext(),"Added " + name,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l){
        Dropdown d = adapter.getItem(position);

    }


    private ArrayList<Dropdown> populateItemData(ArrayList<Dropdown> items) {
        Arrays.sort(Heroes);
        for (int i = 0; i < Heroes.length; i++) {
            String name = Heroes[i];
            items.add(new Dropdown(Heroes[i],findImageMatch(name)));
        }
        return items;
    }

    private int findImageMatch(String name) {
        try {
            String s = name.toLowerCase();
            s = s.replace("(", "_");
            s = s.replace(")", "");
            s = s.replace("'", "");
            s = s.replace(" ", "_");
            int id = getResources().getIdentifier("com.example.david.testapp:drawable/" + s, null, null);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}


