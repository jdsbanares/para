package sp.para.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

        routeTxtField = (TextView) view.findViewById(R.id.txtRoute);

        routeTxtField.setText(this.route.getName() + " Route");

        return view;
    }

    public static RouteLabelFragment newInstance(Route route) {
        RouteLabelFragment newFragment = new RouteLabelFragment();
        newFragment.setRoute(route);
        return newFragment;
    }

    public Route getRoute() {
        return this.route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

}