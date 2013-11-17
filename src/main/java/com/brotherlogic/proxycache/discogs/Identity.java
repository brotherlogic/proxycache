package com.brotherlogic.proxycache.discogs;

import com.brotherlogic.proxycache.LinkURL;

@LinkURL(url = "http://api.discogs.com/oauth/identity")
public class Identity {

	String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
