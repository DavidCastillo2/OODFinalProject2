package hivemind;

import LepinskiEngine.*;
import hivemind.algorithms.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class GLaDOSSearching implements PlayerSearchingTeam {
    Pair<Robot, Algorithm>[] listOfBots;
    Location[][] visibleMaze;
    Algorithm scoutAlg = new RandomAlg(); // Overwritten in Constructor
    Algorithm coinAlg =  new RandomAlg(); // Overwritten in Constructor
    GameState gs = null;


    public void startGame(List<Robot> bots, GameState state) {
        //getPercentSeen();
        listOfBots = new Pair[bots.size()];

        visibleMaze = new Location[state.maze_size_x][state.maze_size_y];
        this.scoutAlg = new ScoutAlg(visibleMaze);
        this.coinAlg = new CoinAlg3(visibleMaze, state);
        ((ScoutAlg) this.scoutAlg).setVision(3);

        // Populate the List of Bots with their respective algorithms
        for (int i=0; i < bots.size(); i++) {
            Robot r=bots.get(i);
            Algorithm a;
            if (r.getModel() == ModelType.ScoutBot) {
                a = scoutAlg;
            } else {
                a = coinAlg;
            }
            Pair<Robot, Algorithm> p = new Pair<Robot, Algorithm>(r, a);
            listOfBots[i] = p;
        }
        gs = state;
    }

    public Location findRobot(List<Location> ls, Robot r) {
        for (Location l : ls) {
            if (l.getRobots() != null) {
                for (Robot robot : l.getRobots()) {
                    if (r.getID() == robot.getID()) return l;
                }
            }
        }
        return null;
    }


    @Override
    public List<Robot> chooseRobots(GameState state) {
        ArrayList<Robot> retVal = new ArrayList<Robot>();

        //CoinBot
        retVal.add(new Robot(ModelType.CoinBot, 0));
        retVal.add(new Robot(ModelType.CoinBot, 1));
        // Scout Bot
        retVal.add(new Robot(ModelType.ScoutBot, 2));
        retVal.add(new Robot(ModelType.ScoutBot, 3));
        // Escort Bot
        //retVal.add(new Robot(ModelType.EscortBot, 4));

        return retVal;
    }


    public List<Command> requestCommands(List<Location> information, List<Robot> robotsAwaitingCommand, GameState state) {
        List<Command> retVal = new ArrayList<Command>();
        LinkedList<Pair<DirType, Location>> chosenLocations = new LinkedList<>();

        if (gs == null ) startGame(robotsAwaitingCommand, state);

        // Populating seen maze array
        for (Location l : information) {
            if (visibleMaze[l.getX()] [l.getY()] == null )  visibleMaze[l.getX()][l.getY()] = l;
        }

        // Make our commands (where decisionMaking() should be called)
        for (Pair<Robot, Algorithm> p : listOfBots) {
            Algorithm a = (Algorithm) p.getValue();
            Robot r = (Robot) p.getKey();
            Location l = findRobot(information, r);

            // For each bot we add a pickUpCoin command since it costs nothing to do so
            Command c = new CommandCoin(r);
            retVal.add(c);

            // Keeps bots split up
            LinkedList<Pair<DirType, Location>> options = a.getMove(l.getX(), l.getY());
            Pair<DirType, Location> chosenPair = options.get(0);

            if (r.getModel() != ModelType.CoinBot) {
                // See if we've already chosen that location
                for (Pair<DirType, Location> takenOption : chosenLocations) {
                    if ((takenOption.getValue().getX() == chosenPair.getValue().getX()) &&
                            (takenOption.getValue().getY() == chosenPair.getValue().getY()) ) {
                        if (options.size() > 1) {
                            // Get other Option
                            // Stubb This value 1 should be edited to have the logic of keep getting next Location until
                            //      we have a location that we haven't seen
                            // Stubb Choose location farthest away from previous selection
                            chosenPair = options.get(1);
                        }
                    }
                }
                // Otherwise we ask for its next move
                //System.out.println("Current Location: (" + l.getX() + ", " + l.getY() + ") Target: (" +
                        //chosenPair.getValue().getX() + ", " + chosenPair.getValue().getY() + ")");
                if (l.getX() == chosenPair.getValue().getX() && l.getY() == chosenPair.getValue().getY() && getPercentSeen() != 0.0) {
                    chosenPair = sameSpotFix(l);
                }

                c = new CommandMove(r, chosenPair.getKey());
                chosenLocations.add(chosenPair);
                retVal.add(c);
            } else {
                c = new CommandMove(r, chosenPair.getKey());
                retVal.add(c);
            }
        }
        return retVal;
    }

    // For the Dark Square shenanigans
    private Pair<DirType, Location> sameSpotFix(Location l) {
        int curX = l.getX();
        int curY = l.getY();
        Pair<DirType, Location> retVal;
        List<DirType> dirs = l.getDirections();
        for(DirType d : dirs) {
            if (d == DirType.North) {
                if (visibleMaze[curX][curY-1] == null) {
                    retVal = new Pair<DirType, Location>(d, visibleMaze[curX][curY]);
                    return retVal;
                }
            }
            if (d == DirType.East) {
                if (visibleMaze[curX+1][curY] == null) {
                    retVal = new Pair<DirType, Location>(d, visibleMaze[curX][curY]);
                    return retVal;
                }
            }
            if (d == DirType.South) {
                if (visibleMaze[curX][curY+1] == null) {
                    retVal = new Pair<DirType, Location>(d, visibleMaze[curX][curY]);
                    return retVal;
                }
            }
            if (d == DirType.West) {
                if (visibleMaze[curX-1][curY] == null) {
                    retVal = new Pair<DirType, Location>(d, visibleMaze[curX][curY]);
                    return retVal;
                }
            }
        }

        return null;
    }

    // Testing Function
    private float getPercentSeen() {
        int nullCount = 0;
        int total = 0;
        //visibleMaze
        for (int x=0; x < visibleMaze.length; x++) {
            for (int y=0; y < visibleMaze[0].length; y++) {
                if (visibleMaze[x][y] == null) {
                    nullCount++;
                }
                total++;
            }
        }
        return (float)nullCount / total;
    }
}

