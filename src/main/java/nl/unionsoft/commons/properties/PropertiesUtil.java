package nl.unionsoft.commons.properties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

    public static String propertyString(Properties properties, String key) {
        String result = null;
        if (properties != null) {
            result = properties.getProperty(key);
        }
        return result;
    }

    public static String getProperty(Properties properties, String key, String defaultValue) {
        String result = null;
        if (properties != null) {
            result = properties.getProperty(key, defaultValue);
        }
        return result;
    }

    public static boolean propertyBoolean(Properties properties, String key) {
        return BooleanUtils.toBoolean(propertyString(properties, key));
    }

    public static boolean propertyBoolean(Properties properties, String key, String defaultValue) {
        return BooleanUtils.toBoolean(getProperty(properties, key, defaultValue));
    }

    public static String getProperty(Properties properties, Properties fallbackProperties, String key) {
        String result = propertyString(properties, key);
        if (StringUtils.isBlank(result)) {
            result = propertyString(fallbackProperties, key);
        }
        return result;
    }

    public static String getProperty(Properties properties, Properties fallbackProperties, String key, String defaultValue) {
        String result = getProperty(properties, fallbackProperties, key);
        if (StringUtils.isBlank(result)) {
            result = defaultValue;
        }
        return result;
    }

    public static String[] getPropertyArray(Properties properties, String key, String separator) {
        return StringUtils.split(propertyString(properties, key), separator);
    }

    public static Set<String> getUniquePropertyValues(Properties properties, String key, String separator) {
        final Set<String> results = new HashSet<String>();
        final String[] props = getPropertyArray(properties, key, separator);
        if (props != null) {
            for (final String prop : props) {
                results.add(prop);
            }
        }
        return results;
    }

    public static void setProperty(Properties properties, String key, String value) {
        if (properties != null) {
            properties.setProperty(key, value);
        }
    }

    public static void setProperty(Properties properties, String key, String[] values, String separator) {
        setProperty(properties, key, StringUtils.join(values, separator));
    }

    public static void setProperty(Properties properties, String key, Collection<String> values, String separator) {
        setProperty(properties, key, StringUtils.join(values, separator));
    }

    public static Properties readFromFile(File file) {
        final Properties properties = new Properties();
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                properties.load(fis);
            } catch(final IOException e) {
                LOG.error("Unable to read properties from file '{}', caught IOException", e);
            } finally {
                IOUtils.closeQuietly(fis);
            }

        }
        return properties;
    }

    public static Properties stringToProperties(String properties) {
        final Properties result = new Properties();
        InputStream propertyInputStream = null;
        if (StringUtils.isNotEmpty(properties)) {
            try {
                propertyInputStream = IOUtils.toInputStream(properties, "UTF-8");
                result.load(propertyInputStream);
            } catch(final IOException e) {
                LOG.error("Unable to parse string to properties, caught IOException", e);
            } finally {
                IOUtils.closeQuietly(propertyInputStream);
            }
        }
        return result;

    }

    public static String propertiesToString(Properties properties) {

        String result = null;
        if (properties != null) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                properties.store(baos, null);
                result = new String(baos.toByteArray());
            } catch(final IOException e) {
                LOG.error("Unable to parse properties to string, caught IOException", e);
                IOUtils.closeQuietly(baos);
            }

        }
        return result;
    }

}
