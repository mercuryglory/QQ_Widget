package com.demo.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.widget.goolview.StickyActivity;
import com.demo.widget.parallex.ParallActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void parall(View view) {
        Intent intent = new Intent(this, ParallActivity.class);
        startActivity(intent);

    }

    public void stick(View view) {
        Intent intent = new Intent(this, StickyActivity.class);
        startActivity(intent);
    }

    public void condition(View view) {
        Intent intent = new Intent(this, MultipleActivity.class);
        startActivity(intent);
    }
}
