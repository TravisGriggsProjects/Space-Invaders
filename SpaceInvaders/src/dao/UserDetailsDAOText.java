/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.SpaceInvadersDAOText.getUserHighScoresInputConnection;
import java.io.*;
import java.util.*;
import model.GameDetails;
import model.GameSettings;
import model.UserDetails;

/**
 *
 * @author sruiz
 */
public class UserDetailsDAOText extends SpaceInvadersDAOText {

    /**
     * Loads user data into a user object
     *
     * @param userWanted User details associated with the user will be loaded into this object
     * @throws java.io.FileNotFoundException
     */
    public static void loadData(UserDetails userWanted) throws IOException {

        UserDetails userInputFromFile;
        boolean finished;
        String aLine;
        StringTokenizer userTokeniser;
        BufferedReader userFile;

        userFile = getUsersInputConnection();
        aLine = userFile.readLine();
        finished = (aLine == null);
        while (!finished) {
            userTokeniser = new StringTokenizer(aLine, ",");
            userInputFromFile = new UserDetails();
            userInputFromFile.setUserName(userTokeniser.nextToken());
            userInputFromFile.setPassword(userTokeniser.nextToken());
            userInputFromFile.setWantingIntroInfo(Boolean.parseBoolean(userTokeniser.nextToken()));

            if (userInputFromFile.equals(userWanted)) {
                //set password & wantingIntroInfo
                userWanted.setPassword(userInputFromFile.getPassword());
                userWanted.setWantingIntroInfo(userInputFromFile.isWantingIntroInfo());
                finished = true;
            }
            if (!finished) {
                aLine = userFile.readLine();
                finished = (aLine == null);
            }
        }

        userFile.close();
        System.gc();
    }

    /**
     * Saves data of given objects to the database. If user settings and/or game settings don't exist new data is
     * inserted else existing data is updated SaveData does the saving using PreparedStatments.
     *
     * @param userToBeSaved UserDetails to save
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public static void saveData(UserDetails userToBeSaved) throws FileNotFoundException, IOException,
        ClassNotFoundException {

        UserDetails userInputFromFile;
        boolean finished;
        String aLine;
        StringTokenizer userTokeniser;
        PrintWriter tempOutputFile;
        File tempFile;
        boolean needToAddUser;
        BufferedReader userFile;

        // Approach is to create a temp file which is the new version with the required update of data then
        // delete the original userDetails file and rename the temp file to become the new userDetails file version.
        tempFile = new File("temp_users.csv");
        tempOutputFile = new PrintWriter(tempFile);
        // Read in the existing user csv file copying the data over to the temp file but updating the user details
        // for the user passed in, userToBeSaved, or adding it if the useer does not yet exist in the file.
        userFile = getUsersInputConnection();
        needToAddUser = true;
        aLine = userFile.readLine();
        finished = (aLine == null);
        while (!finished) {
            userTokeniser = new StringTokenizer(aLine, ",");
            userInputFromFile = new UserDetails();
            userInputFromFile.setUserName(userTokeniser.nextToken());
            userInputFromFile.setPassword(userTokeniser.nextToken());
            userInputFromFile.setWantingIntroInfo(Boolean.parseBoolean(userTokeniser.nextToken()));
            if (userInputFromFile.equals(userToBeSaved)) {
                //write user passed in
                tempOutputFile.println(
                    userToBeSaved.getUserName() + "," + userToBeSaved.getPassword() + "," + userToBeSaved.
                    isWantingIntroInfo());
                needToAddUser = false;
            } else {
                //else write user read from file
                tempOutputFile.println(aLine);
            }
            aLine = userFile.readLine();
            finished = (aLine == null);

        }

        //if need to add user, do so to end of list
        if (needToAddUser) {
            tempOutputFile.println(
                userToBeSaved.getUserName() + "," + userToBeSaved.getPassword() + "," + userToBeSaved.
                isWantingIntroInfo());
        }
        userFile.close();
        tempOutputFile.close();

        //delete old file and rename temp file
        myDelete(new File(SpaceInvadersDAOText.SPACEINVADERS_USERS_DB_NAME));
        tempFile.renameTo(new File(SpaceInvadersDAOText.SPACEINVADERS_USERS_DB_NAME));
    }

    /**
     * Loads users high scores for all games
     *
     * @param user The users details.
     * @throws java.sql.SQLException
     * @return List of GameDetails objects containing the highscores for each type of game (combination of move speed
     * and firing interval) that exists for this user in the database
     */
    public static List<GameDetails> getHighScores(UserDetails user) throws Exception {
        List<GameDetails> usersHighScoresList = new ArrayList<GameDetails>();
        //declare variables
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

            //if line matches for userName then include in data to return
            if (userName.equals(user.getUserName())) {
                gameDetails = new GameDetails(user, gameSettings);
                gameDetails.setHighScore(savedHighScore);
                usersHighScoresList.add(gameDetails);
            }
            aLine = userHighScoresFile.readLine();
        }
        userHighScoresFile.close();

        return usersHighScoresList;
    }
}
