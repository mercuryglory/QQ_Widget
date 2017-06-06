package com.mercury.goolview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mercury.goolview.ui.GooView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GooView(this));
    }
}
