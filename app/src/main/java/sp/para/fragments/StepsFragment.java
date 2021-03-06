package sp.para.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import sp.para.R;
import sp.para.models.InstructionNode;

public class StepsFragment extends Fragment {

    Button backBtn;
    ListView listSteps;
    ArrayList<InstructionNode> instList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steps_fragment, container, false);

        Log.i("StepsFragment - ", "Setting up Steps Fragment...");

        // Goes back to previous fragment
        backBtn = (Button) view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Log.i("StepsFragment - ", "Building instructions...");

        ArrayList<String> stepsList = new ArrayList<String>();
        int listSize = instList.size();
        StringBuilder dirText = new StringBuilder();

        // Iterate over instList to produce step-by-step instructions
        for(int i = 0; i < listSize; i++) {
            InstructionNode inst = instList.get(i);

            dirText.setLength(0);

            // If start and end stops of the current instruction node is not equal,
            // create an instruction for riding a vehicle
            if(!inst.getStartStop().getStop().getStopId().equals(inst.getEndStop().getStop().getStopId())) {
                dirText.append("Take a ");
                // Check which type of transportation will be used
                switch(inst.getRoute().getRouteId().substring(0,9)) {
                    case "LTFRB_PUB":
                        dirText.append("bus with the route ");
                        break;
                    case "LTFRB_PUJ":
                        dirText.append("jeepney with the route ");
                        break;
                    default:
                        dirText.append("vehicle with the route ");
                        break;
                }
                dirText.append(inst.getRoute().getName());
                dirText.append(" from ");
                dirText.append(inst.getStartStop().getStop().getName());
                dirText.append(" to ");
                dirText.append(inst.getEndStop().getStop().getName());
            }

            // If the current instruction node is the last instruction node,
            // inform the user that he/she has arrived at the destination
            // Else, create an instruction for walking
            if (i == (listSize - 1)) {
                // Add instruction to stepsList if dirText is not empty
                if(dirText.length() != 0) {
                    stepsList.add(dirText.toString());
                }

                dirText.setLength(0);
                dirText.append("You have arrived at ");
                dirText.append(inst.getEndStop().getStop().getName());

                stepsList.add(dirText.toString());
            }
            else if (!inst.getEndStop().getStop().getStopId().equals(instList.get(i + 1).getStartStop().getStop().getStopId())) {
                // Add instruction to stepsList if dirText is not empty
                if(dirText.length() != 0) {
                    stepsList.add(dirText.toString());
                }

                dirText.setLength(0);
                dirText.append("Walk from ");
                dirText.append(inst.getEndStop().getStop().getName());
                dirText.append(" to ");
                dirText.append(instList.get(i + 1).getStartStop().getStop().getName());

                stepsList.add(dirText.toString());
            }
        }

        // Assign stepsList to array adapter for list view content in the fragment
        final ArrayAdapter<String> stepsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.steps_list_item, stepsList);

        listSteps = (ListView) view.findViewById(R.id.listSteps);

        listSteps.setAdapter(stepsAdapter);

        return view;
    }

    public void setInstList(ArrayList<InstructionNode> instList) {
        this.instList = instList;
    }

}