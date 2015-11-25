package game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hans on 26/10/2015.
 */

//FRONT END DEVELOPMENT REQUIRES NO CODE QUALITY
//            (Only CSS)
public class Main extends Application {

  static Player player;

  static Engine rmiServer;

  static int[][] visibleBoard;

  static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  static String ip;

  static String name = "";

  public static String score = "0";

  public static Label scoreLabel;

  public static void changeScore(String score) {
    Platform.runLater(() -> scoreLabel.setText(score));
  }

  public static void main(String[] args) throws Exception {
    String ip;

    String name;

    if (args.length == 1) {
      ip = args[0];
      name = "default";
    } else if (args.length > 1) {
      ip = args[0];
      name = args[1];
    } else {
      ip = "localhost";
      name = "default";
    }
    new Thread(() -> {
      Main.launch(args);
    }).start();

    Registry registry = LocateRegistry.getRegistry("localhost");
    rmiServer = (Engine) registry.lookup("EngineImpl");
    //Arrays.stream(rmiServer.getBoard()).flatMapToInt(Arrays::stream).forEach(System.out::println);
    player = new PlayerImpl(PlayerType.SPECTATOR);
    player = rmiServer.registerPlayer(player);
    /*LOGGER.info("Current player has coordinates ({}, {})", player.getX(), player.getY());
    LOGGER.info("Current player has ID {}", player.getId());*/
    visibleBoard = rmiServer.getMaskedBoard(player);

   /* int[][] visibleBoard = rmiServer.getMaskedBoard(player);
    for (int[] ints : visibleBoard) {
      for (int i : ints) {
        System.out.print(i);
      }
      System.out.println();
    }
    //player = rmiServer.makeMove(player, Move.DOUBLEDOWN);
    LOGGER.info("Current player has coordinates ({}, {})", player.getX(), player.getY());
    LOGGER.info("Current player has ID {}", player.getId());

    System.out.println("-------------------");
    visibleBoard = rmiServer.getMaskedBoard(player);
    for (int[] ints : visibleBoard) {
      for (int i : ints) {
        System.out.print(i);
      }
      System.out.println();
    }*/
    //This time active game loop is here
    while (true) {
      //We fps block in frontend, because we are a modern game and build on game tick
      //Read this as a rant
      Thread.sleep(500);
      if (player.getType() == PlayerType.NULL) {
        //You are dead
        System.out.println("Holy crap, you died :(");
        System.exit(0);
      }
      if (player.getType() == PlayerType.SPECTATOR) {
        visibleBoard = rmiServer.getMaskedBoard(player);
        GameFieldElements.updateGameField(visibleBoard, EventCache.rects);
        continue;
      }
      LOGGER.info("Making move: {}", EventCache.previousEvent);
      player = rmiServer.makeMove(player, EventCache.previousEvent);
      EventCache.previousEvent = Move.NULL;
      visibleBoard = rmiServer.getMaskedBoard(player);
      LOGGER.info("Current player has coordinates ({}, {})", player.getX(), player.getY());
      LOGGER.info("Current player has ID {}", player.getId());
      try {
        GameFieldElements.updateGameField(visibleBoard, EventCache.rects);
        changeScore(player.getScore() + "");
      } catch (NullPointerException e) {
        LOGGER.warn("Game not initialized");
      }

    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    LOGGER.info("Client working!");
    Label nameLabel = new Label(name);

    scoreLabel = new Label("0");
    scoreLabel.setLayoutX(650);
    scoreLabel.setLayoutY(100);
    nameLabel.setLayoutX(650);
    nameLabel.setLayoutY(200);

    Pane root = new Pane();
    EventCache.rects = GameFieldElements.initializeGameField(8, 8);
    List<Rectangle> rectangleList = Arrays.stream(EventCache.rects).flatMap(Arrays::stream).collect(Collectors.toList());
    Rectangle[] rectangles = rectangleList.toArray(new Rectangle[rectangleList.size()]);
    LOGGER.debug("Get root children: {}", root.getChildren());
    Scene scene = new Scene(root, 800, 600);
    Button fly = GameFieldElements.chooseFly(scene, primaryStage);
    fly.setLayoutX(700);
    fly.setLayoutY(100);
    root.getChildren().add(fly);
    Button frog = GameFieldElements.chooseFrog(scene, primaryStage);
    frog.setLayoutX(700);
    frog.setLayoutY(200);

    root.getChildren().add(frog);
    root.getChildren().addAll(rectangles);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setOnCloseRequest(event -> System.exit(0));

    fly.setOnAction(event -> {
          root.getChildren().removeAll(fly, frog);
          root.getChildren().addAll(scoreLabel, nameLabel);
          //EventCache.previousEvent = Move.FLY;
          try {
            LOGGER.info("Registering Fly, {}", player);
            player.setType(PlayerType.FLY);
            // player = rmiServer.registerPlayer(player);
            player = rmiServer.putPlayerOnBoard(player);
            visibleBoard = rmiServer.getMaskedBoard(player);
            GameFieldElements.updateGameField(visibleBoard, EventCache.rects);
          } catch (RemoteException e) {
            LOGGER.error("Failed to register fly position!", e);
            System.exit(1);
          }
          primaryStage.setScene(scene);
          primaryStage.show();
        }
    );

    frog.setOnAction(event -> {
          root.getChildren().removeAll(fly, frog);
          root.getChildren().addAll(scoreLabel, nameLabel);
          //EventCache.previousEvent = Move.FROG;
          try {
            player.setType(PlayerType.FROG);
            LOGGER.info("Registering Frog, {}", player);
            //player = rmiServer.registerPlayer(player);
            player = rmiServer.putPlayerOnBoard(player);
            visibleBoard = rmiServer.getMaskedBoard(player);
            GameFieldElements.updateGameField(visibleBoard, EventCache.rects);

          } catch (RemoteException e) {
            LOGGER.error("Failed to register Frog!", e);
            System.exit(1);
          }
          primaryStage.setScene(scene);
          primaryStage.show();
        }
    );

    scene.setOnKeyPressed(event -> {
      Move tmp = Move.NULL;
      if (EventCache.previousEvent != Move.NULL && player.getType() == PlayerType.FROG) {
        LOGGER.info("Temporary event is: {}", EventCache.previousEvent);
        tmp = EventCache.previousEvent;
      }
      if (event.getCode() == KeyCode.UP) {
        if (tmp == Move.UP) {
          EventCache.previousEvent = Move.DOUBLEUP;
        } else {
          EventCache.previousEvent = Move.UP;
        }
      } else if (event.getCode() == KeyCode.DOWN) {
        if (tmp == Move.DOWN) {
          EventCache.previousEvent = Move.DOUBLEDOWN;
        } else {
          EventCache.previousEvent = Move.DOWN;
        }
      } else if (event.getCode() == KeyCode.LEFT) {
        if (tmp == Move.LEFT) {
          EventCache.previousEvent = Move.DOUBLELEFT;
        } else {
          EventCache.previousEvent = Move.LEFT;
        }
      } else if (event.getCode() == KeyCode.RIGHT) {
        if (tmp == Move.RIGHT) {
          EventCache.previousEvent = Move.DOUBLERIGHT;
        } else {
          EventCache.previousEvent = Move.RIGHT;
        }
      }
    });
  }

}
