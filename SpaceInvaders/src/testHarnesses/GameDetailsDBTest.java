/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testHarnesses;

import dao.GameDetailsDAOText;
import dao.SpaceInvadersDAOText;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import model.GameDetails;
import model.GameSettings;
import model.UserDetails;

public class GameDetailsDBTest {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {

        GameDetails gameDetailsForTest;
        UserDetails userDetailsForTest;
        GameSettings gameSettingsForTest;
        boolean saveResult;
        Scanner kbd;
        char choice;

        kbd = new Scanner(System.in);

        System.out.println("WARNING - running this test will delete the current high scores file.");
        System.out.println(
            "Save the file under a different name before running if you want to keep the old high scores");
        System.out.print("Do you want to continue (Y/N):");
        choice = Character.toUpperCase(kbd.next().charAt(0));
        if (choice == 'Y') {

            // Delete the highscores file so that testing starts afresh each run
            SpaceInvadersDAOText.myDelete(new File(SpaceInvadersDAOText.SPACEINVADERS_USER_HIGH_SCORES_DB_NAME));

            //Create a user and settings with a particular high score of 500
            userDetailsForTest = new UserDetails("TestUserName", "TestUserNamePassword");
            gameSettingsForTest = new GameSettings(100, 200);
            gameDetailsForTest = new GameDetails(userDetailsForTest, gameSettingsForTest);
            gameDetailsForTest.setHighScore(500);
            saveResult = GameDetailsDAOText.saveData(gameDetailsForTest);
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Test for gameDetails -> (" + gameDetailsForTest);
            System.out.println("Result of first save, expect true. Got -> " + saveResult);
            //Reset the high score for this user and settings to be 0 then re load from high score file - should get 500
            gameDetailsForTest.setHighScore(0);
            System.out.println("gameDetails highscore before read, expect 0, Got -> " + gameDetailsForTest.
                getHighScore());
            GameDetailsDAOText.loadHighScore(gameDetailsForTest);
            System.out.println("gameDetails highscore after read, expect 500, Got -> " + gameDetailsForTest.
                getHighScore());

            //Now set highscore to be 600 for same user and settings and then repeat save and load
            gameDetailsForTest.setHighScore(600);
            saveResult = GameDetailsDAOText.saveData(gameDetailsForTest);
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Test for gameDetails -> (" + gameDetailsForTest);
            System.out.println("Result of second save, expect true. Got -> " + saveResult);
            //Reset the high score for this user and settings to be 0 then re load from high score file - should get 600
            gameDetailsForTest.setHighScore(0);
            System.out.println("gameDetails highscore before read, expect 0, Got -> " + gameDetailsForTest.
                getHighScore());
            GameDetailsDAOText.loadHighScore(gameDetailsForTest);
            System.out.println("gameDetails highscore after read, expect 600, Got -> " + gameDetailsForTest.
                getHighScore());

            //Now add another setting for same user
            gameSettingsForTest = new GameSettings(300, 300);
            gameDetailsForTest = new GameDetails(userDetailsForTest, gameSettingsForTest);
            gameDetailsForTest.setHighScore(1000);
            saveResult = GameDetailsDAOText.saveData(gameDetailsForTest);
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Test for gameDetails -> (" + gameDetailsForTest);
            System.out.println("Result of third save, expect true. Got -> " + saveResult);
            //Reset the high score for this user and settings to be 0 then re load from high score file - should get 500
            gameDetailsForTest.setHighScore(0);
            System.out.println("gameDetails highscore before read, expect 0, Got -> " + gameDetailsForTest.
                getHighScore());
            GameDetailsDAOText.loadHighScore(gameDetailsForTest);
            System.out.println("gameDetails highscore after read, expect 1000, Got -> " + gameDetailsForTest.
                getHighScore());

            //Now add another user
            userDetailsForTest = new UserDetails("TestUserName2", "TestUserNamePassword2");
            gameSettingsForTest = new GameSettings(100, 200);
            gameDetailsForTest = new GameDetails(userDetailsForTest, gameSettingsForTest);
            gameDetailsForTest.setHighScore(5000);
            saveResult = GameDetailsDAOText.saveData(gameDetailsForTest);
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Test for gameDetails -> (" + gameDetailsForTest);
            System.out.println("Result of fourth save, expect true. Got -> " + saveResult);
            //Reset the high score for this user and settings to be 0 then re load from high score file - should get 5000
            gameDetailsForTest.setHighScore(0);
            System.out.println("gameDetails highscore before read, expect 0, Got -> " + gameDetailsForTest.
                getHighScore());
            GameDetailsDAOText.loadHighScore(gameDetailsForTest);
            System.out.println("gameDetails highscore after read, expect 5000, Got -> " + gameDetailsForTest.
                getHighScore());

            //Now add another user with a very high score for same settings as user2
            userDetailsForTest = new UserDetails("TestUserName3", "TestUserNamePassword3");
            gameSettingsForTest = new GameSettings(100, 200);
            gameDetailsForTest = new GameDetails(userDetailsForTest, gameSettingsForTest);
            gameDetailsForTest.setHighScore(10000);
            saveResult = GameDetailsDAOText.saveData(gameDetailsForTest);
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Test for gameDetails -> (" + gameDetailsForTest);
            System.out.println("Result of fith save, expect true. Got -> " + saveResult);

            //Reload high score for  TestUserName2 - should not have been affected by TestUSerName3 high score save
            userDetailsForTest = new UserDetails("TestUserName2", "TestUserNamePassword2");
            gameSettingsForTest = new GameSettings(100, 200);
            gameDetailsForTest = new GameDetails(userDetailsForTest, gameSettingsForTest);
            GameDetailsDAOText.loadHighScore(gameDetailsForTest);
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Test for gameDetails -> (" + gameDetailsForTest);
            System.out.println("gameDetails highscore for after read, expect 5000, Got -> " + gameDetailsForTest.
                getHighScore());
        } else {
            System.out.println("Test Run aborted.");
        }
    }

}
