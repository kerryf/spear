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
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    private static final int MIN_PASSWORD_LENGTH = 8;
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
            if (!Inspector.checkMatches(username, EMAIL_REGEX, true))
            {
                String message = App.getMessage("email.format");
                throw new InspectorException(message);
            }

            String password = Inspector.getString(body, "password");
            if (!Inspector.checkLength(password, MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH, true))
            {
                String message = App.getMessage("password.length", MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
                throw new InspectorException(message);
            }

            Optional<String> result = LoginService.login(username, password);
            if (result.isEmpty())
            {
                throw new UnauthorizedResponse(App.getMessage("login.failed"));
            }

            context.json(result.get());
        }
    };
}
