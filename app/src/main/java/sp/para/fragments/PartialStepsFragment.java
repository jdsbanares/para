package sp.para.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sp.para.R;
import sp.para.models.InstructionNode;
import sp.para.models.Stops;

public class PartialStepsFragment extends Fragment {

    Stops from;
    Stops to;
    ArrayList<InstructionNode> instList;
    FrameLayout partialFrame;
    TextView fromTxtFld;
    TextView toTxtFld;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.partial_steps_fragment, container, false);

        Log.i("PartialStepsFragment - ", "Setting up Partial Steps Fragment...");

        partialFrame = (FrameLayout) view.findViewById(R.id.partialFrame);
        fromTxtFld = (TextView) view.findViewById(R.id.txtFrom);
        toTxtFld = (TextView) view.findViewById(R.id.txtTo);

        // Set values for from and to text views
        fromTxtFld.setText(this.from.getName());
        toTxtFld.setText(this.to.getName());

        partialFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PartialStepsFragment - ", "Partial steps fragment clicked!");
                // Redirect to steps fragment
                StepsFragment stepsFragment = new StepsFragment();
                stepsFragment.setInstList(getInstList());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.main_activity, stepsFragment, "steps_frag");
                ft.addToBackStack("steps_frag");
                ft.commit();
            }
        });

        return view;
    }

    // Creates new instance for PartialStepsFragment
    public static PartialStepsFragment newInstance(Stops from, Stops to, ArrayList<InstructionNode> instList) {
        Log.i("PartialStepsFragment - ", "Creating new instance of partial steps fragment...");
        PartialStepsFragment newFragment = new PartialStepsFragment();
        newFragment.setInstList(instList);
        newFragment.setPoints(from, to);
        return newFragment;
    }

    // Get instructions list
    public ArrayList<InstructionNode> getInstList() {
        return this.instList;
    }

    // Set instructions list
    public void setInstList(ArrayList<InstructionNode> instList) {
        this.instList = instList;
    }

    // Set origin and destination
    public void setPoints(Stops from, Stops to) {
        this.from = from;
        this.to = to;
    }

}