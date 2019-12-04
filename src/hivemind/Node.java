package hivemind;

import LepinskiEngine.CoinType;
import LepinskiEngine.Location;

import java.util.ArrayList;
import java.util.List;

public class Node {
    ArrayList<Location> outEdges = new ArrayList<Location>();
    ArrayList<Location> inEdges = new ArrayList<Location>();
    boolean hasNullChild = false;
    List<CoinType> coins;
    Node parent;
    boolean isSeen = false;
    int x,y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setNullChild() { this.hasNullChild = true; }

    public boolean hasNullChild() { return this.hasNullChild; }

    public Node getParent() {
        return this.parent;
    }

    public boolean compareTo(Node b) {
        return ((this.getX() == b.getX()) && (this.getY() == b.getY()));
    }

    public Node() {    }

    public String toString() {
        return ("( " + this.x + ", " + this.y + " )");
    }

    public void setParent(Node n) {
        this.parent = n;
    }

    public void setCoins(List<CoinType> coins) {
        this.coins = coins;
    }

    public void setXY(int X, int Y) {
        this.x = X;
        this.y = Y;
    }

    public void setSeen() {
        this.isSeen = true;
    }

    public void setNotSeen() {
        this.isSeen = false;
    }

    public List<CoinType> getCoins(){
        return this.coins;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void addOutNode(Location l) {
        outEdges.add(l);
    }

    public boolean isSeen() {
        return this.isSeen;
    }


    public void addInNode(Location l) {
        inEdges.add(l);
    }

    public ArrayList<Location> getOutEdges() {
        return this.outEdges;
    }

    public ArrayList<Location> getInEdges() {
        return this.inEdges;
    }
}
