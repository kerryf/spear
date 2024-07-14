package spear;

import com.auth0.jwt.algorithms.Algorithm;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spear.security.Pbkdf2PasswordEncoder;

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
 * Contains functionality to perform tasks on application start and stop.
 */
final class Lifecycle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Lifecycle.class);

    private Lifecycle()
    { /* Hidden */ }

    /**
     * Perform required tasks on application start.
     * <ul>
     *     <li>Create the application container</li>
     *     <li>Load configuration</li>
     *     <li>Set the locale</li>
     *     <li>Set the time zone</li>
     *     <li>Create the password encoder</li>
     *     <li>Prepare for token authentication</li>
     *     <li>Load the database connection pool</li>
     * </ul>
     *
     * @throws Exception if an error occurs performing a task
     */
    static void start() throws Exception
    {
        Instant start = Instant.now();
        LOGGER.info("Starting setup tasks...");

        Container container = Container.getInstance();

        Properties config = loadConfig();
        container.put("spear.config", config);
        LOGGER.info("Loaded configuration");

        setLocale(config);
        LOGGER.info("Set locale to {}", Locale.getDefault().toString());

        setTimeZone(config);
        LOGGER.info("Set time zone to {}", TimeZone.getDefault().getDisplayName());

        ResourceBundle bundle = ResourceBundle.getBundle("lang/Messages");
        container.put("spear.bundle", bundle);
        LOGGER.info("Loaded message bundle");

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        container.put("spear.encoder", encoder);
        LOGGER.info("Created the password encoder");

        Algorithm algorithm = Algorithm.HMAC256(App.envStr("JWT_SECRET"));
        container.put("spear.algorithm", algorithm);
        LOGGER.info("Prepared for token authentication");

        HikariDataSource source = loadDataSource(config);
        container.put("spear.source", source);

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

        Locale locale = Locale.of(language, country);
        Locale.setDefault(locale);
    }

    private static void setTimeZone(Properties config)
    {
        String region = config.getProperty("region");

        ZoneId zoneId = ZoneId.of(region);
        TimeZone zone = TimeZone.getTimeZone(zoneId);
        TimeZone.setDefault(zone);
    }

    private static HikariDataSource loadDataSource(Properties config)
    {
        HikariConfig dbConfig = new HikariConfig();
        dbConfig.setJdbcUrl(config.getProperty("database.url"));
        dbConfig.setUsername(App.envStr("DB_USERNAME"));
        dbConfig.setPassword(App.envStr("DB_PASSWORD"));

        return new HikariDataSource(dbConfig);
    }

    /**
     * Perform tasks on application stop. Should be used to gracefully close resources.
     */
    static void stop()
    {
        Container container = Container.getInstance();
        if (container.get("spear.source") instanceof HikariDataSource dataSource)
        {
            dataSource.close();
            System.out.println("Database connection pool closed");
        }
    }
}
