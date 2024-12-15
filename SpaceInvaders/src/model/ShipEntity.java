package model;

import controller.MyLogger;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import model.events.EntityEventListener;
import model.events.EntityHitEvent;

/**
 * The entity that represents the players ship
 *
 * @author Santi Ruiz based on work by Kevin Glass
 */
public class ShipEntity extends Entity implements EntityEventListener {

  /**
   * The list of sprites to use when dying
   */
  static final List<String> DYING_SHIP_SPRITES_LIST = new LinkedList<String>();
  static Clip soundWhenDying;
  static final String SHIP_SPRITE = "ship.gif";

  /**
   * ***************** Class load initilisations ***************************
   */
  /**
   * Create a new entity to represent the players ship
   *
   * @param game The game in which the ship is being created
   * @param ref The reference to the sprite to show for the ship
   * @param x The initial x location of the player's ship
   * @param y The initial y location of the player's ship
   */
  public ShipEntity(int x, int y) {
    super(SHIP_SPRITE, x, y);
    if (DYING_SHIP_SPRITES_LIST.isEmpty()) {
      DYING_SHIP_SPRITES_LIST.add("explosion1.gif");
      DYING_SHIP_SPRITES_LIST.add("explosion2.gif");
      DYING_SHIP_SPRITES_LIST.add("explosion3.gif");
      DYING_SHIP_SPRITES_LIST.add("explosion4.gif");
      DYING_SHIP_SPRITES_LIST.add("explosion5.gif");
      DYING_SHIP_SPRITES_LIST.add("explosion6.gif");
    }
    this.setDyingSpritesList(DYING_SHIP_SPRITES_LIST);
    loadDyingSound();
  }

  /**
   * Sound wanted when the Ship is dying
   */
  private void loadDyingSound() {

    URL url = this.getClass().getClassLoader().getResource("sounds/die.wav");
    AudioInputStream audioIn;
    try {
      audioIn = AudioSystem.getAudioInputStream(url);
      soundWhenDying = AudioSystem.getClip();
      soundWhenDying.open(audioIn);
    } catch (Exception ex) {
      Logger.getLogger(MyLogger.LOGGER_NAME).
              log(Level.SEVERE, "Unable to load sound - sounds/die.wav. Continues with no sound.");
      soundWhenDying = null;
    }

  }

  /**
   * *********************************************************************
   */
  /*
   * Entity Event Handling Methods that we need to respond to
   */
  @Override
  public void collisionHasOccured(EntityHitEvent e) {

    if (e.getHitBy() instanceof BorderEntity) {
      stopMovement(e);
    } else {
      // Must have hit an alien or shot
      // Start to die whenever we get hit
      this.setSound(soundWhenDying);
      this.setCurrentState(State.DYING);
    }
  }

  private void stopMovement(EntityHitEvent e) {
    BorderEntity theBorder;

    theBorder = (BorderEntity) (e.getHitBy());

    if (theBorder.getId().equals("RIGHT")) {
      this.setHorizontalMovement(0);
      this.canMoveRight = false;
    } else if (theBorder.getId().equals("LEFT")) {
      this.setHorizontalMovement(0);
      this.canMoveLeft = false;
    } else if (theBorder.getId().equals("BOTTOM")) {
      this.setVerticalMovement(0);
      this.canMoveDown = false;
    } else if (theBorder.getId().equals("TOP")) {
      this.setVerticalMovement(0);
      this.canMoveUp = false;
    }
  }
}
