package model;

import controller.MyLogger;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;
import model.events.EntityDiedEvent;
import model.events.EntityEventListener;
import model.events.EntityHitEvent;
import model.events.EntityMoveEvent;

/**
 * An entity represents any element that appears in the game. The entity is responsible for responding to collisions and
 * movement based on a set of properties defined either by a subclass.
 *
 * You cannot create an Entity object (the class is abstract). You must extend Entity and provide particular features
 * for your entity. Example of this are AlienEntity, ShipEntity and ShotEntity.
 *
 * Note that doubles are used for positions. This may seem strange given that pixels locations are integers. However,
 * using double means that an entity can updateState a partial pixel. It doesn't of course mean that they will be
 * display half way through a pixel but allows us not lose accuracy as we calculate the updateState.
 *
 * @author Santi Ruiz based on work by Kevin Glass
 * @author Julie Ruiz
 * @version 1.0 Initial version
 * @version 2.0 Changed collision check to return boolean and not detect collision with itself as a collision
 */
public abstract class Entity implements EntityEventListener {

    public static final String DEF_REF_TO_SPRITE_IMAGE_FILE = null;
    public static final int DEF_X = 0;
    public static final int DEF_Y = 0;

    /**
     * The percentage of life left before we are considered dead
     */
    public static final double LIFE_LEFT_TOLERANCE = 0.01;
    /**
     * The dying rate of life left before we are considered dead
     */
    public static final double DEFAULT_DEATH_RATE = 0.002;
    /**
     * The amount of life the entity has left as a fraction of 1.0 Full life is 1.0 Lost half its life then the value
     * would be 0.5 and so on.
     */
    protected double lifeLeft = 1.0;
    /**
     * The current x location of this entity
     */
    protected double x;
    /**
     * The current y location of this entity
     */
    protected double y;
    /**
     * The sprite that represents this entity at any given time
     */
    protected model.Sprite sprite;
    /**
     * The current speed of this entity horizontally (pixels/sec)
     */
    protected double dx;
    /**
     * The current speed of this entity vertically (pixels/sec)
     */
    protected double dy;
    /**
     * The list of objects wanting to know about any entity events
     */
    private transient List<EntityEventListener> entityEventListeners;
    /**
     * The current state of the entity
     */
    protected State currentState = null;
    /**
     * The sprites to use in DYING state
     */
    protected List<String> dyingSpritesList;
    /**
     * The sound the entity is making
     */
    protected Clip sound;
    /**
     * Flag to indicate a state just changed
     */
    protected boolean justEnteredState;
    /**
     * Check allowed movement directions
     */
    protected boolean canMoveRight = true;
    protected boolean canMoveLeft = true;
    protected boolean canMoveUp = true;
    protected boolean canMoveDown = true;

    /**
     * ************************* constructors *********************************
     */
    public Entity() {
        this(DEF_REF_TO_SPRITE_IMAGE_FILE, DEF_X, DEF_Y);
    }

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param refToSpriteImageFile The reference to the image to be displayed for this entity
     * @param x The initial x location of this entity
     * @param y The initial y location of this entity
     */
    public Entity(String refToSpriteImageFile, int x, int y) {
        if (refToSpriteImageFile != null) {
            this.sprite = SpriteStore.get().getSprite(refToSpriteImageFile);
        } else {
            this.sprite = null;
        }
        this.x = x;
        this.y = y;
        this.addEntityEventListener(this);
        this.setCurrentState(State.ALIVE);
        dyingSpritesList = null;
        this.sound = null;
    }

    public Clip getSound() {
        return sound;
    }

    public void setSound(Clip sound) {
        this.sound = sound;
    }

    /**
     * ******************* Setters and Getters
     *
     ********************************
     * @return
     */
    public List<String> getDyingSpritesList() {
        return dyingSpritesList;
    }

    public void setDyingSpritesList(List<String> dyingSpritesList) {
        this.dyingSpritesList = dyingSpritesList;
    }

    /**
     * Set the horizontal speed of this entity
     *
     * @param dx The horizontal speed of this entity (pixels/sec)
     */
    public void setHorizontalMovement(double dx) {
        this.dx = 0;
        if (dx > 0) {
            if (canMoveRight) {
                this.dx = dx;
                canMoveLeft = true;
            }
        } else if (dx < 0) {
            if (canMoveLeft) {
                this.dx = dx;
                canMoveRight = true;
            }
        }
    }

    /**
     * Set the vertical speed of this entity
     *
     * @param dy vertical speed
     */
    public void setVerticalMovement(double dy) {
        this.dy = 0;
        if (dy > 0) {
            if (canMoveDown) {
                this.dy = dy;
                canMoveUp = true;
            }
        } else if (dy < 0) {
            if (canMoveUp) {
                this.dy = dy;
                canMoveDown = true;
            }
        }
    }

    /**
     * Get the horizontal speed of this entity
     *
     * @return The horizontal speed of this entity (pixels/sec)
     */
    public double getHorizontalMovement() {
        return dx;
    }

    /**
     * Get the vertical speed of this entity
     *
     * @return The vertical speed of this entity (pixels/sec)
     */
    public double getVerticalMovement() {
        return dy;
    }

    /**
     * Get the x location of this entity
     *
     * @return The x location of this entity
     */
    public int getX() {
        return (int) x;
    }

    /**
     * Get the y location of this entity
     *
     * @return The y location of this entity
     */
    public int getY() {
        return (int) y;
    }

    /**
     *
     * @return The amount of life this entity has left (1.0 indcated full life)
     */
    public double getLifeLeft() {
        return lifeLeft;
    }

    /**
     *
     * @param lifeLeft set the lifeleft the entity
     */
    public void setLifeLeft(double lifeLeft) {
        this.lifeLeft = lifeLeft;
        Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINE, "New LifeLeft = {0}", lifeLeft);
        if (this.lifeLeft < 0 || Math.abs(this.lifeLeft - 0) <= LIFE_LEFT_TOLERANCE) {
            fireEntityDied(new EntityDiedEvent(this));
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public final void setCurrentState(State newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            justEnteredState = true;
        }
    }

    /**
     * setSprite
     *
     * @param ref String
     */
    public void setSprite(String ref) {
        this.sprite = SpriteStore.get().getSprite(ref);
    }

    /**
     * ************************* Usual Overrides*******************************
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Entity)) {
            return false;
        }
        Entity ent = (Entity) obj;
        if ((ent.x != x) || (ent.y != y)
            || (ent.dx != dx) || (ent.dy != dy)
            || (ent.lifeLeft != lifeLeft)
            || (!ent.sprite.equals(sprite))
            || (ent.currentState != currentState)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 41 * hash + (this.sprite != null ? this.sprite.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return super.toString()
            + "[x = " + x + ", y = " + y + ", dx = " + dx
            + ", dy = " + dy + ", lifeLeft = " + lifeLeft
            + " currentState = " + this.currentState
            + " Dying Sprites List = " + this.dyingSpritesList
            + "]";
    }

    /**
     * ********************** Features ******************************************
     */
    /**
     * Request that this entity updates its state based on a certain amount of time (in milliseconds) passing. The
     * current state will be used to determine what changes in data are needed (eg sprite, lifeleft etc)
     *
     * @param delta The amount of time that has passed in milliseconds
     */
    public void updateState(long delta) {

        if (justEnteredState) {
            if (sound != null) {
                sound.setFramePosition(0);
                sound.start();
            }
        }
        switch (currentState) {
            case ALIVE:
                move(delta);
                break;
            case DYING:
                setLifeLeft(lifeLeft - delta * Entity.DEFAULT_DEATH_RATE);
                if (dyingSpritesList != null && dyingSpritesList.size() > 0) {
                    int spriteChoice;
                    spriteChoice = Math.abs((int) Math.round(lifeLeft * 10)) % dyingSpritesList.size();
                    Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINE, "spritChoice = {0}", spriteChoice);
                    this.setSprite(dyingSpritesList.get(spriteChoice));
                } else {
                    System.out.println("here");
                }
                move(delta);
                break;
            case DEAD:
                break;
            default:
        }
        justEnteredState = false;
    }

    /**
     * Request that this entity updateState itself based on a certain amount of time (in milliseconds) passing.
     *
     * @param delta The amount of time that has passed in milliseconds
     */
    public void move(long delta) {
        // update the location of the entity based on updateState speeds
        // Remember that dx is in pixels per second thus the need to divide by 1000 to get
        // pixels per millisecond. The number of milliseconds passed (delta) multiplied by
        // the speed in pixels/sec (dx and dy) gives the number of pixels to updateState. Speeds which are
        // negative (minus values for dx or dy) updateState in reverse of course.

        // if no time has passed or the movement speed in both directions is zero
        // then there is no move
        if (delta == 0 || (dx == 0 && dy == 0)) {
            return;
        }

        x += delta * (dx / 1000);
        y += delta * (dy / 1000);
        fireEntityMoved(new EntityMoveEvent(this));
    }

    /**
     * Draw this entity to the graphics context provided
     *
     * @param g The graphics context on which to draw
     */
    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    }

    /**
     * Define the area of the Entity to use for collision detection. This default uses the location and size of sprite.
     *
     * @return
     */
    protected Rectangle collisionArea() {
        return new Rectangle((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
    }

    /**
     * Check if this entity collides with another.
     *
     * @param other The other entity to check collision against
     * @return True if the entities collide with each other. Note cannot collide with yourself!
     */
    public boolean collidesWith(Entity other) {
        /**
         * The rectangles used for entities during collision resolution
         */
        Rectangle him;
        Rectangle me;
        boolean collision;

        //Cannot collide with yourself!
        if (this == other) {
            return false;
        }

        him = other.collisionArea();
        me = this.collisionArea();

        collision = me.intersects(him);
        return collision;
    }

    /**
     * Request that this entity move itself to a particular location immediately Be careful with this one as it could
     * jump borders setup by the game
     *
     * @param x x ordinate
     * @param y y ordinate
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
        fireEntityMoved(new EntityMoveEvent(this));
    }

    public Sprite getSprite() {
        return sprite;
    }

    /**
     * ********************** Event Handling Infrastructure **********************
     */
    /**
     * Remove a listener from the listener list
     *
     * @param l The listener to be removed from the listener list
     */
    public synchronized void removeEntityEventListener(EntityEventListener l) {
        entityEventListeners.remove(l);
    }

    /**
     * Add a listener to the listener list
     *
     * @param l The listener to be added to the listener list
     */
    public final synchronized void addEntityEventListener(EntityEventListener l) {
        if (entityEventListeners == null) {
            entityEventListeners = new ArrayList<EntityEventListener>(2);
        }
        if (!entityEventListeners.contains(l)) {
            entityEventListeners.add(l);
        }
    }

    /**
     * Inform listeners that a Collision involving this entity has occurred
     *
     * @param e The details of the collision
     */
    public void fireCollisionHasOccured(EntityHitEvent e) {

        //Some FINE level debugging logs
        if (Logger.getLogger(MyLogger.LOGGER_NAME).getLevel() == Level.FINE) {
            String infoStr;
            infoStr = "[" + e.getHit().getClass() + "] HIT by [" + e.getHitBy().getClass() + "] and will inform " + entityEventListeners.
                size() + " listeners ";
            for (int i = 0; i < entityEventListeners.size(); i++) {
                infoStr = infoStr + "[" + entityEventListeners.get(i).getClass() + "]";
            }
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINE, infoStr);
        }

        if (entityEventListeners != null) {
            List<EntityEventListener> listeners = entityEventListeners;
            int count = listeners.size();
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINEST,
                "\n *****************   Start of firing of collision events ");
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINEST,
                "Entity: {0}\n\tNumber of Listeners: {1} \n\tEntityHitEvent: {2}", new Object[]{this, count, e});
            for (int i = 0; i < count; i++) {
                listeners.get(i).collisionHasOccured(e);
                Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINEST, "\tSent to listener {0} \n\t\t:listener: {1}",
                    new Object[]{i, listeners.get(i)});
            }
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINEST,
                " *******************Finished firing of collision event\n ");
        }
    }

    /**
     * Inform listeners that a this entity has moved
     *
     * @param e The details of the updateState
     */
    protected void fireEntityMoved(EntityMoveEvent e) {
        //Some FINE level debugging logs
        if (Logger.getLogger(MyLogger.LOGGER_NAME).getLevel() == Level.FINEST) {
            String infoStr;
            infoStr = "[" + e + "] MOVED and will inform " + entityEventListeners.size() + " listeners ";
            for (int i = 0; i < entityEventListeners.size(); i++) {
                infoStr = infoStr + "[" + entityEventListeners.get(i).getClass() + "]";
            }
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINEST, infoStr);
        }

        if (entityEventListeners != null) {
            int count = entityEventListeners.size();
            for (int i = 0; i < count; i++) {
                entityEventListeners.get(i).entityMoved(e);
            }
        }
    }

    /**
     * Inform listeners that this entity has no life left ie its dead
     *
     * @param e The details of the death
     */
    protected void fireEntityDied(EntityDiedEvent e) {
        //Some FINE level debugging logs
        if (Logger.getLogger(MyLogger.LOGGER_NAME).getLevel() == Level.FINE) {
            String infoStr;
            infoStr = "[" + e + "] DIED and will inform " + entityEventListeners.size() + " listeners ";
            for (int i = 0; i < entityEventListeners.size(); i++) {
                infoStr = infoStr + "[" + entityEventListeners.get(i).getClass() + "]";
            }
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.FINE, infoStr);
        }
        if (entityEventListeners != null) {
            int count = entityEventListeners.size();
            for (int i = 0; i < count; i++) {
                entityEventListeners.get(i).entityDied(e);
            }
        }
    }

    /**
     * ************************ Event Handling Methods ***********************
     */
    /**
     * No Action by default
     *
     * @param e
     */
    @Override
    public void collisionHasOccured(EntityHitEvent e) {
    }

    /**
     * No Action by default
     *
     * @param e
     */
    @Override
    public void entityMoved(EntityMoveEvent e) {
    }

    /**
     * Set the state of the entity to DEAD
     *
     * @param e
     */
    @Override
    public void entityDied(EntityDiedEvent e) {
        this.setCurrentState(State.DEAD);
    }
}
