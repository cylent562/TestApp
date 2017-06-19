package com.example.david.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        return 0;
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
        //final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_hero_name);
        final ImageView imageViewFavorite = (ImageView)convertView.findViewById(R.id.imageview_rarity);

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

        return convertView;
    }
}