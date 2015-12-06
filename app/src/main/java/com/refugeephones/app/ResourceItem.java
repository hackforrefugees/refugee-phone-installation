package com.refugeephones.app;

import com.google.gson.annotations.SerializedName;

/**
 * Data container for ResourceItem
 * Created by magnus on 05/12/15.
 */
public class ResourceItem {
    @SerializedName("id")
    private long mId;
    @SerializedName("type")
    private String mType;
    @SerializedName("name")
    private String mName;
    @SerializedName("link")
    private String mLink;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("language")
    private String mLanguage; // enum? ... nope, use iso names
    @SerializedName("icon")
    private String mIcon;

    /**
     * Resource type
     */
    public enum ResourceType{
        FACEBOOK("facebook"),
        URL("url"),
        YOUTUBE("youtube"),
        APP("app"),
        UNKNOWN("unknown");

        private String alias;
        private ResourceType(String str){ alias = str; }
        public String getAlias(){ return alias; }
        public static ResourceType getByAlias(String alias){
            for(ResourceType res : ResourceType.values())
                if(res.alias.equalsIgnoreCase(alias))
                    return res;
            return ResourceType.UNKNOWN;
        }
    }

    /*public ResourceItem(long id, String name, String link, String description, String language, ResourceType type, String icon) {
        this.mId = id;
        this.mName = name;
        this.mLink = link;
        this.mLanguage = language;
        this.mDescription = description;
        this.mType = type;
        this.mIcon = icon;
    }*/



    public String getIcon() {
        return mIcon;
    }

    public ResourceType getType() {
        return ResourceType.getByAlias(mType);
    }

    public String getName() {
        return mName;
    }

    public String getLink() {
        return mLink;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public long getId() {
        return mId;
    }

}
