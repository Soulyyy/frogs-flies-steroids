package engine;

import util.ConstantCache;
import util.ServerObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Hans on 21/01/2016.
 * Uses multicast to announce it's existence
 */
public class Announcer implements Runnable {

  //TODO temporary for polling test
/*  public static void main(String[] args) {
    new Thread(() -> {
      new Announcer("127.0.0.1", 8080).run();
    }).start();

    new Thread(() -> new Receiver().run()).run();


  }*/

  private final int port;
  private final ServerObject serverObject;

  Announcer(String ip, int port) {
    this.port = port;
    this.serverObject = new ServerObject(ip, port, ip + ":" + port);
  }

  @Override
  public void run() {
    InetAddress inetAddress = null;
    MulticastSocket socket = null;
    try {
      inetAddress = InetAddress.getByName(ConstantCache.MULTICAST_IP);
      socket = new MulticastSocket(ConstantCache.MULTICAST_PORT);
      socket.joinGroup(inetAddress);
    } catch (IOException e) {
      e.printStackTrace();
    }

    while (true) {
      System.out.println("ab");
      broadcast(inetAddress, socket);
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void broadcast(InetAddress inetAddress, MulticastSocket socket) {
    byte[] data = convertToBytes(serverObject);
    try {
      sendMulticast(data, inetAddress, socket, port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private byte[] convertToBytes(Object object) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutput out = new ObjectOutputStream(bos)) {
      out.writeObject(object);
      return bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //TODO might be a bad idea
    return new byte[0];
  }

  private static void sendMulticast(byte[] bytes, InetAddress address,
                                    MulticastSocket socket, int port) throws IOException {
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
    packet.setAddress(address);
    packet.setPort(ConstantCache.MULTICAST_PORT);
    socket.send(packet);
    System.out.println("Sent msg");
    System.out.println(bytes.length);
  }
}
