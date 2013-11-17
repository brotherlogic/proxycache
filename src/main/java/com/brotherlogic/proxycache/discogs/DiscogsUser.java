package com.brotherlogic.proxycache.discogs;

import com.brotherlogic.proxycache.LinkURL;

@LinkURL(url = "http://api.discogs.com/users/<username>")
public class DiscogsUser {

	private String username;

	private int releases_rated;

	public DiscogsUser(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getReleases_rated() {
		return releases_rated;
	}

	public void setReleases_rated(int releases_rated) {
		this.releases_rated = releases_rated;
	}

}
