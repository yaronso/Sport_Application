package Dao;

import Models.Game;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GameDaoImpl implements GameDao {
    // MySql Connection Details:
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/yaron_db";
    private static final String ID = "root";
    private static final String PASS = "ArchiveYsso6495";

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



    @Override
    public Connection getConnection() {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(DB_URL, ID, PASS);
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Boolean deleteFromMatchGames(String participant, String gameName) {
        Connection conn = null;
        PreparedStatement deleteStmt = null;
        try {
            conn = getConnection();
            // DELETE_FROM_MATCH_TABLE = "DELETE FROM match_games WHERE participant=? AND game_name= ?";
            deleteStmt = conn.prepareStatement(DELETE_FROM_MATCH_TABLE);
            deleteStmt.setString(1, participant);
            deleteStmt.setString(2, gameName);
            System.out.println(deleteStmt);
            deleteStmt.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } finally {
            close(conn);
            close(deleteStmt);
        }
    }

    @Override
    public Boolean deleteGame(String gameName) {
        Connection conn = null;
        PreparedStatement foreignStmt = null;
        PreparedStatement deleteStmt = null;
        try {
            conn = getConnection();
            foreignStmt = conn.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 0);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            deleteStmt = conn.prepareStatement(DELETE_GAME);
            deleteStmt.setString(1, gameName);
            System.out.println(deleteStmt);
            deleteStmt.execute();

            foreignStmt = conn.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 1);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } finally {
            close(conn);
            close(foreignStmt);
            close(deleteStmt);
        }
    }

    // "SELECT distinct t1.user_name, t1.game_name, t1.sport_category, t1.game_date, t3.country_name, t1.city, t1.players, t1.level\n" +
    @Override
    public DefaultTableModel findAllGames() {
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

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL_GAMES);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String game_name = rs.getString(1);
                String user_name = rs.getString(2);
                String sport_category = rs.getString(3);
                String country = rs.getString(4);
                String city = rs.getString(5);
                String game_date = rs.getString(6);
                String players = rs.getString(7);
                String level = rs.getString(8);
                String creationDate = rs.getString(9);
                dm.addRow(new String[]{game_name, user_name, sport_category, country, city, game_date ,  players, level, creationDate});
            }
            return dm;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public DefaultTableModel findMatches(String userName) {
        DefaultTableModel dm = new DefaultTableModel();
        dm.addColumn("user_name");
        dm.addColumn("game_name");
        dm.addColumn("creation_date");
        dm.addColumn("participant");
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_GAME_MATCHES);
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
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
        }
        return null;
    }

    @Override
    public String findColumnRow(int currentRow, String flag) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String gameName = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_GAME_CURR_ROW);
            stmt.setInt(1, currentRow);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
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
        }
    }

    @Override
    public DefaultTableModel findByGameName(String gameName) {
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
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(JOIN_QUERY_GAME_NAME);
            stmt.setString(1, gameName);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String game_name = rs.getString(1);
                String user_name = rs.getString(2);
                String sport_category = rs.getString(3);
                String country = rs.getString(4);
                String city = rs.getString(5);
                String game_date = rs.getString(6);
                String players = rs.getString(7);
                String level = rs.getString(8);
                String creationDate = rs.getString(9);
                dm.addRow(new String[]{game_name, user_name, sport_category, country, city, game_date ,  players, level, creationDate});
            }
            return dm;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    public DefaultTableModel findByCityName(String city) {
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
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(JOIN_QUERY_CITY);
            stmt.setString(1, city);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String game_name = rs.getString(1);
                String user_name = rs.getString(2);
                String sport_category = rs.getString(3);
                String country = rs.getString(4);
                String citySet = rs.getString(5);
                String game_date = rs.getString(6);
                String players = rs.getString(7);
                String level = rs.getString(8);
                String creationDate = rs.getString(9);
                dm.addRow(new String[]{game_name, user_name, sport_category, country, citySet, game_date ,  players, level, creationDate});
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
    public DefaultTableModel findByCountryName(String countryName) {
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
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(JOIN_QUERY_COUNTRY);
            stmt.setString(1, countryName);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String game_name = rs.getString(1);
                String user_name = rs.getString(2);
                String sport_category = rs.getString(3);
                String country = rs.getString(4);
                String citySet = rs.getString(5);
                String game_date = rs.getString(6);
                String players = rs.getString(7);
                String level = rs.getString(8);
                String creationDate = rs.getString(9);
                dm.addRow(new String[]{game_name, user_name, sport_category, country, citySet, game_date ,  players, level, creationDate});
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
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(MAX_LEVEL_GROUP_BY_CATEGORY);
            stmt.setString(1, category);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String game_name = rs.getString(1);
                String user_name = rs.getString(2);
                String sport_category = rs.getString(3);
                String country = rs.getString(4);
                String city = rs.getString(5);
                String game_date = rs.getString(6);
                String players = rs.getString(7);
                String level = rs.getString(8);
                String creationDate = rs.getString(9);
                dm.addRow(new String[]{game_name, user_name, sport_category, country, city, game_date ,  players, level, creationDate});
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

    /*@Override
    public int insertUserGames(Game game, String userName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(INSERT_USER_GAMES, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, game.getGameName());
            stmt.setString(2, userName);
            stmt.setString(3, game.getSportCategory());
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

    @Override
    public int insertGameRegion(Game game) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(INSERT_GAME_REGION, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, game.getGameName());
            stmt.setString(2, game.getCountry());
            stmt.setString(3, game.getCity());
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
    }*/

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
                System.out.println("Should up level");
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
        // Description: Update all the games tables, each with its relevant details
        // 1) UPDATE_GAME_REGION
        // 2) UPDATE_GAME_DETAILS
        // 3) UPDATE_USER_GAMES
        Connection conn = null;
        PreparedStatement foreignStmt = null;
        PreparedStatement gameRegionUpdateStmt = null;
        PreparedStatement gameDetailsUpdateStmt = null;
        PreparedStatement userGameUpdateStmt = null;

        try {
            conn = getConnection();
            // SET_FOREIGN_KEY_CHECKS
            foreignStmt = conn.prepareStatement(SET_FOREIGN_KEY_CHECKS);
            foreignStmt.setInt(1, 0);
            System.out.println(foreignStmt);
            foreignStmt.execute();

            /*gameRegionUpdateStmt = conn.prepareStatement(UPDATE_GAME_REGION);
            gameRegionUpdateStmt.setString(1, game.getGameName());
            gameRegionUpdateStmt.setString(2, game.getCountry());
            gameRegionUpdateStmt.setString(3, game.getCity());
            gameRegionUpdateStmt.setString(4, oldGame);
            System.out.println(gameRegionUpdateStmt);
            gameRegionUpdateStmt.execute();*/

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


            /*userGameUpdateStmt = conn.prepareStatement(UPDATE_USER_GAMES);
            userGameUpdateStmt.setString(1, game.getGameName());
            userGameUpdateStmt.setString(2, game.getSportCategory());
            userGameUpdateStmt.setString(3, oldGame);
            System.out.println(userGameUpdateStmt);
            userGameUpdateStmt.execute();*/

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
            close(gameRegionUpdateStmt);
            close(userGameUpdateStmt);
            close(conn);
        }
    }

    @Override
    public int getCurrNumPlayers(String gameName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int level = -1;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_NUM_OF_PLAYERS_GAME);
            stmt.setString(1, gameName);
            ResultSet rs = stmt.executeQuery();
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
        }
    }
}

