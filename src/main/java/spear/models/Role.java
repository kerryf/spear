package spear.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public record Role(int id,
                   String name,
                   String description,
                   Instant createdAt,
                   Instant updatedAt)
{
    public static Role from(ResultSet row) throws SQLException
    {
        return new Role(
                row.getInt("id"),
                row.getString("name"),
                row.getString("description"),
                row.getTimestamp("created_at").toInstant(),
                row.getTimestamp("updated_at").toInstant());
    }
}
