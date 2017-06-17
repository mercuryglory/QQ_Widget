package com.demo.widget.goolview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.widget.R;
import com.demo.widget.goolview.ui.StickyTestView;
import com.demo.widget.goolview.ui.TextGooView;

import java.util.ArrayList;
import java.util.List;

public class StickyActivity extends AppCompatActivity {

//    StickyTestView mStickyView;
    ListView       mListView;
    private List<String> dataList;
    MyAdapter mAdapter;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
        mListView = (ListView) findViewById(R.id.lv);
        dataList = new ArrayList<>();
        mAdapter = new MyAdapter();
        for (int i = 0; i < 10; i++) {
            dataList.add("我是第" + i + "个条目");
        }
        mListView.setAdapter(mAdapter);

//        mStickyView = (StickyTestView) findViewById(R.id.sticky);
//        mStickyView.setTextNumber("12");

    }

    public void restore(View view) {
//        mStickyView.backToLayout();
    }

    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            convertView = View.inflate(parent.getContext(), R.layout.item_list, null);
            holder = new ViewHolder();
            holder.mTextGooView = (TextGooView) convertView.findViewById(R.id.gootext);
//            if (convertView == null) {
//                convertView = View.inflate(parent.getContext(), R.layout.item_list, null);
//                holder = new ViewHolder();
//                holder.mTextView = (TextView) convertView.findViewById(R.id.tv_number);
//                holder.mTextGooView = (TextGooView) convertView.findViewById(R.id.gootext);
//                convertView.setTag(holder);
//
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 100);

            final TextGooView gooView = holder.mTextGooView;
            final StickyTestView view = gooView.createView(StickyActivity.this);
//            gooView.setMeasureListener(new TextGooView.MeasureListener() {
//                @Override
//                public void create(float centerX, float centerY) {
//                    view.setLayout(gooView);
//                }
//            });

            String text = dataList.get(position);
//            holder.mTextView.setText(text);

            return convertView;
        }


        class ViewHolder {
            TextView mTextView;
            TextGooView mTextGooView;
        }
    }

}
