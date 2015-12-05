package com.refugeephones.app;

/**
 * Created by magnus on 05/12/15.
 */
public class ResourceItem {

    private long mId;
    private ResourceType mType;
    private String mName;
    private String mLink;
    private String mDescription;
    private String mLanguage; // enum?
    private String mIcon;

    public enum ResourceType
    {
        FACEBOOK,
        URL,
        YOUTUBE,
        APP
    }

    public ResourceItem(long id, String name, String link, String description, String language, ResourceType type, String icon) {
        this.mId = id;
        this.mName = name;
        this.mLink = link;
        this.mLanguage = language;
        this.mDescription = description;
        this.mType = type;
        this.mIcon = icon;
    }



    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public ResourceType getType() {
        return mType;
    }

    public void setType(ResourceType type) {
        this.mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = mName;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        this.mLanguage = language;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

}
