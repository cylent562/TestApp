package com.example.david.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David on 6/13/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Collection.db";
    public static final String HEROES_TABLE_NAME = "heroes";
    public static final String HEROES_COLUMN_ID = "id";
    public static final String HEROES_COLUMN_NAME = "name";
    public static final String HEROES_COLUMN_RARITY = "rarity";
    public static final String HEROES_COLUMN_FILENAME = "filename";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table heroes " +
                        "(id integer primary key, name text,rarity integer, filename text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS heroes");
        onCreate(db);
    }

    public boolean insertHero (Data d) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", d.getName());
        contentValues.put("rarity", d.getRarity());
        contentValues.put("filename", d.getFilename());
        db.insert("heroes", null, contentValues);
        return true;
    }

    public boolean updateHero (Integer id, String name, Integer rarity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("rarity", rarity);
        db.update("heroes", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteHero (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("heroes",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Data getHeroDataById(int id) {
        Data d = null;
        String name = "";
        String filename = "";
        int rarity = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from heroes where id="+id, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            name = res.getString(res.getColumnIndex(HEROES_COLUMN_NAME));
            rarity = res.getInt(res.getColumnIndex(HEROES_COLUMN_RARITY));
            filename = res.getString(res.getColumnIndex(HEROES_COLUMN_FILENAME));
            d = new Data(id, name, rarity, filename);
            res.moveToNext();
        }
        return d;
    }


    public ArrayList<Data> getAllHeroes() {
        ArrayList<Data> array_list = new ArrayList<>();
        String name = "";
        String filename = "";
        int rarity = -1;
        int id = -1;

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from heroes", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            name = res.getString(res.getColumnIndex(HEROES_COLUMN_NAME));
            rarity = res.getInt(res.getColumnIndex(HEROES_COLUMN_RARITY));
            id = res.getInt(res.getColumnIndex(HEROES_COLUMN_ID));
            filename = res.getString(res.getColumnIndex(HEROES_COLUMN_FILENAME));
            array_list.add(new Data(id, name, rarity, filename));
            res.moveToNext();
        }
        return array_list;
    }
}
