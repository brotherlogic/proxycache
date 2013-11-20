package com.brotherlogic.proxycache.discogs;

import java.util.Collection;

import com.brotherlogic.proxycache.LinkURL;
import com.brotherlogic.proxycache.Pagination;

public class Folder {

	int count;
	int id;
	String name;
	Collection<Release> releases;
	String resource_url;

	public int getCount() {
		return count;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@LinkURL(url = "<resource_url>", prodClass = "com.brotherlogic.proxycache.discogs.Release", path = "releases")
	@Pagination(botPath = "pagination->")
	public Collection<Release> getReleases() {
		return releases;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReleases(Collection<Release> releases) {
		this.releases = releases;
	}

	@Override
	public String toString() {
		return name;
	}

}
