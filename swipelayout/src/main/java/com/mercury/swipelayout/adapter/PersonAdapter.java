package com.mercury.swipelayout.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mercury.swipelayout.Cheeses;
import com.mercury.swipelayout.R;
import com.mercury.swipelayout.ui.SwipeLayout;

import java.util.HashSet;

/**
 * Created by Mercury on 2016/8/12.
 */
public class PersonAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return Cheeses.NAMES.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(parent.getContext(), R.layout.item_list_swipe, null);
        } else {
            view = convertView;
        }

        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setText(Cheeses.NAMES[position]);

        SwipeLayout sl = (SwipeLayout) view;
        sl.setOnSwipeListener(onSwipeListener);

        return view;
    }

    HashSet<SwipeLayout> openedItems = new HashSet<>();
    SwipeLayout.OnSwipeListener onSwipeListener = new SwipeLayout.OnSwipeListener() {
        @Override
        public void onClose(SwipeLayout layout) {
            System.out.println("onClose");
            openedItems.remove(layout);
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            System.out.println("onOpen");
            openedItems.add(layout);
        }

        @Override
        public void onStartOpen(SwipeLayout layout) {
            System.out.println("onStartOpen");
            closeAllItems();
        }
    };

    public void closeAllItems() {
        for (SwipeLayout openedItem : openedItems) {
            openedItem.close();
        }
        openedItems.clear();
    }


}
