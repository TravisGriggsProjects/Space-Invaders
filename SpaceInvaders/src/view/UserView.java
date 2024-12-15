package view;

import controller.MyLogger;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import model.FiringIntervalComboBoxModel;
import model.GameDetails;
import model.GameSettings;
import model.MoveSpeedComboBoxModel;
import model.UserDetails;

/**
 * <p>
 * Title: Misc
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 *
 * <p>
 * Company: TAFE SA
 * </p>
 *
 * @author Santi Ruiz
 * @version 1.0
 */
public class UserView extends JPanel implements UserViewInterface {

    private UserEventListener theController;
    private JFrame frameForWholeGame;
    private GameDetails currentGame;

    //
    // CURRENT GAME
    private JPanel jPanelCurrentGame = new JPanel();
    private JLabel jLabelShipsDes = new JLabel();
    private JTextField jTextFieldShipsDes = new JTextField(10);
    private JLabel jLabelCurrentScore = new JLabel();
    private JTextField jTextFieldCurrentScore = new JTextField(10);
    private JLabel jLabelShotsUsed = new JLabel();
    private JTextField jTextFieldShotsUsed = new JTextField(10);
    private JLabel jLabelMoveSpeed = new JLabel();
    private JTextField jTextFieldMoveSpeed = new JTextField(10);
    //
    // GAME SETINGS
    private JPanel jPanelGameSettings = new JPanel();
    private JLabel jLabelFiringInterval = new JLabel();
    private JTextField jTextFieldFiringInterval = new JTextField(10);
    private JLabel jLabelHighScore = new JLabel();
    private JTextField jTextFieldHighScore = new JTextField(10);
    //
    // USER
    private JPanel jPanelUserName = new JPanel();
    private JLabel jLabelUserName = new JLabel();
    private JTextField jTextFieldUserName = new JTextField(20);
    //
    // BUTTONS
    private JPanel jPanelButtons = new JPanel();
    private JPanel jPanelAction = new JPanel();
    private JPanel jPanelChange = new JPanel();
    private JButton jButtonPlayPause = new JButton();
    private JButton jButtonSave = new JButton();
    private JButton jButtonExit = new JButton();
    private JButton jButtonNewGame = new JButton();

    private JComboBox<Double> jComboSpeed = new JComboBox<Double>();
    private JComboBox<Integer> jComboFiringInterval = new JComboBox<Integer>();
    private JButton jButtonInterval = new JButton();
    private JButton jButtonMyScores = new JButton();
    private JButton jButtonHighScores = new JButton();

    // create menu items
    private JMenuBar jMenuBar = new JMenuBar();
    private JMenu jMenuFile = new JMenu("File");
    private JMenuItem jMenuItemNewGame = new JMenuItem("New Game");
    private JMenuItem jMenuItemSave = new JMenuItem("Save");
    private JMenuItem jMenuItemExit = new JMenuItem("Exit");
    private JMenu jMenuGame = new JMenu("Game");
    private JMenuItem jMenuItemPlayPause = new JMenuItem("Play Game");
    private JMenu jMenuSettings = new JMenu("Settings");
    private JMenuItem jMenuItemChangeMoveSpeed = new JMenuItem("Change Move Speed ...");
    private JMenuItem jMenuItemChangeFiringInterval = new JMenuItem("Change Firing Interval ...");
    private JMenuItem jMenuItemChangeWantIntroInfo = new JMenuItem("Turn off Intro Info ...");

    private MoveSpeedChangeEventHandler moveSpeedChangeHandler;
    private FiringIntervalChangeEventHandler firingIntervalChangeHandler;

    /**
     * UserView
     *
     * @param pFrameForWholeGame JFrame
     */
    public UserView(JFrame pFrameForWholeGame) {
        try {
            frameForWholeGame = pFrameForWholeGame;
            initialiseComponents();
            pFrameForWholeGame.setLocationByPlatform(true);
        } catch (Exception ex) {
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.SEVERE, "Could not create the View", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void initialiseComponents() throws Exception {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // User Details
        jLabelUserName.setText("User Name      ");
        jTextFieldUserName.setText("TEST USER");
        jTextFieldUserName.setEditable(false);
        jPanelUserName.setBorder(BorderFactory.createRaisedBevelBorder());
        jPanelUserName.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanelUserName.add(jLabelUserName);
        jPanelUserName.add(jTextFieldUserName);

        // Current Game
        jLabelShipsDes.setText("Ships Destroyed");
        jTextFieldShipsDes.setText("");
        jTextFieldShipsDes.setEditable(false);
        jLabelShotsUsed.setText("Shots Used     ");
        jTextFieldShotsUsed.setText("");
        jTextFieldShotsUsed.setEditable(false);
        jLabelCurrentScore.setText("Current Score ");
        jTextFieldCurrentScore.setText("0");
        jTextFieldCurrentScore.setEditable(false);
        jPanelCurrentGame.add(jLabelShipsDes);
        jPanelCurrentGame.add(jTextFieldShipsDes);
        jPanelCurrentGame.add(jLabelShotsUsed);
        jPanelCurrentGame.add(jTextFieldShotsUsed);
        jPanelCurrentGame.add(jLabelCurrentScore);
        jPanelCurrentGame.add(jTextFieldCurrentScore);

        // Game Settings
        jLabelMoveSpeed.setText("Move Speed      ");
        jTextFieldMoveSpeed.setText((new Double(GameSettings.MOVE_SPEED)).toString());
        jTextFieldMoveSpeed.setEditable(false);
        jLabelFiringInterval.setText("Firing Interval");
        jTextFieldFiringInterval.setText((new Integer(GameSettings.FIRING_INTERVAL)).toString());
        jTextFieldFiringInterval.setEditable(false);
        jLabelHighScore.setText("High Score     ");
        jTextFieldHighScore.setText("0");
        jTextFieldHighScore.setEditable(false);
        jPanelGameSettings.add(jLabelMoveSpeed);
        jPanelGameSettings.add(jTextFieldMoveSpeed);
        jPanelGameSettings.add(jLabelFiringInterval);
        jPanelGameSettings.add(jTextFieldFiringInterval);
        jPanelGameSettings.add(jLabelHighScore);
        jPanelGameSettings.add(jTextFieldHighScore);

        // Buttons
        jButtonPlayPause.setText("Play Game");
        jButtonSave.setText("Save Game Details");
        jButtonExit.setText("Exit");
        jButtonNewGame.setText("New Game");
        jComboSpeed.setModel(new MoveSpeedComboBoxModel());
        jComboFiringInterval.setModel(new FiringIntervalComboBoxModel());
        jButtonMyScores.setText("My Scores");
        jButtonHighScores.setText("Game High Scores");

        // JpanelAction
        jPanelAction.setBorder(BorderFactory.createTitledBorder("ACTION"));
        jPanelAction.add(jButtonPlayPause, null);
        jPanelAction.add(jButtonSave, null);
        jPanelAction.add(jButtonExit, null);
        jPanelAction.add(jButtonNewGame, null);
        jPanelAction.add(jButtonMyScores, null);
        jPanelAction.add(jButtonHighScores, null);

        // JpanelChange
        jPanelChange.setBorder(BorderFactory.createTitledBorder("CHANGE"));
        jPanelChange.add(new JLabel("Speed"));
        jPanelChange.add(jComboSpeed);
        jPanelChange.add(new JLabel("Interval"));
        jPanelChange.add(jComboFiringInterval);

        // JPanelButtons
        jPanelButtons.setBorder(BorderFactory.createRaisedBevelBorder());
        jPanelButtons.add(jPanelAction);
        jPanelButtons.add(jPanelChange);

        // add menu functionality
        jMenuFile.add(jMenuItemNewGame);
        jMenuFile.add(jMenuItemSave);
        jMenuFile.add(jMenuItemExit);
        jMenuGame.add(jMenuItemPlayPause);
        // Not in this version
        jMenuSettings.add(jMenuItemChangeMoveSpeed);
        jMenuSettings.add(jMenuItemChangeFiringInterval);
        jMenuSettings.add(jMenuItemChangeWantIntroInfo);
        jMenuBar.add(jMenuFile);
        jMenuBar.add(jMenuGame);
        jMenuBar.add(jMenuSettings);

        frameForWholeGame.setJMenuBar(jMenuBar);
        this.add(jPanelUserName);
        this.add(jPanelCurrentGame);
        this.add(jPanelGameSettings);
        this.add(jPanelButtons);

        // EVENT HANDLERS
        // Button press event handlers
        jButtonPlayPause.addActionListener(new PlayPauseEventHandler());
        jButtonSave.addActionListener(new SaveEventHandler());
        jButtonExit.addActionListener(new ExitEventHandler());
        jButtonNewGame.addActionListener(new NewGameEventHandler());
        jButtonInterval.addActionListener(new FiringIntervalChangeEventHandler());

        moveSpeedChangeHandler = new MoveSpeedChangeEventHandler();
        jComboSpeed.addActionListener(moveSpeedChangeHandler);
        firingIntervalChangeHandler = new FiringIntervalChangeEventHandler();
        jComboFiringInterval.addActionListener(firingIntervalChangeHandler);
        jButtonMyScores.addActionListener(new MyScoresEventHandler());
        jButtonHighScores.addActionListener(new HighScoresEventHandler());

        // Add menu item event listeners
        jMenuItemNewGame.addActionListener(new NewGameEventHandler());
        jMenuItemSave.addActionListener(new SaveEventHandler());
        jMenuItemExit.addActionListener(new ExitEventHandler());
        jMenuItemPlayPause.addActionListener(new PlayPauseEventHandler());
        jMenuItemChangeMoveSpeed.addActionListener(new MoveSpeedChangeEventHandler());
        jMenuItemChangeFiringInterval.addActionListener(new FiringIntervalChangeEventHandler());
        jMenuItemChangeWantIntroInfo.addActionListener(new WantIntroInfoChangeEventHandler());

        jButtonPlayPause.requestFocus();

    }

    public GameDetails getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameDetails currentGame) {
        this.currentGame = currentGame;
    }

    @Override
    public void displayUsersHighScores(UserDetails theUser, java.util.List<GameDetails> highScores) {
        UsersHighScoresView highScoresView = new UsersHighScoresView(highScores, frameForWholeGame);
        highScoresView.setModal(true);
        highScoresView.setVisible(true);
    }

    @Override
    public void displayAllHighScores(java.util.List<GameDetails> highScores) {
        GameHighScoresView allHighScoresView = new GameHighScoresView(highScores, frameForWholeGame);
        allHighScoresView.setModal(true);
        allHighScoresView.setVisible(true);
    }

    /**
     * notifyNewHighScore
     */
    @Override
    public void notifyNewHighScore() {
        JOptionPane.showMessageDialog(this, "WELL DONE!\n" + "\n" + "A New Personal High Score.\n"
            + "(Wonder if it is a game high score?)\n");
    }

    /**
     * notifyWin
     */
    @Override
    public void notifyWin() {
        JOptionPane.showMessageDialog(this, "Got them that time!\n\n");
    }

    /**
     * notifyDeath
     */
    @Override
    public void notifyDeath() {
        JOptionPane.showMessageDialog(this, "Too late!\n\nYou're dead!\n\n");
    }

    @Override
    public boolean confirmResetGame() {
        return (JOptionPane.showConfirmDialog(this, "WARNING! Changing game settings will reset the current game.\n"
            + "\n" + "Do you want to continue?", "Warning", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION);
    }

    @Override
    public void displayIntroductionInfo() {
        IntroductionView info = new IntroductionView(frameForWholeGame, this.theController);
        info.setVisible(true);
    }

    /**
     *
     * @param newGame
     */
    @Override
    public void setUpForNewGame(GameDetails newGame) {
        jButtonPlayPause.setText("Play Game");
        jMenuItemPlayPause.setText("Play Game");
        setCurrentGame(newGame);
        displayGameDetails();
    }

    /**
     * updateGameDetails display according to current game
     */
    @Override
    public void displayGameDetails() {
        jTextFieldShipsDes.setText(new Integer(currentGame.getShipsDestroyed()).toString());
        jTextFieldShotsUsed.setText(new Integer(currentGame.getShotsFired()).toString());
        jTextFieldCurrentScore.setText(new Integer(currentGame.calculateScoreChanges()).toString());
        DecimalFormat df = new DecimalFormat("#0");
        jTextFieldFiringInterval.setText(Integer.toString(currentGame.getGameSettings().getFiringInterval()));
        jTextFieldMoveSpeed.setText(df.format(currentGame.getGameSettings().getMoveSpeed()));
        jTextFieldHighScore.setText(Integer.toString(currentGame.getHighScore()));
        jTextFieldUserName.setText(currentGame.getUserDetails().getUserName());

        jComboFiringInterval.setSelectedItem(currentGame.getGameSettings().getFiringInterval());
        firingIntervalChangeHandler.setOldInterval(currentGame.getGameSettings().getFiringInterval());
        jComboSpeed.setSelectedItem(currentGame.getGameSettings().getMoveSpeed());
        moveSpeedChangeHandler.setOldSpeed(currentGame.getGameSettings().getMoveSpeed());
    }

    /**
     * showSaveGameSettingsError
     */
    @Override
    public void displaySaveGameSettingsError() {
        JOptionPane.showMessageDialog(this, "Unable to save to Database.");
    }

    /**
     * displayErrorDatabaseConnectionLost
     */
    @Override
    public void displayErrorDatabaseConnectionLost() {
        JOptionPane.showMessageDialog(this, "Unable to connect to database.\n"
            + "Will be using default game settings and \n" + "you will be unable to save your score.");
        adjustInterfaceOptionsDueToLostDatabseConnection();
    }

    @Override
    public void displayErrorLoadingSounds() {
        JOptionPane.showMessageDialog(this, "Seems there is a problem loading the game sounds.\n");
    }

    /**
     * Handle unable to load UserDetails Error by displaying message to user and turning off any interface components
     * relying on database access
     *
     * @param userName String
     */
    @Override
    public void displayErrorUnableToLoadUserDetails(String userName) {
        if (userName.equals(UserDetails.DEFAULT_USERNAME)) {
            JOptionPane.showMessageDialog(this, "Unable to load data from Database.");
        } else {
            JOptionPane.showMessageDialog(this, "Unable to load data from Database for user " + userName + ".");
        }

        adjustInterfaceOptionsDueToLostDatabseConnection();
    }

    /**
     * Handle database connection error by turning off any interface components relying on database access
     */
    private void adjustInterfaceOptionsDueToLostDatabseConnection() {
        this.jButtonMyScores.setEnabled(false);
        this.jButtonSave.setEnabled(false);
        this.jButtonHighScores.setEnabled(false);
        this.jMenuItemSave.setEnabled(false);
    }

    @Override
    public void setController(UserEventListener theController) {
        this.theController = theController;
    }

    @Override
    public void notifyGameOver() {
        JOptionPane.showMessageDialog(this, "\n" + "YOUR GAME IS OVER!" + "\n");
    }

    @Override
    public void displayErrorUnableToSaveUserDetails(String specificMessage) {
        JOptionPane.showMessageDialog(this, "User Setting update error - " + specificMessage + ".");
    }

    @Override
    public void adjustInterfaceOptionsDueToWantingIntroInfoChange(boolean wantIntroInfo) {
        if (wantIntroInfo) {
            this.jMenuItemChangeWantIntroInfo.setText("Turn off Intro Info ...");
        } else {
            this.jMenuItemChangeWantIntroInfo.setText("Turn on Intro Info ...");
        }
    }

    // Event handling inner classes
    class PlayPauseEventHandler implements ActionListener {

        /**
         * actionPerformed
         *
         * @param actionEvent ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (jButtonPlayPause.getText().equals("PAUSE")) {
                jButtonPlayPause.setText("Play Game");
                jMenuItemPlayPause.setText("Play Game");
                UserView.this.theController.pauseGame(new UserEvent(this));
            } else {
                jButtonPlayPause.setText("PAUSE");
                jMenuItemPlayPause.setText("PAUSE");
                // SR
                UserView.this.theController.startPlaying(new UserEvent(this));
            }
        }
    }

    class NewGameEventHandler implements ActionListener {

        /**
         * actionPerformed
         *
         * @param actionEvent ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserView.this.theController.setUpForNewGame();
        }
    }

    class ExitEventHandler implements ActionListener {

        /**
         * actionPerformed
         *
         * @param actionEvent ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserView.this.theController.exitGame(new UserEvent(this));
        }
    }

    class MyScoresEventHandler implements ActionListener {

        /**
         * actionPerformed
         *
         * @param actionEvent ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                UserView.this.theController.getUsersHighScores(new UserEvent(this, jTextFieldUserName.getText()));
            } catch (Exception ex) {
                Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class HighScoresEventHandler implements ActionListener {

        /**
         * actionPerformed
         *
         * @param actionEvent ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserView.this.theController.getAllHighScores();
        }
    }

    class SaveEventHandler implements ActionListener {

        /**
         * actionPerformed
         *
         * @param actionEvent ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserView.this.theController.saveCurrentGameResults(new UserEvent(this));
        }
    }

    class UndoSaveEventHandler implements ActionListener {

        /**
         * actionPerformed
         *
         * @param actionEvent ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserView.this.theController.undoLastSave(new UserEvent(this));
        }
    }

    class FiringIntervalChangeEventHandler implements ActionListener {

        private Integer oldInterval = 0;

        public Integer getOldInterval() {
            return oldInterval;
        }

        public void setOldInterval(Integer oldInterval) {
            this.oldInterval = oldInterval;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Integer interval;
            jComboFiringInterval.transferFocus();
            interval = (Integer) jComboFiringInterval.getSelectedItem();
            if (!interval.equals(oldInterval) && theController != null) {
                if (confirmResetGame()) {
                    oldInterval = interval;
                    theController.changeFiringInterval((new UserEvent(this, interval)));
                }
                jComboFiringInterval.setSelectedItem(oldInterval);
            }
        }
    }

    class MoveSpeedChangeEventHandler implements ActionListener {

        private Double oldSpeed = 0.0;

        public Double getOldSpeed() {
            return oldSpeed;
        }

        public void setOldSpeed(Double oldSpeed) {
            this.oldSpeed = oldSpeed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Double speed;

            speed = (Double) jComboSpeed.getSelectedItem();
            jComboSpeed.transferFocus();
            if (!speed.equals(oldSpeed) && theController != null) {
                if (confirmResetGame()) {
                    oldSpeed = speed;
                    theController.changeMoveSpeed(new UserEvent(this, speed));
                }
                jComboSpeed.setSelectedItem(oldSpeed);
            }
        }
    }

    private class WantIntroInfoChangeEventHandler implements ActionListener {

        public WantIntroInfoChangeEventHandler() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem choice = (JMenuItem) e.getSource();
            if (choice.getText().equals("Turn off Intro Info ...")) {
                theController.changeSettingForWantingIntroInfo(false);
            } else {
                theController.changeSettingForWantingIntroInfo(true);
            }

        }
    }
}
