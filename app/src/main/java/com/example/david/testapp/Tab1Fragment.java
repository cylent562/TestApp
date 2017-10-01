package com.example.david.testapp;

import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static com.example.david.testapp.Main2Activity.HERO_DATA;
import static com.example.david.testapp.Main2Activity.PACKAGE_NAME;
import static com.example.david.testapp.Main2Activity.HEROES;


/**
 * Created by David on 6/11/2017.
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

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
        items = populateItemData("5 Stars");
        items2 = new ArrayList<>();
        items2.add(new Dropdown("5 Stars",Rarities[2]));
        items2.add(new Dropdown("4 Stars",Rarities[1]));
        items2.add(new Dropdown("3 Stars",Rarities[0]));

        //set_hero_dropdown(view, "5 stars", 0);
        final Spinner spin_hero = (Spinner) view.findViewById(R.id.spinner);
        spin_hero.getBackground().setColorFilter(ContextCompat.getColor(getContext(),android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        final DropdownAdapter adapter = new DropdownAdapter(getContext(), populateItemData("5 Stars"));
        spin_hero.setAdapter(adapter);


        //set rarities dropdown
        final Spinner spin_rarity = (Spinner) view.findViewById(R.id.spinner2);
        spin_rarity.getBackground().setColorFilter(ContextCompat.getColor(getContext(),android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        final DropdownAdapter adapter2 = new DropdownAdapter(getContext(), items2);
        spin_rarity.setAdapter(adapter2);
        spin_rarity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                View main = view.getRootView();
                DropdownAdapter rarity_adapter = (DropdownAdapter) parent.getAdapter();
                String rarity = rarity_adapter.getItem(position).getName();
                Spinner spin_hero = (Spinner) main.findViewById(R.id.spinner);
                DropdownAdapter adapter = (DropdownAdapter) spin_hero.getAdapter();
                String name = adapter.getItem(spin_hero.getSelectedItemPosition()).getName();
                set_hero_dropdown(main, rarity, name);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Add button

        FloatingActionButton btn =  (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View main = v.getRootView();
                Spinner spin_hero = (Spinner) main.findViewById(R.id.spinner);
                DropdownAdapter adapter = (DropdownAdapter) spin_hero.getAdapter();
                String name = adapter.getItem(spin_hero.getSelectedItemPosition()).getName();
                String rarity = adapter2.getItem(spin_rarity.getSelectedItemPosition()).getName();
                int value = Character.getNumericValue(rarity.charAt(0));
                String filename = name.toLowerCase().replaceAll("[ ]", "_").replaceAll("[')(+]", "");
                //text.toLowerCase().replaceAll("[ ]", "_").replaceAll("[')(+]", "");
                HashMap<String,String> stat_map = getStatBoonBane(main);
                String boon = stat_map.get("boon");
                String bane = stat_map.get("bane");

                if (boon.equals("Neutral") && !bane.equals("Neutral")){
                    Toast.makeText(getContext(),"Please add a plus stat!",Toast.LENGTH_SHORT).show();
                } else if(!boon.equals("Neutral") && bane.equals("Neutral")) {
                    Toast.makeText(getContext(),"Please add a minus stat!",Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(),"+" + boon + " -" + bane,Toast.LENGTH_SHORT).show();
                    mydb = new DBHelper(getContext());
                    mydb.insertHero(new Data(-1, name, value, boon, bane));
                    Toast.makeText(getContext(),"Added " + name,Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void set_hero_dropdown(View view, String rarity, String current_hero){
        //set hero dropdown
        Spinner spin_hero = (Spinner) view.findViewById(R.id.spinner);
        spin_hero.getBackground().setColorFilter(ContextCompat.getColor(getContext(),android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        DropdownAdapter adapter = new DropdownAdapter(getContext(), populateItemData(rarity));
        spin_hero.setAdapter(adapter);
        for (int i = 0; i < spin_hero.getCount(); i++) {
            if(adapter.getItem(i).getName() == current_hero){
                spin_hero.setSelection(i);
                break;
            }
        }
        spin_hero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                View main = view.getRootView();
                displayHeroInfo(main);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private HashMap<String, String> getStatBoonBane(View view) {
        HashMap<String, String> map = new HashMap<>();
        String[] stats = new String[] {"hp","atk","spd","def","res"};
        map.put("boon", "Neutral");
        map.put("bane", "Neutral");
        for (int i = 0; i < stats.length; i++) {
            int resID = view.getResources().getIdentifier(stats[i]+"_max","id",PACKAGE_NAME);
            TextView stat_text = (TextView) view.findViewById(resID);
            String[] tag = (String[]) stat_text.getTag();
            if ( tag == null) {
                return null;
            }
            if ( tag[1].equals("2")) { //boon
                map.put("boon", tag[0]);
            } else if (tag[1].equals("0")) { //bane
                map.put("bane", tag[0]);
            }
        }
        return map;
    }

    private void displayHeroInfo(View view){
        Spinner spin_hero = (Spinner) view.findViewById(R.id.spinner);
        Spinner spin_rarity = (Spinner) view.findViewById(R.id.spinner2);
        DropdownAdapter temp1 = (DropdownAdapter) spin_hero.getAdapter();
        DropdownAdapter temp2 = (DropdownAdapter) spin_rarity.getAdapter();
        String name = temp1.getItem(spin_hero.getSelectedItemPosition()).getName();
        String rarity = temp2.getItem(spin_rarity.getSelectedItemPosition()).getName();

        Spinner hp_spinner = (Spinner) view.findViewById(R.id.spinner_hp);
        TextView hp_text = (TextView) view.findViewById(R.id.hp_max);
        Spinner atk_spinner = (Spinner) view.findViewById(R.id.spinner_atk);
        TextView atk_text = (TextView) view.findViewById(R.id.atk_max);
        Spinner spd_spinner = (Spinner) view.findViewById(R.id.spinner_spd);
        TextView spd_text = (TextView) view.findViewById(R.id.spd_max);
        Spinner def_spinner = (Spinner) view.findViewById(R.id.spinner_def);
        TextView def_text = (TextView) view.findViewById(R.id.def_max);
        Spinner res_spinner = (Spinner) view.findViewById(R.id.spinner_res);
        TextView res_text = (TextView) view.findViewById(R.id.res_max);

        ImageView move = (ImageView) view.findViewById(R.id.icon_move);
        ImageView weapon_type = (ImageView) view.findViewById(R.id.icon_weapon_type);
        TextView rating = (TextView) view.findViewById(R.id.hero_rating);
        TextView hero_title = (TextView) view.findViewById(R.id.hero_title);

        TextView weapon_name = (TextView) view.findViewById(R.id.icon_weapon_name);
        TextView assist_name = (TextView) view.findViewById(R.id.icon_assist_name);
        TextView special_name = (TextView) view.findViewById(R.id.icon_special_name);
        ImageView  passiveA_icon = (ImageView) view.findViewById(R.id.icon_passiveA);
        ImageView  passiveB_icon = (ImageView) view.findViewById(R.id.icon_passiveB);
        ImageView  passiveC_icon = (ImageView) view.findViewById(R.id.icon_passiveC);
        TextView passiveA_name = (TextView) view.findViewById(R.id.icon_passiveA_name);
        TextView passiveB_name = (TextView) view.findViewById(R.id.icon_passiveB_name);
        TextView passiveC_name = (TextView) view.findViewById(R.id.icon_passiveC_name);

        try {
            JSONArray init_stats = HERO_DATA.getJSONObject(name).getJSONArray("stats_"+rarity.charAt(0)).getJSONArray(0);
            JSONArray max_stats = HERO_DATA.getJSONObject(name).getJSONArray("stats_"+rarity.charAt(0)).getJSONArray(1);
            String hero_move = HERO_DATA.getJSONObject(name).getString("move").toLowerCase();
            String hero_weapon_type = HERO_DATA.getJSONObject(name).getString("weapon_type").toLowerCase();
            String hero_color = HERO_DATA.getJSONObject(name).getString("color").toLowerCase();
            String hero_passiveA = parseJSONToDrawable(HERO_DATA.getJSONObject(name), "passiveA");
            String hero_passiveB = parseJSONToDrawable(HERO_DATA.getJSONObject(name), "passiveB");
            String hero_passiveC = parseJSONToDrawable(HERO_DATA.getJSONObject(name), "passiveC");
            String weapon_text = parseJSONToDrawable(HERO_DATA.getJSONObject(name), "weapon");
            String assist_text = parseJSONToDrawable(HERO_DATA.getJSONObject(name), "assist");
            String special_text = parseJSONToDrawable(HERO_DATA.getJSONObject(name), "special");
            String hero_title_text = HERO_DATA.getJSONObject(name).getString("title");

            String weapon_icon = "icon_class_"+ hero_color + "_" + hero_weapon_type;
            String move_icon = "icon_move_" + hero_move;


            String hp = init_stats.getString(0);
            String atk = init_stats.getString(1);
            String spd = init_stats.getString(2);
            String def = init_stats.getString(3);
            String res = init_stats.getString(4);
            String max_hp = max_stats.getString(0);
            String max_atk = max_stats.getString(1);
            String max_spd = max_stats.getString(2);
            String max_def = max_stats.getString(3);
            String max_res = max_stats.getString(4);

            setStats(hp, max_hp, hp_spinner, hp_text, "hp");
            setStats(atk, max_atk, atk_spinner, atk_text, "atk");
            setStats(spd, max_spd, spd_spinner, spd_text, "spd");
            setStats(def, max_def, def_spinner, def_text, "def");
            setStats(res, max_res, res_spinner, res_text, "res");

            setIcon(view, move, move_icon);
            setIcon(view, weapon_type, weapon_icon);
            int sumRatings =
                    Integer.parseInt(max_hp) + Integer.parseInt(max_atk) +
                    Integer.parseInt(max_spd) + Integer.parseInt(max_def) + Integer.parseInt(max_res);
            rating.setText(String.valueOf(sumRatings));

            if (!weapon_text.equals("null")) weapon_name.setText(HERO_DATA.getJSONObject(name).getString("weapon"));
            else weapon_name.setText("");
            if (!assist_text.equals("null")) assist_name.setText(HERO_DATA.getJSONObject(name).getString("assist"));
            else assist_name.setText("");
            if (!special_text.equals("null")) special_name.setText(HERO_DATA.getJSONObject(name).getString("special"));
            else special_name.setText("");

            if (!hero_passiveA.equals("null")) {
                passiveA_name.setText(HERO_DATA.getJSONObject(name).getString("passiveA"));
                setIcon(view, passiveA_icon, hero_passiveA);
            } else {
                passiveA_name.setText("");
                setIcon(view, passiveA_icon, hero_passiveA);
            }
            if (!hero_passiveB.equals("null")) {
                passiveB_name.setText(HERO_DATA.getJSONObject(name).getString("passiveB"));
                setIcon(view, passiveB_icon, hero_passiveB);
            } else {
                passiveB_name.setText("");
                setIcon(view, passiveB_icon, hero_passiveB);
            }
            if (!hero_passiveC.equals("null")) {
                passiveC_name.setText(HERO_DATA.getJSONObject(name).getString("passiveC"));
                setIcon(view, passiveC_icon, hero_passiveC);
            } else {
                passiveC_name.setText("");
                setIcon(view, passiveC_icon, hero_passiveC);
            }

            hero_title.setText(hero_title_text);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String parseJSONToDrawable(JSONObject ob, String key){
        try {
            if (ob.has(key)) {
                String text = ob.getString(key);
                text = text.toLowerCase().replaceAll("[ ]", "_").replaceAll("[')(+]", "");
                return text;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    private void setIcon(View view, ImageView img, String filename) {
        int image_id = view.getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + filename, null, null);
        img.setImageResource(image_id);
    }

    private void setStats(String initStat, String maxStat, Spinner spin, TextView text, String stat_type){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, buildStatVariance(Integer.parseInt(initStat)));
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(dataAdapter);
        spin.setSelection(1);
        int max = Integer.parseInt(maxStat);
        String[] memory = new String[]{String.valueOf(max-3), String.valueOf(max), String.valueOf(max+3), stat_type};
        spin.setTag(memory);
        text.setText(maxStat);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                View main = view.getRootView();
                String[] tag = (String[]) parent.getTag();

                //Integer[] colors = new Integer[]{Color.parseColor("#ff00ff"), Color.BLACK, Color.parseColor("#000e500")};
                Integer[] colors = new Integer[]{Color.RED, Color.BLACK, Color.GREEN};

                if(main != null) {
                    TextView hp = (TextView) main.findViewById(R.id.hp_max);
                    TextView atk = (TextView) main.findViewById(R.id.atk_max);
                    TextView spd = (TextView) main.findViewById(R.id.spd_max);
                    TextView def = (TextView) main.findViewById(R.id.def_max);
                    TextView res = (TextView) main.findViewById(R.id.res_max);

                    switch(tag[3]) {
                        case "hp":
                            String[] map1 = new String[] { "hp", String.valueOf(position)};
                            updateStats(hp, tag[position], colors[position], map1);
                            TextView[] temp1 = new TextView[] {atk,spd,def,res};
                            verifyStats(temp1, main, String.valueOf(position));
                            break;
                        case "atk":
                            String[] map2 = new String[] { "atk", String.valueOf(position)};
                            updateStats(atk, tag[position], colors[position], map2);
                            TextView[] temp2 = new TextView[] {hp,spd,def,res};
                            verifyStats(temp2, main, String.valueOf(position));
                            break;
                        case "spd":
                            String[] map3 = new String[] { "spd", String.valueOf(position)};
                            updateStats(spd, tag[position], colors[position], map3);
                            TextView[] temp3 = new TextView[] {hp,atk,def,res};
                            verifyStats(temp3, main, String.valueOf(position));
                            break;
                        case "def":
                            String[] map4 = new String[] { "def", String.valueOf(position)};
                            updateStats(def, tag[position], colors[position], map4);
                            TextView[] temp4 = new TextView[] {hp,spd,atk,res};
                            verifyStats(temp4, main, String.valueOf(position));
                            break;
                        case "res":
                            String[] map5 = new String[] { "res", String.valueOf(position)};
                            updateStats(res, tag[position], colors[position], map5);
                            TextView[] temp5 = new TextView[] {hp,spd,def,atk};
                            verifyStats(temp5, main, String.valueOf(position));
                            break;
                        default:
                            break;
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateStats(TextView stat_object, String text, int color, String[] map) {
        stat_object.setText(text);
        stat_object.setTextColor(color);
        stat_object.setTag(map);
    }

    private void verifyStats(TextView[] arr, View view, String position) {
        try {
            for (int i = 0; i < arr.length; i++) {
                String[] tag = (String[]) arr[i].getTag();
                if (tag == null) {
                    continue;
                }
                if (tag[1].equals("2") && position.equals("2")) { //boon
                    int resID = view.getResources().getIdentifier("spinner_"+ tag[0], "id", PACKAGE_NAME);
                    Spinner spin = (Spinner) view.findViewById(resID);
                    spin.setSelection(1);
                } else if (tag[1].equals("0") && position.equals("0")) { //bane
                    int resID = view.getResources().getIdentifier("spinner_" + tag[0], "id", PACKAGE_NAME);
                    Spinner spin = (Spinner) view.findViewById(resID);
                    spin.setSelection(1);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> buildStatVariance(int num) {
        ArrayList<String> values = new ArrayList();
        values.add(String.valueOf(num-1));
        values.add(String.valueOf(num));
        values.add(String.valueOf(num+1));
        return values;
    }

    private ArrayList<Dropdown> populateItemData(String rarity) {
        ArrayList<Dropdown> items = new ArrayList<>();
        Collections.sort(HEROES);
        JSONArray stats = null;
        try {
            for (int i = 0; i < HEROES.size(); i++) {
                String name = HEROES.get(i);
                //stats = HERO_DATA.getJSONObject(name).getJSONArray("stats_"+rarity.charAt(0));
                if (HERO_DATA.getJSONObject(name).has("stats_"+rarity.charAt(0))) {
                    items.add(new Dropdown(name, findImageMatch(name)));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    private int findImageMatch(String name) {
        try {
            String s = name.toLowerCase().replaceAll("[ ]", "_").replaceAll("[')(+]", "");
            int id = getResources().getIdentifier(PACKAGE_NAME+":drawable/" + s, null, null);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}


