package com.brotherlogic.proxycache.discogs;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.proxycache.ObjectManager;
import com.brotherlogic.proxycache.runners.DiscogsService;

public class DiscogsUserTest {
	@Test
	public void retrieveUser() throws IOException {
		DiscogsService.forceCache(true);
		DiscogsService service = new DiscogsService();
		ObjectManager<Identity> manager = new ObjectManager<Identity>(
				Identity.class, service);
		Identity ident = manager.get();
		DiscogsUser user = new DiscogsUser(ident.getUsername());
		new ObjectManager<>(DiscogsUser.class, service).refresh(user);
		Assert.assertNotNull("User folders are null", user.getFolders());
		System.out.println(user.getFolders().getClass());
		Assert.assertEquals("Mismatch in user collection sizes", 8, user
				.getFolders().size());
	}
}
