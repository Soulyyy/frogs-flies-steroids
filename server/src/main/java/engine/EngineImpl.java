package engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Scanner;
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
    return EngineHandler.maskBoard(gameField, player.getX(), player.getY(), player);
  }

  @Override
  public int getScore(Player player) throws RemoteException {
    if (player.getType() == PlayerType.FLY) {
      return (int) ((System.currentTimeMillis() - player.getStartTime()) / ConstantCache.TICK);
    }
    if (player.getType() == PlayerType.FROG) {
      return player.getScore();
    }
    return 0;
  }

  @Override
  public Player registerPlayer(Player player) {
    LOGGER.info("Registerd player: {}", player);
    player.setId(ID.incrementAndGet());
    return player;
  }

  @Override
  public Player putPlayerOnBoard(Player player) {
    while (true) {
      int x = (int) (Math.random() * 8);
      int y = (int) (Math.random() * 8);
      if (gameField[x][y].getType() == PlayerType.NULL) {
        LOGGER.info("Added player {} at ({}, {})", player.getType(), x, y);
        LOGGER.info("Player ID set as {}", player.getId());
        gameField[x][y] = player;
        player.setX(x);
        player.setY(y);
        LOGGER.info("Player with coordinates ({}, {})", player.getX(), player.getY());
        return player;
      }
    }
  }

  @Override
  public Player getPlayer(int x, int y) throws RemoteException {
    return gameField[x][y];
  }

  @Override
  public void setPlayer(Player player, int x, int y) {
    gameField[x][y] = player;
  }

  @Override
  public Player makeMove(Player player, Move move) {
    player.setScore(gameField[player.getX()][player.getY()].getScore());
    return MovementHandler.handleMove(move, player, gameField);
  }

  // Source: https://gist.github.com/vorburger/3429822
  private static int findFreePort() {
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(0);
      socket.setReuseAddress(true);
      int port = socket.getLocalPort();
      try {
        socket.close();
      } catch (IOException e) {
        // Ignore IOException on close()
      }
      return port;
    } catch (IOException e) {
    } finally {
      if (socket != null) {
        try {
          socket.close();
        } catch (IOException e) {
        }
      }
    }
    throw new IllegalStateException("Could not find a free TCP/IP port");
  }


  public static void main(String[] args) throws Exception {

    System.out.println("Started server!");

    EngineImpl engine = new EngineImpl();

    Engine stub = (Engine) UnicastRemoteObject.exportObject(engine, 0);

    Registry registry = LocateRegistry.createRegistry(findFreePort());
    registry.bind("EngineImpl", stub);

    String ip = args.length >= 1 ? args[0] : "localhost";
    int port = args.length >= 2 && new Scanner(args[1]).hasNextInt() ? new Integer(args[1]) : 8080;
    new Thread(new Announcer(ip, port)).start();
    //Ok, actually, this loop does not matter, this is for clearing dead frogs
    //Flies don't get old, because of spec and radiation
    while (true) {
      //Ghetto clocking, don't want to burn CPU
      Thread.sleep(1000);
      //Poll the dead, kill them
      Arrays.stream(gameField).flatMap(Arrays::stream).forEach(MovementHandler::death);

    }
  }
}
