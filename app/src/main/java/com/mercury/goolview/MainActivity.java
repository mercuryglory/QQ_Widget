package com.mercury.goolview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mercury.goolview.ui.GooView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GooView(this));
    }
}
