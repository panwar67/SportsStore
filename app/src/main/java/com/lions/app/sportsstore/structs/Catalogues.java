package com.lions.app.sportsstore.structs;

import java.io.Serializable;

/**
 * Created by Panwar on 25/01/18.
 */

public class Catalogues implements Serializable {

    String catName;
    String catId;
    String tags;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public String getTags() {
        return tags;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


}
