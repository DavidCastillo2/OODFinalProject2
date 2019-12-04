package hivemind;

import LepinskiEngine.*;
import java.util.*;
import hivemind.*;
import javafx.util.Pair;

public interface Algorithm{



/**
 * [getMove updates and returns a list of moves and prioritys]
 * @return             [returns a list of DirType (moves) and a priority for each move]
 */
  public LinkedList<Pair<DirType, Location>> getMove(int curX, int curY);



}
