package engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Hans on 22/11/2015.
 */
public class EngineImpl implements Engine {

  private static final Logger LOGGER = LoggerFactory.getLogger(EngineImpl.class);


  public static final AtomicInteger ID = new AtomicInteger(0);

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
      //TODO PROBABLY WRONG
      return (int) (player.getStartTime() / ConstantCache.TICK);
    }
    if (player.getType() == PlayerType.FROG) {
      return player.getScore();
    }
    return 0;
  }

  @Override
  public Player registerPlayer(Player player) {
    player.setId(ID.incrementAndGet());
    while (true) {
      int x = (int) (Math.random() * 8);
      int y = (int) (Math.random() * 8);
      if (gameField[x][y].getType() == PlayerType.NULL) {
        LOGGER.info("Added player {} at ({}, {})", player.getType(), x , y);
        LOGGER.info("Player ID set as {}", player.getId());
        gameField[x][y] = player;
        player.setX(x);
        player.setY(y);
        return player;
      }
    }
  }

  @Override
  public void setPlayer(Player player, int x, int y) {
    gameField[x][y] = player;
  }

  @Override
  public Player makeMove(Player player, Move move) {
    return MovementHandler.handleMove(move, player, gameField);
  }


  public static void main(String[] args) throws Exception {

    System.out.println("Started server!");

    EngineImpl engine = new EngineImpl();

    Engine stub = (Engine) UnicastRemoteObject.exportObject(engine, 0);
    Registry registry = LocateRegistry.createRegistry(1099);
    registry.bind("EngineImpl", stub);

    //Main loop of the game
    while (true) {
      //Ghetto clocking on 2 FPS
      Thread.sleep(500);
      //Poll the dead, kill them
      Arrays.stream(gameField).flatMap(Arrays::stream).forEach(MovementHandler::death);

    }
  }
}
