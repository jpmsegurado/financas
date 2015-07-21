package com.example.joaopedro.minhasfinancas.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

public class Provider extends ContentProvider {
    public Provider() {
    }



    public static final String PROVIDER_NAME = "br.com.joaopedrosegurado.financa";
    public static final String URL = "content://"+PROVIDER_NAME+"/";
    public static final Uri CONTENT_URL = Uri.parse(URL);


    public static final int despesa = 1;
    public static final int receita = 2;

    private DbHelper helper;
    private SQLiteDatabase readDb,writeDb;


    public static final UriMatcher matcher;
    static{
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PROVIDER_NAME,Contract.despesa,despesa);
        matcher.addURI(PROVIDER_NAME,Contract.receita,receita);
    }

    @Override
    public boolean onCreate() {

        helper  = new DbHelper(getContext());
        writeDb = helper.getWritableDatabase();
        readDb = helper.getReadableDatabase();
        return true;
    }

    /*
    () = não precisa
    selection = (where) x = y
    sortOrder = (order by) x ASC, y DESC etc
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        String project = "";
        if(projection != null){
            for(int i=0; i<projection.length; i++){
                if(i == projection.length - 1){
                    project = project+projection[i];
                }else if(i == 0 ){
                    project = projection[i];
                }else{
                    project = project+","+projection[i]+",";
                }
            }
        }else{
            project = "*";
        }

        switch(matcher.match(uri)){
            case despesa:
                if(selection == null && sortOrder == null){
                    cursor = readDb.rawQuery("SELECT "+project+" FROM "+Contract.despesa,selectionArgs);
                }else if(selection == null && sortOrder != null){
                    cursor = readDb.rawQuery("SELECT "+project+" FROM "+Contract.despesa+" "+sortOrder,selectionArgs);
                }else if(selection != null && sortOrder == null){
                    cursor = readDb.rawQuery("SELECT "+project+" FROM "+Contract.despesa+" WHERE "+selection,selectionArgs);
                }else{
                    cursor = readDb.rawQuery("SELECT "+project+" FROM "+Contract.despesa+" WHERE "+selection+" "+sortOrder,selectionArgs);
                }
                break;
            case receita:
                if(selection == null && sortOrder == null){
                    cursor = readDb.rawQuery("SELECT "+project+" FROM "+Contract.receita,selectionArgs);
                }else if(selection == null && sortOrder != null){
                    cursor = readDb.rawQuery("SELECT "+project+" FROM "+Contract.receita+" "+sortOrder,selectionArgs);
                }else if(selection != null && sortOrder == null){
                    cursor = readDb.rawQuery("SELECT "+project+" FROM "+Contract.receita+" WHERE "+selection,selectionArgs);
                }else{
                    cursor = readDb.rawQuery("SELECT "+project+" FROM "+Contract.receita+" WHERE "+selection+" "+sortOrder,selectionArgs);
                }
                break;

        }

        Log.d("len",cursor.getCount()+"");
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        Log.d("uri matcher result", "" + matcher.match(uri));
        long id = 0;
        switch(matcher.match(uri)){
            case despesa:
                id = writeDb.insert(Contract.despesa,null,values);
                if(id > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URL, id);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case receita:
                id = writeDb.insert(Contract.receita,null,values);
                if(id > 0){
                    _uri = ContentUris.withAppendedId(CONTENT_URL, id);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
        }

        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rows = 0;

        switch(matcher.match(uri)){
            case receita:
                rows = writeDb.delete(Contract.receita,selection,selectionArgs);
                break;
            case despesa:
                rows = writeDb.delete(Contract.despesa,selection,selectionArgs);
                break;
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rows = 0;

        switch(matcher.match(uri)){
            case despesa:
                rows = writeDb.update(Contract.despesa,values,selection,selectionArgs);
                break;
            case receita:
                rows = writeDb.update(Contract.receita,values,selection,selectionArgs);
                break;
        }

        return rows;
    }


}
