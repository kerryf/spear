package spear.services;

import com.auth0.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spear.App;
import spear.models.User;

import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public final class LoginService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private LoginService()
    { /* Hidden */ }

    public static Optional<String> login(String username, String password) throws SQLException
    {
        Optional<User> result = UserService.findUser(username);
        if (result.isEmpty())
        {
            LOGGER.debug("No account found with username {}", username);
            return Optional.empty();
        }

        User user = result.get();
        if (!user.enabled())
        {
            LOGGER.debug("User account for {} is disabled", username);
            return Optional.empty();
        }

        boolean valid = App.getEncoder().validate(user.password(), password);
        if (!valid)
        {
            LOGGER.debug("Invalid password for {}", username);
            return Optional.empty();
        }

        return Optional.of(generateToken(username));
    }

    private static String generateToken(String username)
    {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(App.cfgInt("jwt.lifespan"), ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer(App.cfgStr("jwt.issuer"))
                .withSubject(username)
                .withExpiresAt(expiresAt)
                .sign(App.getAlgorithm());
    }
}
