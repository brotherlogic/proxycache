package com.brotherlogic.proxycache.discogs;

/**
 * Track in Discogs
 * 
 * @author simon
 * 
 */
public class Track {
    /**
     * @return The title of the track
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param trackTitle
     *            The title of the track
     */
    public void setTitle(final String trackTitle) {
        this.title = trackTitle;
    }

    private String title;
}
