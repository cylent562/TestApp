package com.example.david.testapp;

import android.media.Image;
import android.provider.ContactsContract;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.david.testapp.Main2Activity.PACKAGE_NAME;


/**
 * Created by David on 9/30/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, HashMap<String, Integer>> expandableListDetail;
    private HashMap<String, Integer> expandableCount;
    private final ArrayList<Collection> collections;

    public ExpandableListAdapter(Context context, List<String> expandableListTitle,
                                 HashMap<String, HashMap<String, Integer>> expandableListDetail, HashMap<String, Integer> expandableCount, ArrayList<Collection> collections) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.collections = collections;
        this.expandableCount = expandableCount;
    }
    public String getChild(int listPosition, int expandedListPosition) {
        HashMap<String, Integer> map = this.expandableListDetail.get(this.expandableListTitle.get(listPosition));
        String hero_name = (String) map.keySet().toArray()[expandedListPosition];
        String count = String.valueOf(map.get(hero_name));
        return count;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        HashMap<String, Integer> map = this.expandableListDetail.get(this.expandableListTitle.get(listPosition));
        String hero_name = (String) map.keySet().toArray()[expandedListPosition];
        String count = String.valueOf(map.get(hero_name));

        String filename = hero_name.toLowerCase().replaceAll("[ ]", "_").replaceAll("[')(+]", "");
        int image_id = convertView.getResources().getIdentifier(PACKAGE_NAME+":drawable/" + filename, null, null);

        ImageView hero_image = (ImageView) convertView.findViewById(R.id.expanded_hero);
        hero_image.setImageResource(image_id);

        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expanded_hero_count);
        expandedListTextView.setText(count);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        if (expandableCount.get(listTitle) != 0 ) {
            listTitleTextView.setText(expandableCount.get(listTitle) + " x " + listTitle);
        } else {
            listTitleTextView.setText(listTitle);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
