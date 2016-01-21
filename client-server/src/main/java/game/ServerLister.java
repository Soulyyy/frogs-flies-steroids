package game;

import util.ConstantCache;
import util.ServerObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hans on 21/01/2016.
 */
public class ServerLister {

  public static final Map<ServerObject, Long> serverObjectList = new ConcurrentHashMap<>();
  public static void updateServers(ServerObject serverObject) {
    synchronized(serverObjectList) {
      boolean found = false;
      for (ServerObject server : serverObjectList.keySet()) {
        if (server.equals(serverObject)) {
          found = true;
          serverObjectList.put(server, System.currentTimeMillis());
        } else {
          System.out.println("hello");
          System.out.println(System.currentTimeMillis() - serverObjectList.get(server));
          if (System.currentTimeMillis() - serverObjectList.get(server) > ConstantCache.TIMEOUT) {
            found = true;
            serverObjectList.remove(server);
          }
        }
      }
      if (!found && serverObject != null) {
        serverObjectList.put(serverObject, System.currentTimeMillis());
      }
    }
  }


}
