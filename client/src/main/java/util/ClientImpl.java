package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * Created by Hans on 23/11/2015.
 */
public class ClientImpl {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientImpl.class);

  public static void main(String[] args) throws Exception {
    Registry registry = LocateRegistry.getRegistry("localhost");
    Engine rmiServer = (Engine) registry.lookup("EngineImpl");
    Arrays.stream(rmiServer.getBoard()).flatMapToInt(Arrays::stream).forEach(System.out::println);
    Player player = new PlayerImpl(PlayerType.FROG);
    player = rmiServer.registerPlayer(player);
    LOGGER.info("Current player has coordinates ({}, {})", player.getX(), player.getY());
    LOGGER.info("Current player has ID {}", player.getId());

    int[][] visibleBoard = rmiServer.getMaskedBoard(player);
    for (int[] ints : visibleBoard) {
      for (int i : ints) {
        System.out.print(i);
      }
      System.out.println();
    }
    player = rmiServer.makeMove(player, Move.DOUBLEDOWN);
    LOGGER.info("Current player has coordinates ({}, {})", player.getX(), player.getY());
    LOGGER.info("Current player has ID {}", player.getId());

    System.out.println("-------------------");
    visibleBoard = rmiServer.getMaskedBoard(player);
    for (int[] ints : visibleBoard) {
      for (int i : ints) {
        System.out.print(i);
      }
      System.out.println();
    }
    while (true) {
      if (player.getType() == PlayerType.NULL) {
        //You are dead
        System.out.println("Holy crap, you died :(");
        System.exit(0);
      }
    }

    //player.setType(PlayerType.NULL);
    //rmiServer.setPlayer(player, player.getX(), player.getY());
  }

}
