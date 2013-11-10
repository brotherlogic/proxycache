package com.brotherlogic.proxycache.twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.proxycache.example.twitter.ObjectBuilderFactory;
import com.google.gson.JsonParser;

public class TweetTest {

	private String getTweet() {
		StringBuffer tweet = new StringBuffer();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					this.getClass().getResourceAsStream("tweet.txt")));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine())
				tweet.append(line);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tweet.toString();
	}

	@Test
	public void testJsonConvert() {
		Tweet tweet = ObjectBuilderFactory.getInstance()
				.getObjectBuilder(Tweet.class).build(getTweet());

		// Check the properties of this tweet
		Assert.assertEquals("Mismatch in id", tweet.getId(), new JsonParser()
				.parse(getTweet()).getAsJsonObject().get("id").getAsLong());
	}
}
