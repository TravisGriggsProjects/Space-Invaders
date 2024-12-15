/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testHarnesses;

import java.sql.SQLException;
import model.GameDetails;
import model.UserDetails;
import model.GameSettings;

/**
 * <p>
 * Title:UserDetailsTest</p>
 * <p>
 * Description: GameDetails Class Test</p>
 * <p>
 * Copyright: Copyright (c) 2006</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Travis Griggs 25/08/2024
 * @version 2.0

 */

public class GameDetailsTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, Exception {

        // Testing the no-arg constructor GameDetails()
        System.out.println("Testing no-arg constructor GameDetails(), expected outcome: userDetails{userName='NO NAME', password=null, wantingIntroInfo=true}, GameSettings{moveSpeed=300.0, firingInterval=700}, highScore=0, score=0, shipsDestroyed=0, shotsFired=0, newHighScore=false");
        GameDetails game1 = new GameDetails();
        System.out.println("Actual outcome: " + game1 + "\n");
        
        // Testing all-arg constructor GameDetails(UserDetails user, GameSettings gameSettings)
        System.out.println("Testing all-arg constructor GameDetails(UserDetails user, GameSettings gameSettings), expected outcome: UserDetails{userName='PlayerOne', password='password1234', wantingIntroInfo=true}, GameSettings{moveSpeed=500.0, firingInterval=600}, highScore=0, score=0, shipsDestroyed=0, shotsFired=0, newHighScore=false");
        UserDetails user2 = new UserDetails("PlayerOne", "password1234", true);
        GameSettings settings2 = new GameSettings(500, 600);
        GameDetails game2 = new GameDetails(user2, settings2);
        System.out.println("Actual outcome: " + game2 + "\n");
        
        // Testing setters and getters
        System.out.println("Testing setters and getters, expected outcome: UserDetails{userName='PlayerTwo', password='password456', wantingIntroInfo=false}, GameSettings{moveSpeed=400, firingInterval=500}, highScore=2000, score=1500, shipsDestroyed=10, shotsFired=20, newHighScore=true");

        game2.setUserDetails(new UserDetails("PlayerTwo", "password456", false));
        game2.setGameSettings(new GameSettings(400, 500));
        game2.setHighScore(2000);
        game2.setScore(1500);
        game2.setShipsDestroyed(10);
        game2.setShotsFired(20);
        game2.setNewHighScore(true);

        System.out.println("Actual outcome:");
        System.out.println("userDetails=" + game2.getUserDetails());
        System.out.println("gameSettings=" + game2.getGameSettings());
        System.out.println("highScore=" + game2.getHighScore());
        System.out.println("score=" + game2.getScore());
        System.out.println("shipsDestroyed=" + game2.getShipsDestroyed());
        System.out.println("shotsFired=" + game2.getShotsFired());
        System.out.println("newHighScore=" + game2.isNewHighScore() + "\n");
        
        // Testing toString method
        System.out.println("Testing toString method, expected outcome: UserDetails{userName='PlayerTwo', password='password456', wantingIntroInfo=false}, GameSettings{moveSpeed=400.0, firingInterval=500}, highScore=2000, score=1500, shipsDestroyed=10, shotsFired=20, newHighScore=true");
        System.out.println("Actual outcome: " + game2.toString() + "\n");
        
        // Testing equals method
        System.out.println("Testing equals method, expected outcome: game1 = game2 false, game1 = game 3 true");
        GameDetails game3 = new GameDetails(new UserDetails("NO NAME", null, true), new GameSettings(300, 700));
        System.out.println("Actual outcome: game1 = game2 " + game1.equals(game2) + "\n");    
        System.out.println("Actual outcome: game1 = game 3 " + game1.equals(game3) + "\n");
    
    }

}
