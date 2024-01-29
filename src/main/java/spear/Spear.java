package spear;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spear.controllers.LoginController;
import spear.security.EndpointAccessManager;
import spear.security.EndpointRole;

import java.util.Map;

/**
 * Entry point to the {@code Spear} application.
 */
public final class Spear
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Spear.class);

    private Spear()
    { /* Hidden */ }

    /**
     * Starts the application.
     *
     * @param args command line arguments (not currently used)
     */
    public static void main(String[] args)
    {
        try
        {
            Javalin app = Javalin.create(config -> {
                config.accessManager(new EndpointAccessManager());
            });

            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(app::stop));

            app.events(listener -> {
                listener.serverStopped(Lifecycle::stop);
            });

            Lifecycle.start();
            Bootstrap.evaluate();

            String greeting = App.getMessage("spear.greeting");
            app.get("/", context -> context.json(Map.of("message", greeting)), EndpointRole.of("GUEST"));

            app.post("/spear/login", LoginController.login, EndpointRole.of("GUEST"));

            String security = App.getMessage("spear.security");
            app.get("/spear/secure", context -> context.json(Map.of("message", security)), EndpointRole.of("LOGGED_IN"));

            String host = App.envStr("API_HOST");
            int port = App.envInt("API_PORT");
            app.start(host, port);
        }
        catch (Exception e)
        {
            LOGGER.error("Error starting Spear", e);
            System.exit(1);
        }
    }
}
