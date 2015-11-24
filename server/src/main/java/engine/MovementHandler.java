package engine;


import util.ConstantCache;
import util.Player;
import util.PlayerType;

/**
 * Created by Hans on 23/11/2015.
 */
public class MovementHandler {

  public static void handleMove(Move move, Player player) {
    switch (move) {
      case DEAD:
        return;
      case LEFT:
        return;
      case RIGHT:
        return;
      case UP:
        handleUp(player, false);
        return;
      case DOWN:
        return;
      case FROG:
        return;
      case FLY:
        return;
      case DOUBLELEFT:
        return;
      case DOUBLERIGHT:
        return;
      case DOUBLEUP:
        return;
      case DOUBLEDOWN:
        return;
      default:
        throw new IllegalArgumentException("No such move!");
    }
  }

  private static void handleUp(Player player, boolean doubleMove) {
    move(player, 0, doubleMove);
  }

  private static void handleDown(Player player, boolean doubleMove) {
    move(player, 1, doubleMove);
  }

  private static void handleLeft(Player player, boolean doubleMove) {
    move(player, 2, doubleMove);
  }

  private static void handleRight(Player player, boolean doubleMove) {
    move(player, 3, doubleMove);
  }

  public static boolean move(Player player, int direction, boolean doubleMove) {
    int moves = doubleMove ? 2 : 1;
    switch (direction) {
      case 0:
        validateMakeMove(player, player.getY() - moves, player.getX());
        break;
      case 1:
        validateMakeMove(player, player.getY() + moves, player.getX());
        break;
      case 2:
        validateMakeMove(player, player.getY(), player.getX() + moves);
        break;
      case 3:
        validateMakeMove(player, player.getY(), player.getX() - moves);
        break;
      default:
        break;
    }
    return validateMakeMove(player, player.getY() - moves, player.getX());

  }

  public static boolean validateMakeMove(Player player, int m, int n) {

    return true;
  }

  public static boolean validateMove(Player player, int m, int n) {
    if(m < 0 || n < 0 || m >= ConstantCache.HEIGHT || n >= ConstantCache.WIDTH) {
      return false;
    }
    //TODO SEND DEATH HERE
    if(player.getType() == PlayerType.FLY);
    return true;
  }

  public static void register(int type) {

  }

  public static void death(Player player) {
    if(player.getType() == PlayerType.FLY);
    if(player.getType() == PlayerType.FROG) {
      if(player.getStartTime() / ConstantCache.TICK >= 1) {
        player.setType(PlayerType.NULL);
      }
    }
  }
}
