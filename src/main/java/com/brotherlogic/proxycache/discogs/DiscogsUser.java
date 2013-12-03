package com.brotherlogic.proxycache.discogs;

import java.util.Collection;

import com.brotherlogic.proxycache.LinkURL;

/**
 * User in the discogs world
 * 
 * @author simon
 * 
 */
@LinkURL(url = "http://api.discogs.com/users/<username>")
public class DiscogsUser {

    private Collection<Folder> folders;

    private int releasesRated;

    private String username;

    /**
     * COnstructor
     * 
     * @param name
     *            THe name of the user
     */
    public DiscogsUser(final String name) {
        this.username = name;
    }

    /**
     * @return The set of folders for this user
     */
    public Collection<Folder> getFolders() {
        return folders;
    }

    /**
     * @return The number of releases rated
     */
    public int getReleasesRated() {
        return releasesRated;
    }

    /**
     * @return The name of this user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param folds
     *            The folders for this user
     */
    @LinkURL(path = "folders", url = "http://api.discogs.com/users/<username>/collection/folders", prodClass = "com.brotherlogic.proxycache.discogs.Folder")
    public void setFolders(final Collection<Folder> folds) {
        folders = folds;
    }

    /**
     * @param relRated
     *            The number of releases rated
     */
    @LinkURL(path = "releases_rated")
    public void setReleasesRated(final int relRated) {
        this.releasesRated = relRated;
    }

    /**
     * @param name
     *            The name of this user
     */
    public void setUsername(final String name) {
        this.username = name;
    }

}
