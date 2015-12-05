package com.refugeephones.app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.refugeephones.app.NewsItem;
import com.refugeephones.app.NewsItemAdapter;
import com.refugeephones.app.R;
import com.refugeephones.app.utils.AppLog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class NewsFragment extends BaseFragment {

    private final String TAG = "com.refugeephones.app.fragment.NewsFragment";

    private ListView m_newsListView;
    private String FEED_URL = "http://www.gp.se/1.16942#sthash.B1QSY5si.dpuf";

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        updateNews();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        m_newsListView.setAdapter(newsItemAdapter);

        m_newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem clickedNewsItem = ((NewsItemAdapter)m_newsListView.getAdapter()).getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedNewsItem.getLink()));
                startActivity(intent);
            }
        });
    }

    public void updateNews() {
        URL url;
        try {
            url = new URL(FEED_URL);
            new RetrieveNewsTask().execute(url); // Update news feed
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    class RetrieveNewsTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {

        public InputStream getInputStream(URL url) {
            try {
                return url.openConnection().getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @SuppressLint("LongLogTag")
        @Override
        protected ArrayList<NewsItem> doInBackground(URL... urls) {
            URL url = urls[0];

            ArrayList<NewsItem> items = new ArrayList<NewsItem>();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));

                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("item");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    NewsItem newsItem = new NewsItem();
                    String title = "";
                    String description = "";
                    String link = "";

                    Node node = nodeList.item(i);
                    Element e = (Element)node;
                    NodeList childNodes = e.getChildNodes();

                    NodeList titleList = e.getElementsByTagName("title");
                    NodeList descriptionList = e.getElementsByTagName("description");
                    NodeList linkList = e.getElementsByTagName("link");

                    if (titleList.getLength() > 0)
                        title = titleList.item(0).getChildNodes().item(0).getNodeValue();
                    if (descriptionList.getLength() > 0)
                        description = descriptionList.item(0).getChildNodes().item(0).getNodeValue();
                    if (linkList.getLength() > 0)
                        link = linkList.item(0).getChildNodes().item(0).getNodeValue();

                    newsItem.setTitle(title);
                    newsItem.setSnippet(description);
                    newsItem.setLink(link);

                    items.add(newsItem);
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return items; // should return the feteched data
        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> newsItems) {
            AppLog.debug(TAG, "post execute");

            ((NewsItemAdapter)m_newsListView.getAdapter()).updateItems(newsItems);
        }
    }
}
