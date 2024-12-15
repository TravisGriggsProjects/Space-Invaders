/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.SpaceInvadersDAOText.getSettingsInputConnection;
import static dao.SpaceInvadersDAOText.myDelete;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import model.GameSettings;
import model.UserDetails;

/**
 *
 * @author sruiz
 */
public class GameSettingsDAOText {

    /**
     * Loads settings data for the settings last played by the user
     *
     * @param userWanted User details associated with the user will be loaded into this object
     * @param gameSettingsWanted
     * @throws java.io.FileNotFoundException
     */
    public static void loadData(UserDetails userWanted, GameSettings gameSettingsWanted)
        throws IOException {

        String userInputFromFile;
        boolean finished;
        String aLine;
        StringTokenizer userTokeniser;
        BufferedReader settingsOIS;

        settingsOIS = getSettingsInputConnection();
        aLine = settingsOIS.readLine();
        finished = (aLine == null);
        while (!finished) {
            userTokeniser = new StringTokenizer(aLine, ",");
            userInputFromFile = userTokeniser.nextToken();

            if (userInputFromFile.equals(userWanted.getUserName())) {
                //set password & wantingIntroInfo
                gameSettingsWanted.setFiringInterval(Integer.parseInt(userTokeniser.nextToken()));
                gameSettingsWanted.setMoveSpeed(Double.parseDouble(userTokeniser.nextToken()));
                finished = true;
            }
            if (!finished) {
                aLine = settingsOIS.readLine();
                finished = (aLine == null);
            }
        }

        settingsOIS.close();

    }

    /**
     * Saves data of given objects to the database. If user settings and/or game settings don't exist new data is
     * inserted else existing data is updated SaveData does the saving using PreparedStatments.
     *
     * @param userForWhichGameSettingsAreToBeSaved UserDetails to save
     * @param gameSettingsToBeSaved
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public static void saveData(UserDetails userForWhichGameSettingsAreToBeSaved, GameSettings gameSettingsToBeSaved)
        throws FileNotFoundException, IOException,
        ClassNotFoundException {

        String userInputFromFile;
        boolean finished;
        String aLine;
        StringTokenizer userTokeniser;
        PrintWriter tempOutputFile;
        File tempFile;
        boolean needToAddUser;
        BufferedReader settingsOIS;

        // Approach is to create a temp file which is the new version with the required update of data then
        // delete the original userDetails file and rename the temp file to become the new userDetails file version.
        tempFile = new File("temp_settings.csv");
        tempOutputFile = new PrintWriter(tempFile);
        // Read in the existing settings csv file copying the data over to the temp file but updating the settings details
        // for the user passed in, userForWhichGameSettingsAreToBeSaved, to be the new ones, gameSettingsToBeSaved,
        // or adding it if the useer does not yet exist in the file.
        settingsOIS = getSettingsInputConnection();
        needToAddUser = true;
        aLine = settingsOIS.readLine();
        finished = (aLine == null);
        while (!finished) {
            userTokeniser = new StringTokenizer(aLine, ",");
            userInputFromFile = userTokeniser.nextToken();
            if (userInputFromFile.equals(userForWhichGameSettingsAreToBeSaved.getUserName())) {
                //write user passed in with the new settings
                tempOutputFile.println(
                    userForWhichGameSettingsAreToBeSaved.getUserName() + ","
                    + gameSettingsToBeSaved.getFiringInterval() + ","
                    + gameSettingsToBeSaved.getMoveSpeed());
                needToAddUser = false;
            } else {
                //else write user and settings read from file
                tempOutputFile.println(aLine);
            }
            aLine = settingsOIS.readLine();
            finished = (aLine == null);

        }

        //if need to add user, do so to end of list
        if (needToAddUser) {
            tempOutputFile.println(
                userForWhichGameSettingsAreToBeSaved.getUserName() + ","
                + gameSettingsToBeSaved.getFiringInterval() + ","
                + gameSettingsToBeSaved.getMoveSpeed());
        }
        settingsOIS.close();
        tempOutputFile.close();

        //delete old file and rename temp file
        myDelete(new File(SpaceInvadersDAOText.SPACEINVADERS_SETTINGS_DB_NAME));
        tempFile.renameTo(new File(SpaceInvadersDAOText.SPACEINVADERS_SETTINGS_DB_NAME));
    }

}
