package util;

import java.io.Serializable;

/**
 * Created by Hans on 21/01/2016.
 */
public class ServerObject implements Serializable {

  public String ip;

  public int port;

  public String name;

  public ServerObject(String ip, int port, String name) {
    this.ip = ip;
    this.port = port;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ServerObject that = (ServerObject) o;

    if (port != that.port) return false;
    if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
    return !(name != null ? !name.equals(that.name) : that.name != null);

  }

  @Override
  public int hashCode() {
    int result = ip != null ? ip.hashCode() : 0;
    result = 31 * result + port;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return name;
  }
}
