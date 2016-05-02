package sp.para.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import sp.para.R;
import sp.para.models.InstructionNode;

public class StepsFragment extends Fragment {

    ArrayList<InstructionNode> instList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steps_fragment, container, false);

        Log.d("-------------APP", "STEPS FRAGMENTTTT ");

        TableLayout tblSteps = (TableLayout) view.findViewById(R.id.tblSteps);

        for(InstructionNode inst: instList) {
            TableRow newRow = new TableRow(getActivity());
            TextView trial = new TextView(getActivity());
            trial.setText(inst.getStartStop().getStop().getName());
            newRow.addView(trial);
            tblSteps.addView(newRow);
        }

        return view;
    }

    public void setInstList(ArrayList<InstructionNode> instList) {
        this.instList = instList;
    }

}