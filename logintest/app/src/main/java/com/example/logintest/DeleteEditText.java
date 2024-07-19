package com.example.logintest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class DeleteEditText extends EditText {


    public DeleteEditText(Context context) {
        super(context);
        init();
    }

    private void init() {

    }

    public DeleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DeleteEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
