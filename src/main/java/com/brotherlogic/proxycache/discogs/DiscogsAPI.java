package com.brotherlogic.proxycache.discogs;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Scribe class for the discogs api
 * 
 * @author simon
 * 
 */
public class DiscogsAPI extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.discogs.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(final Token arg0) {
        return String.format("http://www.discogs.com/oauth/authorize?oauth_token=%s", arg0.getToken());
    }

    @Override
    public String getRequestTokenEndpoint() {
        System.out.println("Getting request token");
        return "http://api.discogs.com/oauth/request_token";
    }

}
