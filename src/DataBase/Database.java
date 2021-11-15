package DataBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database implements IDataBase {
    // Fields
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://localhost/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "ArchiveYsso6495";
    private static final String DB_NAME = "yaron_db";
    private static final String CREATE_DB = "src/SqlUtils/createDB.sql";
    private static final String CREATE_USERS = "src/SqlUtils/createUsersTable.sql";
    private static final String CREATE_GAME_TABLES = "src/SqlUtils/newCreateGamesTables.sql";


    // TODO - Add singleton design pattern
    public Database() {}

    /**
     * Getter for the DB connection.
     * @return
     * @throws SQLException
     */
    public Connection getDBConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }
        return connection;
    }

    /**
     * Set up the Database by triggering sql scripts.
     */
    public void dataBaseConfig() {
        IDataBase db = new Database(); // Dependency Injection
        try {
            int rc = db.runSqlScript(CREATE_DB);
            if (rc == 0) {
                System.out.println("DB yaron_db was created");
            } else {
                System.exit(-1);
            }
            rc = db.runSqlScriptWithParam(CREATE_USERS);
            if (rc == 0) {
                System.out.println("Users Table inside yaron_db was created");
            }
            rc = db.runSqlScript(CREATE_GAME_TABLES);
            if(rc == 0) {
                System.out.println("user_games Table inside yaron_db was created");
            }
            boolean isDataSetLoaded = false;
            if (!isDataSetLoaded) { // Load the CSV file
                isDataSetLoaded = true;
                ((Database) db).loadDataSet();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public int runSqlScript(String path) {
        String string;
        StringBuffer sb = new StringBuffer();
        int rc = 0;
        try
        {
            FileReader fr = new FileReader(new File(path));
            BufferedReader br = new BufferedReader(fr);
            while((string = br.readLine()) != null)
            {
                sb.append(string);
            }
            br.close();

            String[] inst = sb.toString().split(";");
            Connection c = getDBConnection();
            Statement st = c.createStatement();

            for(int i = 0; i<inst.length; i++)
            {
                if(!inst[i].trim().equals(""))
                {
                    st.executeUpdate(inst[i]);
                    System.out.println(">>"+inst[i]);
                }
            }
        }
        catch(Exception e)
        {
            rc = -1;
            System.out.println("*** Error : "+ e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sb.toString());
        }
        return rc;
    }



    @Override
    public int runSqlScriptWithParam(String path) {
        DB_URL = DB_URL + DB_NAME;
        String string;
        StringBuffer sb = new StringBuffer();
        int rc = 0;
        try
        {
            FileReader fr = new FileReader(new File(path));
            BufferedReader br = new BufferedReader(fr);
            while((string = br.readLine()) != null)
            {
                sb.append(string);
            }
            br.close();

            String[] inst = sb.toString().split(";");
            Connection c = getDBConnection();
            Statement st = c.createStatement();

            for(int i = 0; i<inst.length; i++)
            {
                if(!inst[i].trim().equals(""))
                {
                    st.executeUpdate(inst[i]);
                    System.out.println(">> "+inst[i]);
                }
            }
        }
        catch(Exception e)
        {
            rc = -1;
            System.out.println("*** Error occurred: "+ e.toString());
            System.out.println(sb.toString());
        }
        return rc;
    }


    public void loadDataSet() {
        System.out.println("Loading the data set csv file...");
    }

}