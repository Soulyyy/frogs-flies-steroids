package game;

import engine.EngineImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Hans on 10/01/2016.
 */
//CLients go to list of servers
public class ApplicationMain extends Application {

  private static String[] strings;

  @Override
  public void start(Stage primaryStage) throws Exception {
    Pane root = new Pane();

    RadioButton chooseServer = new RadioButton();
    RadioButton chooseClient = new RadioButton();
    chooseServer.setText("Server");
    chooseClient.setText("Client");
    chooseServer.setLayoutX(200);
    chooseServer.setLayoutY(200);
    chooseClient.setLayoutX(200);
    chooseClient.setLayoutY(300);

    Button start = new Button("Start");
    start.setLayoutX(200);
    start.setLayoutY(400);
    start.setOnAction(event -> {
      if (chooseServer.isSelected()) {
        new Thread(() -> {
          try {
            EngineImpl.main(strings);
          } catch (Exception ignore) {
            ignore.printStackTrace();
          }
        }).start();
      }
      if (chooseClient.isSelected()) {
        new Thread(() -> {
          try {
            ClientMain.setup(strings);
            ClientMain.start(primaryStage);
          } catch (Exception ignore) {
            ignore.printStackTrace();
          }
        }).run();
      }
    });

    root.getChildren().addAll(new ButtonBase[]{chooseServer, chooseClient, start});

    Scene scene = new Scene(root, 800, 600);

    primaryStage.setScene(scene);
    primaryStage.show();

  }

  public static void main(String[] args) {
    strings = args;
    new Thread(() -> ApplicationMain.launch(args)).start();
  }
}
