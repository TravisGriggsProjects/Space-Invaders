package model;
import java.awt.Graphics;
import java.awt.Image;

/**
 * A sprite that is used by an Entity as the Entities visual
 * representation (ie what you see on the screen). Note that a sprite
 * contains no state information, i.e. its just the image and
 * not the location. This allows us to use a single sprite in
 * lots of different places without having to store multiple
 * copies of the image.
 *
 * @author Kevin Glass
 */
public class Sprite {

  /** Where this image came from */
  private String ref = null;
  /** The image to be drawn for this sprite */
  private Image image;

  /**
   * Create a new sprite based on an image
   *
   * @param image The image that is this sprite
   */
  public Sprite(String ref, Image image) {
    this.ref = ref;
    this.image = image;
  }

  /**
   * Get the width of the drawn sprite
   *
   * @return The width in pixels of this sprite
   */
  public int getWidth() {
    return image.getWidth(null);
  }

  /**
   * Get the height of the drawn sprite
   *
   * @return The height in pixels of this sprite
   */
  public int getHeight() {
    return image.getHeight(null);
  }

  /**
   * Draw the sprite onto the graphics context provided
   *
   * @param g The graphics context on which to draw the sprite
   * @param x The x location at which to draw the sprite
   * @param y The y location at which to draw the sprite
   */
  public void draw(Graphics g, int x, int y) {
    g.drawImage(image, x, y, null);
  }
  public String getRef() {
    return ref;
  }
  
  @Override
  public String toString() {
      return super.toString() + "[ref = " + ref + ", image = " + image + "]";
  }
  
  @Override
  public boolean equals(Object obj) {
      if(!(obj instanceof Sprite)) 
        return false;
      
      Sprite s = (Sprite)obj;
      if(!s.ref.equalsIgnoreCase(this.ref)) 
        return false;
      if(!s.image.equals(this.image)) 
        return false;
      return true;
  }

  @Override
  public int hashCode() {
    return this.ref.hashCode();
  }
}