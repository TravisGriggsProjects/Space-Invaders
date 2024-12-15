/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controller.MyLogger;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.*;
import model.GameDetails;
import model.GameSettings;
import model.UserDetails;

/**
 *
 * @author sruiz
 */
public class SpaceInvadersDAOText {

    public static final String SPACEINVADERS_USERS_DB_NAME = "users.csv";
    public static final String SPACEINVADERS_SETTINGS_DB_NAME = "settings.csv";
    public static final String SPACEINVADERS_USER_HIGH_SCORES_DB_NAME = "user_high_scores.csv";

    /**
     * Connects to the space invaders related files, in the case of text files simply exnsure that the files exist and
     * are ready to be used
     *
     * @throws java.io.FileNotFoundException
     */
    public static void connectToSpaceInvadersDatabase() throws FileNotFoundException, IOException {
        getUserHighScoresInputConnection();
        getSettingsInputConnection();
        getUserHighScoresInputConnection();
    }

    /**
     *
     * @return a connection to the users file ready for reading line by line.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static BufferedReader getUsersInputConnection() throws
        FileNotFoundException, IOException {
        File spaceInvadersUsersFile = null;
        BufferedReader userOIS = null;
        spaceInvadersUsersFile = new File(SPACEINVADERS_USERS_DB_NAME);
        //Create the file if it doesnt exist
        if (!spaceInvadersUsersFile.exists()) {
            new PrintWriter(spaceInvadersUsersFile).close();
            System.gc();
        }
        userOIS = new BufferedReader(new FileReader(spaceInvadersUsersFile));
        return userOIS;
    }

    public static BufferedReader getSettingsInputConnection() throws FileNotFoundException, IOException {
        File spaceInvadersSettingsFile = null;
        BufferedReader settingsOIS = null;
        spaceInvadersSettingsFile = new File(SPACEINVADERS_SETTINGS_DB_NAME);
        //Create the file if it doesnt exist
        if (!spaceInvadersSettingsFile.exists()) {
            new PrintWriter(spaceInvadersSettingsFile).close();
            System.gc();
        }
        settingsOIS = new BufferedReader(new FileReader(spaceInvadersSettingsFile));
        return settingsOIS;
    }

    public static BufferedReader getUserHighScoresInputConnection() throws FileNotFoundException, IOException {
        File spaceInvadersUserHighScoresFile = null;
        BufferedReader userHighScoreOIS = null;
        spaceInvadersUserHighScoresFile = new File(SPACEINVADERS_USER_HIGH_SCORES_DB_NAME);
        //Create the file if it doesnt exist
        if (!spaceInvadersUserHighScoresFile.exists()) {
            new PrintWriter(spaceInvadersUserHighScoresFile).close();
            System.gc();
        }
        userHighScoreOIS = new BufferedReader(new FileReader(spaceInvadersUserHighScoresFile));
        return userHighScoreOIS;
    }

    /**
     * Loads the games high scores for every existing firing interval and move speed
     *
     * @return List of GameDetails objects containing the high scores for each type of game (combination of move speed
     * and firing interval)
     * @throws java.lang.Exception
     */
    public static List<GameDetails> getGameHighScores() throws Exception {
        //Map to store highest score across all users for a given set of game settings
        HashMap<GameSettings, GameDetails> gameHighScoresMap = new HashMap<GameSettings, GameDetails>();
        List<GameDetails> gameHighScoresList = new ArrayList<GameDetails>();
        BufferedReader userHighScoresFile;
        String aLine;
        String userName;
        int firingInterval;
        double moveSpeed;
        GameSettings gameSettings;
        GameDetails gameDetails;
        int savedHighScore;

        //open file for reading
        userHighScoresFile = getUserHighScoresInputConnection();

        //read first line
        aLine = userHighScoresFile.readLine();

        //while line of file exists
        while (aLine != null) {

            //use string tokenizer to read line
            StringTokenizer gameDetailsTok = new StringTokenizer(aLine, ",");
            userName = gameDetailsTok.nextToken();
            moveSpeed = Double.parseDouble(gameDetailsTok.nextToken());
            firingInterval = Integer.parseInt(gameDetailsTok.nextToken());
            gameSettings = new GameSettings(moveSpeed, firingInterval);
            savedHighScore = Integer.parseInt(gameDetailsTok.nextToken());

            //Update the highest score for these settings
            if (gameHighScoresMap.containsKey(gameSettings)) {
                gameDetails = gameHighScoresMap.get(gameSettings);
                if (savedHighScore > gameDetails.getHighScore()) {
                    gameHighScoresMap.put(gameSettings, new GameDetails(new UserDetails(userName), gameSettings));
                }
            } else {
                gameHighScoresMap.put(gameSettings, new GameDetails(new UserDetails(userName), gameSettings));
            }
            aLine = userHighScoresFile.readLine();
        }
        for (GameSettings gs : gameHighScoresMap.keySet()) {
            gameHighScoresList.add(gameHighScoresMap.get(gs));
        }
        Collections.sort(gameHighScoresList, new HighScoresComparator());

        return gameHighScoresList;

    }

    public static boolean myDelete(File aFile) {
        boolean success;
        int tries = 0;
        success = aFile.delete();
        while (!success && tries < 20) {
            tries++;
            try {
                System.gc();
                System.runFinalization();
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
            success = aFile.delete();
        }
        if (!success) {
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.SEVERE,
                "Unable to delete file-> " + aFile.getName() + ". Probably locked by another application.");
        }
        return success;
    }

    /**
     * Needed to sort the high scores List across all users into moveSpeed and FiringInterval order
     */
    static class HighScoresComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            GameDetails g1;
            GameDetails g2;
            int result;

            g1 = (GameDetails) o1;
            g2 = (GameDetails) o2;

            if (g1.getGameSettings().getMoveSpeed() > g2.getGameSettings().getMoveSpeed()) {
                result = 1;
            } else if (g1.getGameSettings().getMoveSpeed() < g2.getGameSettings().getMoveSpeed()) {
                result = -1;
            } else if (g1.getGameSettings().getFiringInterval() > g2.getGameSettings().getFiringInterval()) {
                result = 1;
            } else if (g1.getGameSettings().getFiringInterval() < g2.getGameSettings().getFiringInterval()) {
                result = -1;
            } else {
                result = 0;
            }
            return result;
        }
    }

}
