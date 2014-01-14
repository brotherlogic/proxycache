package com.brotherlogic.proxycache.discogs;

import java.io.IOException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.proxycache.ObjectManager;

/**
 * Tests the Discogs folder access
 * 
 * @author simon
 * 
 */
public class DiscogsFolderTest extends DiscogsBaseTest {

    /**
     * Runs the main test
     * 
     * @throws IOException
     *             If we can't communicate
     */
    @Test
    public void testFolderProps() throws IOException {
        DiscogsService service = new DiscogsService();
        ObjectManager<Identity> manager = new ObjectManager<Identity>(Identity.class, service);
        Identity ident = manager.get();
        DiscogsUser user = new DiscogsUser(ident.getUsername());
        new ObjectManager<>(DiscogsUser.class, service).refresh(user);
        Assert.assertNotNull("User folders are null", user.getFolders());
        System.out.println(user.getFolders().getClass());
        Assert.assertEquals("Mismatch in user collection sizes", 8, user.getFolders().size());

        for (Folder f : user.getFolders()) {
            if (f.getName().equals("10s")) {
                Assert.assertEquals("Mismatch in 10 inch folder size", 49, f.getCount());
                Assert.assertEquals("Mismatch in 10 inch id", 267115, f.getId());
                Assert.assertEquals("Mismatch in releases size", 49, f.getReleases().size());

                // Check that to array works also
                Object[] rels = f.getReleases().toArray();
                Assert.assertEquals("Mismatch in 10 inch size", f.getCount(), rels.length);
            }

            for (Release r : f.getReleases()) {
                if (r.getTitle().equals("Llanfwrog EP")) {
                    Collection<Label> labels = r.getLabels();
                    Assert.assertEquals("Mismatch in label size", 1, labels.size());

                    Label lab = labels.iterator().next();
                    Assert.assertEquals("Mismatch in label name", "Ankst", lab.getName());

                }
            }
        }
    }

}
