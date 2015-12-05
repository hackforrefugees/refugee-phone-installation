package com.refugeephones.app;

import java.net.URL;

/**
 * Created by magnus on 05/12/15.
 */
public class NewsItem {

    private long mId;
    private String mtitle;
    private String mSubtitle;
    private String mSnippet;
    private String mLink;

    public NewsItem() {

    }

    public long getId() {
        return mId;
    }


    public void setId(long id) {
        this.mId = id;
    }

    public String getTitle() {
        return mtitle;
    }

    public void setTitle(String title) {
        this.mtitle = title;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String subtitle) {
        this.mSubtitle = subtitle;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public void setSnippet(String snippet) {
        this.mSnippet = snippet;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }
}
