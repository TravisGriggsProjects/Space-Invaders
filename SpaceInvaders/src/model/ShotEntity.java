package model;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import model.events.EntityHitEvent;

/**
 * An entity representing a shot fired by the player's ship
 *
 * @author Santi Ruiz (based on some original work by Kevin Glass)
 */
public class ShotEntity extends AnimatedEntity {

  /** The vertical speed at which the players shot moves */
  private static final double DEF_MOVE_SPEED = -300;
  /** The list of sprites to use when dying */
  static List<String> shotAnimationSpritesList = null;
  /** Sound the alien makes when it is dying */
  static Clip soundWhenAlive;
  static final String SHOT_SPRITE = "shot.gif";

  /**
   * Create a new shot from the player
   *
   * @param game The game in which the shot has been created
   * @param sprite The sprite representing this shot
   * @param x The initial x location of the shot
   * @param y The initial y location of the shot
   */
  public ShotEntity( int x, int y) {
    this(x , y, DEF_MOVE_SPEED);

  }

  /**
   * Create a new shot from the player
   *
   * @param game The game in which the shot has been created
   * @param sprite The sprite representing this shot
   * @param x The initial x location of the shot
   * @param y The initial y location of the shot
   */
  public ShotEntity( int x, int y, double yMoveSpeed) {
    super(SHOT_SPRITE, x, y);
    dy = yMoveSpeed;
      // Create animation sprite lists and load sounds to use for shots
    if (shotAnimationSpritesList == null) {

      // images used for animating aliens (when they aren't dying)
      shotAnimationSpritesList = new LinkedList<String>();
      shotAnimationSpritesList.add("shot.gif");
      shotAnimationSpritesList.add("shot1.gif");
      loadAliveSound();
    }
    this.setAnimationSpritesList(shotAnimationSpritesList);
    this.setSound(soundWhenAlive);
  }

  /**
   * Request that this shot moved based on time elapsed
   *
   * @param delta The time that has elapsed since last updateState
   */
 
  private void loadAliveSound() {

  URL url = this.getClass().getClassLoader().getResource("sounds/shot.wav");
//  URL url = this.getClass().getClassLoader().getResource("sounds/thunder_1.wav");
//  URL url = this.getClass().getClassLoader().getResource("sounds/beep_1.wav");
    AudioInputStream audioIn;
    try {
      audioIn = AudioSystem.getAudioInputStream(url);
      soundWhenAlive = AudioSystem.getClip();
      soundWhenAlive.open(audioIn);
      soundWhenAlive.start();
    } catch (Exception ex) {
      soundWhenAlive = null;
    }

  }

  public void collisionHasOccured(EntityHitEvent e) {
    this.setLifeLeft(0.0);
  }
}
