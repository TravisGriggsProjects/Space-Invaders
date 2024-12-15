package model;

/**
 * Tagging class to distinguish an Alien shot from a ship shot
 * @author sruiz
 */
public class AlienShotEntity extends ShotEntity{
    /**
   * Create a new shot from an alien
   *
   * @param game The game in which the shot has been created
   * @param sprite The sprite representing this shot
   * @param x The initial x location of the shot
   * @param y The initial y location of the shot
   */
  public AlienShotEntity( int x, int y, double yMoveSpeed) {
    super(x, y, yMoveSpeed);
  }


}
