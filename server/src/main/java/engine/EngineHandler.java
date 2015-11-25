package engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Player;
import util.PlayerType;

/**
 * Created by Hans on 23/11/2015.
 */
public class EngineHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(EngineHandler.class);

  public static int[][] maskBoard(Player[][] currentBoard, int x, int y, Player player) {
    int visibility;
    switch (player.getType()) {
      case SPECTATOR:
        //Creates an overflow if not reduced
        visibility = Integer.MAX_VALUE - 1_000_000;
        break;
      case FROG:
        visibility = 1;
        break;
      case FLY:
        visibility = 2;
        break;
      case NULL:
        LOGGER.info("Player died: ", player);
        return null;
      default:
        throw new IllegalArgumentException("Did not find matching type!");
    }
    int[][] response = new int[currentBoard.length][currentBoard[0].length];
    for (int i = 0; i < response.length; i++) {
      for (int j = 0; j < response[0].length; j++) {
        if (!(x + visibility >= j && x - visibility <= j && y + visibility >= i && y - visibility <= i)) {
          response[j][i] = 0;
        } else {
          if (currentBoard[j][i].equals(player)) {
            response[j][i] = 5;
          } else {
            response[j][i] = currentBoard[j][i].intValue();
          }
        }
      }
    }
    return response;
  }
}
