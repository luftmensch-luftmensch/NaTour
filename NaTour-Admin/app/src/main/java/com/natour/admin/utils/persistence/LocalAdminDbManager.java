/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.utils.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LocalAdminDbManager {

    private LocalAdminDbHelper localAdminDbHelper;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

     public LocalAdminDbManager (Context context) {
         this.context = context;
     }

    // Funzioni ausiliarie per la gestione del db

    // Settaggio e lettura dei dati in write mode
    public void openW() throws SQLException{
        localAdminDbHelper = new LocalAdminDbHelper(context);
        sqLiteDatabase = localAdminDbHelper.getWritableDatabase();
    }


    // Lettura delle informazioni da un datavase
    public void openR() throws SQLException {
        localAdminDbHelper = new LocalAdminDbHelper(context);
        sqLiteDatabase = localAdminDbHelper.getReadableDatabase();
    }

    public void closeDB(){
        sqLiteDatabase.close();
    }

    public void insert(LocalAdmin user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalAdminDbHelper._ID, user.getId());
        contentValues.put(LocalAdminDbHelper.USERNAME, user.getUsername());
        contentValues.put(LocalAdminDbHelper.URL, user.getUrlFotoProfilo());
        sqLiteDatabase.insert(LocalAdminDbHelper.TABLE_NAME, null, contentValues);
    }

    public LocalAdmin fetchDataUser(){
        String[] proiezione = {
                LocalAdminDbHelper._ID,
                LocalAdminDbHelper.USERNAME,
                LocalAdminDbHelper.URL,
        };

        Cursor cursor = sqLiteDatabase.query(LocalAdminDbHelper.TABLE_NAME, proiezione, null,
                null, null, null,
                null, null );
        LocalAdmin localAdmin = new LocalAdmin();
        while(cursor.moveToNext()){
            localAdmin.setId(cursor.getString(cursor.getColumnIndexOrThrow(LocalAdminDbHelper._ID)));
            localAdmin.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(LocalAdminDbHelper.USERNAME)));
            localAdmin.setUrlFotoProfilo(cursor.getString(cursor.getColumnIndexOrThrow(LocalAdminDbHelper.URL)));
        }
        cursor.close(); // This `Cursor` should be freed up after use with `#close()`
        return localAdmin;
    }

    public void delete(long id){
        sqLiteDatabase.delete(LocalAdminDbHelper.TABLE_NAME, LocalAdminDbHelper._ID + "=" + id, null);
    }

    public boolean isEmpty(){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + LocalAdminDbHelper.TABLE_NAME, null);
        if((cursor != null) && (cursor.getCount() > 0 )){
            cursor.close();
            return false;
        }
        return true;
    }
}