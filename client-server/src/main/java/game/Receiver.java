package game;

import util.ConstantCache;
import util.ServerObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.*;

/**
 * Created by Hans on 21/01/2016.
 */
public class Receiver implements Runnable {

/*
  MulticastSocket socket;

  Receiver(MulticastSocket socket) {
    this.socket = socket;
  }
*/


  private static void receiveMulticast(MulticastSocket socket) throws IOException {
    while (true) {
      DatagramPacket packet = new DatagramPacket(new byte[8096], 8096);
      socket.receive(packet);
      try (ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
           ObjectInput input = new ObjectInputStream(bis)) {
        Object object = input.readObject();
        if (object instanceof ServerObject) {
          ServerObject serverObject = (ServerObject) (object);
          ServerLister.updateServers(serverObject);
          ServerSelect.updateListView();
          System.out.println(serverObject.name);
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void run() {
    try {
      InetAddress inetAddress = InetAddress.getByName(ConstantCache.MULTICAST_IP);
      MulticastSocket socket = new MulticastSocket(ConstantCache.MULTICAST_PORT);
      //NetworkInterface networkInterface = NetworkInterface.getByName()
      socket.joinGroup(inetAddress);
      receiveMulticast(socket);
      //Cleanup task in receiver
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
