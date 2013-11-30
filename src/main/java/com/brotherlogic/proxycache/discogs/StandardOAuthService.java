package com.brotherlogic.proxycache.discogs;

import java.io.IOException;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.brotherlogic.proxycache.Config;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * A standard OAuth Web Service
 * 
 * @author simon
 * 
 */
public abstract class StandardOAuthService {

    private static long lastPullTime = 0;
    private Token accessToken;
    private final JsonParser parser = new JsonParser();
    private OAuthService service;

    /**
     * @return The Access Token for this service
     * @throws IOException
     *             If we can't get the access token
     */
    public abstract Token buildAccessToken() throws IOException;

    /**
     * @param url
     *            The URL to run the GET request for
     * @return The JSON response from the get
     * @throws IOException
     *             If the web request doesn't work
     */
    public synchronized JsonElement get(final String url) throws IOException {

        localWait();

        login(Config.getInstance().getConfig("DISCOGS_KEY"), Config.getInstance().getConfig("DISCOGS_SECRET"));
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        service.signRequest(accessToken, request);
        Response response = request.send();
        lastPullTime = System.currentTimeMillis();
        return parser.parse(response.getBody());
    }

    /**
     * @param key
     *            The key for this service
     * @param secret
     *            The secret for this service
     * @return The corresponding OAuthService
     * @throws IOException
     *             If we can't get at the service
     */
    public abstract OAuthService getService(String key, String secret) throws IOException;

    /**
     * @return THe number of ms to wait between requests
     */
    public abstract Long getWaitTime();

    /**
     * Wait for the right amount of time
     */
    private void localWait() {
        long waitTime = getWaitTime() - (System.currentTimeMillis() - lastPullTime);
        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Login to the service
     * 
     * @param secret
     *            The secret
     * @param key
     *            The key
     * @throws IOException
     *             If we can't login
     */
    public void login(final String secret, final String key) throws IOException {
        if (accessToken == null) {
            accessToken = buildAccessToken();
        }

        if (service == null) {
            service = getService(secret, key);
        }
    }
}
