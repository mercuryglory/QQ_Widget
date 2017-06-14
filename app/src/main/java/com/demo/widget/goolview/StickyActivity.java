package com.demo.widget.goolview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.widget.R;
import com.demo.widget.goolview.ui.StickyTestView;

import java.util.List;

public class StickyActivity extends AppCompatActivity {

    StickyTestView mStickyView;
    ListView       mListView;
    private List<String> dataList;
    MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
//        mListView = (ListView) findViewById(R.id.lv);
//        dataList = new ArrayList<>();
//        mAdapter = new MyAdapter();
//        for (int i = 0; i < 10; i++) {
//            dataList.add("我是第" + i + "个条目");
//        }
//        mListView.setAdapter(mAdapter);

        mStickyView = (StickyTestView) findViewById(R.id.sticky);
//        mStickyView.setTextNumber("12");

    }

    public void restore(View view) {
        mStickyView.backToLayout();
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_list, null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView.findViewById(R.id.tv_number);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String text = dataList.get(position);
            holder.mTextView.setText(text);
            return convertView;
        }


        class ViewHolder {
            TextView mTextView;
        }
    }


}
