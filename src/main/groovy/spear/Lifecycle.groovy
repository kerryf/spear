package spear

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import groovy.util.logging.Slf4j
import spear.security.Pbkdf2PasswordEncoder

import java.time.Duration
import java.time.Instant
import java.time.ZoneId

/**
 * Contains functionality to perform tasks on application start and stop.
 */
@Slf4j
final class Lifecycle
{
    private Lifecycle()
    { /* Hidden */ }

    /**
     * Perform required tasks on application start.
     * <ul>
     *     <li>Create the application container</li>
     *     <li>Load configuration</li>
     *     <li>Set the locale</li>
     *     <li>Set the timezone</li>
     *     <li>Create the password encoder</li>
     *     <li>Create database resources</li>
     * </ul>
     *
     * @throws Exception if an error occurs performing a task
     */
    static void start() throws Exception
    {
        Instant start = Instant.now()
        log.info('Starting setup tasks...')

        Container container = Container.getInstance()

        ConfigObject config = loadConfig()
        container.put('spear.config', config)
        log.info('Loaded configuration')

        setLocale(config)
        log.info('Set locale to {}', Locale.getDefault().toString())

        setTimeZone(config)
        log.info('Set time zone to {}', TimeZone.getDefault().getDisplayName())

        ResourceBundle bundle = ResourceBundle.getBundle('lang/Messages')
        container.put('spear.bundle', bundle)
        log.info('Loaded message bundle')

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder()
        container.put('spear.encoder', encoder)
        log.info('Created the password encoder')

        HikariDataSource dataSource = loadDataSource(config)
        container.put('spear.datasource', dataSource)

        Instant end = Instant.now()
        Duration duration = Duration.between(start, end)
        log.info('Setup tasks completed in {}ms', duration.toMillis())
    }

    private static ConfigObject loadConfig()
    {
        URL location = new File('conf/SpearConf.groovy').toURI().toURL()
        return new ConfigSlurper().parse(location)
    }

    private static void setLocale(ConfigObject config)
    {
        String language = config.locale.language
        String country = config.locale.country

        Locale locale = Locale.of(language, country)
        Locale.setDefault(locale)
    }

    private static void setTimeZone(ConfigObject config)
    {
        String region = config.timezone.region

        ZoneId zoneId = ZoneId.of(region)
        TimeZone zone = TimeZone.getTimeZone(zoneId)
        TimeZone.setDefault(zone)
    }

    private static HikariDataSource loadDataSource(ConfigObject config)
    {
        String url = config.database.url

        HikariConfig dbConfig = new HikariConfig()
        dbConfig.setJdbcUrl(url)

        return new HikariDataSource(dbConfig)
    }

    /**
     * Perform tasks on application stop. Should be used to gracefully close resources.
     */
    static void stop()
    {
        Container container = Container.getInstance()

        def pool = container.get('spear.datasource')
        if (pool instanceof HikariDataSource dataSource)
        {
            dataSource.close()
            System.out.println('Database connection pool closed')
        }
    }
}
