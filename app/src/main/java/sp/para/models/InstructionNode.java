package sp.para.models;

public class InstructionNode {

    // Used for building instructions when a path has been found

    // Start point for this instruction
    private StopTime startStop;

    // End point for this instruction
    private StopTime endStop;

    // Route for this instruction
    // If route is null, instruction calls for walking
    private Route route;

    // Parent node for instruction
    // Used for tracing back
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