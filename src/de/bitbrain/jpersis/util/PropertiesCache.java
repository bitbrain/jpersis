package de.bitbrain.jpersis.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Caches properties and considers developer properties accordingly.
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @version 1.0
 */
public class PropertiesCache {

    private static final String DEV_FLAG = ".dev";
    private static final String PROPERTIES = ".properties";

    private final Properties properties = new Properties();

    private final Properties devProperties = new Properties();

    public PropertiesCache(String basename) {
        try {
            readProperties(basename + PROPERTIES, properties);
            readProperties(basename + DEV_FLAG + PROPERTIES, properties);
        } catch (IOException ex) {
            System.out.println("Warning: " + ex.getMessage());
        }
    }

    public String getProperty(String property) {
        return getProperty(property, null);
    }

    public String getProperty(String property, String defaultValue) {
        if (devProperties.containsKey(property)) {
            return devProperties.getProperty(property);
        } else if (properties.containsKey(property)) {
            return properties.getProperty(property);
        } else {
            return defaultValue;
        }
    }

    private void readProperties(String filename, Properties properties) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        if (in == null) {
            throw new IOException(filename + " is not on classpath!");
        }
        properties.load(in);
    }
}