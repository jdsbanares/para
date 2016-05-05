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

        TableLayout tblSteps = (TableLayout) view.findViewById(R.id.tblSteps);

        for(InstructionNode inst: instList) {
            TableRow newRow = new TableRow(getActivity());

            StringBuilder dirText = new StringBuilder();

            if(inst.getRoute() == null) {
                dirText.append("Walk from ");
                dirText.append(inst.getStartStop().getStop().getName());
                dirText.append(" to ");
                dirText.append(inst.getEndStop().getStop().getName());
            }
            else if(inst.getStartStop().getId() == inst.getEndStop().getId()) {
                dirText.append("You have arrived at ");
                dirText.append(inst.getStartStop().getStop().getName());
            }
            else {
                dirText.append("Take ");
                dirText.append(inst.getRoute().getName());
                dirText.append(" from ");
                dirText.append(inst.getStartStop().getStop().getName());
                dirText.append(" to ");
                dirText.append(inst.getEndStop().getStop().getName());
            }

            TextView trial = new TextView(getActivity());
            trial.setTextAppearance(getActivity(), R.style.TextAppearance_AppCompat_Medium);
            trial.setText(dirText.toString());

            newRow.addView(trial);
            tblSteps.addView(newRow);

            newRow.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            trial.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;

            trial.setPadding(20,10,20,10);
        }

        return view;
    }

    public void setInstList(ArrayList<InstructionNode> instList) {
        this.instList = instList;
    }

}