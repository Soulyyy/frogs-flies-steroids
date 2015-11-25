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
    LOGGER.debug("Handling move {}", move);
    switch (move) {
      case DEAD:
        return null;
      case LEFT:
        return handleLeft(player, false, gameField);
      case RIGHT:
        return handleRight(player, false, gameField);
      case UP:
        return handleUp(player, false, gameField);
      case DOWN:
        return handleDown(player, false, gameField);
      case DOUBLELEFT:
        return handleLeft(player, true, gameField);
      case DOUBLERIGHT:
        return handleRight(player, true, gameField);
      case DOUBLEUP:
        return handleUp(player, true, gameField);
      case DOUBLEDOWN:
        return handleDown(player, true, gameField);
      case NULL:
        //Don't do anything
        return player;
      default:
        throw new IllegalArgumentException("No such move, " + move);
    }
  }

  private static Player handleUp(Player player, boolean doubleMove, Player[][] gameField) {
    LOGGER.info("Handling up event. Is double: {}", doubleMove);
    return move(player, 0, doubleMove, gameField);
  }

  private static Player handleDown(Player player, boolean doubleMove, Player[][] gameField) {
    LOGGER.info("Handling down event. Is double {}", doubleMove);
    return move(player, 1, doubleMove, gameField);
  }

  private static Player handleLeft(Player player, boolean doubleMove, Player[][] gameField) {
    LOGGER.info("Handling left event. Is double: {}", doubleMove);
    return move(player, 2, doubleMove, gameField);
  }

  private static Player handleRight(Player player, boolean doubleMove, Player[][] gameField) {
    LOGGER.info("Handling right event. Is double: {}", doubleMove);
    return move(player, 3, doubleMove, gameField);
  }

  public static Player move(Player player, int direction, boolean doubleMove, Player[][] gameField) {
    int moves = doubleMove ? 2 : 1;
    switch (direction) {
      case 0:
        return makeMove(player, player.getX() - moves, player.getY(), gameField);
      case 1:
        return makeMove(player, player.getX() + moves, player.getY(), gameField);
      case 2:
        return makeMove(player, player.getX(), player.getY() - moves, gameField);
      case 3:
        return makeMove(player, player.getX(), player.getY() + moves, gameField);
      default:
        throw new IllegalArgumentException("No ID for move to make: " + direction);
    }
  }

  public static Player makeMove(Player player, int m, int n, Player[][] gamefield) {
    LOGGER.info("Moving from ({}, {}) -> ({}, {})", player.getX(), player.getY(), m, n);
    if (validateMove(player, n, m)) {
      LOGGER.info("Move passed validation");
      if (gamefield[m][n].getType() == PlayerType.NULL) {
        gamefield[m][n] = player;
        gamefield[player.getX()][player.getY()] = new PlayerImpl(PlayerType.NULL);
        player.setX(m);
        player.setY(n);
      } else if (gamefield[m][n].getType() == PlayerType.FLY && player.getType() == PlayerType.FROG) {
        player.setScore(player.getScore() + 1);
        gamefield[m][n] = player;
        gamefield[player.getX()][player.getY()] = new PlayerImpl(PlayerType.NULL);
        player.setX(m);
        player.setY(n);
      } else if (gamefield[m][n].getType() == PlayerType.FROG && player.getType() == PlayerType.FLY) {
        gamefield[m][n].setScore(gamefield[m][n].getScore() + 1);
        gamefield[player.getX()][player.getY()] = new PlayerImpl(PlayerType.NULL);
        player = new PlayerImpl(PlayerType.NULL);
      }
    }
    return player;
  }

  public static boolean validateMove(Player player, int m, int n) {
    if (m < 0 || n < 0 || m >= ConstantCache.HEIGHT || n >= ConstantCache.WIDTH) {
      LOGGER.info("Invalid move from player type {}", player.getType());
      return false;
    }
    //TODO SEND DEATH HERE
    if (player.getType() == PlayerType.FLY) ;
    return true;
  }

  public static Player register(Player player, int type) {
    if (type == 0) {
      player.setType(PlayerType.FLY);
      return player;
    } else if (type == 1) {
      player.setType(PlayerType.FROG);
      return player;
    } else {
      throw new NullPointerException("Not a valid player type!");
    }
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
