package spear;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Contains functionality to perform application lifecycle tasks.
 */
final class Lifecycle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Lifecycle.class);

    private Lifecycle() { /* Hidden */ }

    /**
     * Perform tasks on application start.
     * <ol>
     *     <li>Load application configuration</li>
     *     <li>Set the locale</li>
     *     <li>Set the time zone</li>
     *     <li>Create and populate the application container</li>
     * </ol>
     *
     * @throws Exception if an error occurs performing a task
     */
    static void start() throws Exception
    {
        Instant start = Instant.now();

        LOGGER.info("Starting setup tasks...");

        Properties config = loadConfig();
        LOGGER.info("Loaded configuration");

        setLocale(config);
        LOGGER.info("Set locale to {}", Locale.getDefault().toString());

        setTimeZone(config);
        LOGGER.info("Set time zone to {}", TimeZone.getDefault().getDisplayName());

        ResourceBundle bundle = ResourceBundle.getBundle("lang/Messages");
        LOGGER.info("Loaded message bundle");

        Container container = Container.getInstance();
        container.put("spear.config", config);
        container.put("spear.bundle", bundle);

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        LOGGER.info("Setup tasks completed in {}ms", duration.toMillis());
    }

    private static Properties loadConfig() throws IOException
    {
        try (InputStream stream = new FileInputStream("conf/Spear.properties"))
        {
            Properties config = new Properties();
            config.load(stream);

            return config;
        }
    }

    private static void setLocale(Properties config)
    {
        String language = config.getProperty("language");
        String country = config.getProperty("country");

        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
    }

    private static void setTimeZone(Properties config)
    {
        String region = config.getProperty("region");

        ZoneId zoneId = ZoneId.of(region);
        TimeZone zone = TimeZone.getTimeZone(zoneId);
        TimeZone.setDefault(zone);
    }

    /**
     * Perform tasks on application stop. Should be used to gracefully close resources.
     */
    static void stop()
    {
        System.out.println("Shutdown tasks completed");
    }
}
