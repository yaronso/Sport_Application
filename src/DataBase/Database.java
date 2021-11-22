package DataBase;

import javax.script.ScriptException;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database implements IDataBase {
    // Fields:
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private static Database instance;
    private Connection connection;
    private String[] propertiesArray;
    private static String DB_DRIVER;
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;



    // TODO - Add singleton design pattern
    private Database() throws IOException {
        this.propertiesArray = getJDBCProperties();
        DB_DRIVER = propertiesArray[0];
        DB_URL = propertiesArray[1];
        DB_USER = propertiesArray[2];
        DB_PASSWORD = propertiesArray[3];

    }


    // Implements the Singleton DP.
    public static Database getInstance() throws SQLException, IOException {
        if (instance == null) {
            instance = new Database();
        } else if (instance.getConnection().isClosed()) {
            instance = new Database();
        }
        return instance;
    }

    private Connection getConnection() {
        return this.connection;
    }

    /**
     * Getter for the DB connection for the main application class.
     * @return
     */
    public Connection getDBConnection() {
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


    public void closeDBConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public String[] getJDBCProperties() throws IOException {
        String[] propertiesArray = new String[5];
        Properties props = new Properties();
        String dbSettingsPropertyFile = "src/Config/jdbc.properties";
        FileReader fReader = new FileReader(dbSettingsPropertyFile);
        props.load(fReader);
        propertiesArray[0] = props.getProperty("db.driver.class");
        propertiesArray[1] = props.getProperty("db.conn.url");
        propertiesArray[2] = props.getProperty("db.username");
        propertiesArray[3] = props.getProperty("db.password");
        return propertiesArray;
    }


    /**
     * Set up the Database by triggering sql scripts.
     */
    public void dataBaseConfig(Database db) {
        try {
            String CREATE_DB = "src/SqlUtils/createDB.sql";
            int rc = db.runSqlScript(CREATE_DB);
            if (rc == 0) {
                System.out.println("DB yaron_db was created");
            } else {
                System.exit(-1);
            }
            String CREATE_USERS = "src/SqlUtils/createUsersTable.sql";
            rc = db.runSqlScript(CREATE_USERS);
            if (rc == 0) {
                System.out.println("Users Table inside yaron_db was created");
            }
            String CREATE_GAME_TABLES = "src/SqlUtils/newCreateGamesTables.sql";
            rc = db.runSqlScript(CREATE_GAME_TABLES);
            if(rc == 0) {
                System.out.println("user_games Table inside yaron_db was created");
            }
            db.loadDataSet(); // Load the CSV file.
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
            while((string = br.readLine()) != null) {
                sb.append(string);
            }
            br.close();
            String[] inst = sb.toString().split(";");
            Connection c = getDBConnection();
            Statement st = c.createStatement();
            for (String s : inst) {
                if (!s.trim().equals("")) {
                    st.executeUpdate(s);
                    System.out.println(">>" + s);
                }
            }
        }
        catch(Exception e) {
            rc = -1;
            System.out.println("*** Error : " + e.toString());
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sb.toString());
        }
        return rc;
    }

    public void loadDataSet() throws ScriptException, IOException, InterruptedException {
        createCostumeCsv();

        System.out.println("Loading the data set csv file...");
        String csvFilePath1 = ".\\src\\DataBase\\countries_data.csv";
        String csvFilePath2 = ".\\src\\DataBase\\removed.csv";

        String sql1 = "INSERT INTO countries (country_name, country_id) VALUES (?, ?)";
        String sql2 = "INSERT INTO cities (city_name, country_id) VALUES (?, ?)";


        insertDb(DB_URL,DB_USER,DB_PASSWORD,sql1,csvFilePath1,20 );
        insertDb(DB_URL,DB_USER,DB_PASSWORD,sql2,csvFilePath2,20 );


    }

    public void createCostumeCsv() throws ScriptException, IOException{
        try{
            ProcessBuilder builder = new ProcessBuilder("python", System.getProperty("user.dir") + "\\src\\DataBase\\script.py" );
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader readers = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String lines=null;

            while ((lines=reader.readLine())!=null){
                System.out.println("lines"+lines);
            }
            while ((lines=readers.readLine())!=null){
                System.out.println("Error lines: "+lines);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void insertDb(String jdbcURL, String username, String password, String insert_query, String file_path, int batchSize ){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(insert_query);
            BufferedReader lineReader = new BufferedReader(new FileReader(file_path));
            String lineText = null;
            int count = 0;
            lineReader.readLine(); // skip header line
            String column1=null ,column2=null;

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                System.out.println(data[0] + "," + data[1] );
                column1 = data[0];column2 = data[1];

                statement.setString(1, column1);
                statement.setString(2, column2);
                statement.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            // execute the remaining queries
            statement.executeBatch();

            connection.commit();
            connection.close();

        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}