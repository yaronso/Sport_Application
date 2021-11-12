package Models;

import java.io.Serializable;

/**
 * Domain Game model
 */
public class Game implements Serializable {
    private String gameName;
    private String sportCategory;
    private String country;
    private String city;
    private String date;
    private int numOfPlayers;
    private int levelOfPlayers;

    public Game(String gameName, String sportCategory, String country, String city, String date, int numOfPlayers, int levelOfPlayers) {
        this.gameName = gameName;
        this.sportCategory = sportCategory;
        this.country = country;
        this.city = city;
        this.date = date;
        this.numOfPlayers = numOfPlayers;
        this.levelOfPlayers = levelOfPlayers;
    }

    public Game() {}

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getSportCategory() {
        return sportCategory;
    }

    public void setSportCategory(String sportCategory) {
        this.sportCategory = sportCategory;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getLevelOfPlayers() {
        return levelOfPlayers;
    }

    public void setLevelOfPlayers(int levelOfPlayers) {
        this.levelOfPlayers = levelOfPlayers;
    }
}
