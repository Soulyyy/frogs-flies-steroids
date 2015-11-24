package engine;


import util.Engine;
import util.Player;
import util.PlayerImpl;
import util.PlayerType;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Hans on 22/11/2015.
 */
public class EngineImpl implements Engine {


  //TODO May need to be of type player
  private static Player[][] gameField = new Player[ConstantCache.HEIGHT][ConstantCache.WIDTH];

  public EngineImpl() throws RemoteException {
    for (int i = 0; i < gameField.length; i++) {
      for (int j = 0; j < gameField[0].length; j++) {
        gameField[i][j] = new PlayerImpl(PlayerType.NULL, i, j);
      }
    }
  }

  @Override
  public int[][] getBoard() {
    int[][] intBoard = new int[ConstantCache.HEIGHT][ConstantCache.WIDTH];
    for (int i = 0; i < gameField.length; i++) {
      for (int j = 0; j < gameField[0].length; j++) {
        intBoard[i][j] = gameField[i][j].intValue();
      }
    }
    return intBoard;
  }

  @Override
  public int[][] getMaskedBoard(Player player) {
    return EngineHandler.maskBoard(gameField,player.getX(),player.getY(), player);
  }

  @Override
  public int getScore(Player player) throws RemoteException {
    if (player.getType() == PlayerType.FLY) {
      return (int) (player.getStartTime() / ConstantCache.TICK);
    }
    if (player.getType() == PlayerType.FROG) {
      return player.getScore();
    }
    return 0;
  }

  @Override
  public void registerPlayer(Player player) {
    while (true) {
      int x = (int) (Math.random() * 8);
      int y = (int) (Math.random() * 8);
      if (gameField[x][y].getType() == PlayerType.NULL) {
        gameField[x][y] = player;
        player.setX(x);
        player.setY(y);
        break;
      }
    }
  }

  public static void main(String[] args) throws Exception {

    System.out.println("Started server!");

    EngineImpl engine = new EngineImpl();


    Engine stub = (Engine) UnicastRemoteObject.exportObject(engine, 0);
    Registry registry = LocateRegistry.createRegistry(1099);
    registry.bind("EngineImpl", stub);
  }
}
