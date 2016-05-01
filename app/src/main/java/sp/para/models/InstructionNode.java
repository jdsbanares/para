package sp.para.models;

import android.util.Log;

public class InstructionNode {

    private StopTime startStop;
    private StopTime endStop;
    private InstructionNode parent;

    public InstructionNode(StopTime startStop, StopTime endStop, InstructionNode parent) {
        this.startStop = startStop;
        this.endStop = endStop;
        this.parent = parent;
    }

    public void setStartStop(StopTime startStop) { this.startStop = startStop; }

    public StopTime getStartStop() { return this.startStop; }

    public void setEndStop(StopTime endStop) { this.endStop = endStop; }

    public StopTime getEndStop() { return this.endStop; }

    public void setParent(InstructionNode parent) { this.parent = parent; }

    public InstructionNode getParent() { return this.parent; }

}