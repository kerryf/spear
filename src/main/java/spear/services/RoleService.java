package spear.services;

import spear.App;
import spear.models.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class RoleService
{
    private static final String GET_ROLE_SQL = """
            SELECT *
            FROM roles
            WHERE id = ?
            """;

    private static final String FIND_ROLE_SQL = """
            SELECT *
            FROM roles
            WHERE name = ?
            """;

    private static final String ADD_USER_ROLE_SQL = """
            INSERT INTO user_roles
            (user_id, role_id)
            VALUES
            (?, ?)
            """;

    private RoleService()
    { /* Hidden */ }

    public static Optional<Role> getRole(int id) throws SQLException
    {
        try (Connection connection = App.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ROLE_SQL))
        {
            statement.setInt(1, id);

            try (ResultSet results = statement.executeQuery())
            {
                if (results.next())
                {
                    Role role = Role.from(results);
                    return Optional.of(role);
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<Role> findRole(String name) throws SQLException
    {
        try (Connection connection = App.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ROLE_SQL))
        {
            statement.setString(1, name);

            try (ResultSet results = statement.executeQuery())
            {
                if (results.next())
                {
                    Role role = Role.from(results);
                    return Optional.of(role);
                }
            }
        }

        return Optional.empty();
    }

    public static void addUserRole(int userId, int roleId) throws SQLException
    {
        try (Connection connection = App.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_USER_ROLE_SQL))
        {
            statement.setInt(1, userId);
            statement.setInt(2, roleId);

            int count = statement.executeUpdate();
            if (count != 1)
            {
                throw new SQLException(App.getMessage("sql.noRow", "user_roles"));
            }
        }
    }
}
