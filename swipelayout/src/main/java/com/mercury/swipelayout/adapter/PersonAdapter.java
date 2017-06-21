package com.mercury.swipelayout.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mercury.swipelayout.R;
import com.mercury.swipelayout.ui.SwipeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Mercury on 2016/8/12.
 */
public class PersonAdapter extends BaseAdapter {

    List<String> data = new ArrayList<>();

    public void setData(List<String> list) {
        data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(parent.getContext(), R.layout.item_list_swipe, null);
        } else {
            view = convertView;
        }

        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        final String text = data.get(position);
        tv_name.setText(text);

        SwipeLayout sl = (SwipeLayout) view;
        sl.setOnSwipeListener(onSwipeListener);

        TextView tvAtop = (TextView) view.findViewById(R.id.tv_atop);
        TextView tvDelete = (TextView) view.findViewById(R.id.tv_delete);
        tvAtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(text);
                data.add(0, text);
                notifyDataSetChanged();
                closeAllItems();
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(text);
                notifyDataSetChanged();
                closeAllItems();
            }
        });
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
