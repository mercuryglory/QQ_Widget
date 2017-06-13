package com.demo.widget.goolview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.widget.R;
import com.demo.widget.goolview.ui.StickyView;

public class StickyActivity extends AppCompatActivity {

    StickyView mStickyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
        mStickyView = (StickyView) findViewById(R.id.sticky);
//        mStickyView.setTextNumber("12");
    }

    public void restore(View view) {
        mStickyView.backToLayout();
    }
}
