package server;

import common.PlayerType;

/**
 * Created by Hans on 23/11/2015.
 */
public class EngineHandler {

  public static int[][] maskBoard(int[][] currentBoard, int x, int y, PlayerType type) {
    int visibility;
    switch (type) {
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

    for (int i = 0; i < currentBoard.length; i++) {
      for (int j = 0; j < currentBoard[0].length; j++) {
        if (!(x + visibility >= j && x - visibility <= j && y + visibility >= i && y - visibility <= i)) {
          currentBoard[j][i] = 0;

        }
      }
    }
    return currentBoard;
  }
}
