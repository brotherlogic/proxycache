package com.brotherlogic.proxycache.example.twitter;

import java.lang.reflect.Method;
import java.util.Arrays;

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

	private String text;
	private long id;

	public long getId() {
		return id;
	}

	@LinkURL(path = "id")
	public void setId(long id) {
		this.id = id;
	}

	@LinkURL(path = "text")
	public void setText(String txt) {
		this.text = txt;
	}

	public String getText(String txt) {
		return text;
	}

	public static void main(String[] args) {
		Class<?> tClass = Tweet.class;
		for (Method m : tClass.getMethods()) {
			System.out.println(m.getName() + " => "
					+ Arrays.toString(m.getAnnotations()));
		}

		System.out.println(tClass.getAnnotations());
	}

}
