package com.brotherlogic.proxycache.discogs;

import java.util.Collection;
import java.util.LinkedList;

import com.brotherlogic.proxycache.LinkURL;

@LinkURL(url = "http://api.discogs.com/users/<username>")
public class DiscogsUser {

	private Collection<Folder> folders = new LinkedList<Folder>();

	private int releases_rated;

	private String username;

	public DiscogsUser(String username) {
		this.username = username;
	}

	public Collection<Folder> getFolders() {
		return folders;
	}

	public int getReleases_rated() {
		return releases_rated;
	}

	public String getUsername() {
		return username;
	}

	@LinkURL(path = "folders", url = "http://api.discogs.com/users/<username>/collection/folders", prodClass = "com.brotherlogic.proxycache.discogs.Folder")
	public void setFolders(Collection<Folder> folds) {
		folders = folds;
	}

	public void setReleases_rated(int releases_rated) {
		this.releases_rated = releases_rated;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
