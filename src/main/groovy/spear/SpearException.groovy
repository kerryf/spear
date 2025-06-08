package spear

final class SpearException extends RuntimeException
{
    SpearException(String message)
    {
        super(message)
    }

    SpearException(String message, Throwable cause)
    {
        super(message, cause)
    }

    SpearException(Throwable cause)
    {
        super(cause)
    }

    static SpearException build(String key, Object... args)
    {
        String message = App.getMessage(key, args)
        return new SpearException(message)
    }
}
