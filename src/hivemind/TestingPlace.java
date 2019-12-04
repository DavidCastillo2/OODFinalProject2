package hivemind;

import LepinskiEngine.*;
import hivemind.algorithms.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestingPlace {
    Location[][] testMaze = new Location[4][4];

    public TestingPlace() {    }

    public void runTest() {
        for (int x=0; x<4; x++) {
            for (int y=0; y<4; y++) {
                Location l = new Location(x,y);
                List<DirType> list = new ArrayList<>();
                list.add(DirType.North);
                list.add(DirType.East);
                list.add(DirType.South);
                list.add(DirType.West);
                if (x==0) list.remove(DirType.West);
                if (x==3) list.remove(DirType.East);
                if (y==0) list.remove(DirType.North);
                if (y==3) list.remove(DirType.South);
                l.setTheDirs(list);
                testMaze[x][y] = l;
            }
        }
        System.out.println("Test Start");
        ScoutAlg scoutalg = new ScoutAlg(testMaze);
        LinkedList<Pair<DirType, Location>> outPut = new LinkedList<Pair<DirType, Location>>();

        for (int x=2; x < 4; x++) {
            for (int y=2; y<4; y++) {
                testMaze[x][y] = null;
            }
        }

        printMaze(testMaze);
        outPut = scoutalg.getMove(0,0);
        System.out.println("OutPut Size : " + outPut.size());
        outPut = scoutalg.getMove(1,2);

        LocationsToNode ltn = new LocationsToNode(testMaze);
        BFS myBFS = new BFS();
        //System.out.println(myBFS.bfs(ltn, 0,0,3,3));
        System.out.println("Test END\n");
    }

    public void printMaze(Location[][] maze) {
        for (int y=0; y < maze.length; y++) {
            System.out.println("");
            for (int x=0; x < maze[0].length; x++) {
                if (maze[x][y] == null) {
                    System.out.print("  NULL    ");
                } else
                System.out.print(" (" + x + ", " + y + ") | ");
            }
        }
    }


}
