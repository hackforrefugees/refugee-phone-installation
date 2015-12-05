package com.refugeephones.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.refugeephones.app.NewsItem;
import com.refugeephones.app.NewsItemAdapter;
import com.refugeephones.app.R;

import java.util.ArrayList;

public class NewsFragment extends BaseFragment {
    private ListView m_newsListView;
    private String[] m_newsItemsMoc = new String[] {"Apple", "Banana", "Citrus"};

    private ArrayList<NewsItem> mNewsItems = new ArrayList<NewsItem>();

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        for (String str:m_newsItemsMoc) {
            NewsItem item = new NewsItem(str, "subtitle", "snippet");
            mNewsItems.add(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_newsListView = (ListView)view.findViewById(R.id.newsListView);
        NewsItemAdapter newsItemAdapter = new NewsItemAdapter(getActivity());
        newsItemAdapter.updateItems(mNewsItems);
        m_newsListView.setAdapter(newsItemAdapter);
    }


}
