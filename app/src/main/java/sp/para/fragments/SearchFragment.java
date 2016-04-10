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
import sp.para.models.StopTime;
import sp.para.models.Stops;
import sp.para.models.StopsNode;

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

                // TODO: Do A* search_fragment using origin and destination
                ArrayList<StopsNode> openList = new ArrayList<StopsNode>();
                ArrayList<StopsNode> closedList = new ArrayList<StopsNode>();
                ArrayList<StopsNode> successors = new ArrayList<StopsNode>();

                openList.add(new StopsNode(origin, orig.distanceTo(dest), 0, null));

                int iter = 0;

                while(!openList.isEmpty()) {

                    Log.d("-------------APP", "Iter SIZE = " + iter++);

                    StopsNode node;
                    node = openList.get(0);

                    Log.d("-------------APP", "Node name = " + node.getStop().getName());
                    Log.d("-------------APP", "Node ID = " + node.getStop().getStopId());
                    Log.d("-------------APP", "Node distance = " + node.getHeuristic());

                    // Remove node from openList
                    openList.remove(0);
                    // Transfer node to closedList
                    closedList.add(node);

                    // Check if distance of node is zero
                    // If zero, break the while loop (Solution found)
                    // Else, proceed to the loop body
                    if(node.getDistance() == 0) {
                        Log.d("-------------APP", "FINAL STOP = " + node.getStop().getName());
                        break;
                    }

                    GeoPoint nodePoint = new GeoPoint(node.getStop().getLat(), node.getStop().getLon());

                    // Generate successor/s
                    for(StopTime st: StopTime.getAllByStops(node.getStop())) {
                        StopTime prev = st.getPrev();
                        StopTime next = st.getNext();

                        if(prev != null) {
                            Stops prevStop = prev.getStop();
                            GeoPoint prevPoint = new GeoPoint(prevStop.getLat(), prevStop.getLon());
                            successors.add(new StopsNode(prevStop, prevPoint.distanceTo(dest), node.getCost() + nodePoint.distanceTo(prevPoint), node));
                        }

                        if(next != null) {
                            Stops nextStop = next.getStop();
                            GeoPoint nextPoint = new GeoPoint(nextStop.getLat(), nextStop.getLon());
                            successors.add(new StopsNode(nextStop, nextPoint.distanceTo(dest), node.getCost() + nodePoint.distanceTo(nextPoint), node));
                        }
                    }

                    int size = successors.size();

                    // Add successors to openList if not found in both lists
                    for(int i = 0; i < size; i++) {

                        StopsNode child = successors.get(i);

                        int closedIndex = indexInList(closedList, child);

                        if (closedIndex > -1) {
                            if (closedList.get(closedIndex).getHeuristic() > child.getHeuristic())
                                insertNode(openList, child);
                            continue;
                        }

                        int openIndex = indexInList(openList, child);

                        if (openIndex > -1) {
                            if (openList.get(openIndex).getHeuristic() > child.getHeuristic()) {
                                openList.remove(openIndex);
                                insertNode(openList, child);
                            }
                            continue;
                        }

                        insertNode(openList, child);
                    }

                    successors.clear();

                }

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

            public int indexInList(List<StopsNode> stopsList, StopsNode node) {
                for(int i = 0; i < stopsList.size(); i++) {
                    if(stopsList.get(i).getStop().getStopId().equals(node.getStop().getStopId()))
                        return i;
                }
                return -1;
            }

            public void insertNode(List<StopsNode> stopsList, StopsNode node) {

                if(stopsList.isEmpty())
                    stopsList.add(node);
                else if(stopsList.get(0).getHeuristic() > node.getHeuristic())
                    stopsList.add(0, node);
                else {
                    int i;

                    for(i = 1; i < stopsList.size(); i++){
                        if(stopsList.get(i).getHeuristic() > node.getHeuristic()){
                            stopsList.add(i,node);
                            break;
                        }
                    }

                    if(i == stopsList.size())
                        stopsList.add(node);
                }

            }
        });
        return view;
    }

}