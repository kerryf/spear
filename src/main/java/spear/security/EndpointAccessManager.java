package spear.security;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EndpointAccessManager implements AccessManager
{
    private static final EndpointRole GUEST = EndpointRole.of("GUEST");
    private static final EndpointRole LOGGED_IN = EndpointRole.of("LOGGED_IN");

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<? extends RouteRole> set) throws Exception
    {
        if (set.contains(GUEST))
        {
            handler.handle(context);
            return;
        }

        if (set.contains(LOGGED_IN))
        {

        }
    }
}
