package com.demo.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;

import com.demo.widget.swipe.Cheeses;
import com.demo.widget.swipe.adapter.PersonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MultipleActivity extends AppCompatActivity {

    @Bind(R.id.lv)
    ListView mLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple);
        ButterKnife.bind(this);

        final PersonAdapter adapter = new PersonAdapter();
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < Cheeses.NAMES.length; i++) {
            dataList.add(Cheeses.NAMES[i]);
        }
        adapter.setData(dataList);
        mLv.setAdapter(adapter);

        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    adapter.closeAllItems();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }
}
