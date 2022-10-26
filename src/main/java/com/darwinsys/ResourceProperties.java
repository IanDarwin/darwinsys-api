package com.darwinsys.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceProperties {
    public static Properties propertiesFromClassPath(Class<?> clazz, String propsName) {
        try (InputStream is = clazz.getResourceAsStream(propsName)) {
            if (is == null) {
                throw new IllegalArgumentException(
                    "File " + propsName + " not on CLASSPATH for " + clazz.getName());
            }
            Properties p = new Properties();
            p.load(is);
            return p;
        } catch (IOException e) {
            throw new IllegalStateException(
                "CANTHAPPEN: IOException in config properties resource " + propsName);
        }
    }
}
