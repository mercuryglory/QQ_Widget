package com.mercury.parallex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mercury.parallex.ui.ParallaxListView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.plv)
    ParallaxListView mPlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //添加一个头布局
        View headView = View.inflate(this, R.layout.header_person, null);
        final ImageView iv_image = (ImageView) headView.findViewById(R.id.iv_image);

        mPlv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //视图树渲染完毕,被执行.设置图片,移除监听
                mPlv.setParallaxImage(iv_image);
                mPlv.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });
//        mPlv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                mPlv.setParallaxImage(iv_image);
//            }
//        }, 50);
        mPlv.addHeaderView(headView);
        mPlv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Cheeses.NAMES));
    }
}
