package Dao;

import Models.Game;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The following class is a concrete implementation of the interface GameDao.
 * This class access the database by using the JDBC API of Java.
 */
public class GameDaoImpl implements GameDao {
    // Fields:
    private final String DB_DRIVER;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;
    private final String DB_NAME;
    // Sql Queries:
    // Insert statements:
    private static final String INSERT_GAME_DETAILS = "INSERT IGNORE INTO game_details(user_name, game_name, game_date, creation_date, city, sport_category, players, level) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_MATCH_GAMES = "INSERT IGNORE INTO match_games(user_name, game_name, creation_date, participant) VALUES(?, ?, ?, ?)";
    private static final String DOES_MATCH_EXIST = "SELECT * FROM match_games WHERE user_name = ? and game_name = ? and creation_date = ? and participant = ?";
    private static final String INSERT_SELECT_ZERO_GAME = "INSERT IGNORE INTO game_details(user_name, game_name, game_date, creation_date, city, sport_category, players, level) " +
            "SELECT user_name, game_name, game_date, creation_date, city, sport_category, players + 1, level " +
            " FROM game_details_archives WHERE game_name=?";

    // SET FOREIGN_KEY_CHECKS Statement:
    private static final String SET_FOREIGN_KEY_CHECKS = "SET FOREIGN_KEY_CHECKS = ?";

    // Delete statement:
    private static final String DELETE_GAME =  "DELETE FROM game_details WHERE game_name = ? AND creation_date = ?";
    private static final String DELETE_FROM_MATCH_TABLE = "DELETE FROM match_games WHERE participant=? AND game_name=?";
    private static final String DELETE_GAME_FROM_ALL_MATCH_TABLES = "DELETE FROM match_games WHERE game_name = ? AND creation_date = ?";

    // Select statements:
    private static final String FIND_NUM_OF_PLAYERS_GAME = "SELECT players FROM game_details WHERE game_name = ?";
    private static final String IS_COUNTRY_EXISTS = "SELECT country_name FROM countries WHERE country_name = ?";
    private static final String IS_CITY_EXISTS = "SELECT city_name FROM cities WHERE city_name = ?";

    private static final String FIND_ALL_GAMES = "SELECT distinct t1.user_name, t1.game_name, t1.sport_category, t3.country_name, t1.city, t1.game_date, t1.players, t1.level, t1.creation_date\n" +
            "         FROM game_details as t1\n" +
            "         JOIN cities as t2\n" +
            "         ON t1.city = t2.city_name\n" +
            "         JOIN countries as t3\n" +
            "         ON t2.country_id = t3.country_id\n" +
            "         ORDER BY unix_timestamp(t1.game_date)";


    private static final String GET_ALL_GAME_MATCHES = "SELECT user_name, game_name, creation_date, participant FROM match_games WHERE participant = ?";

    private static final String JOIN_QUERY_GAME_NAME = "SELECT distinct t1.user_name, t1.game_name, t1.sport_category, t3.country_name, t1.city, t1.game_date, t1.players, t1.level, t1.creation_date\n" +
            "         FROM game_details as t1\n" +
            "         JOIN cities as t2\n" +
            "         ON t1.city = t2.city_name\n" +
            "         JOIN countries as t3\n" +
            "         ON t2.country_id = t3.country_id\n" +
            "         WHERE t1.game_name=?\n" +
            "         ORDER BY unix_timestamp(t1.game_date)";


    private static final String JOIN_QUERY_CITY = "SELECT distinct t1.user_name, t1.game_name, t1.sport_category, t3.country_name, t1.city, t1.game_date, t1.players, t1.level, t1.creation_date\n" +
            "         FROM game_details as t1\n" +
            "         JOIN cities as t2\n" +
            "         ON t1.city = t2.city_name\n" +
            "         JOIN countries as t3\n" +
            "         ON t2.country_id = t3.country_id\n" +
            "         WHERE t2.city_name=?\n" +
            "         ORDER BY unix_timestamp(t1.game_date)";


    private static final String JOIN_QUERY_COUNTRY = "SELECT distinct t1.user_name, t1.game_name, t1.sport_category, t3.country_name, t1.city, t1.game_date, t1.players, t1.level, t1.creation_date\n" +
            "         FROM game_details as t1\n" +
            "         JOIN cities as t2\n" +
            "         ON t1.city = t2.city_name\n" +
            "         JOIN countries as t3\n" +
            "         ON t2.country_id = t3.country_id\n" +
            "         WHERE t3.country_name=?\n" +
            "         ORDER BY unix_timestamp(t1.game_date)";



    private static final String MAX_LEVEL_GROUP_BY_CATEGORY = "SELECT distinct t1.user_name, t1.game_name, t1.sport_category, t3.country_name, t1.city, t1.game_date, t1.players, t1.level, t1.creation_date\n" +
            "         FROM game_details as t1\n" +
            "         JOIN cities as t2\n" +
            "         ON t1.city = t2.city_name\n" +
            "         JOIN countries as t3\n" +
            "         ON t2.country_id = t3.country_id\n" +
            "         WHERE t1.sport_category = ? \n" +
            "         GROUP BY t1.game_name, t1.sport_category, t1.level \n" +
            "         ORDER BY max(t1.level) DESC \n" +
            "         LIMIT 10;";

    // find the country where sport ? is most played.
    private static final String MOST_PLAYED_SPORT_COUNTRY = "SELECT t3.country_name\n" +
            "         FROM game_details as t1\n" +
            "         JOIN cities as t2\n" +
            "         ON t1.city = t2.city_name\n" +
            "         JOIN countries as t3\n" +
            "         ON t2.country_id = t3.country_id\n" +
            "         WHERE t1.sport_category = ? \n" +
            "GROUP BY t3.country_name\n" +
            "ORDER BY COUNT(*) DESC\n" +
            "LIMIT 1";

    // Find the most played sport in the current month (statistics).
    private static final String  MOST_PLAYED_SPORT_OF_MONTH = "SELECT distinct t1.sport_category " +
            "FROM game_details as t1 " +
            "WHERE month(t1.game_date) = (SELECT MONTH(CURDATE())) " +
            "GROUP BY t1.sport_category " +
            "ORDER BY COUNT(*) DESC LIMIT 1";

    private static final String MIN_AVG_PLAYERS_LEFT_COUNTRY = "SELECT distinct t1.sport_category, avg(t1.players) as avg_players\n" +
            "    FROM game_details as t1 JOIN cities as t2\n" +
            "    ON t1.city = t2.city_name\n" +
            "    JOIN countries as t3\n" +
            "    ON t2.country_id = t3.country_id\n" +
            "      where t3.country_name = ?\n" +
            "      group by t1.sport_category\n" +
            "      order by avg_players asc\n" +
            "      limit 1";

    // Update Statements:
    private static final String UPDATE_GAME_LEVEL = "UPDATE game_details SET players = ? WHERE game_name = ?";

    private static final String FIND_GAME_CURR_ROW = "SELECT game_name FROM game_details\n" +
            "ORDER BY unix_timestamp(game_date)\n" +
            "LIMIT ?, 1";


    private static final String UPDATE_GAME_DETAILS = "UPDATE game_details SET game_name = ?, game_date = ?, city = ? ,sport_category = ?, " +
            "players = ?, level =? WHERE game_name = ?";

    private static final String UPDATE_MATCH_GAMES = "UPDATE match_games SET game_name = ? WHERE game_name = ?";


    /**
     * Class Constructor
     * @throws IOException
     */
    public GameDaoImpl() throws IOException {
        String[] propertiesArray = Utils.PropertiesReaders.getJDBCProperties(); // Read the database properties.
        DB_DRIVER = propertiesArray[0];
        DB_URL = propertiesArray[1];
        DB_USER = propertiesArray[2];
        DB_PASSWORD = propertiesArray[3];
        DB_NAME = propertiesArray[4];
    }

    /**
     * Receive the database connection.
     * @return Connection.
     */
    @Override
    public Connection getConnection() {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DB Connection close.
     * @param con
     */
    public static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Query Statement close.
     * @param stmt
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The following function performs a delete statement from the match_games table.
     * @param participant
     * @param gameName
     * @return boolean that indicates if the operation was succeeded.
     * @throws SQLException
     */
    @Override
    public Boolean deleteFromMatchGames(String participant, String gameName) throws SQLException {
        Connection connection = null;
        PreparedStatement Stmt = null;
        try { // Transactional Function.
            connection = getConnection();
            connection.setAutoCommit(false);
            Stmt = connection.prepareStatement(DELETE_FROM_MATCH_TABLE);
            Stmt.setString(1, participant);
            Stmt.setString(2, gameName);
            System.out.println(Stmt);
            Stmt.execute();
            if(getCurrNumPlayersAndUpdatePlayers(connection, Stmt, gameName)) {
                connection.commit(); // If there is no error.
                return true;
            } else {
                connection.rollback(); // If there is any error.
                return false;
            }
        } catch (SQLException throwables) {
            connection.rollback(); // If there is any error.
            throwables.printStackTrace();
            return false;
        } finally {
            close(connection);
            close(Stmt);
        }
    }

    /**
     * The following function performs a delete statement from the game_details table.
     * @param gameName
     * @param creationDate
     * @return boolean that indicates if the operation was succeeded.
     * @throws SQLException
     */
    @Override
    public Boolean deleteGame(String gameName, String creationDate) throws SQLException {
        Connection connection = null;
        PreparedStatement foreignStmt = null;
        PreparedStatement deleteStmt = null;
        try { // Transactional Function.
            connection = getConnection();
            connection.setAutoCommit(false);
            foreignStmt = connection.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 0);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            deleteStmt = connection.prepareStatement(DELETE_GAME);
            deleteStmt.setString(1, gameName);
            deleteStmt.setString(2, creationDate);
            System.out.println(deleteStmt);
            deleteStmt.execute();

            foreignStmt = connection.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 1);
            System.out.println(foreignStmt);
            foreignStmt.execute();
            if(deleteAllGameMatches(connection, deleteStmt, gameName, creationDate)) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            connection.rollback(); // If there is any error.
            throwables.printStackTrace();
            return false;
        } finally {
            close(connection);
            close(foreignStmt);
            close(deleteStmt);
        }
    }

    /**
     * The following function performs a delete statement of a game from match_game table.
     * @param connection
     * @param stmt
     * @param gameName
     * @param creationDate
     * @return boolean that indicates if the operation was succeeded.
     */
    private boolean deleteAllGameMatches(Connection connection, PreparedStatement stmt, String gameName, String creationDate) {
        try {
            stmt = connection.prepareStatement(DELETE_GAME_FROM_ALL_MATCH_TABLES);
            stmt.setString(1, gameName);
            stmt.setString(2, creationDate);
            System.out.println(stmt);
            stmt.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * General Method that returns a built in DefaultTableModel object for the client side.
     * @return DefaultTableModel object that holds game_details table's record.
     */
    private DefaultTableModel buildDefaultTableModel() {
        DefaultTableModel dm = new DefaultTableModel();
        dm.addColumn("user_name");
        dm.addColumn("game_name");
        dm.addColumn("sport_category");
        dm.addColumn("Country");
        dm.addColumn("city");
        dm.addColumn("game_date");
        dm.addColumn("players");
        dm.addColumn("level");
        dm.addColumn("creation_date");
        return dm;
    }

    /**
     * General Method that returns an array of strings.
     * @param rs
     * @return array of Strings.
     * @throws SQLException
     */
    private String[] resultSetStrings(ResultSet rs) throws SQLException {
        String game_name = rs.getString(1);
        String user_name = rs.getString(2);
        String sport_category = rs.getString(3);
        String country = rs.getString(4);
        String city = rs.getString(5);
        String game_date = rs.getString(6);
        String players = rs.getString(7);
        String level = rs.getString(8);
        String creationDate = rs.getString(9);
        return new String[]{game_name, user_name, sport_category, country, city, game_date ,  players, level, creationDate};
    }

    /**
     * The following function retrieve the whole games details from table game_details.
     * @return DefaultTableModel.
     * @throws SQLException
     */
    @Override
    public DefaultTableModel findAllGames() throws SQLException {
        DefaultTableModel dm = buildDefaultTableModel();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FIND_ALL_GAMES);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dm.addRow(resultSetStrings(rs));
            }
            return dm;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(connection);
            close(stmt);
            assert rs != null;
            rs.close();
        }
        return null;
    }

    /**
     * The following function returns an object of type DefaultTableModel with all the match games of the current user.
     * @param userName
     * @return DefaultTableModel.
     * @throws SQLException
     */
    @Override
    public DefaultTableModel findMatches(String userName) throws SQLException {
        DefaultTableModel dm = new DefaultTableModel();
        dm.addColumn("user_name");
        dm.addColumn("game_name");
        dm.addColumn("creation_date");
        dm.addColumn("participant");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(GET_ALL_GAME_MATCHES);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            System.out.println(stmt);
            while (rs.next()) {
                String user_name = rs.getString(1);
                String game_name = rs.getString(2);
                String creation_date = rs.getString(3);
                String participant = rs.getString(4);
                dm.addRow(new String[]{user_name, game_name, creation_date, participant});
            }
            return dm;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(conn);
            close(stmt);
            assert rs != null;
            rs.close();
        }
        return null;
    }

    /**
     * The following function finds the current selected row of a game inside the table game_details.
     * @param currentRow
     * @param flag
     * @return String.
     * @throws SQLException
     */
    @Override
    public String findColumnRow(int currentRow, String flag) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String gameName = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_GAME_CURR_ROW);
            stmt.setInt(1, currentRow);
            System.out.println(stmt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                gameName = rs.getString(1);
                System.out.println(gameName);
            }
            return gameName;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return gameName;
        } finally {
            close(stmt);
            close(conn);
            assert rs != null;
            rs.close();
        }
    }

    /**
     * The following function finds the details of specific according its name as input (FindGame).
     * @param gameName
     * @return DefaultTableModel.
     * @throws SQLException
     */
    @Override
    public DefaultTableModel findByGameName(String gameName) throws SQLException {
        DefaultTableModel dm = buildDefaultTableModel();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(JOIN_QUERY_GAME_NAME);
            stmt.setString(1, gameName);
            System.out.println(stmt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dm.addRow(resultSetStrings(rs));
            }
            return dm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
            rs.close();
        }
    }

    /**
     * The following function finds the details of specific according its city as input (FindGame).
     * @param city
     * @return DefaultTableModel.
     * @throws SQLException
     */
    public DefaultTableModel findByCityName(String city) throws SQLException {
        DefaultTableModel dm = buildDefaultTableModel();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(JOIN_QUERY_CITY);
            stmt.setString(1, city);
            System.out.println(stmt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dm.addRow(resultSetStrings(rs));
            }
            return dm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
            rs.close();
        }
    }

    /**
     * The following function finds the details of specific according its country as input (FindGame).
     * @param countryName
     * @return DefaultTableModel.
     */
    @Override
    public DefaultTableModel findByCountryName(String countryName) {
        DefaultTableModel dm = buildDefaultTableModel();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(JOIN_QUERY_COUNTRY);
            stmt.setString(1, countryName);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dm.addRow(resultSetStrings(rs));
            }
            return dm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     * The following function finds the details of all the games with max level under an input sport category (FindGame).
     * @param category
     * @return DefaultTableModel.
     */
    public DefaultTableModel findMaxLevelGamesInEachCountry(String category) {
        DefaultTableModel dm = buildDefaultTableModel();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(MAX_LEVEL_GROUP_BY_CATEGORY);
            stmt.setString(1, category);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dm.addRow(resultSetStrings(rs));
            }
            return dm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     * The following function finds the country where sport ? is most played (FindGame).
     * @param sportCategory
     * @return DefaultTableModel.
     */
    @Override
    public DefaultTableModel findCountryMostPlayedSport(String sportCategory) {
        DefaultTableModel dm = new DefaultTableModel();
        dm.addColumn("country");
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(MOST_PLAYED_SPORT_COUNTRY);
            stmt.setString(1, sportCategory);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String country = rs.getString(1);
                dm.addRow(new String[]{country});
            }
            return dm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     * The following function finds the minimum average of players that was left in a input country (FindGame - Statistics table).
     * @param Country
     * @return
     */
    // TODO -
    @Override
    public DefaultTableModel findMinAvgPlayersLeftInCountry(String Country) {
        DefaultTableModel dm = new DefaultTableModel();
        dm.addColumn("Sport Category");
        dm.addColumn("Average Players Left");
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(MIN_AVG_PLAYERS_LEFT_COUNTRY);
            stmt.setString(1, Country);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String sport_category = rs.getString(1);
                String min_avg_players = rs.getString(2);
                dm.addRow(new String[]{sport_category, min_avg_players});
            }
            return dm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     * The following function finds the most played sport of the current month (FindGame - Statistics table).
     * @return DefaultTableModel.
     */
    @Override
    public DefaultTableModel findMostPlayedSportOfMonth() {
        DefaultTableModel dm = new DefaultTableModel();
        dm.addColumn("country");
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(MOST_PLAYED_SPORT_OF_MONTH);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String country = rs.getString(1);
                dm.addRow(new String[]{country});
            }
            return dm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     * The following function inserts an entry to table game_details in Transactional methodology.
     * @param userName
     * @param game
     * @return
     * @throws SQLException
     */
    @Override
    public boolean insertGameDetails(String userName, Game game) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        try { // Transactional Function.
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(INSERT_GAME_DETAILS, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, userName);
            stmt.setString(2, game.getGameName());
            stmt.setString(3, game.getDate());
            LocalDateTime creationDate = LocalDateTime.now();
            stmt.setString(4, dtf.format(creationDate));
            stmt.setString(5, game.getCity());
            stmt.setString(6, game.getSportCategory());
            stmt.setInt(7, game.getNumOfPlayers());
            stmt.setInt(8, game.getLevelOfPlayers());
            System.out.println(stmt);
            stmt.executeUpdate();
            if(insertToMatchGameTable(conn, stmt, userName, game.getGameName(), dtf.format(creationDate), userName)) {
                conn.commit(); // If there is no error.
                return true;
            } else {
                conn.rollback(); // If there is any error.
                return false;
            }
        } catch (SQLException e) {
            conn.rollback(); // If there is any error.
            return false;
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     * The following function verifies if the input record is already exists in table match_games.
     * @param userName
     * @param gameName
     * @param creationDate
     * @param participant
     * @return boolean that indicates if the operation was succeeded.
     * @throws SQLException
     */
    @Override
    public boolean checkIfMatchExists(String userName, String gameName, String creationDate, String participant) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try { // Transactional Function.
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(DOES_MATCH_EXIST);
            stmt.setString(1, userName);
            stmt.setString(2, gameName);
            stmt.setString(3, creationDate);
            stmt.setString(4, participant);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) return false;
            return true;
        } catch (SQLException e) {
            conn.rollback(); // If there is any error.
            return false;
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     *  The following function inserts a record to match_games table
     *  & downgrade the number of player of the relevant game in table game_details (Transactional).
     * @param userName
     * @param gameName
     * @param creationDate
     * @param participant
     * @param gameNumOfPlayers
     * @return boolean that indicates if the operation was succeeded.
     * @throws SQLException
     */
    @Override
    public boolean insertToMatchGameTableAndDownPlayers(String userName, String gameName, String creationDate, String participant, int gameNumOfPlayers) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try { // Transactional Function.
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(INSERT_MATCH_GAMES, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, userName);
            stmt.setString(2, gameName);
            stmt.setString(3, creationDate);
            stmt.setString(4, participant);
            System.out.println(stmt);
            stmt.executeUpdate();
            if(updateGameLevel(conn, stmt, gameName, gameNumOfPlayers, "DownPlayer", creationDate)) {
                conn.commit(); // If there is no error.
                return true;
            } else {
                conn.rollback(); // If there is any error.
                return false;
            }
        } catch (SQLException e) {
            conn.rollback(); // If there is any error.
            return false;
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     *  The following function get the current number of players in a specific game and upgrade the number of players
     *  in table game_details in case of delete game from match_games table.
     * @param conn
     * @param stmt
     * @param gameName
     * @return boolean that indicates if the operation was succeeded.
     */
    @Override
    public boolean getCurrNumPlayersAndUpdatePlayers(Connection conn, PreparedStatement stmt, String gameName)  {
        ResultSet rs;
        int players = -1;
        try {
            stmt = conn.prepareStatement(FIND_NUM_OF_PLAYERS_GAME);
            stmt.setString(1, gameName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                players = rs.getInt(1);
            }
            if(updateGameLevel(conn, stmt, gameName,  players, "UpPlayer", null)) { // Up the number of players
                return true;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * The following function inserts a record to table match_games.
     * @param connection
     * @param stmt
     * @param userName
     * @param gameName
     * @param creationDate
     * @param participant
     * @return boolean that indicates if the operation was succeeded.
     */
    @Override
    public boolean insertToMatchGameTable(Connection connection, PreparedStatement stmt, String userName, String gameName, String creationDate, String participant) {
        try {
            stmt = connection.prepareStatement(INSERT_MATCH_GAMES, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, userName);
            stmt.setString(2, gameName);
            stmt.setString(3, creationDate);
            stmt.setString(4, participant);
            System.out.println(stmt);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * The following function verifies if the tables countries & cities are already filled up.
     * @param input
     * @param flag
     * @return boolean that indicates if the operation was succeeded.
     * @throws SQLException
     */
    @Override
    public boolean isCountryOrCityValid(String input, String flag) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = null;
        boolean isExists = false;
        try {
            connection = getConnection();
            if(flag.equals("country")) {
                stmt = connection.prepareStatement(IS_COUNTRY_EXISTS);
            } else {
                stmt = connection.prepareStatement(IS_CITY_EXISTS);
            }
            stmt.setString(1, input);
            System.out.println(stmt);
            rs =  stmt.executeQuery();
            while(rs.next()){
                isExists = true;
            }
            return isExists;
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
            return false;
        } finally {
            close(connection);
            close(stmt);
            assert rs != null;
            rs.close();
        }
    }

    /**
     *  // The following function is triggered with two optional flags: DownPlayer in case of a match and UpPlayer in case
     *  // of deleting a match from match_games.
     * @param connection
     * @param stmt
     * @param gameName
     * @param gameNumOfPlayers
     * @param flag
     * @param creationDate
     * @return boolean that indicates if the operation was succeeded.
     */
    @Override
    public boolean updateGameLevel(Connection connection, PreparedStatement stmt, String gameName, int gameNumOfPlayers, String flag, String creationDate) {
        boolean isPlayersZero = false;
        boolean isGameBack = false;
        try { // "UPDATE game_details SET players = ? WHERE game_name = ?";
            stmt = connection.prepareStatement(UPDATE_GAME_LEVEL);
            if (flag.equals("DownPlayer") && gameNumOfPlayers >= 0) {
                stmt.setInt(1, gameNumOfPlayers - 1);
                isPlayersZero = gameNumOfPlayers - 1 == 0;
            } else if (flag.equals("UpPlayer") && gameNumOfPlayers >= -1) {
                stmt.setInt(1, gameNumOfPlayers + 1);
                isGameBack = gameNumOfPlayers + 1 == 0;
            }
            stmt.setString(2, gameName);
            System.out.println(stmt);
            stmt.execute();
            if (isPlayersZero) { // If the number of players in game_details is zero delete this game from game_details.
                if(removeGameFromGameDetails(connection, stmt, gameName, creationDate)) { /* Store the game details before deleting it using BEFORE DELETE TRIGGER. */
                    System.out.println("BEFORE DELETE Trigger - game is zero"); // 	TODO - Debug print (remove)
                    return true;
                } else {
                    return false;
                }
            } if (isGameBack) { // Insert the game back to game details table from the archive table.
                if(retrieveTheArchivedGame(connection, stmt, gameName)) {
                    System.out.println("GameBack"); // TODO - Debug print (remove)
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * The following function retrieves the archive game & insert its record to game_details table.
     * @param connection
     * @param stmt
     * @param gameName
     * @return boolean that indicates if the operation was succeeded.
     * @throws SQLException
     */
    private boolean retrieveTheArchivedGame(Connection connection, PreparedStatement stmt, String gameName) throws SQLException {
        try {
            stmt = connection.prepareStatement(INSERT_SELECT_ZERO_GAME, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, gameName);
            System.out.println(stmt);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     *  // The following function holds the implementation of the trigger that save the deleted game record inside the
     *  // game details archive table before its deletion from the real game details table.
     * @param connection
     * @param stmt
     * @param gameName
     * @param creationDate
     * @return boolean that indicates if the operation was succeeded.
     */
    private boolean removeGameFromGameDetails(Connection connection, PreparedStatement stmt, String gameName, String creationDate) {
        PreparedStatement foreignStmt;
        try { // Using BEFORE DELETE TRIGGER
            connection.createStatement().execute("DROP TRIGGER IF EXISTS `before_delete_game`");
            StringBuilder triggerBuilder = new StringBuilder();
            triggerBuilder.append(" CREATE TRIGGER before_delete_game BEFORE DELETE ON game_details ");
            triggerBuilder.append(" FOR EACH ROW Begin ");
            triggerBuilder.append(" INSERT INTO game_details_archives(user_name , game_name , game_date , creation_date , city ,  sport_category , players , level ) " );
            triggerBuilder.append("VALUES");
            triggerBuilder.append(" ( " );
            triggerBuilder.append(" OLD.user_name , ");
            triggerBuilder.append(" OLD.game_name , " );
            triggerBuilder.append(" OLD.game_date , " );
            triggerBuilder.append(" OLD.creation_date , " );
            triggerBuilder.append(" OLD.city , " );
            triggerBuilder.append(" OLD.sport_category , " );
            triggerBuilder.append(" OLD.players , " );
            triggerBuilder.append(" OLD.level " );
            triggerBuilder.append(" ) ; ");
            triggerBuilder.append(" END ");
            System.out.println(triggerBuilder.toString());
            connection.createStatement().execute(triggerBuilder.toString());

            foreignStmt = connection.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 0);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            stmt = connection.prepareStatement(DELETE_GAME);
            stmt.setString(1, gameName);
            stmt.setString(2, creationDate);
            System.out.println(stmt);
            stmt.execute();

            foreignStmt = connection.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 1);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * The following function update a specific game's details according the user inputs (the user most be the creator of the game).
     * @param game
     * @param oldGame
     * @return boolean that indicates if the operation was succeeded.
     * @throws SQLException
     */
    @Override
    public boolean updateGameFullDetails(Game game, String oldGame) throws SQLException {
        Connection conn = null;
        PreparedStatement foreignStmt = null;
        PreparedStatement gameDetailsUpdateStmt = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            foreignStmt = conn.prepareStatement(SET_FOREIGN_KEY_CHECKS); // SET_FOREIGN_KEY_CHECKS
            foreignStmt.setInt(1, 0);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            gameDetailsUpdateStmt = conn.prepareStatement(UPDATE_GAME_DETAILS);
            gameDetailsUpdateStmt.setString(1, game.getGameName());
            gameDetailsUpdateStmt.setString(2, game.getDate());
            gameDetailsUpdateStmt.setString(3, game.getCity());
            gameDetailsUpdateStmt.setString(4, game.getSportCategory());
            gameDetailsUpdateStmt.setInt(5, game.getNumOfPlayers());
            gameDetailsUpdateStmt.setInt(6, game.getLevelOfPlayers());
            gameDetailsUpdateStmt.setString(7, oldGame);
            System.out.println(gameDetailsUpdateStmt);
            gameDetailsUpdateStmt.execute();

            foreignStmt = conn.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 1);
            System.out.println(foreignStmt);
            foreignStmt.execute();
            if (updateMatchGames(conn, gameDetailsUpdateStmt, game.getGameName(), oldGame)) { // Update the game name in match_games table
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        } finally {
            close(foreignStmt);
            close(gameDetailsUpdateStmt);
            close(conn);
        }
    }

    /**
     * The following function updates a record in table match_games table.
     * @param conn
     * @param stmt
     * @param gameName
     * @param oldGame
     * @return boolean that indicates if the operation was succeeded.
     */
    private boolean updateMatchGames(Connection conn, PreparedStatement stmt, String gameName, String oldGame) {
        try {
            stmt = conn.prepareStatement(UPDATE_MATCH_GAMES);
            stmt.setString(1, gameName);
            stmt.setString(2, oldGame);
            System.out.println(stmt);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}