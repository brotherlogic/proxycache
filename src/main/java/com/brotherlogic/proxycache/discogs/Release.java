package com.brotherlogic.proxycache.discogs;

import java.util.Collection;

import com.brotherlogic.proxycache.LinkURL;

public class Release {

	long id;
	String title;
	Collection<Label> labels;

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setId(long id) {
		this.id = id;
	}

	@LinkURL(path = "basic_information->title")
	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<Label> getLabels() {
		return labels;
	}

	public void setLabels(Collection<Label> labels) {
		this.labels = labels;
	}

}
