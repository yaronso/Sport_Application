package Dao;

import Models.Game;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.SQLException;

public interface GameDao {
    Connection getConnection();
    Boolean deleteFromMatchGames(String userName, String gameName);
    Boolean deleteGame(String gameName);
    DefaultTableModel findAllGames() throws SQLException;
    DefaultTableModel findMatches(String userName) throws SQLException;
    String findColumnRow(int currentRow, String flag) throws SQLException;
    DefaultTableModel findByGameName(String gameName) throws SQLException;
    DefaultTableModel findByCityName(String cityName) throws SQLException;
    DefaultTableModel findByCountryName(String countryName);
    DefaultTableModel findMaxLevelGamesInEachCountry(String category);
    DefaultTableModel findCountryMostPlayedSport(String sportCategory);
    int insertGameDetails(String userName, Game game);
    int insertToMatchGameTable(String userName, String gameName, String creationDate, String participant);
    boolean updateGameLevel(String gameName, int gameNumOfPlayers, String flag);
    boolean updateGameFullDetails(Game game, String oldGame);
    int getCurrNumPlayers(String gameName) throws SQLException;
}
