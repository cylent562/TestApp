package com.example.david.testapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by David on 6/18/2017.
 */

public class CollectionAdapter extends BaseAdapter{

    private final Context mContext;
    private final ArrayList<Collection> collections;

    public CollectionAdapter(Context context, ArrayList<Collection> collections) {
        this.mContext = context;
        this.collections = collections;
    }

    // 2
    @Override
    public int getCount() {
        return collections.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        Collection collection = collections.get(position);
        return collection.getId();
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Collection collection = collections.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.collection_layout, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_portrait);
        final ImageView imageViewFavorite = (ImageView)convertView.findViewById(R.id.imageview_rarity);
        TextView hero_boon = (TextView) convertView.findViewById(R.id.hero_boon);
        TextView hero_bane = (TextView) convertView.findViewById(R.id.hero_bane);

        imageView.setImageResource(collection.getImageResource());
        if ( collection.getRarity() == 5) {
            imageView.setBackgroundResource(R.drawable.image_gold_border);
        } else if ( collection.getRarity() == 4) {
            imageView.setBackgroundResource(R.drawable.image_silver_border);
        } else if ( collection.getRarity() == 3) {
            imageView.setBackgroundResource(R.drawable.image_bronze_border);
        }
        //nameTextView.setText(collection.getName());
        imageViewFavorite.setImageResource(collection.getRarityImage());

        if (collection.getBoon().equals("Neutral")) {
            hero_boon.setText("Neutral");
            hero_boon.setTextColor(Color.GRAY);
            hero_bane.setVisibility(View.GONE);
        } else {
            hero_boon.setText("+"+collection.getBoon().toUpperCase());
            hero_bane.setText("-"+collection.getBane().toUpperCase());
        }

        return convertView;
    }
}