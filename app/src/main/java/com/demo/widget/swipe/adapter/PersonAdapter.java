package com.demo.widget.swipe.adapter;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.widget.R;
import com.demo.widget.goolview.ui.PlaceView;
import com.demo.widget.goolview.ui.StickyView;
import com.demo.widget.swipe.ui.MyListView;
import com.demo.widget.swipe.ui.SwipeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Mercury on 2016/8/12.
 * 演示qq效果的适配器
 */
public class PersonAdapter extends BaseAdapter {

    List<String>     data      = new ArrayList<>();
    HashSet<String> removePos = new HashSet<>();

    public void setData(List<String> list) {
        data.clear();
        data.addAll(list);
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_list_swipe, null);
            holder.mTextView = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mPlaceView = (PlaceView) convertView.findViewById(R.id.gootext);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyListView listView = (MyListView) parent;
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (openedItems.size() > 0) {
                    Log.e("releaseListview", "我要这个事件");
                    closeAllItems();
                    listView.requestDisallowIntercept(false);
                }
                return false;
            }
        });

        Log.e("adapter", position + "---" + removePos.size());

        final String text = data.get(position);
        holder.mTextView.setText(text);
        holder.mPlaceView.setText(String.valueOf(position));

        final SwipeLayout sl = (SwipeLayout) convertView;
        sl.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.e("listener", "close");
                openedItems.remove(layout);
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.e("listener", "open");
                openedItems.add(layout);
                listView.requestDisallowIntercept(true);

            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.e("listener", "startopen");
                closeAllItems();
            }
        });

        TextView tvAtop = (TextView) convertView.findViewById(R.id.tv_atop);
        TextView tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
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

        convertView.measure(0, 0);
        int height = convertView.getMeasuredHeight();

        PlaceView placeView = holder.mPlaceView;
        //创建粘性控件,并将listview的条目高度值传递给它
        StickyView view = placeView.createView(parent.getContext(), height);
        view.setOnDisappearListener(new StickyView.OnDisappearListener() {
            @Override
            public void onDisappear() {
                removePos.add(text);
            }
        });
        boolean isVisible = !removePos.contains(text);
        if (isVisible) {
            holder.mPlaceView.setVisibility(View.VISIBLE);
            holder.mPlaceView.setStatus(PlaceView.Status.NORMAL);
        } else {
            holder.mPlaceView.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView  mTextView;
        PlaceView mPlaceView;
    }

    HashSet<SwipeLayout> openedItems = new HashSet<>();
    SwipeLayout.OnSwipeListener onSwipeListener = new SwipeLayout.OnSwipeListener() {
        @Override
        public void onClose(SwipeLayout layout) {
            Log.e("listener", "close");
            openedItems.remove(layout);
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            Log.e("listener", "open");
            openedItems.add(layout);
        }

        @Override
        public void onStartOpen(SwipeLayout layout) {
            Log.e("listener", "startopen");
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
