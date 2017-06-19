package com.example.david.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 6/11/2017.
 */

public class Dropdown {
    private String name = "";
    private int icon = -1;
    private int id = 0;

    Dropdown(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName(){
        return this.name;
    }

    public int getIcon(){
        return this.icon;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }
}

class DropdownAdapter extends ArrayAdapter<Dropdown> {
    ArrayList<Dropdown> item, tempItem;

    public DropdownAdapter(Context context, ArrayList<Dropdown> objects) {
        super(context, R.layout.dropdown_row, R.id.dropdown_text, objects);
        this.item = objects;
        this.tempItem = new ArrayList<Dropdown>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, null);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        Dropdown item = getItem(position);
        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_row, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_row, parent, false);
        }
        TextView txtCustomer = (TextView) convertView.findViewById(R.id.dropdown_text);
        ImageView ivCustomerImage = (ImageView) convertView.findViewById(R.id.dropdown_icon);

        if (txtCustomer != null)
            txtCustomer.setText(item.getName());

        if (ivCustomerImage != null)
            ivCustomerImage.setImageResource(item.getIcon());

        return convertView;
    }
}

