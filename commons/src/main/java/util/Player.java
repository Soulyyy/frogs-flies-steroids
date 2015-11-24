package util;

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

  long getStartTime();

  int getScore();

  void setScore(int score);

  void setX(int x);

  void setY(int y);

  int getId();

  void setId(int id);
}
