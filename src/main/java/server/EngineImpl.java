package server;

import common.Player;
import common.PlayerImpl;
import common.PlayerType;

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
      for(int j =0; j < gameField[0].length; j++) {
        gameField[i][j] = new PlayerImpl(PlayerType.NULL, i, j);
      }
    }
  }

  @Override
  public int[][] getBoard() {
    int[][] intBoard = new int[ConstantCache.HEIGHT][ConstantCache.WIDTH];
    for (int i = 0; i < gameField.length; i++) {
      for(int j =0; j < gameField[0].length; j++) {
       intBoard[i][j] = gameField[i][j].intValue();
      }
    }
    return intBoard;
  }

  @Override
  public int[][] getMaskedBoard(int x, int y, Player player) {
    return EngineHandler.maskBoard(getBoard(), x, y, player.getType());
  }

  @Override
  public int getScore(Player player) throws RemoteException {
    if(player.getType() == PlayerType.FLY) {
      return
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
