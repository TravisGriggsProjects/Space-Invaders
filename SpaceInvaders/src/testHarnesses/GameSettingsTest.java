
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testHarnesses;

import model.GameSettings;

/**
 * <p>
 * Title:UserDetailsTest</p>
 * <p>
 * Description: GameSettings Class Test</p>
 * <p>
 * Copyright: Copyright (c) 2006</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Travis Griggs 19/08/2024
 * @version 2.0

 */

public class GameSettingsTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Testing all-arg constructor GameSettings(double moveSpeed, int firingInterval)
        System.out.println("Testing all-arg constructor GameSettings(double moveSpeed, int firingInterval), expected outcome: moveSpeed=400.0, firingInterval=600");
        GameSettings settings1 = new GameSettings(400.0, 600);
        System.out.println("Actual outcome: " + settings1 + "\n");
        
        // Testing no-arg constructor GameSettings()
        System.out.println("Testing no-arg constructor GameSettings(), expected outcome: moveSpeed=300.0, firingInterval=700");
        GameSettings settings2 = new GameSettings();
        System.out.println("Actual outcome: " + settings2 + "\n");

        // Testing setters and getters
        System.out.println("Testing setters and getters, expected outcome: moveSpeed=500.0, firingInterval=800");
        settings2.setMoveSpeed(500.0);
        settings2.setFiringInterval(800);
        System.out.println("Actual outcome" + "\n" + "moveSpeed: " + settings2.getMoveSpeed() + ", firingInterval: " + settings2.getFiringInterval()+ "\n");

        // Testing toString()
        System.out.println("Testing toString(), expected outcome: GameSettings{moveSpeed=500.0, firingInterval=800}");
        System.out.println("Actual outcome" + "\n" + settings2.toString()+ "\n");
        
        // Testing equals method
        System.out.println("Testing equals method, expected outcome: settings1.equals(settings3) == true, settings1.equals(settings2) == false");
        GameSettings settings3 = new GameSettings(400.0, 600);
        System.out.println("Actual outcome" + "\n" + "settings1.equals(settings3): " + settings1.equals(settings3)+ "\n"); // should be true
        System.out.println("settings1.equals(settings2): " + settings1.equals(settings2)); // should be false
    }
}
