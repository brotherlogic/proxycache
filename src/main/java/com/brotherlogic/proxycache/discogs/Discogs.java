package com.brotherlogic.proxycache.discogs;

import com.brotherlogic.proxycache.ObjectManager;

public class Discogs {

	DiscogsService service = new DiscogsService();

	public DiscogsUser getMe() throws Exception {
		// Force load a user from the identity URL
		ObjectManager<Identity> manager = new ObjectManager<Identity>(
				Identity.class, service);
		Identity ident = manager.get();
		DiscogsUser user = new DiscogsUser(ident.getUsername());
		new ObjectManager<>(DiscogsUser.class, service).refresh(user);
		return user;
	}

	public static void main(String[] args) throws Exception {
		Discogs me = new Discogs();
		DiscogsUser user = me.getMe();
		System.out.println(user.getReleases_rated());
		System.out.println(user.getFolders() + " => "
				+ user.getFolders().getClass());
	}
}
