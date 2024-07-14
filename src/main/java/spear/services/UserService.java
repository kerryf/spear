package spear.services;

import spear.App;
import spear.models.Role;
import spear.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public final class UserService
{
    private static final String GET_USER_SQL = """
            SELECT *
            FROM users
            WHERE id = ?
            """;

    private static final String FIND_USER_SQL = """
            SELECT *
            FROM users
            WHERE username = ?
            """;

    private static final String CREATE_USER_SQL = """
            INSERT INTO users
            (username, password, enabled, created_at, updated_at)
            VALUES
            (?, ?, 1, ?, ?)
            """;

    private UserService()
    { /* Hidden */ }

    public static Optional<User> getUser(int id) throws SQLException
    {
        try (Connection connection = App.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_SQL))
        {
            statement.setInt(1, id);

            try (ResultSet results = statement.executeQuery())
            {
                if (results.next())
                {
                    User user = User.from(results);
                    return Optional.of(user);
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<User> findUser(String username) throws SQLException
    {
        try (Connection connection = App.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_SQL))
        {
            statement.setString(1, username);

            try (ResultSet results = statement.executeQuery())
            {
                if (results.next())
                {
                    User user = User.from(results);
                    return Optional.of(user);
                }
            }
        }

        return Optional.empty();
    }

    public static int createUser(String username, String password) throws SQLException
    {
        Instant now = Instant.now();

        try (Connection connection = App.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setTimestamp(3, Timestamp.from(now));
            statement.setTimestamp(4, Timestamp.from(now));

            int count = statement.executeUpdate();
            if (count != 1)
            {
                throw new SQLException(App.getMessage("sql.noRow", "users"));
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys())
            {
                if (generatedKeys.next())
                {
                    return generatedKeys.getInt(1);
                }
                else
                {
                    throw new SQLException(App.getMessage("sql.noKey", "users"));
                }
            }
        }
    }

    public static int createUserWithRole(String username, String password, String roleName) throws SQLException
    {
        Optional<Role> optional = RoleService.findRole(roleName);
        Role role = optional.orElseThrow(() -> new SQLException(App.getMessage("sql.notFound", "role", roleName)));

        int userId = createUser(username, password);
        RoleService.addUserRole(userId, role.id());

        return userId;
    }
}
