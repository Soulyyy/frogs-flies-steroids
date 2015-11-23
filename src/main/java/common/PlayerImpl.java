package common;

import common.Player;

import java.io.Serializable;

/**
 * Created by Hans on 23/11/2015.
 */
public class PlayerImpl implements Player {

  private static final long serialVersionUID = 1L;

  private PlayerType type;

  private int x;

  private int y;

  private long startTime;

  public PlayerImpl(PlayerType type, int x, int y) {
    this.x = x;
    this.y = y;
    this.type = type;
    startTime = System.nanoTime();
  }

  public PlayerImpl() {
    this.type = PlayerType.SPECTATOR;
    this.x = 0;
    this.y = 0;
  }


  @Override
  public PlayerType getType() {
    return type;
  }

  @Override
  public void setType(PlayerType type) {
    this.type = type;
  }

  public int intValue() {
    switch (this.type) {
      case NULL:
        return 1;
      case SPECTATOR:
        return 2;
      case FROG:
        return 3;
      case FLY:
        return 4;
      default:
        throw new IllegalArgumentException("Not a valid player type!");
    }
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }
}
