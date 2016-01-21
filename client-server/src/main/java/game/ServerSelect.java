package game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.ServerObject;

/**
 * Created by Hans on 21/01/2016.
 * Contains methods for selecting a server from a list
 */
public class ServerSelect {

  private static ListView<ServerObject> listView;
  private static Pane pane;

  public static void serverSelect(Stage primaryStage) {
    pane = new Pane();
    Scene scene = new Scene(pane, 800, 600);
    primaryStage.setScene(scene);
    listView = new ListView<>();
/*    ObservableList<ServerObject> items = FXCollections.observableArrayList(
        new ServerObject("allahu", 1234, "akbar"), new ServerObject("akbar", 12344, "akbarmode1334567"));*/
    //listView.setItems(items);
    new Thread(() -> new Receiver().run()).start();
    Button start = new Button("Select Server");
    start.setLayoutX(100);
    start.setLayoutY(400);
    start.setOnAction(event -> {
      try {
        //ClientMain.setup(new String[]{listView.getSelectionModel().getSelectedItem().ip, listView.getSelectionModel().getSelectedItem().port + ""}, primaryStage);
        ClientMain.setup(new String[]{}, primaryStage);
      } catch (Exception ignore) {
        ignore.printStackTrace();
      }
    });

    new Thread(() -> {
      while (true) {
        System.out.println("cleanup");
        ServerLister.updateServers(null);
        ServerSelect.updateListView();
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

    pane.getChildren().addAll(listView, start);
    primaryStage.setOnCloseRequest(event -> System.exit(0));
    primaryStage.show();
  }


  public static void start(Stage primaryStage) throws Exception {
    serverSelect(primaryStage);

  }

  public static void updateListView() {
    System.out.println("updating list view");
    Platform.runLater(() -> {
          pane.getChildren().remove(listView);
          ObservableList<ServerObject> items = FXCollections.observableArrayList(
              ServerLister.serverObjectList.keySet());
          listView.setItems(items);
          pane.getChildren().add(listView);
        }
    );

  }

}
