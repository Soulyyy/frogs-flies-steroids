package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import util.Move;

/**
 * Created by Hans on 09/11/2015.
 */
public class GameFieldElements {

  public static Rectangle[][] initializeGameField(int M, int N) {
    Rectangle[][] field = new Rectangle[M][N];
    //Arrays.stream(field).flatMap(Arrays::stream).map(j -> j = new Rectangle(50, 50, Color.YELLOW));
    //Can this be done with a stream?
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        field[i][j] = new Rectangle(70, 70, Color.YELLOW);
        field[i][j].setX(72 * i + 4);
        field[i][j].setY(72 * j + 4);
      }
    }
    return field;

  }

  public static void updateGameField(int[][] ints, Rectangle[][] rects) {
    for (int i = 0; i < rects.length; i++) {
      for (int j = 0; j < rects[0].length; j++) {
        rects[i][j].setFill(mapIntToColor(ints[i][j]));
      }
    }
  }

  public static Color mapIntToColor(int i) {
    switch (i) {
      case 0:
        return Color.BLACK;
      case 1:
        return Color.YELLOW;
      case 2:
        return Color.RED;
      case 3:
        return Color.GREEN;
      case 4:
        return Color.BLUE;
      default:
        return null;
    }
  }

  public static Button chooseFly(Scene scene, Stage stage) {
    Button button = new Button("Choose Fly");
    button.setOnAction(event -> {
      EventCache.previousEvent = Move.NULL;
      stage.setScene(scene);
      stage.show();
    });

    return button;
  }


  public static Button chooseFrog(Scene scene, Stage stage) {
    Button button = new Button("Choose Frog");
    button.setOnAction(event -> {
      EventCache.previousEvent = Move.NULL;
      stage.setScene(scene);
      stage.show();

    });
    return button;
  }
}
