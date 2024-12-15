package model;

/**
 * <p>
 * Title: Alien Army</p>
 * <p>
 * Description: An army of alien ships that work as a group</p>
 * <p>
 * Copyright: Copyright (c) 2005</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @version 1.0
 */
import controller.*;
import model.events.*;

public class AlienArmyArray extends EntityListArray implements EntityEventListener {
//	public class AlienArmy extends EntityList implements EntityEventListener {

    int rows;
    int cols;
    // The army advance speed in pixels when the army moves towards the ship
//  private static int ADVANCE_SPEED = 40;
    // The speed of horizontal movement is increased by this percentage each time the army
    // looses an alien ship
    private static int INCREASE_MOVE_SPEED_PERCENT = 2;

    /**
     * AlienArmy
     *
     * @param game Game
     * @param rows int
     * @param cols int
     */
    public AlienArmyArray(Game game, int rows, int cols) {

        super();

        int row;
        int col;
        AlienEntity alien;

        this.rows = rows;
        this.cols = cols;
        for (row = 0; row < rows; row++) {
            for (col = 0; col < cols; col++) {
                //TODO Need to make these positioning calculations more generic - based on the number of rows and cols
                // NOTE - not part of assignment
                alien = new AlienEntity(100 + (col * 50), (50) + row * 40);
//        alien = new AlienEntity(100 + (col * 50), (50) + row * 140);
                this.add(alien);

                // Register as wanting to know of any alien events as we may need to
                // change the army eg. change the moving direction if an alien army member
                // hits the side borders or increase the movement speed if the army looses a ship
                // (ie the ship dies)
                alien.addEntityEventListener(this);
            }
        }
    }

    /**
     * setAnimated sets animated status of all aliens
     */
    public void setAnimated(boolean value) {
        AlienEntity alien;

        for (Entity entity : entities) {
            if (entity instanceof AlienEntity) {
                alien = (AlienEntity) entity;
                alien.setAnimated(value);

            }
        }
    }

    /**
     * ************** Respond to the Three events that can occur **************
     */
    /**
     * entityMoved
     *
     * @param e EntityMoveEvent
     */
    public void entityMoved(EntityMoveEvent e) {
        AlienEntity alien;
        // Every time an alien army member dies increase the speed of all aliens in the army
        if (e.getEntity() instanceof AlienEntity) {
            alien = (AlienEntity) e.getEntity();
            if (alien.getY() >= 500) {
                registerEntityForRemoval(alien);
            }
        }
    }

    /**
     * collisionHasOccured for the army will move all entries in the army when the side borders are hit
     *
     * @param e EntityHitEvent
     */
    public void collisionHasOccured(EntityHitEvent e) {
        Entity entity;
        int i;

        if (e.getHitBy() instanceof BorderEntity) {
            BorderEntity border;
            border = (BorderEntity) e.getHitBy();
            for (i = 0; i < numEntities; i++) {
                entity = entities[i];
                if (border.getId().equals("RIGHT") && entity.getHorizontalMovement() > 0) {
                    entity.moveTo(entity.getX() - 10, entity.getY() + AlienEntity.ADVANCE_SPEED);
                    entity.setHorizontalMovement(-entity.getHorizontalMovement());
                } else if (border.getId().equals("LEFT") && entity.getHorizontalMovement() < 0) {
                    entity.moveTo(entity.getX() + 10, entity.getY() + AlienEntity.ADVANCE_SPEED);
                    entity.setHorizontalMovement(-entity.getHorizontalMovement());
                }
            }
        }
    }

    /**
     * entityDied
     *
     * @param e EntityDiedEvent
     */
    public void entityDied(EntityDiedEvent e) {
        AlienEntity alien;
        // Every time an alien army member dies increase the speed of all aliens in the army

        if (e.getEntity() instanceof AlienEntity) {
            alien = (AlienEntity) e.getEntity();
            registerEntityForRemoval(alien);

            this.increaseSpeed(INCREASE_MOVE_SPEED_PERCENT);

        }
    }
}
