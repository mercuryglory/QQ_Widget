package com.demo.widget.goolview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.widget.R;
import com.demo.widget.goolview.ui.PlaceView;
import com.demo.widget.goolview.ui.StickyView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StickyActivity extends AppCompatActivity {

    ListView mListView;
    private List<String> dataList;
    MyAdapter mAdapter;


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


    }

    public void restore(View view) {
        //        mStickyView.backToLayout();
    }

    public class MyAdapter extends BaseAdapter {

        private HashSet<Integer> removePos = new HashSet<>();

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
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.item_list, null);
                holder.mTextView = (TextView) convertView.findViewById(R.id.tv_number);
                holder.mPlaceView = (PlaceView) convertView.findViewById(R.id.gootext);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Log.e("adapter", position + "---" + removePos.size());

            convertView.measure(0, 0);
            int height = convertView.getMeasuredHeight();


            PlaceView placeView = holder.mPlaceView;
            //创建粘性控件,并将listview的条目高度值传递给它
            StickyView view = placeView.createView(StickyActivity.this, height);
            view.setOnDisappearListener(new StickyView.OnDisappearListener() {
                @Override
                public void onDisappear() {
                    removePos.add(position);
                }
            });
            boolean isVisible = !removePos.contains(position);
            if (isVisible) {
                holder.mPlaceView.setVisibility(View.VISIBLE);
            } else {
                holder.mPlaceView.setVisibility(View.GONE);
            }
            return convertView;
        }


        class ViewHolder {
            TextView  mTextView;
            PlaceView mPlaceView;
        }
    }

}
