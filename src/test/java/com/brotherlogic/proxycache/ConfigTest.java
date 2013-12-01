package com.brotherlogic.proxycache;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests that the config is working correctly
 * 
 * @author simon
 * 
 */
public class ConfigTest {

    /**
     * Removes the test keys
     */
    @After
    public void cleanup() {
        Config.getInstance().delete("Test-Key");
    }

    /**
     * Tests that we can add into the config correctly
     */
    @Test
    public void testAddConfig() {
        Config.getInstance().store("Test-Key", "Test-Value");
        Assert.assertEquals("Retrieved incorrectly", "Test-Value", Config.getInstance().getConfig("Test-Key"));
        Config.getInstance().store("Test-Key", "New-Test-Value");
        Assert.assertEquals("Retrieved incorrectly", "New-Test-Value", Config.getInstance().getConfig("Test-Key"));
    }

    /**
     * Tests that we can load a directory
     */
    @Test
    public void testDirLoad() {
        Config.getInstance().loadDir(new File("test-config"));
        Assert.assertEquals("Retrieved incorrectly", "Test-Value", Config.getInstance().getConfig("Test-Key"));
    }

    /**
     * Tests that we can delete a non-existant key
     */
    @Test
    public void testEmptyDelete() {
        Config.getInstance().delete("Test-Key");
    }

    /**
     * Tests that we can retrieve a null value from a made up key
     */
    @Test
    public void testEmptyRetreive() {
        String val = Config.getInstance().getConfig("MadeUpKey");
        Assert.assertNull("Emptry retrieve is not null", val);
    }

}
