package spear.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public record User(int id,
                   String username,
                   String password,
                   boolean enabled,
                   Instant createdAt,
                   Instant updatedAt)
{
    public static User from(ResultSet row) throws SQLException
    {
        return new User(
                row.getInt("id"),
                row.getString("username"),
                row.getString("password"),
                row.getBoolean("enabled"),
                row.getTimestamp("created_at").toInstant(),
                row.getTimestamp("updated_at").toInstant());
    }
}
