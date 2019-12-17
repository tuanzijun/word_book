package com.example.word_book;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.word_book.translate.*;
import com.google.gson.Gson;
import java.util.List;

public class baidu extends AppCompatActivity {
    private TextView tvword,tvvalues;
    private Button bt_src;
    private EditText etword;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youdao);
        initView();

    }

    private void initView(){
        etword = (EditText)findViewById(R.id.src_word);
        tvvalues = (TextView)findViewById(R.id.y_values);
        bt_src = (Button)findViewById(R.id.bt_src);
        bt_src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String query = etword.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String resultJson = new TransApi().getTransResult(query,"auto","en");
                        //拿到结果，对结果进行解析。
                        Gson gson = new Gson();
                        TranslateResult translateResult = gson.fromJson(resultJson, TranslateResult.class);
                        final List<TranslateResult.TransResultBean> trans_result = translateResult.getTrans_result();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                String dst = "";
                                for (TranslateResult.TransResultBean s : trans_result
                                ) {
                                    dst = dst + "\n" + s.getDst();
                                }
                                tvvalues.setText(dst);
                            }
                        });

                    }
                }).start();
            }
        });
    }



}
