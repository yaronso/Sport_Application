package DataBase;
import java.sql.Connection;
import java.sql.SQLException;

public interface IDataBase {
    public Connection getDBConnection() throws SQLException;
    public int runSqlScript(String path);
    public int runSqlScriptWithParam(String path);
}
