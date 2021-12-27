package Controllers;

import Dao.GameDao;
import Dao.GameDaoImpl;
import Models.Game;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.SQLException;

/**
 * The following class maps requests between the client side and the server/database side in any context that
 * related to game management & find games application's windows.
 */
public class GameController {
    // Field.
    GameDao gameDao;

    // CTR.
    public GameController() throws IOException {
        this.gameDao = new GameDaoImpl();
    }

    public Boolean deleteFromMatchGames(String userName, String gameName) throws SQLException {
        return gameDao.deleteFromMatchGames(userName, gameName);
    }

    public Boolean deleteGame(String gameName, String creationDate) throws SQLException {
        return gameDao.deleteGame(gameName, creationDate);
    }

    public DefaultTableModel findAllGames() throws SQLException {
        return gameDao.findAllGames();
    }

    public DefaultTableModel findMatches(String userName) throws SQLException {
        return gameDao.findMatches(userName);
    }

    public String findColumnRow(int currentRow, String flag) throws SQLException {
        return gameDao.findColumnRow(currentRow, flag);
    }

    public DefaultTableModel findByGameName(String gameName) throws SQLException {
        return gameDao.findByGameName(gameName);
    }

    public DefaultTableModel findByCityName(String cityName) throws SQLException {
        return gameDao.findByCityName(cityName);
    }

    public DefaultTableModel findByCountryName(String countryName) throws SQLException {
        return gameDao.findByCountryName(countryName);
    }

    public DefaultTableModel findMaxLevelGamesInEachCountry(String category) throws SQLException {
        return gameDao.findMaxLevelGamesInEachCountry(category);
    }

    public DefaultTableModel findCountryMostPlayedSport(String sportCategory) throws SQLException {
        return gameDao.findCountryMostPlayedSport(sportCategory);
    }

    public DefaultTableModel findMinAvgPlayersLeftInCountry(String Country) throws SQLException {
        return gameDao.findMinAvgPlayersLeftInCountry(Country);
    }

    public boolean insertGameDetails(String userName, Game game) throws SQLException {
        return gameDao.insertGameDetails(userName, game);
    }

    public boolean checkIfMatchExists(String userName, String gameName, String creationDate, String participant) throws SQLException {
        return gameDao.checkIfMatchExists(userName, gameName, creationDate, participant);
    }

    public boolean insertToMatchGameTableAndDownPlayers(String userName, String gameName, String creationDate, String participant, int i) throws SQLException {
        return gameDao.insertToMatchGameTableAndDownPlayers(userName, gameName, creationDate, participant, i);
    }


    public boolean updateGameFullDetails(Game game, String oldGame) throws SQLException {
        return gameDao.updateGameFullDetails(game, oldGame);
    }


    public boolean isCountryOrCityValid(String input, String flag) throws SQLException {
        return gameDao.isCountryOrCityValid(input, flag);
    }

    public DefaultTableModel findMostPlayedSportOfMonth() {
        return gameDao.findMostPlayedSportOfMonth();
    }
}
