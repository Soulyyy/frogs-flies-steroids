package util;


import util.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Hans on 22/11/2015.
 */
public interface Engine extends Remote {

  int[][] getBoard() throws RemoteException;

  int[][] getMaskedBoard(Player player) throws RemoteException;

  int getScore(Player player) throws RemoteException;

  Player registerPlayer(Player player) throws RemoteException;

  void setPlayer(Player player, int x, int y) throws RemoteException;

  Player makeMove(Player player, Move move) throws RemoteException;

  Player putPlayerOnBoard(Player player) throws RemoteException;

  Player getPlayer(int x, int y) throws RemoteException;

}
