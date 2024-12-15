package view;

import java.util.EventListener;

/**
 * <p>
 * Title: Misc</p>
 * <p>
 * Interface that a control would need to implement. It defines all of the application actions that the user causes
 * (somehow) through the user interface</p>
 * <p>
 * Copyright: Copyright (c) 2006</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @version 1.0
 */
public interface UserEventListener extends EventListener {

    /**
     * User wants to see the high scores they have for each game setting
     *
     * @param userEvent - contains the users details
     */
    public void getUsersHighScores(UserEvent userEvent) throws Exception;

    /**
     * User wants to see the high scores for all the players
     */
    public void getAllHighScores();

    /**
     * User wants to start playing the game
     *
     * @param e details of the View that the event came from
     */
    public void startPlaying(UserEvent e);

    /**
     * User wants to exit the game
     *
     * @param e details of the View that the event came from
     */
    public void exitGame(UserEvent e);

    /**
     * User wants to pause the game
     *
     * @param e details of the View that the event came from
     */
    public void pauseGame(UserEvent e);

    /**
     * User wants to start a new game
     *
     * @param e details of the View that the event came from but at the moment the data provide is not actually needed
     */
    public abstract void setUpForNewGame();

    /**
     * User wants to change the firing interval
     *
     * @param e Would contain the new value for the firing interval
     */
    public void changeFiringInterval(UserEvent e);

    /**
     * User wants to change the move speed
     *
     * @param e Would contain the new value for the firing interval
     */
    public void changeMoveSpeed(UserEvent e);

    /**
     * User wants to save the setting for this game
     *
     * @param e details of the View that the event came from but at the moment the data provide is not actually needed
     */
    public void saveCurrentGameResults(UserEvent e);

    public void undoLastSave(UserEvent userEvent);

    public void changeSettingForWantingIntroInfo(boolean wantIntroInfo);
}
