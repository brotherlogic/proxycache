package com.brotherlogic.proxycache.discogs;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class DiscogsAPI extends DefaultApi10a {

	@Override
	public String getAccessTokenEndpoint() {
		return "http://api.discogs.com/oauth/access_token";
	}

	@Override
	public String getAuthorizationUrl(Token arg0) {
		return String.format(
				"http://www.discogs.com/oauth/authorize?oauth_token=%s",
				arg0.getToken());
	}

	@Override
	public String getRequestTokenEndpoint() {
		return "http://api.discogs.com/oauth/request_token";
	}

}
