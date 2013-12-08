package com.brotherlogic.proxycache.runners;

import java.util.Collection;

import com.brotherlogic.proxycache.LinkURL;
import com.brotherlogic.proxycache.discogs.Release;

/**
 * Master release in Discogs
 * 
 * @author simon
 * 
 */
@LinkURL(url = "http://api.discogs.com/masters/<Id>")
public class Master {
    private int id;
    private String title;
    private Collection<Release> versions;

    /**
     * @return The ID of the master
     */
    public int getId() {
        return id;
    }

    /**
     * @param masterId
     *            The ID of the master
     */
    public void setId(final int masterId) {
        this.id = masterId;
    }

    /**
     * @return The title of the master
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param masterTitle
     *            The title of the master
     */
    public void setTitle(final String masterTitle) {
        this.title = masterTitle;
    }

    /**
     * @return The releases for this master
     */
    public Collection<Release> getVersions() {
        return versions;
    }

    /**
     * @param masterVersions
     *            The releases for this master
     */
    @LinkURL(prodClass = "com.brotherlogic.proxycache.discogs.Release", url = "http://api.discogs.com/masters/<Id>/versions", path = "versions")
    public void setVersions(final Collection<Release> masterVersions) {
        this.versions = masterVersions;
    }
}
