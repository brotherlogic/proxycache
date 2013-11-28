package com.brotherlogic.proxycache.discogs;

import java.io.IOException;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public abstract class StandardOAuthService {

	static long lastPullTime = 0;
	Token accessToken;
	JsonParser parser = new JsonParser();
	OAuthService service;

	public abstract Token buildAccessToken() throws IOException;

	public synchronized JsonElement get(String url) throws IOException {

		localWait();

		login("RCyqexMcezQoBfTGpcsG", "wQMnFXZYkoyyVXEjrjBnbIMxBynUvpDB");
		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		service.signRequest(accessToken, request);
		Response response = request.send();
		lastPullTime = System.currentTimeMillis();
		return parser.parse(response.getBody());
	}

	public abstract OAuthService getService(String key, String secret)
			throws IOException;

	public abstract Long getWaitTime();

	private void localWait() {
		long waitTime = getWaitTime()
				- (System.currentTimeMillis() - lastPullTime);
		if (waitTime > 0)
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public void login(String secret, String key) throws IOException {
		if (accessToken == null)
			accessToken = buildAccessToken();

		if (service == null)
			service = getService(secret, key);
	}
}
