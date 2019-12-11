
package hivemind.algorithms;
import LepinskiEngine.*;

import java.util.LinkedList;
import java.util.List;

public class DFS {

    public static class DFSLocation {
        // we need this class because there is no way
        // to set the directions of a Location
        // outside of LepinskiEngine (?)
        public int x;
        public int y;
        public List<DirType> dirs;
        public boolean marked;
        public DFSLocation(int x, int y, List<DirType> dirs) {
            this.x = x;
            this.y = y;
            this.dirs = dirs;
        }

        public void mark() {
            this.marked = true;
        }

        public Location convertToLocation() {
            return new Location(x, y);
        }
    }

    public static LinkedList<Location> convertToLocList(LinkedList<DFSLocation> input) {
        LinkedList<Location> output = new LinkedList<Location>();
        for (DFSLocation inputLoc : input) {
            output.add(inputLoc.convertToLocation());
        }
        return output;
    }

    public static LinkedList<Location> dfs(int spawnX, int spawnY, RectMaze maze, Location target) {
        return dfs(spawnX, spawnY, maze);
    }

    public static LinkedList<Location> dfs(int spawnX, int spawnY, RectMaze maze) {
        DFSLocation[][] mazeGrid = new DFSLocation[maze.getMaxX()][maze.getMaxY()];
        // assume the maze is non-empty
        for (int x = 0; x < mazeGrid.length; x++) {
            // this is x
            for (int y = 0; y < mazeGrid[0].length; y++) {
                // this is y
                DFSLocation current = new DFSLocation(x, y, maze.getDirections(x, y));
                mazeGrid[x][y] = current;
            }
        }
        DFSLocation dfsSpawn = mazeGrid[spawnX][spawnY];
        LinkedList<DFSLocation> stack = new LinkedList<DFSLocation>();
        LinkedList<Location> longestPath = new LinkedList<Location>();
        stack.push(dfsSpawn);
        while (!stack.isEmpty()) {
            DFSLocation current = stack.peek();
            // never eat soggy waffles
            current.mark();
            if (current.dirs.contains(DirType.North) && !mazeGrid[current.x][current.y - 1].marked) {
                current = mazeGrid[current.x][current.y - 1];
                stack.add(current);
            }
            else if (current.dirs.contains(DirType.East) && !mazeGrid[current.x + 1][current.y].marked) {
                current = mazeGrid[current.x + 1][current.y];
                stack.add(current);
            }
            else if (current.dirs.contains(DirType.South) && !mazeGrid[current.x][current.y + 1].marked) {
                current = mazeGrid[current.x][current.y + 1];
                stack.add(current);
            }
            else if (current.dirs.contains(DirType.West) && !mazeGrid[current.x - 1][current.y].marked) {
                current = mazeGrid[current.x - 1][current.y];
                stack.add(current);
            }
            else {
                // backtrack and check if this is the longest path
                if (stack.size() > longestPath.size()) {longestPath = convertToLocList(stack);
                    current = stack.pop();
                }
            }
        }
        return longestPath;
    }
}