package sp.para.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import sp.para.R;
import sp.para.models.InstructionNode;
import sp.para.models.Route;
import sp.para.models.Stops;

public class RoutesFragment extends Fragment {

    Button backBtn;
    ListView listRoutes;
    AutoCompleteTextView routeSearch;

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

        final ArrayAdapter<Route> routeAdapter = new ArrayAdapter<Route>(getActivity(), android.R.layout.simple_list_item_1, Route.getAll());

        routeSearch = (AutoCompleteTextView) view.findViewById(R.id.routeSearch);

        routeSearch.setAdapter(routeAdapter);

        routeSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View currView = getActivity().getCurrentFocus();
                if(currView != null) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                MapFragment mf = (MapFragment) getFragmentManager().findFragmentByTag("map_frag");
                mf.showExisting(routeAdapter.getItem(position));
            }
        });

        listRoutes = (ListView) view.findViewById(R.id.listRoutes);

        listRoutes.setAdapter(routeAdapter);

        listRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View currView = getActivity().getCurrentFocus();
                if(currView != null) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                MapFragment mf = (MapFragment) getFragmentManager().findFragmentByTag("map_frag");
                mf.showExisting(routeAdapter.getItem(position));
            }
        });

        return view;
    }

}