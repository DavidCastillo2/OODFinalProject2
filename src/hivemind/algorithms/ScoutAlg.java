package hivemind.algorithms;

import hivemind.*;

import java.lang.reflect.Array;
import java.util.*;
import LepinskiEngine.*;
import javafx.util.Pair;

public class ScoutAlg implements Algorithm {
  Location[][] maze;
  int[] target = {-1, -1};
  public int visionRadius = 3;
  scoutBFS sBFS = new scoutBFS();

  public ScoutAlg(Location[][] Maze) {
    this.maze = Maze;
  }

  public void setVision(int v) {
    this.visionRadius = v;
  }

  public int[] getTarget() {
    return this.target;
  }

  /**
   * [getMove updates and returns a list of moves and prioritys]
   *
   * @return [returns a list of DirType (moves) and a priority for each move]
   */
  public LinkedList<Pair<DirType, Location>> getMove(int curX, int curY) {

    LinkedList<Pair<DirType, Location>> retVal = new LinkedList<>();
    LocationsToNode ltn = new LocationsToNode(maze);

    // Find All edges of Graph
    ArrayList<Location> possibleTargets = findTargets(ltn, curX, curY);

    // We populate retVal by finding the shortest path to a possible Target
    //    if we run out of possible Targets before lengthOfLinkedList then we stop adding items
    int size = possibleTargets.size();

    for (int i=0; i<size; i++) {
      // Find shortest path from list of Targets
      LinkedList<Location> currTarget = findBestTargetPath(ltn, curX, curY, possibleTargets);

      // Add that path to our list and remove it from possible Targets
      retVal.add(getDir(curX, curY, currTarget));

      possibleTargets.remove(currTarget.getLast());

    }

    return retVal;
  }

  public Pair<DirType, Location> getDir(int curX, int curY, LinkedList<Location> bestPath) {
    // Find the Shortest path to one of those Locations
    //LinkedList<Location> bestPath = findBestTargetPath(ltn, curX, curY, possibleTarget);
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

    public boolean isNotNearBorder(LocationsToNode graph, Location l) {
      // if Too close to Left Edge
      return ((l.getX() - visionRadius >= 0) &&
              // if Too close to Top Edge
              (l.getY() - visionRadius >= 0) &&
              // if Too close to Bottom Edge
              (l.getY() + visionRadius <= graph.height) &&
              // if Too close to Right Edge
              (l.getX() + visionRadius <= graph.width));
    }

    public boolean hasVoidChildren(Location l) {
      List<DirType> children = l.getDirections();
      for (DirType d : children) {
        if (DirType.North == d) {
          if (maze[l.getX()][l.getY() - 1] == null) return true;
        } else if (DirType.East == d) {
          if (maze[l.getX() + 1][l.getY()] == null) return true;
        } else if (DirType.South == d) {
          if (maze[l.getX()][l.getY() + 1] == null) return true;
        } else {
          if (maze[l.getX() - 1][l.getY()] == null) return true;
        }
      }
      return false;
    }

    public ArrayList<Location> findTargets(LocationsToNode graph, int curX, int curY) {
      ArrayList<Location> targets = sBFS.bfs(graph, curX, curY);
      if (targets.size() == 0) {
        // This is where the bot has no actual target. For coinBot this is where you would say now be a scoutBot
        targets.add(graph.getLocation(curX, curY));
      }
      return targets;
    }

    // This is where we decide which path we end up taking
    public LinkedList<Location> findBestTargetPath(LocationsToNode graph, int curX, int curY, ArrayList<Location> al) {
      int bestLength = 2147483647; // Max int btw
      LinkedList<Location> bestPath = new LinkedList<Location>();
      for (Location l : al) {
        if ( (l.getX() == curX) && (l.getY() == curY) ) {
          bestPath.clear();
          bestPath.add(l);
          return bestPath;
        }
        LinkedList<Location> path = sBFS.bfs(graph, curX, curY, l.getX(), l.getY());
        if (bestLength > path.size()) {
          bestLength = path.size();
          bestPath = path;
        }
      }
      return bestPath;
    }
}

class scoutBFS extends BFS {

  public scoutBFS() {
    super();
  }

  public ArrayList<Location> bfs(LocationsToNode graph, int x, int y) {
    graph.setAllNotSeen();
    Node item = graph.getNode(x, y);
    Queue<Node> q = new LinkedList<Node>();
    q.add(item);
    item.setSeen();
    ArrayList<Location> retVal = new ArrayList<Location>();
    while (!q.isEmpty()) {
      Node i = q.remove();
      // This adds an item when it has no Children AKA is an edge
      if (i.hasNullChild()) retVal.add(graph.getLocation(i));

      for (Location j : i.getOutEdges()) {
        Node n = graph.getNode(j);
        if (!n.isSeen()) {
          n.setParent(i);
          n.setSeen();
          q.add(n);
        }
      }
    }
    graph.setAllNotSeen();
    return retVal;
  }
}