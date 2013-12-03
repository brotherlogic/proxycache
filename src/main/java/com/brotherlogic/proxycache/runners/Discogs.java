package com.brotherlogic.proxycache.runners;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.brotherlogic.proxycache.Config;
import com.brotherlogic.proxycache.ObjectManager;
import com.brotherlogic.proxycache.discogs.DiscogsUser;
import com.brotherlogic.proxycache.discogs.Folder;
import com.brotherlogic.proxycache.discogs.Identity;
import com.brotherlogic.proxycache.discogs.Release;

/**
 * Master discogs
 * 
 * @author simon
 * 
 */
public class Discogs {

	private final DiscogsService service = new DiscogsService();

	/**
	 * Gets the identify user
	 * 
	 * @return The DiscogsUser
	 * @throws Exception
	 *             if we can't pull the identity
	 */
	public DiscogsUser getMe() throws Exception {
		// Force load a user from the identity URL
		ObjectManager<Identity> manager = new ObjectManager<Identity>(
				Identity.class, service);
		Identity ident = manager.get();
		DiscogsUser user = new DiscogsUser(ident.getUsername());

		new ObjectManager<>(DiscogsUser.class, service).refresh(user);
		System.out.println(user.getFolders().getClass());
		System.out.println(user.getFolders().size());

		if (true)
			System.exit(1);

		return user;
	}

	/**
	 * Runner
	 * 
	 * @param args
	 *            we con't use cla
	 * @throws Exception
	 *             if something goes wrong
	 */
	public static void main(final String[] args) throws Exception {
		Config.getInstance().loadDir(new File("configs"));

		Discogs me = new Discogs();
		DiscogsUser user = me.getMe();

		for (Folder f : user.getFolders()) {
			if (f.getName().equals("12s")) {
				List<Release> rels = new LinkedList<>(f.getReleases());

				Collections.shuffle(rels);
				for (Release rel : rels) {
					if (rel.getRating() <= 0) {
						System.out.println(rel.getTitle());
						System.exit(1);
					}
				}
			}
		}
	}
}
