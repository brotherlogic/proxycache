package com.brotherlogic.proxycache.twitter;

import com.brotherlogic.proxycache.LinkURL;
import com.brotherlogic.proxycache.ObjectProperties;

/**
 * Example Tweet class
 * 
 * @author simon
 * 
 */
@LinkURL(url = "https://api.twitter.com/1.1/statuses/show.json")
@ObjectProperties(staletime = 86400)
public class Tweet {

	private long id;

	private String text;

	public long getId() {
		return id;
	}

	public String getText(String txt) {
		return text;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setText(String txt) {
		this.text = txt;
	}

}
