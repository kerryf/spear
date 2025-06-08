package spear

import groovy.util.logging.Slf4j
import spear.models.Role
import spear.services.RoleService
import spear.services.UserService

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.SQLException

/**
 * Contains functionality to perform configurable tasks on application start.
 */
@Slf4j
final class Bootstrap
{
    private Bootstrap()
    { /* Hidden */ }

    /**
     * Perform optional tasks on application start.
     * <ul>
     *     <li>Drop database tables</li>
     *     <li>Create database tables</li>
     *     <li>Load initial data</li>
     *     <li>Create system user</li>
     * </ul>
     *
     * @throws Exception if an error occurs performing a task
     */
    static void evaluate() throws Exception
    {
        dropTables()
        createTables()
        loadData()
        createUser()
    }

    private static void dropTables() throws IOException, SQLException
    {
        boolean drop = App.config.database.drop
        log.info('Drop tables? {}', drop)

        if (drop)
        {
            runScript('drop_tables.sql')
            log.info('Tables dropped')
        }
    }

    private static void createTables() throws IOException, SQLException
    {
        boolean create = App.config.database.create
        log.info('Create tables? {}', create)

        if (create)
        {
            runScript('create_tables.sql')
            log.info('Tables created')
        }
    }

    private static void loadData() throws IOException, SQLException
    {
        boolean load = App.config.database.load
        log.info('Load data? {}', load)

        if (load)
        {
            runScript('add_roles.sql')
            log.info('Data loaded')
        }
    }

    private static void createUser() throws SQLException
    {
        boolean user = App.config.database.user
        log.info('Create user? {}', user)

        if (user)
        {
            String password = App.config.spearUser.password
            String encoded = App.encoder.encode(password)

            String username = App.config.spearUser.username
            String firstName = App.config.spearUser.firstName
            String lastName = App.config.spearUser.lastName
            String roleName = App.config.spearUser.roleName

            Role role = RoleService.findRole(roleName).orElseThrow(() -> SpearException.build('role.notFound', roleName))
            int roleId = role.id()

            int userId = UserService.createUser(username, encoded, firstName, lastName)

            RoleService.addUserRole(userId, roleId)
            log.info('User {} created with role {}', username, roleName)
        }
    }

    private static void runScript(String file) throws IOException, SQLException
    {
        String script = readScript(file)
        App.sql.execute(script)
    }

    private static String readScript(String file) throws IOException
    {
        Path path = Paths.get('scripts', file)
        return Files.readString(path)
    }
}
