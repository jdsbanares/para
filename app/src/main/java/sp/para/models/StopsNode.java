package sp.para.models;

public class StopsNode {

    // Used for path finding

    // Distance from current stop to destination stop
    private int distance;

    // Cost of going from origin stop to current stop
    private int cost;

    // Parent node
    private StopsNode parent;

    // Current stop
    private StopTime time;

    public StopsNode(int distance, int cost, StopsNode parent, StopTime time) {
        this.distance = distance;
        this.cost = cost;
        this.parent = parent;
        this.time = time;
    }

    public Stops getStop() { return this.time.getStop(); }

    public void setDistance(int distance) { this.distance = distance; }

    public int getDistance() { return this.distance; }

    public void setCost(int cost) { this.cost = cost; }

    public int getCost() { return this.cost; }

    public void setParent(StopsNode parent) { this.parent = parent; }

    public StopsNode getParent() { return this.parent; }

    public void setTime(StopTime time) { this.time = time; }

    public StopTime getTime() { return this.time; }

    // Helper method to get the heuristic used
    public int getHeuristic() {
        return this.cost + this.distance;
    }

}