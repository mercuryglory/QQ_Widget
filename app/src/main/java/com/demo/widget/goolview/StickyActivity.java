package com.demo.widget.goolview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.demo.widget.goolview.ui.StickyView;

public class StickyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new StickyView(this));
    }
}
