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

	Token accessToken;
	OAuthService service;
	JsonParser parser = new JsonParser();

	public abstract Token buildAccessToken() throws IOException;

	public JsonElement get(String url) {
		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		service.signRequest(accessToken, request);
		Response response = request.send();
		return parser.parse(response.getBody());
	}
}
