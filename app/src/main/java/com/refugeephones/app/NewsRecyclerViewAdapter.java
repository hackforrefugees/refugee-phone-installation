package com.refugeephones.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by magnus on 06/12/15.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private ArrayList<NewsItem> mItems;

    public NewsRecyclerViewAdapter(ArrayList<NewsItem> newsItems) {
        this.mItems = newsItems;
    }

    public ArrayList<NewsItem> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<NewsItem> items) {
        this.mItems = items;
    }

    public void updateItems(ArrayList<NewsItem> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_view, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ((RecyclerView)v.getParent()).getChildAdapterPosition(v);
                NewsItem clickedNewsItem = getItems().get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedNewsItem.getLink()));
                v.getContext().startActivity(intent);
            }
        });
        NewsViewHolder nvh = new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.title.setText(mItems.get(position).getTitle());
        holder.description.setText(mItems.get(position).getSnippet());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView description;
        String link;

        NewsViewHolder(View itemView) {
            super(itemView);

            cv = (CardView)itemView.findViewById(R.id.card_view);
            title = (TextView)itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);
        }
    }
}
