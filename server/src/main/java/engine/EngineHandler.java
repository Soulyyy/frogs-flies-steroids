package engine;


import util.Player;

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
          response[i][j] = 0;
        } else {
          if(currentBoard[i][j].equals(player)) {
            response[i][j] = 5;
          } else {
            response[i][j] = currentBoard[i][j].intValue();
          }
        }
      }
    }
    return response;
  }
}
