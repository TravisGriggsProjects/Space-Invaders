/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testHarnesses;

import model.AlienEntity;
import model.EntityListArray;
import model.ShipEntity;

public class EntityListArrayTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //create new EntityListArray
        EntityListArray entityListArray = new EntityListArray();

        //create an AlienEntity object and add to EntityListArray
        AlienEntity alienEntity1 = new AlienEntity(30, 40);
        entityListArray.add(alienEntity1);

        //create a ShipEntity object and add to EntityListArray
        ShipEntity shipEntity1 = new ShipEntity(250, 350);
        entityListArray.add(shipEntity1);

        System.out.println("entitylistarray get num entities (should be 2): " + entityListArray.getNumEntities());

        //create a new AlienEntity and ShipEntity and set horizontal and vertical movement
        AlienEntity alienEntity2 = new AlienEntity(300, 10);
        alienEntity2.setVerticalMovement(7000);
        alienEntity2.setHorizontalMovement(7000);

        ShipEntity shipEntity2 = new ShipEntity(100, 100);
        shipEntity2.setVerticalMovement(200);
        shipEntity2.setHorizontalMovement(2000);

        //add to EntityListArray
        entityListArray.add(alienEntity2);
        entityListArray.add(shipEntity2);

        System.out.println("entitylistarray get num entities (should be 4): " + entityListArray.getNumEntities());
        System.out.println("EntityListArray =" + entityListArray);

        //check random
        System.out.println("Get random entity");
        System.out.println(entityListArray.getRandom());

        //check remove
        entityListArray.remove(alienEntity2);
        System.out.println("removed an entity");
        System.out.println("entitylistarray get num entities (should be 3): " + entityListArray.getNumEntities());
        System.out.println("The following should be removed - Alien Entity with data: 300, 10, 7000, 7000");
        System.out.println("EntityListArray =" + entityListArray);

        //check collides with
        System.out.println("does shipEntity2 collide with something in the list (should be null)? " + entityListArray.
            collidesWith(shipEntity2));

        //check collides with with two entities at same position
        AlienEntity alienEntity3 = new AlienEntity(30, 40);
        entityListArray.add(alienEntity3);
        System.out.println("does alienEntity3 collide with something in the list (YES, should print the entity)? " + entityListArray.
            collidesWith(alienEntity3));

        //clear
        entityListArray.clear();
        System.out.println("Cleared all entities");
        System.out.println("entitylistarray get num entities: (should be 0)" + entityListArray.getNumEntities());

    }

}
