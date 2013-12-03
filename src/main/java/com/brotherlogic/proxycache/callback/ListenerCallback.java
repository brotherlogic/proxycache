package com.brotherlogic.proxycache.callback;

import java.util.Map;

/**
 * A callback on the listener
 * 
 * @author simon
 * 
 */
public interface ListenerCallback {
    /**
     * Deals with the properties from the web response
     * 
     * @param props
     *            The props to process
     */
    void processResponse(Map<String, String> props);
}
