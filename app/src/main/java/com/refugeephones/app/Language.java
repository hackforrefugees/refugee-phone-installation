package com.refugeephones.app;

import com.google.gson.annotations.SerializedName;

/**
 * Data container for Language
 * Created by magnus on 05/12/15.
 */
public class Language {
    @SerializedName("code")
    private String mCode;
    @SerializedName("name")
    private String mName;
    @SerializedName("url")
    private String mUrl;
    public String getName() {
        return mName;
    }
    public String getCode() { return mCode; }
    public String getUrl() { return mUrl; }
}
