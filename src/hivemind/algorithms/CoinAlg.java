package hivemind.algorithms;

import hivemind.*;
import java.util.*;
import LepinskiEngine.*;
import javafx.util.Pair;

public class CoinAlg implements Algorithm{

  // HashMap<Pair<from, shortest_paths_to_every_other_coin>, paths>
  HashMap<Location, LinkedList<LinkedList<Location>>> paths;
  Location[][] maze;
  int turnsRemaining;
  ArrayList<LinkedList<Location>> doablePaths;

  // i'm isaac: i'm adding gamestate like the interface so i have access to the number of turns
  // remaining
  public CoinAlg(GameState game){
    // we do care about the number of turns we have remaining for this.
    turnsRemaining = game.turns_remaining;
  }

    /**
     * [getMove updates and returns a list of moves and prioritys]
     * @return             [returns a list of DirType (moves) and a priority for each move]
     */
    public LinkedList<Pair<DirType, Location>> getMove(int curX, int curY) {
      // find the shortest path to each coin from this current position (we don't actually care about
      // coins that we don't have a path to)
      // then, find the shortest path from each of those coins to each other coin

      // then, we want all Hamiltonian paths (and all subpaths?) Traveling Salesman Problem
      //  i.e. we care about paths for as long as the number of steps it takes to complete
      //  is less than or equal to the number of turns we have remaining

      // convert the Location[][] we have into David and Austin's LocationsToNode
      LocationsToNode graph = new LocationsToNode(maze);
      // knownCoins are ONLY the coins we have paths to. we will calculate the
      // shortest paths later. there are repeat calculations below.
      LinkedList<Node> coins = bfsFindCoins(graph, curX, curY);
      for (Node coinFrom : coins) {
        Location coinFromLoc = graph.getLocation(coinFrom);
        LinkedList<LinkedList<Location>> pathsToOthers = new LinkedList<LinkedList<Location>>();
        for (Node coinTo : coins) {
          Location coinToLoc = graph.getLocation(coinTo);
          LinkedList<Location> path = getShortestPath(coinFrom, coinTo, graph);
          pathsToOthers.add(path);
        }
        paths.put(coinFromLoc,pathsToOthers);
      }

      // calculating all possible Hamiltonian paths....
      // this is A LOT of paths we're talking about
      //  i.e. we want every path that visits every single coin
      //  so if we know 20 coins, starting at point 1, we have 19 choices
      //  for the second point, 18 for the third, and so on (this is on the order of
      //  n! total paths without dynamic programming, and might be too much brute force)
      //  https://en.wikipedia.org/wiki/Hamiltonian_path#Properties
      //  https://en.wikipedia.org/wiki/Travelling_salesman_problem#Heuristic_and_approximation_algorithms
      //  https://stackoverflow.com/questions/16555978/example-of-a-factorial-time-algorithm-o-n
      // then, we will decide which Hamiltonian is the best for the number of
      // moves we have available to us

      // instead, i am going to try only calculating the paths we can actually make with our current number
      // of turns because we want the most coins in the fewest turns

      // WE ONLY CARE ABOUT THE LONGEST (COIN) PATH WITH THE SAME NUMBER OF TURNS

      return new LinkedList<Pair<DirType, Location>>();
    }


    // uses paths member field (a path in this case has vertices of coin locations. it is a
    // coin path of location paths)
    // here is my thinking for this method: (it's very late, and i am struggling to conceptualize it)
    // think of this like a tree whose depth is limited by the number of turns remaining
    // (the number of turns it takes to get to each coin is the weight of an edge)
    // first, we create a list of locations called currentCoinPath and add the start location to it
    // we want to use dfs to descend as far down into the tree as we can without exceeding our
    // turns remaining.
    // as we descend, we add the coin representations (which themselves are LinkedList<Location>) 
    // getting us to each node we visit to the currentPath
    // when we run out of turns, store the currentCoinPath and
    // we must remove locations from the currentCoinPath until
    // we have another node option to take that does not exceed turns remaining
    // repeat this process until dfs is complete
    // convert the longest coinPath (which will always be the largest number of coins with our current
    // turns reamining) into a list of plain old locations and return this
    /*private LinkedList<Location> dfsGetLongestCoinPathWithinTurns(Location start, int turns) {
      LinkedList<LinkedList<Location>> doablePaths = new LinkedList<LinkedList<Location>>();
      Stack<Location> s = new Stack<Location>();
      s.push(start);
      LinkedList<Location> currentPath = new LinkedList<Location>();
      while (!s.empty()) {
        for (LinkedList<Location> path : paths.get(s.pop())) {
          if (path.size() + currentPath.size() > turns) {
            // we cannot keep going
            doablePaths.add(currentPath);
          }
          else {
            // add all the locations to this path between the two coins
            currentPath.addAll(path);
          }
        }
      }
    }*/


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
      System.out.println(coins.size());
      return coins;
    }


    private LinkedList<Location> getShortestPath(Node start, Node end, LocationsToNode graph) {
      Node n = end;
      LinkedList<Location> retVal = new LinkedList<Location>();
      while (!n.compareTo(start)) {
          retVal.addFirst(graph.getLocation(n));
          n = n.getParent();
      }
      return retVal;
    }


    /**
     * [setMaze passes a DarkMaze to be used in the algorithms YOU NEED THIS!!!]
     * @param maze [ADD MEEEEEE]
     */
    public void setMaze(Location[][] maze){
      this.maze = maze;
    }
}
