package sp.para.models;

import android.util.Log;

public class StopsNode {

    private Stops stop;
    private int distance;
    private int cost;
    private StopsNode parent;

    public StopsNode(Stops stop, int distance, int cost, StopsNode parent) {
        this.stop = stop;
        this.distance = distance;
        this.cost = cost;
        this.parent = parent;
    }

    public void setStop(Stops stop) { this.stop = stop; }

    public Stops getStop() { return this.stop; }

    public void setDistance(int distance) { this.distance = distance; }

    public int getDistance() { return this.distance; }

    public void setCost(int cost) { this.cost = cost; }

    public int getCost() { return this.cost; }

    public void setParent(StopsNode parent) { this.parent = parent; }

    public StopsNode getParent() { return this.parent; }

    public int getHeuristic() {
        return this.cost + this.distance;
    }

}