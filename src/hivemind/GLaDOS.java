package hivemind;
import LepinskiEngine.*;
import hivemind.algorithms.DFS;
import hivemind.algorithms.DavidDFS;

import java.io.DataInput;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class GLaDOS implements PlayerHidingTeam {
    Location[][] visibleMaze;

    @Override
    public void startGame(List<ObstacleType> obs, List<CoinType> coins, RectMaze maze, GameState state) {
        // Stubb
    }

    @Override
    public List<PlaceObstacle> setObstacles(List<ObstacleType> list_obs, RectMaze maze, GameState state)
    {

        LocationsToNode ltn = new LocationsToNode(maze);
        DavidDFS dDfs = new DavidDFS();
        LinkedList<Location> longPath = dDfs.dfs(ltn, 0, 0);
        int x = longPath.getLast().getX();
        int y = longPath.getLast().getY();

        List<PlaceObstacle> placements = new ArrayList<PlaceObstacle>();

        for (ObstacleType ob : list_obs){
            PlaceObstacle place = new PlaceObstacle(ob, x % maze.getMaxX(), y % maze.getMaxY());
            placements.add(place);
            x = x + 1;
        }

        return placements;
    }

    @Override
    public List<PlaceCoin> hideCoins(List<CoinType> coins, RectMaze maze, GameState state)
    {
        int[] x = {0, maze.getMaxX()-1, maze.getMaxX()-1, 0};
        int[] y = {0, maze.getMaxY()-1, 0, maze.getMaxY()-1};
        int X=0;
        LinkedList<Location>[] optionsArray = new LinkedList[4];

        List<PlaceCoin> placements = new ArrayList<PlaceCoin>();
        LocationsToNode ltn = new LocationsToNode(maze);
        DavidDFS dDfs = new DavidDFS();
        for (int i=0; i<4; i++) {
            LinkedList<Location> longPath = dDfs.dfs(ltn, x[i], y[i]);
            optionsArray[i] = longPath;
        }

        for (CoinType c : coins){
            PlaceCoin place = new PlaceCoin(c, optionsArray[X].getLast().getX(), optionsArray[X].getLast().getY());
            X = (X+1) % 4;
            placements.add(place);
        }

        return placements;
    }

}
