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
import sp.para.models.Route;
import sp.para.models.Stops;

public class RouteLabelFragment extends Fragment {

    Route route;
    TextView routeTxtField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_label_fragment, container, false);

        Log.i("RouteLabelFragment - ", "Setting up Route Label Fragment...");

        routeTxtField = (TextView) view.findViewById(R.id.txtRoute);

        // Set value for route name
        routeTxtField.setText(this.route.getName() + " Route");

        return view;
    }

    // Creates new instance for RouteLabelFragment
    public static RouteLabelFragment newInstance(Route route) {
        Log.i("RouteLabelFragment", "Creating new instance of route label fragment...");
        RouteLabelFragment newFragment = new RouteLabelFragment();
        newFragment.setRoute(route);
        return newFragment;
    }

    // Get route
    public Route getRoute() {
        return this.route;
    }

    // Set route
    public void setRoute(Route route) {
        this.route = route;
    }

}