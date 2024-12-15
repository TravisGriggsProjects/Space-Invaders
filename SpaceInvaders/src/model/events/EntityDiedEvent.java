package model.events;

import java.util.EventObject;
import model.Entity;

/**
 * <p>Title: Misc</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TAFE SA</p>
 * @author Santi Ruiz
 * @version 1.0
 */

public class EntityDiedEvent extends EventObject {

  public EntityDiedEvent(Object source) {
    super(source);
  }

  public Entity getEntity(){
    return (Entity)source;
  }
}