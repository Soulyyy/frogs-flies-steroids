package common;

import java.io.Serializable;

/**
 * Created by Hans on 23/11/2015.
 */
public interface Player extends Serializable {

  PlayerType getType();

  void setType(PlayerType type);

  int intValue();

  int getX();

  int getY();
}
