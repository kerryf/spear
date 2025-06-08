package spear

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

final class SpearHelper
{
    private static final String SQLITE_DATE_FORMAT = 'yyyy-MM-dd HH:mm:ss'
    private static final DateTimeFormatter SQLITE_DATE_FORMATTER = DateTimeFormatter.ofPattern(SQLITE_DATE_FORMAT)

    private SpearHelper()
    { /* Hidden */ }

    static LocalDateTime toLocalDateTime(String text)
    {
        if (!text)
        {
            return null
        }

        return SQLITE_DATE_FORMATTER.parse(text, LocalDateTime::from)
    }
}
