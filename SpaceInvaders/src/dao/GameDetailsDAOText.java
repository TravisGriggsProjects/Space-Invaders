package dao;

/**
 * <p>
 * Title: GameDetailsDAOText</p>
 *
 * <p>
 * Description: Contains the Data Access methods to handle saving and loading game details to text files</p>
 *
 * <p>
 * Copyright: Copyright 2018</p>
 *
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @version 1.0
 */
import controller.MyLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

/**
 * Database access object for Model element GameDetails. The only game detail we are storing is the highScore, but the
 * high score is for a particular, username+firingInterval+moveSpeed
 *
 */
public class GameDetailsDAOText extends SpaceInvadersDAOText {

    /**
     *
     * @param gameDetails the gameDetails object containing all the data to be saved
     * @return true if all saving was successful otherwise false
     * @throws Exception - either file not found or another IO Exception
     */
    public static boolean saveData(GameDetails gameDetails) throws Exception {
        boolean saveResult;
        UserDetailsDAOText.saveData(gameDetails.getUserDetails());
        GameSettingsDAOText.saveData(gameDetails.getUserDetails(), gameDetails.getGameSettings());
        saveResult = saveHighScore(gameDetails);
        return saveResult;
    }

    /**
     *
     * @param gameDetails the gameDetails object containing all the data to be saved
     * @return true if all loading was successful otherwise false
     * @throws Exception - either file not found or another IO Exception
     */
    public static boolean loadData(GameDetails gameDetails) throws Exception {
        // Get the UserDetails and the GameSettings to do their bit
        UserDetailsDAOText.loadData(gameDetails.getUserDetails());
        GameSettingsDAOText.loadData(gameDetails.getUserDetails(), gameDetails.getGameSettings());
        return true;
    }

    /**
     * Saves high Score for a userName for the game settings currently userHighScoresFile use. The approach is to create
     * a temp file and copy all entries from the existing highscore file into the temp file updating the entry matching
     * the provided username+settings record if the provided highscore is higher than that already in the the file. If
     * it doesn't find a record matching the provided username+settings info then it will write the provided data as a
     * new entry in the temp file. After finishing all processing of records it will delete the old highscore file
     * rename the temp file so it becomes the new highscore file.
     *
     * The userHighScoreFile is a csv file with the following format per line
     * userName,moveSpeed,firingInterval,highscore with the datatype of each item being String,double,integer,integer
     *
     * @param gameDetails the gameDetails object containing all the data (username and gamesettings) for which a
     * highscore needs to be saved
     * @return true if the saving was completed successfully
     * @throws java.io.IOException - occurs if something goes wrong with the reading of the file.
     */
    public static boolean saveHighScore(GameDetails gameDetails) throws IOException {
        String aLine;
        String userName;
        int firingInterval;
        double moveSpeed;
        int highScoreInGameDetailsObject;
        int highScoreInFile;
        File tempHighScoresFile;
        BufferedReader userHighScoresFile;
        boolean needToAddNewEntry = true;
        boolean deleteSuccess = false;

        // Current high score setting passed in
        highScoreInGameDetailsObject = gameDetails.getHighScore();

        // Create a temp file to write updated data
        tempHighScoresFile = new File("temp_highscores.csv");
        FileWriter outFileWriter = new FileWriter(tempHighScoresFile);
        PrintWriter tempHighScoreFilePW = new PrintWriter(outFileWriter);

        try {
            // Connect to the existing user high score file
            userHighScoresFile = getUserHighScoresInputConnection();

            // Read the first line of the file
            aLine = userHighScoresFile.readLine();

            // While we have a valid line, process the line
            while (aLine != null) {
                // Extract the components of the line (userName, moveSpeed, firingInterval, highScoreInFile)
                StringTokenizer tokenizer = new StringTokenizer(aLine, ",");
                userName = tokenizer.nextToken();
                moveSpeed = Double.parseDouble(tokenizer.nextToken());
                firingInterval = Integer.parseInt(tokenizer.nextToken());
                highScoreInFile = Integer.parseInt(tokenizer.nextToken());

                // Compare the line data with the data in gameDetails
                if (userName.equals(gameDetails.getUserDetails().getUserName()) &&
                    moveSpeed == gameDetails.getGameSettings().getMoveSpeed() &&
                    firingInterval == gameDetails.getGameSettings().getFiringInterval()) {

                    needToAddNewEntry = false;

                    // Update high score if the new one is higher
                    if (highScoreInGameDetailsObject > highScoreInFile) {
                        tempHighScoreFilePW.println(userName + "," + moveSpeed + "," + firingInterval + "," + highScoreInGameDetailsObject);
                    } else {
                        tempHighScoreFilePW.println(userName + "," + moveSpeed + "," + firingInterval + "," + highScoreInFile);
                    }
                } else {
                    // Write the line read from the file back to the temp file
                    tempHighScoreFilePW.println(aLine);
                }

                // Read the next line
                aLine = userHighScoresFile.readLine();
            }

            // Close the original file
            userHighScoresFile.close();

        } catch (FileNotFoundException e) {
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.INFO, "High Score CSV does not exist, will be created...");
        }

        // If the entry was not found, add it as a new record
        if (needToAddNewEntry) {
            userName = gameDetails.getUserDetails().getUserName();
            firingInterval = gameDetails.getGameSettings().getFiringInterval();
            moveSpeed = gameDetails.getGameSettings().getMoveSpeed();
            tempHighScoreFilePW.println(userName + "," + moveSpeed + "," + firingInterval + "," + highScoreInGameDetailsObject);
        }

        // Close the temp file
        tempHighScoreFilePW.close();

        // Delete the old high score file and rename the temp file
        deleteSuccess = myDelete(new File(SpaceInvadersDAOText.SPACEINVADERS_USER_HIGH_SCORES_DB_NAME));
        tempHighScoresFile.renameTo(new File(SpaceInvadersDAOText.SPACEINVADERS_USER_HIGH_SCORES_DB_NAME));

        return deleteSuccess;
    }

    /**
     * Loads high Score for a particular userName for the game settings currently in use
     *
     * @param gameDetails - contains the user and settings details, will have its highscore value set if a matching
     * entry is found in the file
     * @throws java.lang.Exception
     */
    public static void loadHighScore(GameDetails gameDetails) throws Exception {

        //declare variables
        BufferedReader userHighScoresFile;
        String aLine;
        String userName;
        int firingInterval;
        double moveSpeed;
        GameSettings gameSettings;
        int savedHighScore;
        boolean finished;

        //open file for reading
        userHighScoresFile = getUserHighScoresInputConnection();

        //read first line
        aLine = userHighScoresFile.readLine();
        finished = (aLine == null);

        //while line of file exists and we have not found the username we want.
        while (!finished) {

            //use string tokenizer to read line
            StringTokenizer gameDetailsTok = new StringTokenizer(aLine, ",");
            userName = gameDetailsTok.nextToken();
            moveSpeed = Double.parseDouble(gameDetailsTok.nextToken());
            firingInterval = Integer.parseInt(gameDetailsTok.nextToken());
            gameSettings = new GameSettings(moveSpeed, firingInterval);
            savedHighScore = Integer.parseInt(gameDetailsTok.nextToken());

            //if line matches for userName and gamesettings, set highscore
            if (userName.equals(gameDetails.getUserDetails().getUserName())
                && gameSettings.equals(gameDetails.getGameSettings())) {
                gameDetails.setHighScore(savedHighScore);
                //We are done now so set finished so loop exits
                finished = true;
            }
            if (!finished) {
                //read next line
                aLine = userHighScoresFile.readLine();
                finished = (aLine == null);
            }
        }
        userHighScoresFile.close();
    }
}