package com.refugeephones.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by magnus on 05/12/15.
 */

public class NewsItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NewsItem> items = new ArrayList<NewsItem>();

    public NewsItemAdapter(Context context) {
        this.mContext = context;
    }

    public void updateItems(ArrayList<NewsItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public NewsItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position; //hack
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_news_row, parent, false);
        }

        TextView title = (TextView)convertView.findViewById(R.id.newsRowTitle);
        TextView subtitle = (TextView)convertView.findViewById(R.id.newsRowSubtitle);

        NewsItem item = getItem(position);
        title.setText(item.getTitle());
        subtitle.setText(item.getSubtitle());

        return convertView;
    }
}
