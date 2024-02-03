package spear.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;
import spear.App;
import spear.services.LoginService;

import java.util.Optional;

public final class LoginController
{
    private static final String USERNAME_REGEX = "[a-zA-Z0-9_-]{4,24}";
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 64;

    private LoginController()
    { /* Hidden */ }

    public static final Handler login = new Handler()
    {
        @Override
        public void handle(@NotNull Context context) throws Exception
        {
            JsonNode body = Inspector.getBody(context, "username", "password");

            String username = Inspector.getString(body, "username");
            if (!Inspector.checkMatches(username, USERNAME_REGEX, true))
            {
                String message = App.getMessage("credential.invalid", "username", "format");
                throw new InspectorException(message);
            }

            String password = Inspector.getString(body, "password");
            if (!Inspector.checkLength(password, MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH, true))
            {
                String message = App.getMessage("credential.invalid", "password", "length");
                throw new InspectorException(message);
            }

            Optional<String> result = LoginService.login(username, password);
            if (result.isEmpty())
            {
                throw new UnauthorizedResponse();
            }

            context.json(result.get());
        }
    };
}
