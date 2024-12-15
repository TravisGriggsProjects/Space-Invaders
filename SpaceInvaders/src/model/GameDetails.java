package model;

import dao.GameDetailsDAOText;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * <p>
 * The details for one game, including the user playing, the game settings the scoring (shots fired, ships destroyed,
 * score and previous high score)
 * </p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @author Julie Ruiz 
 * @author Travis Griggs 25/08/2024
 * @version 1.0 Initial version
 * @version 2.0 Updated to improve setting of highscore
 */
public class GameDetails implements Serializable {

    //constants
    public static final int POINTS_GAINED_FOR_SHIP_DESTROYED = 100;
    public static final int POINTS_LOST_FOR_WASTED_SHOT = 50;

    //instance variables
    private UserDetails userDetails;
    private GameSettings gameSettings;
    private int highScore;
    private int score;
    private int shipsDestroyed;
    private int shotsFired;
    private boolean newHighScore;

    //only 2 constructors required - do not make changes
 
    /**
     * no arg constructor
     * @throws Exception
     */
    public GameDetails() throws Exception {
        this(new UserDetails(), new GameSettings());        
    }

    /**
     *all args constructor
     * @param user
     * @param gameSettings
     * @throws Exception
     */
    public GameDetails(UserDetails user, GameSettings gameSettings) throws Exception {        
        this.userDetails = user;
        this.gameSettings = gameSettings;        
        
        resetTheScoringDetails();
    }

    //Getters 

    /**
     *userDetails getter
     * @return userDetails
     */
    public UserDetails getUserDetails() {
        return userDetails;
    }

    /**
     *gameSettings getter
     * @return gameSettings
     */
    public GameSettings getGameSettings() {
        return gameSettings;
    }

    /**
     *highScore getter
     * @return highScore
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     *score getter
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     *shipsDestroyed getter
     * @return shipsDestroyed
     */
    public int getShipsDestroyed() {
        return shipsDestroyed;
    }

    /**
     *shotsFired getter
     * @return shotsFired
     */
    public int getShotsFired() {
        return shotsFired;
    }

    /**
     *newHighScore getter
     * @return newHighScore
     */
    public boolean isNewHighScore() {
        return newHighScore;
    }
    
    //Setters

    /**
     *userDetails setter
     * @param userDetails
     */
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    /**
     *gameSettings setter
     * @param gameSettings
     */
    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    /**
     *highScore setter
     * @param highScore
     */
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    /**
     *score setter
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     *shipsDestroyed setter
     * @param shipsDestroyed
     */
    public void setShipsDestroyed(int shipsDestroyed) {
        this.shipsDestroyed = shipsDestroyed;
    }

    /**
     *shotsFired setter
     * @param shotsFired
     */
    public void setShotsFired(int shotsFired) {
        this.shotsFired = shotsFired;
    }

    /**
     *newHighScore setter
     * @param newHighScore
     */
    public void setNewHighScore(boolean newHighScore) {
        this.newHighScore = newHighScore;
    }
    
    //toString method

    /**
     *toString method
     * @return GameDetials as String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();       
        sb.append(super.toString()).append(", ");        
        sb.append("GameDetails{");
        sb.append("userDetails=");
        sb.append(userDetails);
        sb.append(", gameSettings=");
        sb.append(gameSettings);
        sb.append(", highScore=");
        sb.append(highScore);
        sb.append(", score=");
        sb.append(score);
        sb.append(", shipsDestroyed=");
        sb.append(shipsDestroyed);
        sb.append(", shotsFired=");
        sb.append(shotsFired);
        sb.append(", newHighScore=");
        sb.append(newHighScore);
        sb.append('}');
        return sb.toString();
    }
 
    //equals method

    /**
     *equals method
     * @param obj
     * @return true if objects are equal, otherwise return false
     */
    @Override
     public boolean equals(Object obj) {
        if (!(obj instanceof GameDetails)) {
            return false;
        }
        GameDetails other = (GameDetails) obj;
        return userDetails.equals(other.getUserDetails()) && gameSettings.equals(other.getGameSettings());
    }


    /**
     *
     * @return
     */
    public int calculateScoreChanges() {

        //missedShots = shotsFired - shipsDestroyed
        int missedShots = this.shotsFired - this.shipsDestroyed;

        //currentScore = shipsDestroyed*POINTS - missedShots*POINTS
        int currentScore = (this.shipsDestroyed * POINTS_GAINED_FOR_SHIP_DESTROYED)
            - (missedShots * POINTS_LOST_FOR_WASTED_SHOT);

        //set score to currentScore
        this.score = currentScore;

        // check if it's a new high score
        if (this.score > this.highScore) {
            newHighScore = true;
            this.highScore = this.score;
        } else {
            newHighScore = false;
        }

        //return currentScore, set to 0 if currentScore negative
        if (currentScore < 0) {
            currentScore = 0;
        }
        return currentScore;
    }

    /**
     *
     * @throws Exception
     */
    public final void resetTheScoringDetails() throws Exception {
		GameDetailsDAOText.saveHighScore(this);
        //reset newHighScore flag
        newHighScore = false;

        // reset score, shipsDestroyed, shotsFired to 0
        this.score = 0;
        this.shipsDestroyed = 0;
        this.shotsFired = 0;

        //Load the previous high score for this user for these settings
        //If there is an error then set highscore to 0 and continue
        try {
            GameDetailsDAOText.loadHighScore(this);
        } catch (Exception ex) {
            this.highScore = 0;
        }
    }

    /**
     *
     * @throws Exception
     */
    public void saveData() throws Exception {
        GameDetailsDAOText.saveData(this);
    }

    /**
     *
     * @throws Exception
     */
    public void loadData() throws Exception {
        GameDetailsDAOText.loadData(this);
    }
}
