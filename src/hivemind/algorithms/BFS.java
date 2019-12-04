package hivemind.algorithms;

import LepinskiEngine.Location;
import hivemind.*;

import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public BFS() {    }

    // If target not found it returns NULL
    public LinkedList<Location> bfs(LocationsToNode graph, int x, int y, int targetX, int targetY) {
        Node item = graph.getNode(x, y);
        Queue<Node> q = new LinkedList<Node>();
        if ( (x == targetX) && (y ==targetY) ) return new LinkedList<Location>();
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
                    // Means we have found the target
                    if ((n.getX() == targetX) && (n.getY() == targetY)) {
                        q.clear();
                        graph.setAllNotSeen();
                        return getShortestPath(graph.getNode(x,y), n, graph);
                    }
                }
            }
        }
        return null;
    }

    public LinkedList<Location> getShortestPath(Node start, Node end, LocationsToNode graph) {
        Node n = end;
        LinkedList<Location> retVal = new LinkedList<Location>();
        while (!n.compareTo(start)) {
            retVal.addFirst(graph.getLocation(n));
            n = n.getParent();
        }
        return retVal;
    }
}
