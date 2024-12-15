package testHarnesses;

import dao.SpaceInvadersDAOText;
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
 * @author S Ruiz
 * @version 1.0
 */
public class UserDetailsDBTestHarness {

    public UserDetailsDBTestHarness() {
    }

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

        String userName;
        String password;
        testUser = new UserDetails("1", "1");
        testUser.saveData();

        System.out.println("UserDetails (1) = " + testUser);
        // Test Save
        userName = "TEST USER2";
        password = "TEST PASSWORD2";
        testUser = new UserDetails(userName);
        testUser.setPassword(password);
        testUser.setWantingIntroInfo(false);
        testUser.saveData();

        System.out.println("UserDetails (SAVED) = " + testUser);
        testUser.setPassword("NOT THIS!");
        testUser.setWantingIntroInfo(true);
        System.out.println("UserDetails (DATA CHANGED) = " + testUser);
        testUser.loadData();
        System.out.println("UserDetails (SAVED RELOADED) = " + testUser);

    }
}
