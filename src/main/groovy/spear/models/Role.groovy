package spear.models

import groovy.sql.GroovyRowResult

import java.time.LocalDateTime

import static spear.SpearHelper.toLocalDateTime

record Role(int id,
            String name,
            LocalDateTime createdAt,
            LocalDateTime updatedAt)
{
    static Role from(GroovyRowResult row)
    {
        return new Role(
                row.id as int,
                row.name as String,
                toLocalDateTime(row.created_at as String),
                toLocalDateTime(row.updated_at as String))
    }
}
