package spear.models

import groovy.sql.GroovyRowResult

import java.time.LocalDateTime

import static spear.SpearHelper.toLocalDateTime

record User(int id,
            String username,
            String password,
            String firstName,
            String lastName,
            boolean enabled,
            LocalDateTime createdAt,
            LocalDateTime updatedAt)
{
    static User from(GroovyRowResult row)
    {
        return new User(
                row.id as int,
                row.username as String,
                row.password as String,
                row.first_name as String,
                row.last_name as String,
                row.enabled as boolean,
                toLocalDateTime(row.created_at as String),
                toLocalDateTime(row.updated_at as String)
        )
    }
}
