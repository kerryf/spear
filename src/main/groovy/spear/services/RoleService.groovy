package spear.services

import spear.App
import spear.models.Role

import java.sql.SQLException

final class RoleService
{
    private static final String GET_ROLE = """
            SELECT * FROM roles WHERE id = ?
            """

    private static final String FIND_ROLE = """
            SELECT * FROM roles WHERE name = ?
            """

    private static final String ADD_USER_ROLE = """
            INSERT INTO user_role (user_id, role_id) VALUES (?, ?)
            """

    private RoleService()
    { /* Hidden */ }

    static Optional<Role> getRole(int id) throws SQLException
    {
        def row = App.sql.firstRow(GET_ROLE, [id])
        if (row)
        {
            Role role = Role.from(row)
            return Optional.of(role)
        }

        return Optional.empty()
    }

    static Optional<Role> findRole(String name) throws SQLException
    {
        def row = App.sql.firstRow(FIND_ROLE, [name])
        if (row)
        {
            Role role = Role.from(row)
            return Optional.of(role)
        }

        return Optional.empty()
    }

    static void addUserRole(int userId, int roleId) throws SQLException
    {
        App.sql.execute(ADD_USER_ROLE, [userId, roleId])
    }
}
