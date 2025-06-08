package spear.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.http.Context
import spear.App

final class Inspector
{
    private Inspector()
    { /* Hidden */ }

    static JsonNode getBody(Context context) throws InspectorException
    {
        String body = context.body()
        if (body.isEmpty())
        {
            throw new InspectorException(App.getMessage('body.empty'))
        }

        try
        {
            return new ObjectMapper().readTree(body)
        }
        catch (JsonProcessingException e)
        {
            throw new InspectorException(App.getMessage('body.parse'), e)
        }
    }

    static JsonNode getBody(Context context, String... keys) throws InspectorException
    {
        JsonNode json = getBody(context)

        if (!hasKeys(json, keys))
        {
            throw new InspectorException(App.getMessage('body.keys'))
        }

        return json
    }

    static boolean hasKeys(JsonNode json, String... keys)
    {
        int needed = keys.length
        int found = 0

        for (String key : keys)
        {
            if (json.has(key))
            {
                found++
            }
        }

        return found == needed
    }

    static String getString(JsonNode body, String key)
    {
        String value = body.get(key).asText()
        return evalStr(value)
    }

    static String evalStr(String value)
    {
        if (value)
        {
            String str = value.strip()
            int length = str.length()

            return length == 0 ? null : str
        }

        return null
    }

    static boolean checkMatches(String value, String regex, boolean required)
    {
        if (value)
        {
            return value.matches(regex)
        }

        return !required
    }

    static boolean checkLength(String value, int min, int max, boolean required)
    {
        if (value)
        {
            int length = value.length()
            return length >= min && length <= max
        }

        return !required
    }
}
