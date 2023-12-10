package spear;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;

public final class App
{
    private App() { /* Hidden */ }

    public static String envStr(String key)
    {
        return System.getenv(key);
    }

    public static int envInt(String key)
    {
        String value = System.getenv(key);
        return Integer.parseInt(value);
    }

    public static Properties getConfig()
    {
        return (Properties) Container.getInstance().get("spear.config");
    }

    public static String cfgStr(String key)
    {
        return getConfig().getProperty(key);
    }

    public static int cfgInt(String key)
    {
        String value = getConfig().getProperty(key);
        return Integer.parseInt(value);
    }

    public static boolean cfgBool(String key)
    {
        String value = getConfig().getProperty(key);
        return Boolean.parseBoolean(value);
    }

    public static ResourceBundle getResourceBundle()
    {
        return (ResourceBundle) Container.getInstance().get("spear.bundle");
    }

    public static String getMessage(String key)
    {
        return getResourceBundle().getString(key);
    }

    public static String getMessage(String key, Object... args)
    {
        String pattern = getResourceBundle().getString(key);
        return MessageFormat.format(pattern, args);
    }
}
