package view;

import java.util.EventObject;

/**
 * <p>Title: Misc</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TAFE SA</p>
 * @author Santi Ruiz
 * @version 1.0
 */

public class UserEvent extends EventObject {

  private int firingInterval;
  private double moveSpeed;
  private String userName;

  public String getUserName() {
    return userName;
  }

  /**
   * UserEvent
   *
   * @param source Object
   */
  public UserEvent(Object source) {
    super(source);
  }

  /**
   * UserEvent
   *
   * @param source Object
   * @param username
   */
  public UserEvent(Object source, String username) {
    super(source);
    this.userName = username;
  }

  /**
   * UserEvent
   *
   * @param source Object
   * @param firingInterval int
   */
  public UserEvent(Object source, int firingInterval) {
    super(source);
    this.firingInterval = firingInterval;
  }

  /**
   * UserEvent
   *
   * @param source Object
   * @param moveSpeed double
   */
  public UserEvent(Object source, double moveSpeed) {
    super(source);
    this.moveSpeed = moveSpeed;
  }

  /**
   * getFiringInterval
   *
   * @return int
   */
  public int getFiringInterval() {
    return firingInterval;
  }

  public double getMoveSpeed() {
    return moveSpeed;
  }

}
