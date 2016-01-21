package util;

import java.rmi.Remote;

/**
 * Created by Hans on 23/11/2015.
 */
public class ConstantCache implements Remote {

  public static final int HEIGHT = 8;
  public static final int WIDTH = 8;

  public static final long TICK = 120000L;

  public static final int MULTICAST_PORT = 12345;

  public static final String MULTICAST_IP = "224.0.0.0";

  public static final long TIMEOUT = 20000L;
}
