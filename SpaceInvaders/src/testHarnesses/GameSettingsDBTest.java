package testHarnesses;

import dao.GameSettingsDAOText;
import dao.SpaceInvadersDAOText;
import model.GameSettings;
import model.UserDetails;

/**
 * <p>
 * Title: Misc</p>
 *
 * <p>
 * Description: </p>
 *
 * <p>
 * Copyright: Copyright (c) 2005</p>
 *
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @version 1.0
 */
public class GameSettingsDBTest {

    /**
     * **************************************************
     */
    /**
     * test harness
     */
    public static void main(String args[]) throws Exception {
        System.out.println("Test saving to a database");
        SpaceInvadersDAOText.connectToSpaceInvadersDatabase();
        UserDetails testUser;
        GameSettings testSettings;

        String userName;
        String password;
        testUser = new UserDetails("1", "1");
        double moveSpeed;
        int firingInterval;
        testUser.saveData();
        //test loads up defaults
        testSettings = new GameSettings();

        System.out.println("UserDetails (1) = " + testUser);
        System.out.println("Settings (1) = " + testSettings);
        // Test Save
        userName = "TEST USER2";
        password = "TEST PASSWORD2";

        testUser = new UserDetails(userName, password);

        testUser.saveData();
        GameSettingsDAOText.saveData(testUser, testSettings);
        System.out.println("GameSettings (SAVED) = " + testSettings);

        moveSpeed = 100;
        firingInterval = 100;
        testSettings = new GameSettings(moveSpeed, firingInterval);
        GameSettingsDAOText.saveData(testUser, testSettings);
        System.out.println("GameSettings (DATA CHANGED & SAVED) = " + testSettings);

        //demonstrate movespeed/firinginterval doesn't change unless save
        testSettings.setMoveSpeed(500);
        testSettings.setFiringInterval(200);
        //didn't save so will upload old settings
        GameSettingsDAOText.loadData(testUser, testSettings);
        System.out.println("GameSettings (LOADED)" + testSettings);
        System.out.println();

        //now change movespeed and firinginterval and save, then will change
        testSettings.setMoveSpeed(500);
        testSettings.setFiringInterval(200);
        GameSettingsDAOText.saveData(testUser, testSettings);
        System.out.println("GameSettings (DATA CHANGED) = " + testSettings);
        GameSettingsDAOText.loadData(testUser, testSettings);
        System.out.println("GameSettings (SAVED RELOADED) = " + testSettings);

    }
}
