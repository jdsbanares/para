package sp.para.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import sp.para.R;
import sp.para.models.InstructionNode;
import sp.para.models.Route;

public class RoutesFragment extends Fragment {

    Button backBtn;
    TableLayout tblRoutes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.routes_fragment, container, false);

        backBtn = (Button) view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        tblRoutes = (TableLayout) view.findViewById(R.id.tblRoutes);

        for(final Route currRoute: Route.getAll()) {
            TableRow newRow = new TableRow(getActivity());

            TextView trial = new TextView(getActivity());
            trial.setTextAppearance(getActivity(), R.style.TextAppearance_AppCompat_Medium);
            trial.setText(currRoute.getName());

            newRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapFragment mf = (MapFragment) getFragmentManager().findFragmentByTag("map_frag");
                    mf.showExisting(currRoute);
                }
            });

            newRow.addView(trial);
            tblRoutes.addView(newRow);

            newRow.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            trial.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;

            trial.setPadding(20,10,20,10);
        }
        return view;
    }

}