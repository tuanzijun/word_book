package com.example.word_book;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MyDialog extends Dialog {
    private EditText word,mean;
    private Button confirm,cancel;
    private Context mcontext;
    private OnNoClickListener onNoClickListener;
    private OnYesClickListener yesClickListener;

    public MyDialog(Context context) {
        super(context);
        mcontext = context;
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mcontext = context;
    }

    protected MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_my);
        init();
        setSize();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }

    //初始化控件
    private void init(){
        word = (EditText)findViewById(R.id.word_name);
        mean = (EditText)findViewById(R.id.meaning);
        confirm = (Button)findViewById(R.id.confirm);
        cancel = (Button)findViewById(R.id.cancel);
    }

    //设置对话框大小
    private void setSize(){
        Window window = getWindow();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        int y = display.getHeight();
        int x = display.getWidth();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width=(x*3)/4;
        layoutParams.height=2*y/3;
        window.setAttributes(layoutParams);
    }

    public interface OnNoClickListener{
        public void cancel();
    }

    public interface OnYesClickListener{
        public void confirm();
    }

    public String getWord(){
        return word.getText().toString();
    }

    public String getMean(){
        return mean.getText().toString();
    }
}
