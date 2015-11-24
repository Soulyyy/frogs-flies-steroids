package engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

/**
 * Created by Hans on 23/11/2015.
 */
public class MovementHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(MovementHandler.class);

  public static Player handleMove(Move move, Player player, Player[][] gameField) {
    LOGGER.info("Handling move {}", move);
    switch (move) {
      case DEAD:
        return null;
      case LEFT:
        return null;
      case RIGHT:
        return null;
      case UP:
        return handleUp(player, false, gameField);
      case DOWN:
        return null;
      case FROG:
        return null;
      case FLY:
        return null;
      case DOUBLELEFT:
        return null;
      case DOUBLERIGHT:
        return null;
      case DOUBLEUP:
        return null;
      case DOUBLEDOWN:
        return null;
      default:
        throw new IllegalArgumentException("No such move!");
    }
  }

  private static Player handleUp(Player player, boolean doubleMove, Player[][] gameField) {
    LOGGER.info("Handling up event. Is double: {}", doubleMove);
    return move(player, 0, doubleMove, gameField);
  }

  private static void handleDown(Player player, boolean doubleMove, Player[][] gameField) {
    move(player, 1, doubleMove, gameField);
  }

  private static void handleLeft(Player player, boolean doubleMove, Player[][] gameField) {
    move(player, 2, doubleMove, gameField);
  }

  private static void handleRight(Player player, boolean doubleMove, Player[][] gameField) {
    move(player, 3, doubleMove, gameField);
  }

  public static Player move(Player player, int direction, boolean doubleMove, Player[][] gameField) {
    int moves = doubleMove ? 2 : 1;
    switch (direction) {
      case 0:
        return makeMove(player, player.getX(), player.getY() - moves, gameField);
      case 1:
        return makeMove(player, player.getX(), player.getY() + moves, gameField);
      case 2:
        return makeMove(player, player.getX() + moves, player.getY(), gameField);
      case 3:
        return makeMove(player, player.getX() - moves, player.getY(), gameField);
      default:
        throw new IllegalArgumentException("No ID for move to make: " + direction);
    }
  }

  public static Player makeMove(Player player, int m, int n, Player[][] gamefield) {
    LOGGER.info("Moving from ({}, {}) -> ({}, {})", player.getX(), player.getY(), m, n);
    if (validateMove(player, n, m)) {
      LOGGER.info("Move passed validation");
      if (gamefield[n][m].getType() == PlayerType.NULL) {
        gamefield[n][m] = player;
        gamefield[player.getX()][player.getY()] = new PlayerImpl();
        player.setX(m);
        player.setY(n);
      }
    }
    return player;
  }

  public static boolean validateMove(Player player, int m, int n) {
    if (m < 0 || n < 0 || m >= ConstantCache.HEIGHT || n >= ConstantCache.WIDTH) {
      return false;
    }
    //TODO SEND DEATH HERE
    if (player.getType() == PlayerType.FLY) ;
    return true;
  }

  public static void register(int type) {

  }

  private void checkForFly(int m, int n) {

  }

  public static void death(Player player) {
    if (player.getType() == PlayerType.FLY) ;
    if (player.getType() == PlayerType.FROG) {
      LOGGER.debug("Polled a frog with start time {}. Time elapsed: {}", player.getStartTime(), System.currentTimeMillis() - player.getStartTime());
      if (System.currentTimeMillis() - player.getStartTime() >= ConstantCache.TICK) {
        LOGGER.info("Killed {} on ({}, {})", player.getType(), player.getX(), player.getY());
        LOGGER.info("Dead one had ID {}", player.getId());
        player.setType(PlayerType.NULL);
      }
    }
  }
}
