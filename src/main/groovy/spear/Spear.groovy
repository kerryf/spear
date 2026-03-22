package spear

import groovy.util.logging.Slf4j
import io.javalin.Javalin
import io.javalin.http.BadRequestResponse
import io.javalin.http.InternalServerErrorResponse
import spear.controllers.InspectorException
import spear.controllers.LoginController
import spear.security.EndpointHandler
import spear.security.EndpointRole

import java.sql.SQLException

@Slf4j
final class Spear
{
    private Spear()
    { /* Hidden */ }

    static void main(String[] args)
    {
        try
        {
            Javalin app = Javalin.create(config -> {
                // Security
                config.routes.beforeMatched(new EndpointHandler())

                // Routes
                config.routes.get('/', context -> context.json(Map.of('message', 'Hello World!')), EndpointRole.of('guest'))
                config.routes.post('/spear/login', LoginController.login, EndpointRole.of('guest'))

                // Exceptions
                config.routes.exception(InspectorException.class, (e, context) -> {
                    log.debug('Validation Error', e)
                    throw new BadRequestResponse(e.getMessage())
                })

                config.routes.exception(SQLException.class, (e, context) -> {
                    log.error('Database Error', e)
                    throw new InternalServerErrorResponse(e.getMessage())
                })

                // Lifecycle
                config.events.serverStopped(listener -> { Lifecycle.stop() })
            })

            Runtime.getRuntime().addShutdownHook(Thread.ofPlatform().unstarted(app::stop))

            Lifecycle.start()
            Bootstrap.evaluate()

            app.start('0.0.0.0', 8081)
        }
        catch (Exception e)
        {
            log.error('Error starting Spear', e)
            System.exit(1)
        }
    }
}
