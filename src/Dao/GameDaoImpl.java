package Dao;

import Models.Game;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GameDaoImpl implements GameDao {
    // Fields:
    private final String DB_DRIVER;
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;
    // Sql Queries:
    // Insert statements:
    private static final String INSERT_GAME_DETAILS = "INSERT IGNORE INTO game_details(user_name, game_name, game_date, creation_date, city, sport_category, players, level) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_MATCH_GAMES = "INSERT IGNORE INTO match_games(user_name, game_name, creation_date, participant) VALUES(?, ?, ?, ?)";
    private static final String INSERT_SELECT_ZERO_GAME = "INSERT IGNORE INTO game_details(user_name, game_name, game_date, creation_date, city, sport_category, players, level) " +
                                                            "SELECT user_name, game_name, game_date, creation_date, city, sport_category, players + 1, level " +
                                                                " FROM game_details_archives WHERE game_name=?";

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
    private static final String UPDATE_USING_ARCHIVE = "UPDATE game_details SET players = (SELECT )";
    private static final String FIND_GAME_CURR_ROW = "SELECT game_name FROM game_details\n" +
                                                        "ORDER BY unix_timestamp(game_date)\n" +
                                                            "LIMIT ?, 1";

    // TODO - should update the country last
    private static final String UPDATE_GAME_DETAILS = "UPDATE game_details SET game_name = ?, game_date = ?, city = ? ,sport_category = ?, " +
                                                        "players = ?, level =? WHERE game_name = ?";


    public GameDaoImpl() throws IOException {
        String[] propertiesArray = Utils.PropertiesReaders.getJDBCProperties();
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
    public boolean insertGameDetails(String userName, Game game) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        try { // Transactional Function.
            conn = getConnection();
            conn.setAutoCommit(false);
            // "INSERT IGNORE INTO game_details(user_name, game_name, game_date, creation_date, city, sport_category, players, level) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
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
            if(updateGameLevel(conn, stmt, gameName, gameNumOfPlayers, "DownPlayer")) {
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
            if(updateGameLevel(conn, stmt, gameName,  players, "UpPlayer")) { // Up the number of players
                return true;
            } else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


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


    @Override
    public boolean updateGameLevel(Connection connection, PreparedStatement stmt, String gameName, int gameNumOfPlayers, String flag) {
        boolean isPlayersZero = false;
        boolean isGameBack = false;
        try { // "UPDATE game_details SET players = ? WHERE game_name = ?";
            stmt = connection.prepareStatement(UPDATE_GAME_LEVEL);
            if (flag.equals("DownPlayer") && gameNumOfPlayers >= 0) {
                stmt.setInt(1, gameNumOfPlayers - 1);
                System.out.println("gameNumOfPlayers - 1 = " + (gameNumOfPlayers - 1));
                isPlayersZero = gameNumOfPlayers - 1 == 0;
            } else if (flag.equals("UpPlayer") && gameNumOfPlayers >= -1) {
                stmt.setInt(1, gameNumOfPlayers + 1);
                System.out.println("gameNumOfPlayers + 1 = " + gameNumOfPlayers + 1);
                isGameBack = gameNumOfPlayers + 1 == 0;
            }
            stmt.setString(2, gameName);
            System.out.println(stmt);
            stmt.execute();
            if (isPlayersZero) { // If the number of players in game_details is zero delete this game from game_details and alert the creator user!
                if(removeGameFromGameDetails(connection, stmt, gameName)) { /* Store the game details before deleting it using BEFORE DELETE TRIGGER. */
                    System.out.println("BEFORE DELETE Trigger - game is zero"); // 	Debug print
                    return true;
                } else {
                    return false;
                }
            } if (isGameBack) { // Insert the game back to game details.
                if(retrieveTheArchivedGame(connection, stmt, gameName)) {
                    System.out.println("GameBack"); // 	Debug print
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


    private boolean removeGameFromGameDetails(Connection connection, PreparedStatement stmt, String gameName) {
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

}

