package spear;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spear.models.Role;
import spear.services.RoleService;
import spear.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Contains functionality to perform configurable tasks on application start.
 */
final class Bootstrap
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private Bootstrap()
    { /* Hidden */ }

    /**
     * Perform optional tasks on application start.
     * <ul>
     *     <li>Drop the database</li>
     *     <li>Create the database, tables, and default user</li>
     * </ul>
     *
     * @throws Exception if an error occurs performing a task
     */
    static void evaluate() throws Exception
    {
        dropTables();
        createTables();
    }

    private static void dropTables() throws IOException, SQLException
    {
        boolean drop = App.cfgBool("database.drop");
        if (drop)
        {
            String sql = readScript("drop_tables.sql");

            try (Connection connection = App.getConnection();
                 Statement statement = connection.createStatement())
            {
                statement.execute(sql);
            }
        }
    }

    private static void createTables() throws IOException, SQLException
    {
        boolean create = App.cfgBool("database.create");
        if (create)
        {
            String sql = readScript("create_tables.sql");

            try (Connection connection = App.getConnection();
                 Statement statement = connection.createStatement())
            {
                statement.execute(sql);
            }

            addRoles();
            createUser();
        }
    }

    private static void addRoles() throws IOException, SQLException
    {
        String sql = readScript("add_roles.sql");

        try (Connection connection = App.getConnection();
             Statement statement = connection.createStatement())
        {
            statement.execute(sql);
        }
    }

    private static void createUser() throws SQLException
    {
        String username = App.cfgStr("spear.username");
        String password = App.cfgStr("spear.password");
        String encoded = App.getEncoder().encode(password);

        int userId = UserService.createUserWithRole(username, encoded, "SPEAR_MASTER");
        LOGGER.debug("Created master user {} with ID {}", username, userId);
    }

    private static String readScript(String file) throws IOException
    {
        Path path = Paths.get("scripts", file);
        return Files.readString(path);
    }
}
