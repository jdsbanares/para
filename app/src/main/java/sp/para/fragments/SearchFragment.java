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

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import sp.para.R;
import sp.para.models.Stops;

public class SearchFragment extends Fragment {

    AutoCompleteTextView originTxtFld;
    AutoCompleteTextView destTxtFld;
    Button findRouteBtn;
    Stops origin;
    Stops destination;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        List<Stops> stopsList = Stops.getAll();

        originTxtFld = (AutoCompleteTextView) view.findViewById(R.id.searchOrigin);
        destTxtFld = (AutoCompleteTextView) view.findViewById(R.id.searchDestination);

        final ArrayAdapter<Stops> originAdapter = new ArrayAdapter<Stops>(getActivity(), android.R.layout.simple_list_item_1, stopsList);
        originTxtFld.setAdapter(originAdapter);

        originTxtFld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                origin = originAdapter.getItem(position);
            }
        });

        final ArrayAdapter<Stops> destAdapter = new ArrayAdapter<Stops>(getActivity(), android.R.layout.simple_list_item_1, stopsList);
        destTxtFld.setAdapter(destAdapter);

        destTxtFld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destination = destAdapter.getItem(position);
            }
        });

        findRouteBtn = (Button) view.findViewById(R.id.findRouteBtn);

        findRouteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                GeoPoint orig = new GeoPoint(origin.getLat(), origin.getLon());
                GeoPoint dest = new GeoPoint(destination.getLat(), destination.getLon());

                Log.d("-------------APP", "DISTANCE = " + orig.distanceTo(dest));

                // TODO: Do A* search_fragment using origin and destination
                ArrayList<Stops> openList = new ArrayList<Stops>();
                ArrayList<Stops> closedList = new ArrayList<Stops>();

                openList.add(origin);

                // Note: Find way to get previous Stop from current Stop

                waypoints.add(orig);
                waypoints.add(dest);
                MapFragment mf = (MapFragment) getFragmentManager().findFragmentByTag("map_frag");
                mf.showRoute(waypoints);
                /*
                Log.d("-------------APP", "ORIGIN SELECTED = " + origin.getLat());
                Log.d("-------------APP", "DESTIN SELECTED = " + destination.getLat());

                List<StopTime> originStopTimeList = StopTime.getAllByStops(origin);

                Log.d("-------------APP", "ORIGIN ST SIZE = " + originStopTimeList.size());

                for(StopTime st : originStopTimeList) {
                    StopTime prev = st.getPrev();
                    StopTime next = st.getNext();
                    if(prev != null)
                        Log.d("-------------APP", "ST PREV = " + prev.getStop().getName());
                    if(next != null)
                        Log.d("-------------APP", "ST NEXT = " + next.getStop().getName());
                }
                */


//                List<Trip> originTripList = new ArrayList<Trip>();
//                for(StopTime st : originStopTimeList) {
//                    originTripList.add(st.getTrip());
//                }
//
//                Set<Trip> uniqueTrips = new LinkedHashSet<Trip>(originTripList);
//
//                Log.d("-------------APP", "ORIGIN TRIP SIZE = " + uniqueTrips.size());
            }
        });
        return view;
    }

}