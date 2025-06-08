package spear.services

import groovy.util.logging.Slf4j
import spear.App
import spear.models.User

import java.sql.SQLException

@Slf4j
final class LoginService
{
    private LoginService()
    { /* Hidden */ }

    static boolean login(String username, String password) throws SQLException
    {
        Optional<User> result = UserService.findUser(username)
        if (result.isEmpty())
        {
            log.debug('No account found with username {}', username)
            return false
        }

        User user = result.get()
        if (!user.enabled())
        {
            log.debug('User account for {} is disabled', username)
            return false
        }

        boolean valid = App.getEncoder().validate(user.password(), password)
        if (!valid)
        {
            log.debug('Invalid password for {}', username)
            return false
        }

        return true
    }
}
