package model;

import java.net.URL;
import java.util.*;
import javax.sound.sampled.*;
import model.events.*;

/**
 * An entity which represents one of our space invader aliens.
 *
 * @author Santi Ruiz based on work be Kevin Glass
 */
public class AlienEntity extends AnimatedEntity implements EntityEventListener {

  /** The list of sprites to use when dying */
  static List<String> alienDyingSpritesList = null;
  /** The list of sprites to use fir animation*/
  static List<String> alienAnimationSpritesList = null;
  /** Sound the alien makes when it is dying */
  static Clip soundWhenDying;
  /** The speed at which the alient moves horizontally */
  private static final double MOVE_SPEED = 100;
  /** The initial image to be used for the alien */
  private static String ALIEN_SPRITE = "newalien1.gif";
  public static final int ADVANCE_SPEED = 20;

  /******************* Constructors *****************************************/
  /**
   * Create a new alien entity
   *
   * @param game The game in which this entity is being created
   * @param ref The sprite which should be displayed for this alien
   * @param x The intial x location of this alien
   * @param y The intial y location of this alient
   */
  public AlienEntity(int x, int y) {
    super(ALIEN_SPRITE, x, y);
    dx = MOVE_SPEED;
    // Aliens do not move vertically unless we hit the side borders
    dy = 0;
    // Create the dying and animation sprite lists to use for aliens and load
    // the necessary sounds
    if (alienDyingSpritesList == null) {
      // images used for  aliens when they are dying
      alienDyingSpritesList = new LinkedList<String>();
      alienDyingSpritesList.add("explosion1.gif");
      alienDyingSpritesList.add("explosion2.gif");
      alienDyingSpritesList.add("explosion3.gif");
      alienDyingSpritesList.add("explosion4.gif");
      alienDyingSpritesList.add("explosion5.gif");
      alienDyingSpritesList.add("explosion6.gif");

      // images used for animating aliens (when they aren't dying)
      alienAnimationSpritesList = new LinkedList<String>();
      alienAnimationSpritesList.add("newalien1.gif");
      alienAnimationSpritesList.add("newalien2.gif");
      alienAnimationSpritesList.add("newalien3.gif");
      alienAnimationSpritesList.add("newalien4.gif");
      alienAnimationSpritesList.add("newalien5.gif");
      alienAnimationSpritesList.add("newalien6.gif");
      alienAnimationSpritesList.add("newalien7.gif");
      alienAnimationSpritesList.add("newalien8.gif");

      loadDyingSound();
    }
    setDyingSpritesList(alienDyingSpritesList);
    setAnimationSpritesList(alienAnimationSpritesList);

  }

  /**
   * Sound wanted whenan Alien is dying
   */
  private void loadDyingSound() {

    URL url = this.getClass().getClassLoader().getResource("sounds/die.wav");
    AudioInputStream audioIn;
    try {
      audioIn = AudioSystem.getAudioInputStream(url);
      soundWhenDying = AudioSystem.getClip();
      soundWhenDying.open(audioIn);
    } catch (Exception ex) {
      soundWhenDying = null;
    }

  }

  /**
   * Entity events we need to repsond to
   *
   * @param e EntityHitEvent
   */
  @Override
  public void collisionHasOccured(EntityHitEvent e) {

    if (e.getHitBy() instanceof BorderEntity) {
      BorderEntity border;
      border = (BorderEntity) e.getHitBy();
      if (border.getId().equals("RIGHT")) {
        this.moveTo(this.getX() - 10, this.getY() + ADVANCE_SPEED);
        this.setHorizontalMovement(-this.getHorizontalMovement());
      } else if (border.getId().equals("LEFT")) {
        this.moveTo(this.getX() + 10, this.getY() + ADVANCE_SPEED);
        this.setHorizontalMovement(-this.getHorizontalMovement());
      }
    } else {
      // Must have hit an alien(not possible), ship or shot
      // Start to die whenever we get hit
      this.sound = soundWhenDying;
      this.setCurrentState(State.DYING);
    }
  }
}
