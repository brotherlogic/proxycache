package com.brotherlogic.proxycache.discogs;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.proxycache.ObjectManager;

/**
 * Tests the identify retrieval
 * 
 * @author simon
 * 
 */
public class IdentityTest extends DiscogsBaseTest {

    /**
     * Runs the main test
     * 
     * @throws IOException
     *             if we can't pull data
     */
    @Test
    public void retrieveIdentity() throws IOException {
        DiscogsService service = new DiscogsService();
        ObjectManager<Identity> manager = new ObjectManager<Identity>(Identity.class, service);
        Identity ident = manager.get();
        Assert.assertEquals("Mismatch in identity names", "BrotherLogic", ident.getUsername());
    }

}
