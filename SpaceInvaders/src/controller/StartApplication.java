package controller;

import dao.SpaceInvadersDAOText;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import model.*;
import view.*;

/**
 * <p>
 * Title: Misc
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: TAFE SA
 * </p>
 *
 * @author Santi Ruiz
 * @version 9.0
 * @since April 2012 General upgrade, including following aspects: DAO - included table creation and recreation code
 * into appropriate DAO classes - separated the connection to mysql from connecting to the spaceinvaders database so
 * creation of database can be included in DB setup. Controller for Game - Fixed bug in dying sprite sequence for ship
 * General - added user game information aspects when logging in - Improved logging so same logger used through project
 *
 */
public class StartApplication {

    static MyLogger theLogger;
    private static UserView userControlView;

    /**
     * The entry point into the game.
     *
     * @param argv The arguments that are passed into our game
     */
    public static void main(String argv[]) throws Exception {
        JFrame frameForWholeGame;
        UserDetails userDetails;
        LoginController loginController;
        Game theGame = null;
        Thread gameThread;
        GameSettings gameSettings;
        try {
            // A SpaceInvaders specific log file is used but the default is for
            // all
            // logging to be turned off. Change the following to Level.ALL for
            // full
            // logging information
            theLogger = new MyLogger();
        } catch (IOException ex) {
            Logger.getLogger(StartApplication.class.getName()).log(
                Level.SEVERE, "Couldn't create spaceinvaders logger", ex);
        }
        Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.INFO,
            "Starting SpaceInvaders...");
        Logger.getLogger(MyLogger.LOGGER_NAME).setLevel(Level.INFO);

        // Show all app's windows with System look and feel rather than cross
        // platform look and feel (JLF)
        // Lightweight menus will not show on top of heavyweight Canvas (the
        // game)
        // hence turn off the default use of light weight popup menus
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JPopupMenu.setDefaultLightWeightPopupEnabled(true);
        } catch (Exception e) {
        }
        frameForWholeGame = new JFrame("Space Invaders TAFE SA");
        frameForWholeGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameForWholeGame.setLayout(new BorderLayout());

        // Create the view for user control and communication
        userControlView = new UserView(frameForWholeGame);

        gameSettings = new GameSettings();
        userDetails = new UserDetails();

        // Setup the connection to the database
        // If we can connect to database then get the details from there
        // otherwise will have to use the defaults
        // and inform the user through the user control view
        try {
            SpaceInvadersDAOText.connectToSpaceInvadersDatabase();
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.INFO,
                "Connected to DB...");

            // Setup the users details (eg name and high score) and the settings
            // for
            // playing the game (eg
            // firing interval and ship movement speed)
            JDialogLogin dlgLogin = new JDialogLogin(frameForWholeGame);
            dlgLogin.setLocation(400, 200);
            loginController = new LoginController(userDetails, dlgLogin);

            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.INFO,
                "Logging in...");
            loginController.login();
            if (loginController.isLoginCancelled()) {
                System.exit(0);
            } else if (loginController.isDatabaseError()) {
                userControlView.displayErrorDatabaseConnectionLost();
            } else {
                gameSettings.loadData(userDetails);
            }

        } catch (Exception ex) {
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.SEVERE,
                "During login...", ex);
            userControlView.displayErrorDatabaseConnectionLost();
        }
        try {
            // and any data changes (eg high score, game settings) .
            theGame = new Game(userDetails, gameSettings, userControlView);
        } catch (SQLException ex) {
            userControlView.displayErrorDatabaseConnectionLost();
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.SEVERE,
                "Lost database during creation of the game object.", ex);
        }
        Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.INFO,
            "USER Logged in...[{0}]", userDetails.getUserName());
        // Create complete user interface showing the game panel and the user
        // control view
        frameForWholeGame.add(theGame.getGamePanel(), BorderLayout.CENTER);
        frameForWholeGame.add(userControlView, BorderLayout.SOUTH);

        // Ensure the view knows which controller to send user requests to
        userControlView.setController(theGame);

        // make the frame showing the game and the user interface visible
        frameForWholeGame.pack();
        frameForWholeGame.setResizable(false);
        Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.INFO,
            "About to make View visible..");
        frameForWholeGame.setVisible(true);
        if (userDetails.isWantingIntroInfo()) {
            userControlView.displayIntroductionInfo();
        }
        frameForWholeGame.requestFocus();

        // finally start the game in its own thread - only now will the game
        // sprites be added to the game panel
        // The game will be paused until the user control view tells it to start
        gameThread = new Thread(theGame);
        Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.INFO,
            "About to start game thread..");
        gameThread.start();
    }
}
