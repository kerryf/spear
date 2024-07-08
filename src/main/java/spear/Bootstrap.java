package spear;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spear.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
     *     <li>Create database table</li>
     *     <li>Create an API user</li>
     * </ul>
     *
     * @throws Exception if an error occurs performing a task
     */
    static void evaluate() throws Exception
    {
        createTable();
        createUser();
    }

    private static void createTable() throws IOException, SQLException
    {
        boolean createTables = App.cfgBool("table.create");
        if (createTables)
        {
            String sql = readScript("create_tables.sql");

            try (Connection connection = App.getConnection();
                 Statement statement = connection.createStatement())
            {
                statement.execute(sql);
            }
        }
    }

    private static String readScript(String file) throws IOException
    {
        Path path = Paths.get("scripts", file);
        return Files.readString(path);
    }

    private static void createUser() throws SQLException
    {
        boolean createUser = App.cfgBool("user.create");
        if (createUser)
        {
            String username = App.cfgStr("user.username");
            String password = App.cfgStr("user.password");
            String encoded = App.getEncoder().encode(password);

            UserService.createUser(username, encoded);
            LOGGER.debug("Created API user {}", username);
        }
    }
}
