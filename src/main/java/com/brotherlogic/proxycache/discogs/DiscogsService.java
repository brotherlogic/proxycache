package com.brotherlogic.proxycache.discogs;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.brotherlogic.proxycache.callbacklistener.SocketListener;

public class DiscogsService extends StandardOAuthService {

	String consumerKey = "RCyqexMcezQoBfTGpcsG";
	String consumerSecret = "wQMnFXZYkoyyVXEjrjBnbIMxBynUvpDB";

	@Override
	public Token buildAccessToken() throws IOException {

		OAuthService service = getService();

		Token requestToken = service.getRequestToken();
		String authURL = service.getAuthorizationUrl(requestToken);

		// Run up the listening server
		SocketListener listener = new SocketListener();

		try {
			Desktop.getDesktop().browse(new URI(authURL));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Map<String, String> response = listener.listenForWebRequest(8094);

		Verifier v = new Verifier(response.get("oauth_verifier"));
		Token accessToken = service.getAccessToken(requestToken, v);

		return accessToken;
	}

	@Override
	public OAuthService getService() throws IOException {
		return new ServiceBuilder().provider(DiscogsAPI.class)
				.apiKey(consumerKey).apiSecret(consumerSecret)
				.callback("http://localhost:8094/blah").build();
	}

	public static void main(String[] args) throws Exception {
		DiscogsService serv = new DiscogsService();
		System.out.println(serv.buildAccessToken());
	}
}
