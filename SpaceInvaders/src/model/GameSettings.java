package model;

import dao.GameSettingsDAOText;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * <p>
 * Title: GameSettings</p>
 * <p>
 * Description: Class to handle game settings details</p>
 * <p>
 * Copyright: Copyright (c) 2005</p>
 * <p>
 * Company: TAFE SA</p>
 *
 * @author Santi Ruiz
 * @author Julie Ruiz
 * @author Travis Griggs 19/08/2024
 * @version 1.0
 */
public class GameSettings implements Serializable {

    // Instance Variables
        private double moveSpeed;
        private int firingInterval;    
    
    // Default Values
        public static final double MOVE_SPEED = 300;
        public static final int FIRING_INTERVAL = 700;
    
    // Constructors
    /**
     * All-Arg GameSettings Constructor
     * @param moveSpeed
     * @param firingInterval
     */
    public GameSettings(double moveSpeed, int firingInterval) {
        this.moveSpeed = moveSpeed;
        this.firingInterval = firingInterval;
    }

    /**
     * No-Arg UserDetails Constructor
     */
    public GameSettings() {
        this(MOVE_SPEED, FIRING_INTERVAL);
    }

    // Getters

    /**
     * moveSpeed getter
     * @return moveSpeed
     */
    public double getMoveSpeed() {
        return moveSpeed;
    }

    /**
     * firingInterval getter
     * @return firingInterval
     */
    public int getFiringInterval() {
        return firingInterval;
    }
    
    // Setters
    /**
     * moveSpeed setter
     * @param moveSpeed
     */
    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
    
    /**
     * firingInterval setter
     * @param firingInterval
     */
    public void setFiringInterval(int firingInterval) {
        this.firingInterval = firingInterval;
    }
    
    // Equals method
    /**
     * Equals method
     * @param obj
     * @return true (if objects are equal), false (if objects are not equal)
     */
    @Override    
    public boolean equals(Object obj) {
        if (!(obj instanceof GameSettings)) {
            return false;
        }
        GameSettings other = (GameSettings) obj;
        return Double.compare(this.moveSpeed, other.getMoveSpeed()) == 0 && this.firingInterval == other.getFiringInterval();
    }

    
    // toString method
    /**
     * toString method
     * @return GameSettings as string
     */
    @Override    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameSettings{");
        sb.append("moveSpeed=");
        sb.append(moveSpeed);
        sb.append(", firingInterval=");
        sb.append(firingInterval);
        sb.append('}');
        return sb.toString();
    }
 
    /**
     *
     * @param user
     * @throws Exception
     */
    public void saveData(UserDetails user) throws Exception {
        GameSettingsDAOText.saveData(user, this);
    }

    /**
     *
     * @param user
     * @throws Exception
     */
    public void loadData(UserDetails user) throws Exception {
        GameSettingsDAOText.loadData(user, this);
    }
}
