package hivemind;

import LepinskiEngine.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    The Node[][] will never have nulls, since when the maze Location[][] is handed to it,
        It generates Nodes with an X,Y and if they Location[x][y] is null, then it still
        adds the Node with empty (null) toEdges and InEdges

        Once finished using a search algorithm, you need to call setAllNotSeen

 */

public class LocationsToNode {
    Location[][] mazeArray;
    Node[][] graph;
    public int totalNodes=0;
    public int width = 0;
    public int height = 0;

    public LocationsToNode(Location[][] Maze) {
        this.mazeArray = Maze;
        this.graph = new Node[Maze.length][Maze[0].length];
        this.height = Maze.length;
        this.width = Maze[0].length;
        convert(Maze);
    }

    public LocationsToNode(RectMaze Maze) {
        this.mazeArray = new Location[Maze.getMaxX()][Maze.getMaxY()];
        this.graph = new Node[Maze.getMaxX()][Maze.getMaxY()];
        this.height = Maze.getMaxX();
        this.width = Maze.getMaxY();
        convert(Maze);
    }

    public Node getNode(int x , int y) {
        return graph[x][y];
    }

    public Node getNode(Location l) {
        return graph[l.getX()][l.getY()];
    }

    public Node[][] getNodeArray() { return graph; }

    public Location getLocation(int x, int y) {
        return mazeArray[x][y];
    }

    public Location getLocation(Node n) {
        return mazeArray[n.getX()][n.getY()];
    }

    public void setAllNotSeen() {
        for (Node[] n : graph) {
            for (Node N : n) {
                if (N == null) continue;
                N.setNotSeen();
            }
        }
    }

    public void convert(Location[][] Maze) {
        for (Location[] Xs : Maze) {
            for (Location y : Xs) {
                if (y==null) continue;
                totalNodes++;
                Node newNode = new Node(y.getX(), y.getY());

                newNode.setCoins(y.getCoins());
                //newNode.setXY(y.getX(), y.getY());
                List<DirType> directions = y.getDirections();
                // We try and get all the Children of this node by reaching into the Left,Right,etc. Location
                for (DirType d : directions) {
                    try {
                        if (DirType.North == d) {
                            if (Maze[y.getX()][y.getY()-1] == null) { newNode.setNullChild(); continue; }
                            newNode.addOutNode(Maze[y.getX()][y.getY()-1]);
                            newNode.addInNode (Maze[y.getX()][y.getY()-1]);
                        } else if (DirType.East == d) {
                            if (Maze[y.getX()+1][y.getY()] == null) { newNode.setNullChild(); continue; }
                            newNode.addOutNode(Maze[y.getX()+1][y.getY()]);
                            newNode.addInNode (Maze[y.getX()+1][y.getY()]);
                        } else if (DirType.South == d) {
                            if (Maze[y.getX()][y.getY()+1] == null) { newNode.setNullChild(); continue; }
                            newNode.addOutNode(Maze[y.getX()][y.getY()+1]);
                            newNode.addInNode (Maze[y.getX()][y.getY()+1]);
                        } else {
                            if (Maze[y.getX()-1][y.getY()] == null) { newNode.setNullChild(); continue; }
                            newNode.addOutNode(Maze[y.getX()-1][y.getY()]);
                            newNode.addInNode (Maze[y.getX()-1][y.getY()]);
                        }
                    } catch (Exception e) {
                        // This means we cant see the Node to our Left/Right/etc.
                    }
                }
                // Once we've updated all of Node's member fields we add it to graph[][]
                graph[y.getX()][y.getY()] = newNode;
            }
        }
    }

    public void convert(RectMaze Maze) {
        for (int x=0; x < Maze.getMaxX(); x++) {
            for (int y=0; y < Maze.getMaxY(); y++) {
                if (Maze.getDirections(x, y) == null) continue;
                totalNodes++;
                Node newNode = new Node(x, y);
                mazeArray[x][y] = new Location(x, y);

                //newNode.setXY(y.getX(), y.getY());
                List<DirType> directions = Maze.getDirections(x, y);
                // We try and get all the Children of this node by reaching into the Left,Right,etc. Location
                for (DirType d : directions) {
                    try {
                        if (DirType.North == d) {
                            newNode.addOutNode(new Location(x,y-1));
                            newNode.addInNode (new Location(x,y-1));
                        } else if (DirType.East == d) {
                            newNode.addOutNode(new Location(x + 1,y));
                            newNode.addInNode (new Location(x + 1,y));
                        } else if (DirType.South == d) {
                            newNode.addOutNode(new Location(x,y+1));
                            newNode.addInNode (new Location(x,y+1));
                        } else {
                            newNode.addOutNode(new Location(x-1,y));
                            newNode.addInNode (new Location(x-1,y));
                        }
                    } catch (Exception e) {
                        // This means we cant see the Node to our Left/Right/etc.
                    }
                }
                // Once we've updated all of Node's member fields we add it to graph[][]
                graph[x][y] = newNode;
            }
        }
    }

    public Node[][] getGraph() {
        return graph;
    }

    public int getTotalNodes() { return this.totalNodes; }
}