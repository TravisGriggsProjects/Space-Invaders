package view;

import java.util.List;
import model.GameDetails;
import model.UserDetails;

/**
 * <p>
 * Title: Misc</p>
 * <p>
 * Description: </p>
 * <p>
 * Copyright: Copyright (c) 2005</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @version 1.0
 */
public interface UserViewInterface {

    /**
     * Display the information about the game
     */
    public void displayIntroductionInfo();

    /**
     * Display a table of highscores for the current user
     *
     * @param theUser
     * @param highScores
     */
    public void displayUsersHighScores(UserDetails theUser, List<GameDetails> highScores);

    /**
     * Display a table of highscores for the all the users
     *
     * @param highScores
     */
    public void displayAllHighScores(List<GameDetails> highScores);

    /**
     * Notify the player of a new high score
     */
    public void notifyNewHighScore();

    /**
     * Notify the user of a win
     */
    public void notifyWin();

    /**
     * Notify the user that they got killed
     */
    public void notifyDeath();

    /**
     * Update the interface ready to start a new game. The game will be in pause mode so the interface needs to
     * communicate this to the user and give the user some way of starting the game
     *
     * @param newGame
     */
    public void setUpForNewGame(GameDetails newGame);

    /**
     * Update info for current game
     */
    public void displayGameDetails();

    /**
     * Display an error in trying to save the current game settings
     */
    public void displaySaveGameSettingsError();

    /**
     * Display an error in trying to load the current game settings for this user
     *
     * @param userName
     */
    public void displayErrorUnableToLoadUserDetails(String userName);

    /**
     * Display an error in connecting to the database and that defaults will be used. Since cannto connect to the
     * database this should also disable the way the user can ask for a save details (eg disable the SAVE button)
     */
    public void displayErrorDatabaseConnectionLost();

    /**
     * The controller that the view will get to do the 'logic' when the user makes a request via the view (user
     * interface)
     *
     * @param theController
     */
    public void setController(UserEventListener theController);

    /**
     * A message displkayed when something goes wrong with loading the game sounds.
     */
    public void displayErrorLoadingSounds();

    /**
     * Give the user a game over message when aliens get too far!
     */
    public void notifyGameOver();

    /**
     * Confirm a rest of the game
     *
     * @return true if ok to proceed and reset, false otherwise
     */
    public boolean confirmResetGame();

    public void displayErrorUnableToSaveUserDetails(String specificMessage);

    /**
     *
     * @param wantIntroInfo
     */
    public void adjustInterfaceOptionsDueToWantingIntroInfoChange(boolean wantIntroInfo);

    public GameDetails getCurrentGame();

    public void setCurrentGame(GameDetails currentGame);

}
