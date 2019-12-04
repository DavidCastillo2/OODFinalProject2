package hivemind.algorithms;

import hivemind.*;
import java.util.*;
import LepinskiEngine.*;
import javafx.util.Pair;

public class CoinAlg3 implements Algorithm {
  int turnsRemaining;
  Location[][] maze;

    public CoinAlg3(Location[][] Maze, GameState game) {
      // we do care about the number of turns we have remaining for this.
      turnsRemaining = game.turns_remaining;
      this.maze = Maze;
    }

  /**
   * [getMove updates and returns a list of moves and prioritys]
   * @return             [returns a list of DirType (moves) and a priority for each move]
   */
    public LinkedList<Pair<DirType, Location>> getMove(int curX, int curY) {
      LinkedList<Pair<DirType, Location>> retVal = new LinkedList<>();
      LocationsToNode ltn = new LocationsToNode(maze);

      // Get all the Coins Locations
      LinkedList<Node> coins = bfsFindCoins(ltn, curX, curY);

      // Fill in our data Structure
      @SuppressWarnings("unchecked")
      LinkedList<Location>[][] megaHolder = new LinkedList[coins.size() + 1][coins.size() + 1];
      coins.addFirst(ltn.getNode(curX, curY));
      fillDataStructure(megaHolder, coins, ltn);


      // just getting the thing closest to the CoinBot
      LinkedList<Location> path = smallestPath(megaHolder);
      Pair<DirType, Location> closestToBot= getDir(curX, curY, path);
      retVal.add(closestToBot);


      // Get Hamiltonian paths This kills the program
      /*
      HamiltonianPaths hamPaths = new HamiltonianPaths();
      ArrayList<int[]> pathsArray = hamPaths.getAllHamPaths(megaHolder.length);
      Location bestTarget = getBestLocation(pathsArray, megaHolder);
      Pair<DirType, Location> target = getDir(curX, curY, bestTarget);
      retVal.add(target);
       */

      return retVal;
    }

    private Location getBestLocation(ArrayList<int[]> hamPaths, LinkedList<Location>[][] megaHolder) {
      int bestPathLength = 2147483647; // max int btw
      Location bestLocation = null;

      for (int[] path : hamPaths) {
        int currLength = 0;
        for (int i=0; i < path.length - 1; i++) {
          currLength += megaHolder[i][i+1].size();
        }
        if (bestPathLength > currLength) {
          bestPathLength = currLength;
          bestLocation = megaHolder[0][path[1]].getFirst();
        }
      }


      return bestLocation;
    }

    private void fillDataStructure(LinkedList<Location>[][] megaHolder, LinkedList<Node> coins, LocationsToNode ltn) {
      BFS thisBFS = new BFS();

      for (int i=0; i < megaHolder.length; i++) {
        for (int j=0; j < megaHolder[0].length; j++) {
          megaHolder[i][j] = thisBFS.bfs(ltn, coins.get(i).getX(), coins.get(i).getY(), coins.get(j).getX(), coins.get(j).getY());
        }
      }
    }

    private LinkedList<Location> smallestPath(LinkedList<Location>[][] megaHolder) {
      int bestDistance = 2147483647; // max int btw
      LinkedList<Location> bestPath = new LinkedList<>();

      for (LinkedList<Location> path : megaHolder[0]) {
        if ( (path.size() < bestDistance) && (path.size() != 0) ) {
          bestDistance = path.size();
          bestPath = path;
        }
      }
      return bestPath;
    }


    private LinkedList<Node> bfsFindCoins(LocationsToNode graph, int startX, int startY) {
      LinkedList<Node> coins = new LinkedList<Node>();
      Node item = graph.getNode(startX, startY);
      Queue<Node> q = new LinkedList<Node>();
      q.add(item);
      item.setSeen();
      while (!q.isEmpty()) {
        Node i = q.remove();
        for (Location j : i.getOutEdges()) {
          Node n = graph.getNode(j);
          if (n==null) continue;
          if (!n.isSeen()) {
            n.setParent(i);
            q.add(n);
            n.setSeen();
            int numCoinsHere = n.getCoins().size();
            // if this position has more than one coin, we should have an edge of length
            // zero and recalculate (i.e. all outward edges will be identical)
            for (int c = 0; c < numCoinsHere; c++) {
              coins.add(n);
            }
          }
        }
      }
      graph.setAllNotSeen();
      //System.out.println(coins.size());
      return coins;
    }

    // Copied from ScoutAlg
    public Pair<DirType, Location> getDir(int curX, int curY, LinkedList<Location> bestPath) {
      // Find the Shortest path to one of those Locations
      // Find DirType
      Pair<DirType, Location> p;
      if (curX == bestPath.getFirst().getX()) {
        // Down
        if (curY < bestPath.getFirst().getY()) {
          p = new Pair<>(DirType.South, bestPath.getLast());
          // Up
        } else {
          p = new Pair<>(DirType.North, bestPath.getLast());
        }
      } else {
        // Left
        if (curX > bestPath.getFirst().getX()) {
          p = new Pair<>(DirType.West, bestPath.getLast());
          // Right
        } else {
          p = new Pair<>(DirType.East, bestPath.getLast());
        }
      }
      return p;
    }

  public Pair<DirType, Location> getDir(int curX, int curY, Location l) {
      // Old Method that could be used if you give it a target Location
    // Find the Shortest path to one of those Locations
    // Find DirType
    Pair<DirType, Location> p;
    if (curX == l.getX()) {
      // Down
      if (curY < l.getY()) {
        p = new Pair<>(DirType.South, l);
        // Up
      } else {
        p = new Pair<>(DirType.North, l);
      }
    } else {
      // Left
      if (curX > l.getX()) {
        p = new Pair<>(DirType.West, l);
        // Right
      } else {
        p = new Pair<>(DirType.East, l);
      }
    }
    return p;
  }
}
