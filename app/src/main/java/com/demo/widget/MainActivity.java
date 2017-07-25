package com.demo.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by wang.zhonghao on 2017/6/21
 * descript:  入口
 */
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

    public void test(View view) {
        Toast.makeText(this,"haha",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TriAngViewActivity.class));
    }
}
