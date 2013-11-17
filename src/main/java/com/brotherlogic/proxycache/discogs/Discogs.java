package com.brotherlogic.proxycache.discogs;

import com.brotherlogic.proxycache.ObjectManager;

public class Discogs {

	DiscogsService service = new DiscogsService();

	public DiscogsUser getMe() throws Exception {
		// Force load a user from the identity URL
		ObjectManager<Identity> manager = new ObjectManager<Identity>(
				Identity.class, new DiscogsService());
		Identity ident = manager.get();
		DiscogsUser user = new DiscogsUser(ident.getUsername());
		manager.refresh(user);
		return user;
	}

	public static void main(String[] args) throws Exception {
		Discogs me = new Discogs();
		me.getMe();
	}
}
