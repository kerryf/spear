package spear.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spear.App;

import java.util.Set;

public final class EndpointHandler implements Handler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointHandler.class);

    private static final EndpointRole GUEST = EndpointRole.of("GUEST");
    private static final EndpointRole LOGGED_IN = EndpointRole.of("LOGGED_IN");

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_TYPE = "Bearer";
    private static final int TOKEN_INDEX = AUTH_TYPE.length() + 1;

    public EndpointHandler()
    { /* Default */ }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        Set<RouteRole> roles = context.routeRoles();

        if (roles.contains(GUEST))
        {
            return;
        }

        if (roles.contains(LOGGED_IN))
        {
            boolean valid = checkToken(context);
            if (!valid)
            {
                throw new ForbiddenResponse();
            }
        }
        else
        {
            throw new UnauthorizedResponse();
        }
    }

    private static boolean checkToken(Context context)
    {
        boolean valid = false;

        String value = context.header(AUTH_HEADER);
        if (value != null && value.startsWith(AUTH_TYPE))
        {
            String token = value.substring(TOKEN_INDEX);

            JWTVerifier verifier = JWT.require(App.getAlgorithm())
                    .withIssuer(App.cfgStr("jwt.issuer"))
                    .build();

            try
            {
                DecodedJWT decoded = verifier.verify(token);
                LOGGER.debug("Valid Token: {}", decoded.getSubject());

                valid = true;
            }
            catch (JWTVerificationException e)
            {
                LOGGER.debug("Invalid Token: {}", e.getMessage());
            }
        }

        return valid;
    }
}
