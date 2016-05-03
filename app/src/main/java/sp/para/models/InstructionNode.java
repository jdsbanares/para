package sp.para.models;

import android.util.Log;

public class InstructionNode {

    private StopTime startStop;
    private StopTime endStop;
    private Route route;
    private InstructionNode parent;

    public InstructionNode(StopTime startStop, StopTime endStop, Route route, InstructionNode parent) {
        this.startStop = startStop;
        this.endStop = endStop;
        this.route = route;
        this.parent = parent;
    }

    public void setRoute(Route route) { this.route = route; }

    public Route getRoute() { return this.route; }

    public void setStartStop(StopTime startStop) { this.startStop = startStop; }

    public StopTime getStartStop() { return this.startStop; }

    public void setEndStop(StopTime endStop) { this.endStop = endStop; }

    public StopTime getEndStop() { return this.endStop; }

    public void setParent(InstructionNode parent) { this.parent = parent; }

    public InstructionNode getParent() { return this.parent; }

}