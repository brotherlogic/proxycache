package com.brotherlogic.proxycache.twitter;

import java.util.LinkedList;
import java.util.List;

import com.brotherlogic.proxycache.LinkURL;

@LinkURL(url = "https://api.twitter.com/1.1/users/show.json?[screen_name=<ScreenName>|user_id=<UserId>]")
public class TweetUser {

	String name;

	List<Tweet> tweets = new LinkedList<Tweet>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

}
