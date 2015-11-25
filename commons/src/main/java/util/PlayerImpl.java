package util;

/**
 * Created by Hans on 23/11/2015.
 */
public class PlayerImpl implements Player {

  private static final long serialVersionUID = 1L;

  private PlayerType type;

  private int x;

  private int y;

  private long startTime;

  private int score;

  private int id;

  public PlayerImpl(PlayerType type, int x, int y) {
    this.x = x;
    this.y = y;
    this.type = type;
    this.score = 0;
    startTime = System.currentTimeMillis();
  }

  public PlayerImpl() {
    this.type = PlayerType.SPECTATOR;
    this.x = 0;
    this.y = 0;
    this.score = 0;
    this.startTime = System.currentTimeMillis();

  }

  public PlayerImpl(PlayerType type) {
    this.type = type;
    this.score = 0;
    this.startTime = System.currentTimeMillis();
  }


  @Override
  public PlayerType getType() {
    return type;
  }

  @Override
  public void setType(PlayerType type) {
    //We create object before registering as a fly or frog
    this.startTime = System.currentTimeMillis();
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

  @Override
  public long getStartTime() {
    return startTime;
  }

  @Override
  public int getScore() {
    if (this.type == PlayerType.FLY) {
      return (int) ((System.currentTimeMillis() - this.startTime) / ConstantCache.TICK);
    }
    return score;
  }

  @Override
  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public void setX(int x) {
    this.x = x;
  }

  @Override
  public void setY(int y) {
    this.y = y;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlayerImpl player = (PlayerImpl) o;

    return id == player.id;

  }

  @Override
  public int hashCode() {
    return id;
  }
}
