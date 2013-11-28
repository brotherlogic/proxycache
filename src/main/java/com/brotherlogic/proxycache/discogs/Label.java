package com.brotherlogic.proxycache.discogs;

import com.brotherlogic.proxycache.LinkURL;

/**
 * A label in the discogs world
 * 
 * @author simon
 * 
 */
@LinkURL(url = "http://api.discogs.com/labels/<Id>")
public class Label {

    private int id;
    private String name;

    /**
     * @return The id of the label
     */
    public int getId() {
        return id;
    }

    /**
     * @return The name of the label
     */
    public String getName() {
        return name;
    }

    /**
     * @param labelId
     *            The id of the label
     */
    public void setId(final int labelId) {
        this.id = labelId;
    }

    /**
     * @param nme
     *            The name of the label
     */
    public void setName(final String nme) {
        this.name = nme;
    }

}
