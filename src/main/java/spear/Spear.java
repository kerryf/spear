package spear;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spear.controllers.InspectorException;
import spear.controllers.LoginController;
import spear.security.EndpointHandler;
import spear.security.EndpointRole;

import java.sql.SQLException;
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
                config.useVirtualThreads = true;
            });

            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(app::stop));

            app.events(listener -> {
                listener.serverStopped(Lifecycle::stop);
            });

            Lifecycle.start();
            Bootstrap.evaluate();

            app.beforeMatched(new EndpointHandler());

            String greeting = App.getMessage("spear.greeting");
            app.get("/", context -> context.json(Map.of("message", greeting)), EndpointRole.of("GUEST"));

            app.post("/spear/login", LoginController.login, EndpointRole.of("GUEST"));

            String security = App.getMessage("spear.security");
            app.get("/spear/secure", context -> context.json(Map.of("message", security)), EndpointRole.of("LOGGED_IN"));

            app.exception(InspectorException.class, (e, context) -> {
                LOGGER.debug("Validation Error", e);
                throw new BadRequestResponse(e.getMessage());
            });

            app.exception(SQLException.class, (e, context) -> {
                LOGGER.error("Database Error", e);
                throw new InternalServerErrorResponse(e.getMessage());
            });

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
