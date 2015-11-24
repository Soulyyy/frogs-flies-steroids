package util;

/**
 * Created by Hans on 23/11/2015.
 */
public enum PlayerType {
  SPECTATOR,
  FROG,
  FLY,
  NULL;

  @Override
  public String toString() {
    switch (this) {
      case SPECTATOR:
        return "Spectator";
      case FROG:
        return "Frog";
      case FLY:
        return "Fly";
      case NULL:
        return "Null";
      default:
        throw new IllegalArgumentException("Not a Player Type!");
    }
  }
}
