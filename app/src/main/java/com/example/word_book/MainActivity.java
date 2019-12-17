package com.example.word_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static int POSTION;
    private static String TAG="MainActivity";
    private static int FLAG = 1;
    private database db;
    private SQLiteDatabase sqLiteDatabase;
    private ListAdapter listAdapter;
    private ListView listView;
    private Cursor cursor;

    private EditText etword,etmean;
    private Button modify,add,query;

    private List<WordBean> wordBeanList = new ArrayList<>();
    private List<WordBean> wordqList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);


        db = new database(MainActivity.this,"wordbook",null,1);
        sqLiteDatabase = db.getWritableDatabase();  //调动database.onCreate()函数
        Log.d(TAG,db.toString());

//        扫描数据库，将数据信息放入list
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM wordlist",null);

        while (cursor.moveToNext()){
            String ids = cursor.getString(cursor.getColumnIndex("id"));
            String words = cursor.getString(cursor.getColumnIndex("word"));
            String means = cursor.getString(cursor.getColumnIndex("mean"));
            WordBean w = new WordBean();
            w.setId(ids);
            w.setWord(words);
            w.setMeaning(means);
            wordBeanList.add(w);
        }
        setContentView(R.layout.activity_main);
        Configuration configuration = this.getResources().getConfiguration();
        int ori = configuration.orientation;
//      如果是竖屏的话
        if(ori == configuration.ORIENTATION_PORTRAIT) {

            etword = (EditText) findViewById(R.id.etWord);
            etmean = (EditText) findViewById(R.id.etMean);
            add = (Button) findViewById(R.id.bt_add);
            modify = (Button) findViewById(R.id.bt_modify);
            query = (Button) findViewById(R.id.bt_query);
            listView = (ListView) findViewById(R.id.list_view);
//
            listAdapter = new ListAdapter();
            listView.setAdapter(listAdapter);
            //点击时的事件
            listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    setSelectedValues(i);
                }

            });


//        插入数据ListView更新
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etword.getText().length() > 1 && etmean.getText().length() > 0) {

                        WordBean word = new WordBean();
                        word.setWord(etword.getText().toString());
                        word.setMeaning(etmean.getText().toString());
                        Log.d(TAG, word.toString());

                        ContentValues values = new ContentValues();
                        values.put("word", word.getWord());
                        values.put("mean", word.getMeaning());

                        Log.d(TAG, values.toString());

                        Long wordID = db.getReadableDatabase().insert("wordlist", null, values);

                        if (wordID > 0)
                            Toast.makeText(MainActivity.this, "scucces", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "faild", Toast.LENGTH_SHORT).show();
                        cursor = sqLiteDatabase.rawQuery("SELECT id FROM wordlist where word='" + word.getWord() + "'", null);
                        while (cursor.moveToNext()) {
                            String ids = cursor.getString(cursor.getColumnIndex("id"));
                            word.setId(ids);
                        }
                        wordBeanList.add(word);
                        listView.setAdapter(new MainActivity.ListAdapter());
                        resetFrom();
                    }
                }
            });

            //进行修改
            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wordBeanList.get(POSTION).setWord(etword.getText().toString());
                    wordBeanList.get(POSTION).setMeaning(etmean.getText().toString());

                    ContentValues values = new ContentValues();
                    values.put("word", etword.getText().toString());
                    values.put("mean", etmean.getText().toString());
                    sqLiteDatabase.update("wordlist", values, "id=" + wordBeanList.get(POSTION).getId(), null);
                    listView.setAdapter(new ListAdapter());
                    resetFrom();
                }
            });

            //查询按钮
            query.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (etword.getText().length() > 0) {
                        FLAG = 0;
                        wordqList.clear();
//                        cursor = sqLiteDatabase.rawQuery("SELECT * FROM wordlist where word like ? '" + etword.getText().toString() + "'", null);
                        String current_sql_sel = "SELECT  * FROM wordlist" +" where word like '%"+etword.getText().toString()+"%'";
                        cursor = sqLiteDatabase.rawQuery(current_sql_sel,null);
                        while (cursor.moveToNext()) {
                            String ids = cursor.getString(cursor.getColumnIndex("id"));
                            String words = cursor.getString(cursor.getColumnIndex("word"));
                            String means = cursor.getString(cursor.getColumnIndex("mean"));
                            WordBean wordBean = new WordBean(words, means, ids);
                            wordqList.add(wordBean);
                            listView.setAdapter(new QListAdapter());
                            resetFrom();
                        }
                    } else if (etword.getText().length() == 0) {
                        FLAG = 1;
                        listView.setAdapter(new ListAdapter());
                        resetFrom();
                    } else {
                        Toast.makeText(MainActivity.this, "仅支持单词查询！", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            });
        }
//        else if(ori == configuration.ORIENTATION_LANDSCAPE){
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        }
    }
//  重值form
    private void resetFrom(){
        etword.setText("");
        etmean.setText("");
    }

    @Override
    protected void onDestroy() {
//        sqLiteDatabase.delete("wordlist", null, null);
        super.onDestroy();
    }

    //设置Listview 的值
    private void setSelectedValues(int position){
        POSTION = position;

        etword.setText(wordBeanList.get(position).getWord());
        etmean.setText(wordBeanList.get(position).getMeaning());
    }

    public List<WordBean> getlist() {

        return wordBeanList;
    }


//    public String toString(){
////        return wordBeanList.get(1).getWord();
////    }

    private class ListAdapter extends BaseAdapter{
        public ListAdapter(){
            super();
        }
        @Override
        public int getCount() {
            return wordBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view;

            if(convertView == null){
                view = View.inflate(getBaseContext(),R.layout.word_item,null);
            }
            else
                view = convertView;
            //将word信息放入
            final WordBean wo = wordBeanList.get(i);
            TextView word_view = (TextView)view.findViewById(R.id.iword);
            TextView mean_view = (TextView)view.findViewById(R.id.imean);
            TextView remove = (TextView)view.findViewById(R.id.btremove);

            word_view.setText(wo.getWord());
            mean_view.setText(wo.getMeaning());

            remove.setText("删除");
            remove.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        sqLiteDatabase.delete("wordlist","id="+wo.getId(),null);
                        Toast.makeText(MainActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                        wordBeanList.remove(i);
                        listView.setAdapter(new ListAdapter());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


            return view;
        }
    }

    private class QListAdapter extends BaseAdapter{
        public QListAdapter(){
            super();
        }
        @Override
        public int getCount() {
            return wordqList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view;

            if(convertView == null){
                view = View.inflate(getBaseContext(),R.layout.word_item,null);
            }
            else
                view = convertView;
            //将word信息放入
            final WordBean wo = wordqList.get(i);
            TextView word_view = (TextView)view.findViewById(R.id.iword);
            TextView mean_view = (TextView)view.findViewById(R.id.imean);
            TextView remove = (TextView)view.findViewById(R.id.btremove);

            word_view.setText(wo.getWord());
            mean_view.setText(wo.getMeaning());

            remove.setText("删除");
            remove.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        sqLiteDatabase.delete("wordlist","id="+wo.getId(),null);
                        Toast.makeText(MainActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                        wordBeanList.remove(i);
                        listView.setAdapter(new ListAdapter());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


            return view;
        }


    }

    //菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.world_mean,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //通过对话框式添加单词 还未开发，以后有机会再说
            case  R.id.add:
                MyDialog dialog = new MyDialog(MainActivity.this);
                dialog.show();

            case R.id.sratch:
                Intent intent = new Intent(MainActivity.this, baidu.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
