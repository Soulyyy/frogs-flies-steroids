package engine;


import util.Player;
import util.PlayerType;

/**
 * Created by Hans on 23/11/2015.
 */
public class EngineHandler {

  public static int[][] maskBoard(Player[][] currentBoard, int x, int y, Player player) {
    int visibility;
    switch (player.getType()) {
      case SPECTATOR:
        visibility = Integer.MAX_VALUE;
        break;
      case FROG:
        visibility = 1;
        break;
      case FLY:
        visibility = 2;
        break;
      default:
        throw new IllegalArgumentException("Did not find matching type!");
    }
    int[][] response = new int[currentBoard.length][currentBoard[0].length];
    for (int i = 0; i < response.length; i++) {
      for (int j = 0; j < response[0].length; j++) {
        if (!(x + visibility >= j && x - visibility <= j && y + visibility >= i && y - visibility <= i)) {
          response[j][i] = 0;
        } else {
          if(currentBoard[j][i].equals(player)) {
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
