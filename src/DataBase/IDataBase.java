package DataBase;
import java.sql.Connection;

/**
 * The following interface is a general interface for any database connection (SQL or NoSQL).
 * In our application we use SQL (relational) database.
 */
public interface IDataBase {
    Connection getDBConnection();
    void closeDBConnection(Connection connection);
    int runSqlScript(String path);
}
