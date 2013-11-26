package com.brotherlogic.proxycache.discogs;

import com.brotherlogic.proxycache.LinkURL;

@LinkURL(url = "http://api.discogs.com/labels/<Id>")
public class Label {

	String name;
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
