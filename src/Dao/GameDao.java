package Dao;

import Models.Game;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface GameDao {
    Connection getConnection();
    Boolean deleteFromMatchGames(String userName, String gameName) throws SQLException;
    Boolean deleteGame(String gameName, String creationDate) throws SQLException;
    DefaultTableModel findAllGames() throws SQLException;
    DefaultTableModel findMatches(String userName) throws SQLException;
    String findColumnRow(int currentRow, String flag) throws SQLException;
    DefaultTableModel findByGameName(String gameName) throws SQLException;
    DefaultTableModel findByCityName(String cityName) throws SQLException;
    DefaultTableModel findByCountryName(String countryName) throws SQLException;
    DefaultTableModel findMaxLevelGamesInEachCountry(String category) throws SQLException;
    DefaultTableModel findCountryMostPlayedSport(String sportCategory) throws SQLException;
    boolean insertGameDetails(String userName, Game game) throws SQLException;
    boolean insertToMatchGameTableAndDownPlayers(String userName, String gameName, String creationDate, String participant, int i) throws SQLException;
    boolean updateGameLevel(Connection connection, PreparedStatement stmt, String gameName, int gameNumOfPlayers, String flag, String creationDate);
    boolean updateGameFullDetails(Game game, String oldGame);
    boolean getCurrNumPlayersAndUpdatePlayers(Connection conn, PreparedStatement stmt, String gameName) throws SQLException;
    boolean insertToMatchGameTable(Connection connection, PreparedStatement stmt, String userName, String gameName, String creationDate, String participant);
}
