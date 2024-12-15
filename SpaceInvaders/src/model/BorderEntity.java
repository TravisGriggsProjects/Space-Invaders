package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * <p>
 * Title: BorderEntity</p>
 * <p>
 * Description:BorderEntity Class </p>
 * <p>
 * Copyright: Copyright (c) 2005</p>
 * <p>
 * Company: TAFE SA</p>
 * 
 * Entity of the game which is a Border. Borders will have IDs so that we can query which border we are working with.
 * Extends Entity class with width, height and border id
 *
 * @author sruiz
 * @author jruiz 
 * @author Travis Griggs 19/09/24
 * @version 1.0 initial version
 * @version 2.0 updated to get better consistency in the order of parameters
 *
 */
public class BorderEntity extends Entity {

    //constants    
    public static final int DEFAULT_WIDTH = 0;    
    public static final int DEFAULT_HEIGHT = 0;    
    public static final String DEFAULT_ID = "None";

    //instance variables
    private int width;
    private int height;
    private String id;

    //no args constructors

    /**
     * No-args constructor
     * Initializes the border entity with default ID, width, and height.
     */
    public BorderEntity() {
        this(DEFAULT_ID, DEFAULT_WIDTH, DEFAULT_HEIGHT);

    }

    /**
     * 1-arg constructor
     * Initializes the border entity with a specified ID and default width and height.
     * @param id
     */
    public BorderEntity(String id) {
        this(id, DEFAULT_WIDTH, DEFAULT_HEIGHT);

    }

    /**
     * 3-arg constructor
     * Initializes the border entity with a specified ID, width, and height.
     * @param id
     * @param width
     * @param height
     */
    public BorderEntity(String id, int width, int height) {
        this(id, DEF_X, DEF_Y, width, height);  

    }

    /**
     * 4-arg constructor
     * Initializes the border entity with a specified X and Y position, width, and height, and uses a default ID.
     * @param initialXPos
     * @param initialYPos
     * @param width
     * @param height
     */
    public BorderEntity(int initialXPos, int initialYPos, int width, int height) {
         this(DEFAULT_ID, initialXPos, initialYPos, width, height);

    }

     /**
     * all-args constructor
     * Initializes the border entity with a specified ID, X and Y positions, width, and height.
     * @param id
     * @param initialXPos
     * @param initialYPos
     * @param width
     * @param height
     */
    public BorderEntity(String id, int initialXPos, int initialYPos, int width, int height) {
        super(DEF_REF_TO_SPRITE_IMAGE_FILE, initialXPos, initialYPos);
        this.id = id;
        this.width = width;
        this.height = height;

    }
    
    //Getters and Setters

    /**
     * Width getter
     * @width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Width setter
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Height getter
     * @height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Height setter
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Id getter
     * @Id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Id setter
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
  
    //toString (no equals)
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();  // Initialize StringBuilder as 'sb'
        sb.append(super.toString()).append(", "); 
        sb.append("BorderEntity {");
        sb.append("id='");
        sb.append(id);
        sb.append('\'');
        sb.append(", width=");
        sb.append(width);
        sb.append(", height=");
        sb.append(height);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Define the area of the Entity to use for collision detection. This default uses the location and size of sprite.
     *
     * @return the Rectangle this border covers
     */
    @Override
    protected Rectangle collisionArea() {
        return new Rectangle(this.getX(), this.getY(), this.width, this.height);
    }

    /**
     *
     * @param g
     */
    @Override
    public void draw(Graphics g) {

        g.setColor(Color.red);
        g.fillRect(this.getX(), this.getY(), width, height);
        g.drawRect(this.getX(), this.getY(), width, height);
    }

}
