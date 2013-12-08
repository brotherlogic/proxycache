package com.brotherlogic.proxycache.discogs;

/**
 * Artist in the discogs world
 * 
 * @author simon
 * 
 */

public class Artist {

    private String name;

    /**
     * @return The name of the artist
     */
    public String getName() {
        return name;
    }

    /**
     * @param artistName
     *            The name of the artist
     */
    public void setName(final String artistName) {
        this.name = artistName;
    }
}
