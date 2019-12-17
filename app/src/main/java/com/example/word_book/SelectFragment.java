package com.example.word_book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.word_book.R.*;

public class SelectFragment extends Fragment {
    static final String TAG = "SelectFragment";
    private ArrayList<String> wordlist;
    private ListView listView;
    private Cursor cursor;

    public SelectFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(layout.select,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<WordBean> names = null;
        names = ((MainActivity)getActivity()).getlist();
        final String[] words = new String[names.size()];
        final String[] means = new String[names.size()];
        for(int i =0;i < names.size();i++) {
            words[i] = names.get(i).getWord();
            means[i] = names.get(i).getMeaning();
            Log.d(TAG,words[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_activated_1,words);
        listView = (ListView)getView().findViewById(id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        final List<WordBean> finalNames = names;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ResultFragment fragment = (ResultFragment)fm.findFragmentById(id.fregment_result);
                fragment.setText(means[i]);
            }
        });
    }
}
