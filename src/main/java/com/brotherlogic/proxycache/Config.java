package com.brotherlogic.proxycache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Handles any config files
 * 
 * @author simon
 * 
 */
public final class Config {

    private static Config config;

    private final DBCollection configCollection;

    /**
     * Constructor
     * 
     * @throws UnknownHostException
     *             If we can't connect to the db
     */
    private Config() throws UnknownHostException {
        configCollection = new MongoClient().getDB("proxy-config").getCollection("config");
    }

    /**
     * @param key
     *            Config key
     * @return corresponding value for given key
     */
    public String getConfig(final String key) {
        BasicDBObject query = new BasicDBObject();
        query.append("key", key);
        return (String) configCollection.find(query).next().get("key");
    }

    /**
     * Loads up a config directory
     * 
     * @param dir
     *            The directory to load
     */
    public void loadDir(final File dir) {
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".config")) {
                loadFile(f);
            }
        }
    }

    /**
     * Loads up a config file
     * 
     * @param f
     *            The file to be loaded
     */
    public void loadFile(final File f) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String key = line.substring(0, line.indexOf("="));
                String value = line.substring(line.indexOf("="));
                store(key, value);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key
     *            The key to store
     * @param value
     *            The value to store with this key
     */
    public void store(final String key, final String value) {
        BasicDBObject query = new BasicDBObject();
        query.put("key", key);
        query.put("value", value);
        configCollection.apply(query);
    }

    /**
     * Gets a config instance
     * 
     * @return A valid config instance or null if we can't create one
     */
    public static Config getInstance() {
        if (config == null) {
            try {
                config = new Config();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return config;
    }

}
