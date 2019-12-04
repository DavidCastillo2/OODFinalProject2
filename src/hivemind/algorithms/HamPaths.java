package hivemind.algorithms;

import java.util.ArrayList;
import java.util.List;

// data structure to store graph edges
class Edge
{
    int source, dest;

    public Edge(int source, int dest) {
        this.source = source;
        this.dest = dest;
    }
};

// class to represent a graph object
class Graph
{
    // An array of Lists to represent adjacency list
    List<List<Integer>> adjList = null;

    // Constructor
    Graph(List<Edge> edges, int N)
    {
        adjList = new ArrayList<>(N);

        for (int i = 0; i < N; i++) {
            adjList.add(i, new ArrayList<>());
        }

        // add edges to the undirected graph
        for (int i = 0; i < edges.size(); i++)
        {
            int src = edges.get(i).source;
            int dest = edges.get(i).dest;

            adjList.get(src).add(dest);
            adjList.get(dest).add(src);
        }
    }
}

class HamiltonianPaths
{
    ArrayList<int[]> retVal = new ArrayList<>();

    public ArrayList<int[]> getAllHamPaths(int numOfNodes) {
        // Make all the edges
        List<Edge> edges = new ArrayList<>();
        for (int i=0; i < numOfNodes; i++) {
            for (int j=i+1; j<numOfNodes; j++) {
                edges.add(new Edge(i, j));
            }
        }

        Graph g = new Graph(edges, numOfNodes);

        // starting node
        int start = 0;

        // add starting node to the path
        List<Integer> path = new ArrayList<>();
        path.add(start);

        // mark start node as visited
        boolean[] visited = new boolean[numOfNodes];
        visited[start] = true;

        printAllHamiltonianPaths(g, start, visited, path, numOfNodes);
        return this.retVal;
    }

    public void printAllHamiltonianPaths(Graph g, int v,
                                                boolean[] visited, List<Integer> path, int N)
    {
        // if all the vertices are visited, then
        // hamiltonian path exists
        if (path.size() == N)
        {
            // print hamiltonian path
            int[] item = new int[path.size()];

            for (int i=0; i < path.size(); i++) {
                item[i] = path.get(i);
            }
            retVal.add(item);
            return;
        }

        // Check if every edge starting from vertex v leads
        // to a solution or not
        for (int w : g.adjList.get(v))
        {
            // process only unvisited vertices as hamiltonian
            // path visits each vertex exactly once
            if (!visited[w])
            {
                visited[w] = true;
                path.add(w);

                // check if adding vertex w to the path leads
                // to solution or not
                printAllHamiltonianPaths(g, w, visited, path, N);

                // Backtrack
                visited[w] = false;
                path.remove(path.size()-1);
            }
        }
    }
}