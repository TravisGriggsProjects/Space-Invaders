/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testHarnesses;

import model.BorderEntity;

public class BorderEntityTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

         // Testing no-arg constructor BorderEntity(), expected outcome: id='None', width=0, height=0
        System.out.println("Testing no-arg constructor BorderEntity(), expected outcome: id='None', width=0, height=0, xPos=0, yPos=0");
        BorderEntity border1 = new BorderEntity();
        System.out.println("Actual outcome: " + border1 + "\n");

        // Testing 1-arg constructor BorderEntity(String id), expected outcome: id='100', width=0, height=0
        System.out.println("Testing 1-arg constructor BorderEntity(String id), expected outcome: id='100', width=0, height=0, xPos=0, yPos=0");
        BorderEntity border2 = new BorderEntity("100");
        System.out.println("Actual outcome: " + border2 + "\n");

        // Testing 3-arg constructor BorderEntity(String id, int width, int height), expected outcome: id='200', width=300, height=400
        System.out.println("Testing 3-arg constructor BorderEntity(String id, int width, int height), expected outcome: id='200', width=300, height=400, xPos=0, yPos=0");
        BorderEntity border3 = new BorderEntity("200", 300, 400);
        System.out.println("Actual outcome: " + border3 + "\n");

         // Testing 4-arg constructor BorderEntity(int xPos, int yPos, int width, int height), expected outcome: id='None', width=400, height=500
        System.out.println("Testing 4-arg constructor BorderEntity(int xPos, int yPos, int width, int height), expected outcome: id='None', width=400, height=500, xPos=150, yPos=250");
        BorderEntity border4 = new BorderEntity(150, 250, 400, 500);
        System.out.println("Actual outcome: " + border4 + "\n");

        // Testing all-arg constructor BorderEntity(String id, int xPos, int yPos, int width, int height), expected outcome: id='300', width=500, height=600
        System.out.println("Testing all-arg constructor BorderEntity(String id, int xPos, int yPos, int width, int height), expected outcome: id='300', width=500, height=600, xPos=100, yPos=200");
        BorderEntity border5 = new BorderEntity("300", 100, 200, 500, 600);
        System.out.println("Actual outcome: " + border5 + "\n");
        
        // Testing setters and getters, expected outcome: id='123', width=350, height=450, xPos=80, yPos=40
        System.out.println("Testing setters and getters, expected outcome: id='123', width=350, height=450, xPos=80, yPos=40");
        border1.setId("123");
        border1.setWidth(350);
        border1.setHeight(450);
                
        System.out.println("Actual outcome:");
        System.out.println("id=" + border1.getId());
        System.out.println("width=" + border1.getWidth());
        System.out.println("height=" + border1.getHeight());
        
        // Testing toString() method
        System.out.println("Testing toString() method");
        System.out.println("Actual outcome: " + border1.toString());
    }    

}
