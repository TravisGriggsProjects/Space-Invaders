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

public class EntityMoveEvent extends EventObject {

  public EntityMoveEvent(Object source) {
    super(source);
  }

  public Entity getEntity(){
    return (Entity)source;
  }
}