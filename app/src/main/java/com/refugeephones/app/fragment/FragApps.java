package com.refugeephones.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.refugeephones.app.NewsItem;
import com.refugeephones.app.NewsItemAdapter;
import com.refugeephones.app.R;
import com.refugeephones.app.ResourceItem;
import com.refugeephones.app.ResourceItemAdapter;

import java.util.ArrayList;

/**
 * Fragment to show apps list
 * Created by Waqas on 005 05-Dec-15.
 */
public class FragApps extends BaseFragment {
    private final String TAG = "FragApps";

    private ListView m_resourceListView;
    private String[] m_mockupNames = new String[]
            {
                    "I love Sweden",
                    "1177 Vårdguiden",
                    "Google translate",
                    "Jag kan svenska",
                    "Migrationsverket.se"
            };
    private String[] m_mockupDescriptions = new String[]
            {
                    "Discuss Sweden",
                    "Healthcare in Arabic.",
                    "App to translate between a lot of languages. Download languages in the app to have it available offline.",
                    "Learn Swedish from Arabic with these easy to follow youtube lessons from Rateb.",
                    "The site of the Swedish Immigration Office in Arabic."
            };
    private String[] m_mockupUrls = new String[]
            {
                    "https://www.facebook.com/yem.swed",
                    "http://www.1177.se/Orebrolan/Other-languages/Arabiska/",
                    "com.google.android.apps.translate",
                    "https://www.youtube.com/channel/UCIVfQ_O1w8t4sK5FTrdbnNA",
                    "http://www.migrationsverket.se/Other-languages/-alrbyt-.html"
            };
    private ResourceItem.ResourceType[] m_mockupTypes = new ResourceItem.ResourceType[]
            {
                    ResourceItem.ResourceType.FACEBOOK,
                    ResourceItem.ResourceType.URL,
                    ResourceItem.ResourceType.APP,
                    ResourceItem.ResourceType.YOUTUBE,
                    ResourceItem.ResourceType.URL
            };

    private ArrayList<ResourceItem> mResourceItems = new ArrayList<ResourceItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTag(TAG);
        super.onCreate(savedInstanceState);

        String language = "ar";
        String icon = "";
        for (int i = 0; i< m_mockupNames.length; i++) {
            ResourceItem item = new ResourceItem(i, m_mockupNames[i], m_mockupUrls[i], m_mockupDescriptions[i], language, m_mockupTypes[i], icon);
            mResourceItems.add(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_apps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_resourceListView = (ListView)view.findViewById(R.id.resourceListView);
        ResourceItemAdapter resourceItemAdapter = new ResourceItemAdapter(getActivity());
        resourceItemAdapter.updateItems(mResourceItems);
        m_resourceListView.setAdapter(resourceItemAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
