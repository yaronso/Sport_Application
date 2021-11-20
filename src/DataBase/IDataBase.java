package DataBase;
import java.sql.Connection;
import java.sql.SQLException;

public interface IDataBase {
    Connection getDBConnection();
    void closeDBConnection(Connection connection);
    int runSqlScript(String path);
}
