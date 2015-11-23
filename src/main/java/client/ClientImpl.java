package client;

import common.Player;
import common.PlayerImpl;
import common.PlayerType;
import server.Engine;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * Created by Hans on 23/11/2015.
 */
public class ClientImpl {

  public static void main(String[] args) throws Exception {
    Registry registry = LocateRegistry.getRegistry("localhost");
    Engine rmiServer = (Engine) registry.lookup("EngineImpl");
    Player player = new PlayerImpl();
    player.setType(PlayerType.FROG);
    Arrays.stream(rmiServer.getBoard()).flatMapToInt(Arrays::stream).forEach(System.out::println);
    int[][] visibleBoard = rmiServer.getMaskedBoard(4, 0, player);
    for (int[] ints : visibleBoard) {
      for (int i : ints) {
        System.out.print(i);
      }
      System.out.println();
    }
  }
}
