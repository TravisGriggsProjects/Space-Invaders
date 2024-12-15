package model.events;

import java.util.EventObject;
import model.Entity;

/**
 * <p>Title: Misc</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: TAFESA</p>
 * @author Santi Ruiz
 * @version 1.0
 */

public class EntityHitEvent extends EventObject {
  Entity hit;
  Entity hitBy;

  public EntityHitEvent(Entity hit, Entity hitBy) {
    super(hitBy);
    this.hit = hit;
    this.hitBy = hitBy;

  }
  public Entity getHit() {
    return hit;
  }
  public Entity getHitBy() {
    return hitBy;
  }

  @Override
  public String toString() {
    StringBuilder temp = new StringBuilder(super.toString());
    temp.append("[hit = ");
    temp.append(hit);
    temp.append(",hitBy = ");
    temp.append(hitBy);
    temp.append("]");
    return temp.toString();
  }

}