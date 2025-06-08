package spear.security

import groovy.util.logging.Slf4j
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.security.RouteRole
import org.jetbrains.annotations.NotNull

@Slf4j
final class EndpointHandler implements Handler
{
    private static final EndpointRole GUEST = EndpointRole.of('GUEST')

    private static final String AUTH_HEADER = 'Authorization'
    private static final String AUTH_TYPE = 'Bearer'
    private static final int TOKEN_INDEX = AUTH_TYPE.length() + 1

    EndpointHandler()
    { /* Default */ }

    @Override
    void handle(@NotNull Context context) throws Exception
    {
        Set<RouteRole> roles = context.routeRoles()

        if (roles.contains(GUEST))
        {
            return
        }

        // TODO: check roles
    }
}
