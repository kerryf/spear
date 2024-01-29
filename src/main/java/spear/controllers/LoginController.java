package spear.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;
import spear.services.LoginService;

import java.util.Optional;

public final class LoginController
{
    private LoginController()
    { /* Hidden */ }

    public static Handler login = new Handler()
    {
        @Override
        public void handle(@NotNull Context context) throws Exception
        {
            JsonNode body = Inspector.getBody(context, "username", "password");

            String username = Inspector.getString(body, "username");
            String password = Inspector.getString(body, "password");
            // TODO: validation

            Optional<String> result = LoginService.login(username, password);
            if (result.isEmpty())
            {
                throw new UnauthorizedResponse();
            }

            context.json(result.get());
        }
    };
}
