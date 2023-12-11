package spear;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * A convenient way to access the {@code Container} from anywhere in the application.
 */
public final class App
{
    private App() { /* Hidden */ }

    /**
     * Returns an environment variable value.
     *
     * @param key name of the environment variable
     * @return value of the variable or {@code null}
     */
    public static String envStr(String key)
    {
        return System.getenv(key);
    }

    /**
     * Returns an environment variable value as an {@code int}.
     *
     * @param key name of the environment variable
     * @return value of the variable as an {@code int}
     * @see java.lang.Integer#parseInt(String)
     */
    public static int envInt(String key)
    {
        String value = System.getenv(key);
        return Integer.parseInt(value);
    }

    /**
     * Returns the application configuration from the {@code Container}.
     *
     * @return the {@code Properties} object holding the application configuration
     * @see java.util.Properties
     */
    public static Properties getConfig()
    {
        return (Properties) Container.getInstance().get("spear.config");
    }

    /**
     * Returns an application configuration value.
     *
     * @param key key for the desired value
     * @return the configuration value or {@code null}
     */
    public static String cfgStr(String key)
    {
        return getConfig().getProperty(key);
    }

    /**
     * Returns an application configuration value as an {@code int}.
     *
     * @param key key for the desired value
     * @return the configuration variable as an {@code int}
     * @see java.lang.Integer#parseInt(String)
     */
    public static int cfgInt(String key)
    {
        String value = getConfig().getProperty(key);
        return Integer.parseInt(value);
    }

    /**
     * Returns an application configuration value as a {@code boolean}.
     *
     * @param key key for the desired value
     * @return the configuration variable as a {@code boolean}
     * @see java.lang.Boolean#parseBoolean(String)
     */
    public static boolean cfgBool(String key)
    {
        String value = getConfig().getProperty(key);
        return Boolean.parseBoolean(value);
    }

    /**
     * Returns the message bundle from the {@code Container}.
     *
     * @return the {@code ResourceBundle} instance in the default locale
     * @see java.util.ResourceBundle
     */
    public static ResourceBundle getResourceBundle()
    {
        return (ResourceBundle) Container.getInstance().get("spear.bundle");
    }

    /**
     * Returns a message from the message bundle.
     *
     * @param key key for the desired message
     * @return the message for the given key
     */
    public static String getMessage(String key)
    {
        return getResourceBundle().getString(key);
    }

    /**
     * Returns a message from the message bundle formatted with the given arguments.
     *
     * @param key key for the associated message to format and return
     * @param args object(s) to format
     * @return a formatted message
     * @see java.text.MessageFormat
     */
    public static String getMessage(String key, Object... args)
    {
        String pattern = getResourceBundle().getString(key);
        return MessageFormat.format(pattern, args);
    }
}
