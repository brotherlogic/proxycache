package com.brotherlogic.proxycache.discogs;

import com.brotherlogic.proxycache.LinkURL;

/**
 * Format in the Discogs world
 * 
 * @author simon
 * 
 */
public class Format {
    private int quantity;
    private String name;

    /**
     * @return Number of discs
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param formatQuantity
     *            Number of discs
     */
    @LinkURL(path = "qty")
    public void setQuantity(final int formatQuantity) {
        this.quantity = formatQuantity;
    }

    /**
     * @return The name of the format
     */
    public String getName() {
        return name;
    }

    /**
     * @param formatName
     *            The name of the format
     */
    public void setName(final String formatName) {
        this.name = formatName;
    }
}
