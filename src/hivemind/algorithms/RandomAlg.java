package hivemind.algorithms;

import hivemind.*;
import java.util.*;
import LepinskiEngine.*;
import javafx.util.Pair;

public class RandomAlg implements Algorithm{

  Location[][] maze;
  Random ran;

  public RandomAlg(){
    ran = new Random();

  }
  /**
   * [getMove updates and returns a list of moves and prioritys]
   * @return             [returns a list of DirType (moves) and a priority for each move]
   */
    public LinkedList<Pair<DirType, Location>> getMove(int curX, int curY){
      LinkedList<Pair<DirType, Location>> moves = new LinkedList<Pair<DirType, Location>>();
      int choice = ran.nextInt(4);
      switch (choice) {
        case 0:
          moves.add(new Pair<DirType, Location>(DirType.West, null));
          break;
        case 1:
          moves.add(new Pair<DirType, Location>(DirType.South, null));
          break;
        case 2:
          moves.add(new Pair<DirType, Location>(DirType.North, null));
          break;
        case 3:
          moves.add(new Pair<DirType, Location>(DirType.East, null));
          break;
      }
      moves.add(new Pair<DirType, Location>(DirType.East, null));
      return moves;
    }


    /**
     * [setMaze passes a DarkMaze to be used in the algorithms YOU NEED THIS!!!]
     * @param maze [ADD MEEEEEE]
     */
    public void setMaze(Location[][] maze){
      this.maze = maze;
    }


}
