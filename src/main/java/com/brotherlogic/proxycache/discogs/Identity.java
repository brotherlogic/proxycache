package com.brotherlogic.proxycache.discogs;

import com.brotherlogic.proxycache.LinkURL;

/**
 * Identity in the discogs world
 * 
 * @author simon
 * 
 */
@LinkURL(url = "http://api.discogs.com/oauth/identity")
public class Identity {

    private String username;

    /**
     * @return The name of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param name
     *            The name of this user
     */
    public void setUsername(final String name) {
        this.username = name;
    }
}
