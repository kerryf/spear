package spear;

import java.util.HashMap;
import java.util.Map;

public final class Container
{
    private static final Container CONTAINER = new Container();
    private final Map<String, Object> storage;

    private Container()
    {
        storage = new HashMap<>();
    }

    public static Container getInstance()
    {
        return CONTAINER;
    }

    public Object get(String key)
    {
        return storage.get(key);
    }

    public void put(String key, Object value)
    {
        storage.put(key, value);
    }

    public boolean has(String key)
    {
        return storage.containsKey(key);
    }
}
