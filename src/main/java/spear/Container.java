package spear;

import java.util.HashMap;
import java.util.Map;

/**
 * A place to store objects that live for the duration of the application.
 */
public final class Container
{
    private static final Container CONTAINER = new Container();
    private final Map<String, Object> storage;

    private Container()
    {
        storage = new HashMap<>();
    }

    /**
     * Get the shared {@code Container} instance.
     *
     * @return the {@code Container} instance
     */
    public static Container getInstance()
    {
        return CONTAINER;
    }

    /**
     * Retrieve an object from the {@code Container}.
     *
     * @param key key for the associated value to return
     * @return the value mapped to key or {@code null}
     */
    public Object get(String key)
    {
        return storage.get(key);
    }

    /**
     * Add an object to the {@code Container}.
     *
     * @param key key to associate with value
     * @param value value to store in the {@code Container}
     */
    public void put(String key, Object value)
    {
        storage.put(key, value);
    }

    /**
     * Returns {@code true} if the {@code Container} has an object associated with {@code key}.
     *
     * @param key the key whose presence to test for
     * @return {@code true} if an object is associated with the given key
     */
    public boolean has(String key)
    {
        return storage.containsKey(key);
    }
}
