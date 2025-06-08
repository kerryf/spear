package spear.controllers

final class InspectorException extends Exception
{
    InspectorException(String message)
    {
        super(message)
    }

    InspectorException(String message, Throwable cause)
    {
        super(message, cause)
    }
}
