package model;

/**
 * <p>
 * Title: Collection of Entity objects</p>
 * <p>
 * Description: Organises for a collection of entities that we want to treat as a group. Acts as a farcade for List but
 * adds functionality applicable to all entities in the list, such as Move</p>
 * <p>
 * Copyright: Copyright (c) 2004</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @version 1.0
 */
import java.awt.Graphics2D;
import model.events.EntityEventListener;

/**
 *
 * @author sruiz
 * @author jruiz
 */
public interface EntityListInterface {

    /**
     * Clear the list
     */
    public void clear();

    /**
     * Add an entity
     *
     * @param e The entity to be added
     */
    public void add(Entity e);

    /**
     * Increase the speed of all members of this list by a certain percentage
     *
     * @param percentIncr
     */
    public void increaseSpeed(int percentIncr);

    /**
     * Registers an entity for removal next time the state of the list is updated
     *
     * @param e The entity to register for removal next time the upDateState is called on the list
     */
    public void registerEntityForRemoval(Entity e);

    /**
     * Move all entities in the list according to time passed delta.
     *
     * @param delta Time passed since last updateState (ms)
     */
    public void updateState(long delta);

//write remove() method that removes an entity from a list
    /**
     * draw all entities in the list
     *
     * @param g The graphics context to draw to
     */
    public void draw(Graphics2D g);

    /**
     * Move and draw
     *
     * @param delta Time passed since last updateState (ms)
     * @param g The graphics context to draw to
     */
    public void moveAndDraw(long delta, Graphics2D g);

    /**
     * Check if this entity collides with any entity in this entity list.
     *
     * @param other The entity to check collision against
     * @return null if there is no collision, the entity if there is a collision
     */
    public Entity collidesWith(Entity other);

    /**
     * Manage event listener registration
     *
     * @param l the listener to add
     */
    public void addEntityEventListener(EntityEventListener l);

    /**
     * Remove an entity from the list
     *
     * @param e The entity to remove
     */
    public void remove(Entity e);

    /**
     * Number of entities
     *
     * @return The number of entities
     */
    public int getNumEntities();

    /**
     * return one of the entities in the list - choosing it randomly If there are no entities in the list will return
     * null
     *
     * @return
     */
    public Entity getRandom();

}
