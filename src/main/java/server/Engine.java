package server;

import common.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Hans on 22/11/2015.
 */
public interface Engine extends Remote {

  int[][] getBoard() throws RemoteException;

  int[][] getMaskedBoard(int x, int y, Player player) throws RemoteException;

  int getScore(Player player) throws RemoteException;
}
