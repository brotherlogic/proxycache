package com.brotherlogic.proxycache.discogs;

import java.util.Collection;

import com.brotherlogic.proxycache.LinkURL;

/**
 * A release in the discogs world
 * 
 * @author simon
 * 
 */
public class Release {

    private long id;
    private Collection<Label> labels;
    private int rating = -1;
    private String title;

    /**
     * @return The id of the release
     */
    public long getId() {
        return id;
    }

    /**
     * @return A {@link Collection} of all the {@link Label} for this release
     */
    public Collection<Label> getLabels() {
        return labels;
    }

    /**
     * @return The rating of this release
     */
    public int getRating() {
        return rating;
    }

    /**
     * @return The name of this release
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param relId
     *            The id of this release
     */
    public void setId(final long relId) {
        this.id = relId;
    }

    /**
     * @param relLabels
     *            All the {@link Label} suitable for this release
     */
    @LinkURL(path = "basic_information->labels", prodClass = "com.brotherlogic.proxycache.discogs.Label")
    public void setLabels(final Collection<Label> relLabels) {
        this.labels = relLabels;
    }

    /**
     * @param rate
     *            The user rating of this release
     */
    @LinkURL(path = "basic_information->rating")
    public void setRating(final int rate) {
        this.rating = rate;
    }

    /**
     * @param name
     *            The name of this release
     */
    @LinkURL(path = "basic_information->title")
    public void setTitle(final String name) {
        this.title = name;
    }

}
