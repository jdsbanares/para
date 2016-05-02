package sp.para.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sp.para.R;
import sp.para.models.InstructionNode;
import sp.para.models.Stops;

public class PartialStepsFragment extends Fragment {

    Stops from;
    Stops to;
    ArrayList<InstructionNode> instList;
    TextView fromTxtFld;
    TextView toTxtFld;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.partial_steps_fragment, container, false);

        fromTxtFld = (TextView) view.findViewById(R.id.txtFrom);
        toTxtFld = (TextView) view.findViewById(R.id.txtTo);

        fromTxtFld.setText(this.from.getName());
        toTxtFld.setText(this.to.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public static PartialStepsFragment newInstance(Stops from, Stops to, ArrayList<InstructionNode> instList) {
        PartialStepsFragment newFragment = new PartialStepsFragment();
        newFragment.setInstList(instList);
        newFragment.setPoints(from, to);
        return newFragment;
    }

    public ArrayList<InstructionNode> getInstList() {
        return this.instList;
    }

    public void setInstList(ArrayList<InstructionNode> instList) {
        this.instList = instList;
    }

    public void setPoints(Stops from, Stops to) {
        this.from = from;
        this.to = to;
    }

}