package com.refugeephones.app.fragment;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.refugeephones.app.NewsItem;
import com.refugeephones.app.NewsRecyclerViewAdapter;
import com.refugeephones.app.R;
import com.refugeephones.app.utils.AppLog;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class NewsFragment extends BaseFragment {

    private final String TAG = "NewsFragment";

    private String FEED_URL = "http://www.refugeephones.com/news?format=rss";
    private ArrayList<NewsItem> mNewsItems = new ArrayList<NewsItem>();
    private RecyclerView mNewsRecyclerView;
    private ImageView mSadFaceImageView;

    public NewsFragment() {
        // Required empty public constructor
    }

    public void hideSadFace() {
        mSadFaceImageView.setVisibility(ImageView.INVISIBLE);
    }
    public void showSadFace() {
        mSadFaceImageView.setVisibility(ImageView.VISIBLE);
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

        mNewsRecyclerView = (RecyclerView)view.findViewById(R.id.newsRecyclerView);
        mNewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mNewsRecyclerView.setLayoutManager(llm);

        mSadFaceImageView = (ImageView)view.findViewById(R.id.noContentImageView);

        NewsRecyclerViewAdapter nrvAdapter = new NewsRecyclerViewAdapter(mNewsItems);
        mNewsRecyclerView.setAdapter(nrvAdapter);
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

        public String getOgImage(String url) {
            String imageUrl = "";
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                //Elements meta = doc.select("meta");
                for(org.jsoup.nodes.Element meta : doc.select("meta")) {
                    if (meta.attr("property").equalsIgnoreCase("og:image")) {
                        imageUrl = meta.attr("content");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imageUrl;
        }

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
                    String cleanDescription = description.replaceAll("\\n", "");
                    newsItem.setSnippet(cleanDescription);
                    newsItem.setLink(link);
                    newsItem.setImage(getOgImage(link));

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
            mNewsItems = newsItems;

            if (newsItems.size() > 0)
                hideSadFace();
            else
                showSadFace();

            ((NewsRecyclerViewAdapter)mNewsRecyclerView.getAdapter()).updateItems(newsItems);
        }
    }
}
