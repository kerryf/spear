package spear

import groovy.sql.Sql
import spear.security.Pbkdf2PasswordEncoder

import java.text.MessageFormat

/**
 * A convenient way to access the {@code Container} from anywhere in the application.
 */
final class App
{
    private App()
    { /* Hidden */ }

    /**
     * Returns an environment variable value.
     *
     * @param key name of the environment variable
     * @return value of the variable or {@code null}
     */
    static String envStr(String key)
    {
        return System.getenv(key)
    }

    /**
     * Returns an environment variable value as an {@code int}.
     *
     * @param key name of the environment variable
     * @return value of the variable as an {@code int}
     * @see java.lang.Integer#parseInt(String)
     */
    static int envInt(String key)
    {
        String value = System.getenv(key)
        return Integer.parseInt(value)
    }

    /**
     * Returns the application configuration from the {@code Container}.
     *
     * @return the {@code ConfigObject} object holding the application configuration
     * @see groovy.util.ConfigObject
     */
    static ConfigObject getConfig()
    {
        return Container.instance.get('spear.config') as ConfigObject
    }

    /**
     * Returns the message bundle from the {@code Container}.
     *
     * @return the {@code ResourceBundle} instance in the default locale
     * @see java.util.ResourceBundle
     */
    static ResourceBundle getResourceBundle()
    {
        return Container.instance.get('spear.bundle') as ResourceBundle
    }

    /**
     * Returns a message from the message bundle.
     *
     * @param key key for the desired message
     * @return the message for the given key
     */
    static String getMessage(String key)
    {
        return resourceBundle.getString(key)
    }

    /**
     * Returns a message from the message bundle formatted with the given arguments.
     *
     * @param key key for the associated message to format and return
     * @param args object(s) to format
     * @return a formatted message
     * @see java.text.MessageFormat
     */
    static String getMessage(String key, Object... args)
    {
        String pattern = resourceBundle.getString(key)
        return MessageFormat.format(pattern, args)
    }

    /**
     * Returns the password encoder from the {@code Container}.
     *
     * @return the shared {@code Pbkdf2PasswordEncoder} instance
     */
    static Pbkdf2PasswordEncoder getEncoder()
    {
        return Container.instance.get('spear.encoder') as Pbkdf2PasswordEncoder
    }

    /**
     * Returns the database facade from the {@code Container}.
     *
     * @return the {@code Sql} object providing database access
     */
    static Sql getSql()
    {
        return Container.instance.get('spear.database') as Sql
    }
}
