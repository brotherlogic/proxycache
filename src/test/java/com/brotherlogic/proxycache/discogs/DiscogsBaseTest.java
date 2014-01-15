package com.brotherlogic.proxycache.discogs;

import org.junit.Before;

/**
 * Base properties for discogs tests
 * 
 * @author simon
 * 
 */
public class DiscogsBaseTest {

    /**
     * Readys the discogs environment for use
     */
    @Before
    public void prep() {
        DiscogsService.forceCache("discogstestcache");
    }

}
