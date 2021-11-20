package Dao;

import Models.Game;
import javax.swing.table.DefaultTableModel;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


public class GameDaoImpl implements GameDao {
    // Fields:
    private final String DB_DRIVER;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;
    // Sql Queries:
    // Insert statements:
    private static final String INSERT_GAME_DETAILS = "INSERT INTO game_details(user_name, game_name, game_date, creation_date, city, sport_category, players, level) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_MATCH_GAMES = "INSERT IGNORE INTO match_games(user_name, game_name, creation_date, participant) VALUES(?, ?, ?, ?)";

    // SET FOREIGN_KEY_CHECKS Statement:
    private static final String SET_FOREIGN_KEY_CHECKS = "SET FOREIGN_KEY_CHECKS = ?";

    // Delete statement:
    private static final String DELETE_GAME =  "DELETE FROM game_details WHERE game_name = ?";
    private static final String DELETE_FROM_MATCH_TABLE = "DELETE FROM match_games WHERE participant=? AND game_name=?";

    // Select statements:
    private static final String FIND_NUM_OF_PLAYERS_GAME = "SELECT players FROM game_details WHERE game_name = ?";

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

    // Update Statements:
    private static final String UPDATE_GAME_LEVEL = "UPDATE game_details SET players = ? WHERE game_name = ?";
    private static final String FIND_GAME_CURR_ROW = "SELECT game_name FROM game_details\n" +
                                                        "ORDER BY unix_timestamp(game_date)\n" +
                                                            "LIMIT ?, 1";

    // TODO - should update the country last
    private static final String UPDATE_GAME_DETAILS = "UPDATE game_details SET game_name = ?, game_date = ?, city = ? ,sport_category = ?, " +
                                                        "players = ?, level =? WHERE game_name = ?";


    public GameDaoImpl() throws IOException {
        String[] propertiesArray = getJDBCProperties();
        DB_DRIVER = propertiesArray[0];
        DB_URL = propertiesArray[1];
        DB_USER = propertiesArray[2];
        DB_PASSWORD = propertiesArray[3];
    }


    @Override
    public Connection getConnection() {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // DB Connection close.
    public static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Query Statement close
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
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

    @Override
    public Boolean deleteFromMatchGames(String participant, String gameName) {
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            // DELETE_FROM_MATCH_TABLE = "DELETE FROM match_games WHERE participant=? AND game_name= ?";
            connection = getConnection();
            deleteStmt = connection.prepareStatement(DELETE_FROM_MATCH_TABLE);
            deleteStmt.setString(1, participant);
            deleteStmt.setString(2, gameName);
            System.out.println(deleteStmt);
            deleteStmt.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } finally {
            close(connection);
            close(deleteStmt);
        }
    }

    @Override
    public Boolean deleteGame(String gameName) {
        Connection connection = null;
        PreparedStatement foreignStmt = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = getConnection();
            foreignStmt = connection.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 0);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            deleteStmt = connection.prepareStatement(DELETE_GAME);
            deleteStmt.setString(1, gameName);
            System.out.println(deleteStmt);
            deleteStmt.execute();

            foreignStmt = connection.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 1);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } finally {
            close(connection);
            close(foreignStmt);
            close(deleteStmt);
        }
    }

    // General Method.
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

    // General Method.
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



    // "SELECT distinct t1.user_name, t1.game_name, t1.sport_category, t1.game_date, t3.country_name, t1.city, t1.players, t1.level\n" +
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


    @Override
    public int insertGameDetails(String userName, Game game) {
        Connection conn = null;
        PreparedStatement stmt = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        try {
            conn = getConnection();
            // "INSERT INTO game_details(user_name, game_name, game_date, creation_date, city, sport_category, players, level) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(INSERT_GAME_DETAILS, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, userName);
            stmt.setString(2, game.getGameName());
            stmt.setString(3, game.getDate());
            stmt.setString(4, dtf.format(LocalDateTime.now()));
            stmt.setString(5, game.getCity());
            stmt.setString(6, game.getSportCategory());
            stmt.setInt(7, game.getNumOfPlayers());
            stmt.setInt(8, game.getLevelOfPlayers());
            System.out.println(stmt);
            int result = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                game.setGameName(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    // "INSERT IGNORE INTO match_games(user_name, game_name, creation_date, participant) VALUES(?, ?, ?, ?)";
    @Override
    public int insertToMatchGameTable(String userName, String gameName, String creationDate, String participant) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(INSERT_MATCH_GAMES, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, userName);
            stmt.setString(2, gameName);
            stmt.setString(3, creationDate);
            stmt.setString(4, participant);
            System.out.println(stmt);
            int result = stmt.executeUpdate();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    @Override
    public boolean updateGameLevel(String gameName, int gameNumOfPlayers, String flag) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            System.out.println(gameNumOfPlayers);
            stmt = conn.prepareStatement(UPDATE_GAME_LEVEL);
            if (flag.equals("DownLevel")) {
                stmt.setInt(1, gameNumOfPlayers - 1);
            } else {
                stmt.setInt(1, gameNumOfPlayers + 1);
            }
            stmt.setString(2, gameName);
            System.out.println(stmt);
            return stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(stmt);
            close(conn);
        }
    }

    @Override
    public boolean updateGameFullDetails(Game game, String oldGame) {
        Connection conn = null;
        PreparedStatement foreignStmt = null;
        PreparedStatement gameDetailsUpdateStmt = null;
        try {
            conn = getConnection();
            foreignStmt = conn.prepareStatement(SET_FOREIGN_KEY_CHECKS); // SET_FOREIGN_KEY_CHECKS
            foreignStmt.setInt(1, 0);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            // UPDATE_GAME_DETAILS = "UPDATE game_details SET game_name = ?, game_date = ?, city = ? ,sport_category = ?, players = ?, level =? WHERE game_name = ?";
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
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(foreignStmt);
            close(gameDetailsUpdateStmt);
            close(conn);
        }
    }

    @Override
    public int getCurrNumPlayers(String gameName) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int level = -1;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_NUM_OF_PLAYERS_GAME);
            stmt.setString(1, gameName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                level = rs.getInt(1);
            }
            return level;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return level;
        } finally {
            close(stmt);
            close(conn);
            rs.close();
        }
    }
}

