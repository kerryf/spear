package spear.controllers;

public final class InspectorException extends Exception
{
    public InspectorException(String message)
    {
        super(message);
    }

    public InspectorException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
