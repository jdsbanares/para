package sp.para.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sp.para.R;
import sp.para.models.StopTime;
import sp.para.models.Stops;
import sp.para.models.StopsNode;

public class StepsFragment extends Fragment {

    Stops from;
    Stops to;
    TextView fromTxtFld;
    TextView toTxtFld;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steps_fragment, container, false);

        fromTxtFld = (TextView) view.findViewById(R.id.txtFrom);
        toTxtFld = (TextView) view.findViewById(R.id.txtTo);

        fromTxtFld.setText(this.from.getName());
        toTxtFld.setText(this.to.getName());

        return view;
    }

    public static StepsFragment newInstance(Stops from, Stops to) {
        StepsFragment newFragment = new StepsFragment();
        newFragment.setPoints(from, to);
        return newFragment;
    }

    public void setPoints(Stops from, Stops to) {
        this.from = from;
        this.to = to;
    }

}