package model;

import controller.MyLogger;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * A resource manager for sprites in the game. Its often quite important how and
 * where you get your game resources from. In most cases it makes sense to have
 * a central resource loader that goes away, gets your resources and caches them
 * for future use. <p> [singleton] <p>
 *
 * @author Kevin Glass
 */
public class SpriteStore {

  protected static String SPRITE_FOLDER = "sprites/";
  /**
   * The single instance of this class
   */
  private static SpriteStore single = new SpriteStore();
  /**
   * The cached sprite map, from reference to sprite instance
   */
  private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

  /**
   * Get the single instance of this class
   *
   * @return The single instance of this class
   */
  public static SpriteStore get() {
    return single;
  }

  /**
   * Retrieve a sprite from the store
   *
   * @param ref The reference to the image to use for the sprite
   * @return A sprite instance containing an accelerate image of the request
   * reference
   */
  public Sprite getSprite(String ref) {
    // if we've already got the sprite in the cache
    // then just return the existing version
    if (sprites.get(ref) != null) {
      return sprites.get(ref);
    }

    // otherwise, go away and grab the sprite from the resource
    // loader
    BufferedImage sourceImage = null;

    try {
      // The ClassLoader.getResource() ensures we get the sprite
      // from the appropriate place, this helps with deploying the game
      // with things like webstart. You could equally do a file look
      // up here.
      
      
      /*
       ImageIcon image = (new ImageIcon(getClass().getResource("yourpackage/mypackage/image.gif")));
 

In general, you can retrieve an InputStream in the following way:

 InputStream is = this.getClass().getClassLoader()    .getResourceAsStream("yourpackage/mypackage/myfile.xml");
 
       */
      
      URL url = this.getClass().getClassLoader().getResource(SPRITE_FOLDER + ref);

      if (url == null) {
        fail("Can't find ref: " +SPRITE_FOLDER + ref);
      }

      // use ImageIO to read the image in
      sourceImage = ImageIO.read(url);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Failed to load: " + ref);
    }

    // create an accelerated image of the right size to store our sprite in
    GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

    // draw our source image into the accelerated image
    image.getGraphics().drawImage(sourceImage, 0, 0, null);

    // create a sprite, add it the cache then return it
    Sprite sprite = new Sprite(ref, image);
    sprites.put(ref, sprite);

    return sprite;
  }

  /**
   * Utility method to handle resource loading failure
   *
   * @param message The message to display on failure
   */
  private void fail(String message) {
    // we're pretty dramatic here, if a resource isn't available
    // we dump the message and exit the game
    System.err.println(message);
    Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.SEVERE, message);
    //   System.exit(0);
  }
}
