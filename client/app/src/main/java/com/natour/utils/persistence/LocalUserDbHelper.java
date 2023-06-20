/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.utils.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalUserDbHelper extends SQLiteOpenHelper {
    // Attributi
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LocalUser.DB";

    // Campi della table
    public static final String TABLE_NAME = "local_user";
    public static final String _ID = "_id";
    public static final String USERNAME = "username";
    public static final String URL = "url";
    public static final String LOGGED_WITH_GOOGLE = "logged_with_google";

    /*
        `varchar` is `TEXT` in SQLite. Internally the data is always stored as `TEXT`,
        pertanto anche creando la tabella con VARCHAR(LEN), SQLite userà le regole del tipo `TEXT`
    */
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY UNIQUE, "
                    + USERNAME + " TEXT NOT NULL, " + URL + " TEXT NOT NULL, " + LOGGED_WITH_GOOGLE + " TEXT NOT NULL);";

    private static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Costruttore
    public LocalUserDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }
}