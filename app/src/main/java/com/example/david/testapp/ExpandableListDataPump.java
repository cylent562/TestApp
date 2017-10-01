package com.example.david.testapp;

import android.support.v4.view.ViewPager;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.david.testapp.Main2Activity.SKILL_DATA;
import static com.example.david.testapp.Main2Activity.HERO_DATA;


/**
 * Created by David on 9/30/2017.
 */

public class ExpandableListDataPump {
    public static List<HashMap> getData(ArrayList<Collection> collections) {

        List<HashMap> expandableListDetail = new ArrayList<>();
        HashMap<String, HashMap<String, Integer>> skill_chain = new HashMap<>();
        HashMap<String, Integer> hero_map = new HashMap<>();
        HashMap<String, Integer> skill_map = new HashMap<>();

        try {
            for (Collection c : collections) {
                String key = c.getName();
                if (hero_map.containsKey(key)) {
                    hero_map.put(key, hero_map.get(key) + 1);
                } else {
                    hero_map.put(key, 1);
                }
            }

            for (Iterator<String> iterator = SKILL_DATA.keys(); iterator.hasNext(); ) {
                String key = iterator.next();
                JSONArray heroes = SKILL_DATA.getJSONObject(key).getJSONArray("characters");
                HashMap<String, Integer> temp = new HashMap<>();
                int count = 0;
                for (int i = 0; i < heroes.length() ; i++) {
                    String hero = heroes.getString(i);
                    temp.put(hero, 0);
                    if (hero_map.containsKey(hero)) {
                        temp.put(hero, hero_map.get(hero));
                        count += hero_map.get(hero);
                    }
                }
                skill_map.put(key, count);
                skill_chain.put(key, temp);
            }

            expandableListDetail.add(skill_chain);
            expandableListDetail.add(skill_map);

            /*

            List<String> cricket = new ArrayList<>();
            cricket.add("India");
            cricket.add("Pakistan");
            cricket.add("Australia");
            cricket.add("England");
            cricket.add("South Africa");

            List<String> football = new ArrayList<>();
            football.add("Brazil");
            football.add("Spain");
            football.add("Germany");
            football.add("Netherlands");
            football.add("Italy");

            List<String> basketball = new ArrayList<>();
            basketball.add("United States");
            basketball.add("Spain");
            basketball.add("Argentina");
            basketball.add("France");
            basketball.add("Russia");

            expandableListDetail.put("CRICKET TEAMS", cricket);
            expandableListDetail.put("FOOTBALL TEAMS", football);
            expandableListDetail.put("BASKETBALL TEAMS", basketball);*/
            return expandableListDetail;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
