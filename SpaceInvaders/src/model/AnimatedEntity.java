package model;

import java.util.*;

/**
 * Adds infrastructure to give the entity animation through a provided list of
 * sprites when the entity is in the ALIVE state
 * @author sruiz
 */
public abstract class AnimatedEntity extends Entity {

  /**
   * The speed of animation in milliseconds per image
   */
  protected static final double ANIMATION_SPEED = 100;

  protected boolean animated = true;
  protected int currentMoveSprite = 0;
  protected int currentSprite;
  protected double millisLastRotated = System.currentTimeMillis();
   /** The sprites to use in animation */
  protected List<String> animationSpritesList;

  /********************* Setters and Getters*********************************/
  public List<String> getAnimationSpritesList() {
    return animationSpritesList;
  }

  public void setAnimationSpritesList(List<String> animationSpritesList) {
    this.animationSpritesList = animationSpritesList;
  }


  /**
   * setAnimated
   * sets alien animation status
   */
  public void setAnimated(boolean value) {
    animated = value;
  }

  public AnimatedEntity(String ref, int x, int y) {
    super(ref, x, y);
  }

  @Override
  public void updateState(long delta) {
    switch (currentState) {
      case ALIVE:
        if (animated && animationSpritesList.size()>0) {
          if (System.currentTimeMillis() - millisLastRotated >= ANIMATION_SPEED) {
            currentMoveSprite = (currentMoveSprite + 1) % animationSpritesList.size();
            this.setSprite(animationSpritesList.get(currentMoveSprite));
            millisLastRotated = System.currentTimeMillis();
          }
        }
        break;
      default:
    }

    // Do the normal updateState
    super.updateState(delta);
  }
}
