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
            Javalin app = Javalin.create()

            Runtime.getRuntime().addShutdownHook(Thread.ofPlatform().unstarted(app::stop))

            app.events(listener -> {
                listener.serverStopped(Lifecycle::stop)
            })

            Lifecycle.start()
            Bootstrap.evaluate()

            app.beforeMatched(new EndpointHandler())

            String greeting = App.getMessage('spear.greeting')
            app.get('/', context -> context.json(Map.of('message', greeting)), EndpointRole.of('guest'))

            app.post('/spear/login', LoginController.login, EndpointRole.of('guest'))

            app.exception(InspectorException.class, (e, context) -> {
                log.debug('Validation Error', e)
                throw new BadRequestResponse(e.getMessage())
            })

            app.exception(SQLException.class, (e, context) -> {
                log.error('Database Error', e)
                throw new InternalServerErrorResponse(e.getMessage())
            })

            String host = App.envStr('API_HOST')
            int port = App.envInt('API_PORT')
            app.start(host, port)
        }
        catch (Exception e)
        {
            log.error('Error starting Spear', e)
            System.exit(1)
        }
    }
}
