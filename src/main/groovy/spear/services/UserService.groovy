package spear.services

import spear.App
import spear.models.User

import java.sql.SQLException

final class UserService
{
    private static final String GET_USER = """
            SELECT * FROM users WHERE id = ?
            """

    private static final String FIND_USER = """
            SELECT * FROM users WHERE username = ?
            """

    private static final String CREATE_USER = """
            INSERT INTO users
            (username, password, first_name, last_name, enabled, created_at, updated_at)
            VALUES
            (?, ?, ?, ?, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """

    private UserService()
    { /* Hidden */ }

    static Optional<User> getUser(int id) throws SQLException
    {
        def row = App.sql.firstRow(GET_USER, [id])
        if (row)
        {
            User user = User.from(row)
            return Optional.of(user)
        }

        return Optional.empty()
    }

    static Optional<User> findUser(String username) throws SQLException
    {
        def row = App.sql.firstRow(FIND_USER, [username])
        if (row)
        {
            User user = User.from(row)
            return Optional.of(user)
        }

        return Optional.empty()
    }

    static int createUser(String username, String password, String firstName, String lastName) throws SQLException
    {
        def result = App.sql.executeInsert(CREATE_USER, [username, password, firstName, lastName])
        def keys = result[0]

        return keys[0] as int
    }
}
