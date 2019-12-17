package com.example.word_book;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class MyContentProvider extends ContentProvider {
    private final static String TAG = "DatabaseProvider";

    private final static  String AUTOHORITY = "com.example.word_book.provider";
    private final static int WORD_URI_CODE = 0;
    private final static int WORD_ITE = 1;

    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private static UriMatcher sUriMarcher;
    static {
        sUriMarcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMarcher.addURI(AUTOHORITY,"wordlist",WORD_URI_CODE);
        sUriMarcher.addURI(AUTOHORITY,"wordlist/#",WORD_ITE);
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        sqLiteDatabase = new database(context,"wordbook.db",null,2).getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        int uriType = sUriMarcher.match(uri);
        Cursor cursor;
        switch (uriType){
            case WORD_URI_CODE:
                cursor = sqLiteDatabase.query(database.TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            case WORD_ITE:
                String wordId = uri.getPathSegments().get(1);
                String[] projection = new String[]{ wordId };
                cursor = sqLiteDatabase.query(database.TABLE_NAME,strings,"id=?",projection,null,null,s1);
                break;
            default:
                throw new IllegalArgumentException("UnSupport Uri : "+uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriType = sUriMarcher.match(uri);
        long row;
        Uri uriReturn = null;

        switch (uriType){
            case WORD_URI_CODE:
            case WORD_ITE:
                row = sqLiteDatabase.insert(database.TABLE_NAME,null,contentValues);
                uriReturn = Uri.parse("contrnt://"+AUTOHORITY+"/wordlist/"+row);
                break;
            default:
                throw new IllegalArgumentException("UnSupport Uri : " + uri);
        }

        if(row > -1){
            context.getContentResolver().notifyChange(uri,null);
            return ContentUris.withAppendedId(uri,row);
        }

        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int uriType = sUriMarcher.match(uri);
        int rowDelet=0;

        switch (uriType){
            case WORD_URI_CODE:
                rowDelet = sqLiteDatabase.delete(database.TABLE_NAME,s,strings);
                break;
            case WORD_ITE:
                String wordId = uri.getPathSegments().get(1);
                rowDelet = sqLiteDatabase.delete(database.TABLE_NAME,"id=?",new String[]{wordId});
                break;
            default:
                break;
        }

        return rowDelet;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int uriType = sUriMarcher.match(uri);
        int rowUpdate=0 ;

        switch (uriType){
            case WORD_URI_CODE:
                rowUpdate = sqLiteDatabase.update(database.TABLE_NAME,contentValues,s,strings);
                break;
            case WORD_ITE:
                String wordId = uri.getPathSegments().get(1);
                rowUpdate = sqLiteDatabase.update(database.TABLE_NAME,contentValues,"id=?",new String[]{wordId});
                break;
            default:
                break;
        }

        return rowUpdate;
    }
}
