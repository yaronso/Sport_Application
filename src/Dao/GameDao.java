package Dao;

import Models.Game;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;

public interface GameDao {
    public Connection getConnection();
    public Boolean deleteFromMatchGames(String userName, String gameName);
    public Boolean deleteGame(String gameName);
    public DefaultTableModel findAllGames();
    public DefaultTableModel findMatches(String userName);
    public String findColumnRow(int currentRow, String flag);
    public DefaultTableModel findByGameName(String gameName);
    public DefaultTableModel findByCityName(String cityName);
    public DefaultTableModel findByCountryName(String countryName);
    public DefaultTableModel findMaxLevelGamesInEachCountry(String category);
    public DefaultTableModel findCountryMostPlayedSport(String sportCategory);
    public int insertUserGames(Game game, String userName);
    public int insertGameRegion(Game game);
    public int insertGameDetails(Game game);
    public int insertToMatchGameTable(String userName, String gameName, String gameDate);
    public boolean updateGameLevel(String gameName, int gameNumOfPlayers, String flag);
    public boolean updateGameFullDetails(Game game, String oldGame);
    public int getCurrNumPlayers(String gameName);
}
