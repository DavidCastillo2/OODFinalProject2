package hivemind;

import LepinskiEngine.*;

public class KnownMaze {
    MazeLocation[][] rooms;
    int max_x;
    int max_y;
    int total_coins;

    public int getTotalCoins(){
      return total_coins;
    }

    public int getCoinsFound(){
      int coins_found = 0;
      return coins_found;
    }
    public MazeLocation getLocation(int x, int y){
	return rooms[x][y];
    }

    public void setLocation(int x, int y, MazeLocation loc){
	rooms[x][y] = loc;
    }

    public MazeLocation getTeamStart(){
	return null;
    }

    public int getMaxX(){
	return max_x;
    }

    public int getMaxY(){
	return max_y;
    }


    public KnownMaze(int max_x, int max_y, int total_coins){
	this.max_x = max_x;
	this.max_y = max_y;
	rooms = new MazeLocation[max_x][max_y];
	for(int i = 0; i<max_x; i++){
	    for(int j = 0; j<max_y; j++){
		rooms[i][j] = null;
	    }
	}
    }
}
