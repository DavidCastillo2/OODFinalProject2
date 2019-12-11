package hivemind.algorithms;

import LepinskiEngine.*;
import hivemind.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class DavidDFS {

    protected static byte white = 0, grey = 1, black = 2;
    DFSNode farthestTarget; int pathLength = 0;

    public DavidDFS() {
    }

    class DFSNode {
        ArrayList<Location> outEdges = new ArrayList<Location>();
        int color= white;
        DFSNode parent;
        int x; int y;

        public DFSNode(int X, int Y) {
            this.x= X;
            this.y = Y;
        }

        public void setColor(int c) {
            color = c;
        }

        public void setParent(DFSNode n) { this.parent = n; }

        public DFSNode getParent() { return this.parent; }

        public int getColor() { return this.color; }

        public int getX() { return this.x; }

        public int getY() { return this.y; }

        public void setOutEdges(ArrayList<Location> l) { this.outEdges = l; }

        public ArrayList<Location> getOutEdges() { return this.outEdges; }

        public boolean compareTo(DFSNode n) {
            return ( (n.getX() == x) && (n.getY() == y) );
        }
    }

    class DFSNodeGraph {
        DFSNode[][] graph;

        public DFSNodeGraph(LocationsToNode ltn) {
            graph = new DFSNode[ltn.height][ltn.width];

            Node[][] nodeArray = ltn.getNodeArray();
            for (Node[] row : nodeArray) {
                for (Node n : row) {
                    graph[n.getX()][n.getY()] = new DFSNode(n.getX(), n.getY());
                    graph[n.getX()][n.getY()].setOutEdges(n.getOutEdges());
                }
            }
        }

        public void setAllWhite() {
            for (DFSNode[] row : graph) {
                for (DFSNode n : row) {
                    n.setColor(white);
                }
            }
        }

        public Location getLocation( DFSNode n ) {
            return new Location(n.getX(), n.getY());
        }

        public DFSNode getDFSNode(Location n) {
            return graph[n.getX()][n.getY()];
        }

        public DFSNode getDFSNode(int x, int y) {
            return graph[x][y];
        }

        public DFSNode getDFSNode(DFSNode n) {
            return graph[n.getX()][n.getY()];
        }
    }

    public LinkedList<Location> dfs(LocationsToNode g, int x, int y) {
        DFSNodeGraph dfsGraph = new DFSNodeGraph(g);
        DFSNode firstDFSNode = dfsGraph.getDFSNode(x, y);
        dfs(dfsGraph, firstDFSNode, 0);
        // Reset Things
        pathLength = 0;
        return getPath(firstDFSNode, farthestTarget, dfsGraph);
    }

    public void dfs(DFSNodeGraph g, DFSNode n, int distance) {
        n.setColor(grey); // currently visiting i
        for (Location j : n.getOutEdges()) {
            DFSNode edgeNode = g.getDFSNode(j);
            if (edgeNode.getColor() == white) {
                edgeNode.setParent(n);
                distance++;
                edgeNode.setColor(grey);
                dfs(g, edgeNode, distance);
            }
        }
        n.setColor(black); // done visiting i
        if (distance > pathLength) {
            pathLength = distance;
            farthestTarget = g.getDFSNode(n);
        }
    }

    public LinkedList<Location> getPath(DFSNode start, DFSNode end, DFSNodeGraph graph) {
        DFSNode n = end;
        LinkedList<Location> retVal = new LinkedList<Location>();
        while (!n.compareTo(start)) {
            retVal.addFirst(graph.getLocation(n));
            n = n.getParent();
        }
        return retVal;
    }
}