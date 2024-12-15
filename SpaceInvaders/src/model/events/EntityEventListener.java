package model.events;

import java.util.EventListener;

/**
 * <p>Title: The Entity Events</p>
 * <p>Description: The events that an entity may be involved in</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TAFE SA</p>
 * @author Santi Ruiz
 * @version 1.0
 */

public interface EntityEventListener extends EventListener {
  public void collisionHasOccured(EntityHitEvent e);
  public void entityMoved(EntityMoveEvent e);
  public void entityDied(EntityDiedEvent e);
}