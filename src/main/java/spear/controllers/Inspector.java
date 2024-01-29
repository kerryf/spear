package spear.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import spear.App;

public final class Inspector
{
    private Inspector()
    { /* Hidden */ }

    public static JsonNode getBody(Context context) throws InspectorException
    {
        String body = context.body();
        if (body.isEmpty())
        {
            throw new InspectorException(App.getMessage("body.empty"));
        }

        try
        {
            return new ObjectMapper().readTree(body);
        }
        catch (JsonProcessingException e)
        {
            throw new InspectorException(App.getMessage("body.parse"));
        }
    }

    public static JsonNode getBody(Context context, String... keys) throws InspectorException
    {
        JsonNode json = getBody(context);

        if (!hasKeys(json, keys))
        {
            throw new InspectorException(App.getMessage("body.keys"));
        }

        return json;
    }

    public static boolean hasKeys(JsonNode json, String... keys)
    {
        int needed = keys.length;
        int found = 0;

        for (String key : keys)
        {
            if (json.has(key))
            {
                found++;
            }
        }

        return found == needed;
    }

    public static String getString(JsonNode body, String key)
    {
        String value = body.get(key).asText();
        return evalStr(value);
    }

    public static String evalStr(String value)
    {
        if (value != null)
        {
            String str = value.strip();
            int length = str.length();

            return length == 0 ? null : str;
        }

        return null;
    }
}
