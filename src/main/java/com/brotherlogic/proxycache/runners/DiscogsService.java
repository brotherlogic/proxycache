package com.brotherlogic.proxycache.runners;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.brotherlogic.proxycache.Config;
import com.brotherlogic.proxycache.callbacklistener.SocketListener;

/**
 * Discogs service
 * 
 * @author simon
 * 
 */
public class DiscogsService extends CachingOAuthService {

	@Override
	public Token buildAccessToken() throws IOException {

		// First check the config system
		if (Config.getInstance().getConfig("DISCOGS_ACCESS_KEY") != null) {
			return new Token(Config.getInstance().getConfig(
					"DISCOGS_ACCESS_KEY"), Config.getInstance().getConfig(
					"DISCOGS_ACCESS_SECRET"));
		}

		OAuthService service = getService(
				Config.getInstance().getConfig("DISCOGS_KEY"), Config
						.getInstance().getConfig("DISCOGS_SECRET"));

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

		Config.getInstance()
				.store("DISCOGS_ACCESS_KEY", accessToken.getToken());
		Config.getInstance().store("DISCOGS_ACCESS_SECRET",
				accessToken.getSecret());

		return accessToken;
	}

	@Override
	public OAuthService getService(final String consumerKey,
			final String consumerSecret) throws IOException {
		return new ServiceBuilder().provider(DiscogsAPI.class)
				.apiKey(consumerKey).apiSecret(consumerSecret)
				.callback("http://localhost:8094/blah").build();
	}

	@Override
	public Long getWaitTime() {
		return new Long(1000);
	}
}
