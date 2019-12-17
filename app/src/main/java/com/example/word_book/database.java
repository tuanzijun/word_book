package com.example.word_book;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class database extends SQLiteOpenHelper {

    private Context context;

    public final static String DATA_NAME = "wordbook";
    public final static String TABLE_NAME = "wordlist";
    public final static int DATA_VERSION = 1;

    private static final String CREATE_TABLES="create table wordlist(id integer primary key autoincrement,word,mean)";

    public database(Context context,String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context,name, null, version);
        this.context = context;
    }

    public database(Context mcontext){
        super(mcontext,DATA_NAME, null, DATA_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLES);
//        sqLiteDatabase.execSQL("CREATE TABLE "+TB_NAME+"("
//                                +"_id INTEGER PRIMARY KEY AUTOINCREMENT,word,mean)");
        Toast.makeText(context,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL("drop table if exists wordlist");
        onCreate(sqLiteDatabase);
    }

}
