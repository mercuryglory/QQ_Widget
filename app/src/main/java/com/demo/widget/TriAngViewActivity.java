package com.demo.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wang.zhonghao on 2017/6/21
 * descript:  入口
 */
public class TriAngViewActivity extends AppCompatActivity {

    @Bind(R.id.rv)
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangel);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }


        rv.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(TriAngViewActivity.this).inflate(R.layout
                        .item_list_triview, parent, false);
                return new MyViewHolder(view);

            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
