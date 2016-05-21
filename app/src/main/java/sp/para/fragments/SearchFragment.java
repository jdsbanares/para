package sp.para.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sp.para.R;
import sp.para.models.Route;
import sp.para.models.StopTime;
import sp.para.models.Stops;
import sp.para.models.StopsNode;
import sp.para.models.Trip;

public class SearchFragment extends Fragment {

    AutoCompleteTextView originTxtFld;
    AutoCompleteTextView destTxtFld;
    Button backBtn;
    Button routesBtn;
    Button findRouteBtn;
    Stops origin = null;
    Stops destination = null;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        Log.i("SearchFragment - ", "Setting up Search Fragment...");
        Log.i("SearchFragment - ", "Stops size = " + Stops.getAll().size());

        List<Stops> stopsList = Stops.getAll();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        originTxtFld = (AutoCompleteTextView) view.findViewById(R.id.searchOrigin);
        destTxtFld = (AutoCompleteTextView) view.findViewById(R.id.searchDestination);

        // Assign an adapter containing the choices for origin field
        final ArrayAdapter<Stops> originAdapter = new ArrayAdapter<Stops>(getActivity(), android.R.layout.simple_list_item_1, stopsList);
        originTxtFld.setAdapter(originAdapter);

        originTxtFld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                origin = originAdapter.getItem(position);
            }
        });

        // Assign an adapter containing the choices for destination field
        final ArrayAdapter<Stops> destAdapter = new ArrayAdapter<Stops>(getActivity(), android.R.layout.simple_list_item_1, stopsList);
        destTxtFld.setAdapter(destAdapter);

        destTxtFld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destination = destAdapter.getItem(position);
            }
        });

        // Goes back to previous fragment
        backBtn = (Button) view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SearchFragment - ", "Back button clicked!");
                getFragmentManager().popBackStack();
            }
        });

        routesBtn = (Button) view.findViewById(R.id.routesBtn);

        // Goes to routes fragment
        routesBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SearchFragment - ", "Routes button clicked!");

                View currView = getActivity().getCurrentFocus();
                // Closes on-screen keyboard if visible
                if(currView != null) {
                    Log.i("SearchFragment - ", "Closing on-screen keyboard...");
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                // Go to RoutesFragment
                RoutesFragment routesFragment = new RoutesFragment();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.main_activity, routesFragment, "routes_frag");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // Goes to update fragment
        routesBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("SearchFragment - ", "Routes button long clicked!");

                View currView = getActivity().getCurrentFocus();
                // Closes on-screen keyboard if visible
                if(currView != null) {
                    Log.i("SearchFragment - ", "Closing on-screen keyboard...");
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                // Go to UpdateFragment
                UpdateFragment updateFragment = new UpdateFragment();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.main_activity, updateFragment, "update_frag");
                ft.addToBackStack(null);
                ft.commit();
                return true;
            }
        });


        findRouteBtn = (Button) view.findViewById(R.id.findRouteBtn);

        findRouteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SearchFragment - ", "Find Routes button clicked!");

                View currView = getActivity().getCurrentFocus();
                // Closes on-screen keyboard if visible
                if(currView != null) {
                    Log.i("SearchFragment - ", "Closing on-screen keyboard...");
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                // Create toast for null inputs
                if(origin == null || destination == null) {
                    Toast.makeText(getActivity(), R.string.null_orig_dest, Toast.LENGTH_LONG).show();
                }
                else if(origin.getStopId() == destination.getStopId()) {
                    Toast.makeText(getActivity(), R.string.same_orig_dest, Toast.LENGTH_LONG).show();
                }
                else {
                    // Disable buttons and fields
                    Log.i("SearchFragment - ", "Disabling buttons and fields...");

                    backBtn.setEnabled(false);
                    routesBtn.setEnabled(false);
                    findRouteBtn.setEnabled(false);
                    originTxtFld.setEnabled(false);
                    destTxtFld.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    Log.i("SearchFragment - ", "Starting A* search...");
                    // Create new thread for A* search
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GeoPoint orig = new GeoPoint(origin.getLat(), origin.getLon());
                            GeoPoint dest = new GeoPoint(destination.getLat(), destination.getLon());

                            // Set up lists for A*
                            ArrayList<StopsNode> openList = new ArrayList<StopsNode>();
                            ArrayList<StopsNode> closedList = new ArrayList<StopsNode>();
                            ArrayList<StopsNode> successors = new ArrayList<StopsNode>();

                            ArrayList<StopTime> initialStops = (ArrayList) StopTime.getAllByStops(origin);

                            // Add origin to openList
                            openList.add(new StopsNode(orig.distanceTo(dest), 0, null, initialStops.get(0)));

                            // Start A* search
                            while (!openList.isEmpty()) {

                                StopsNode node;
                                node = openList.get(0);

                                // Remove node from openList
                                openList.remove(0);
                                // Transfer node to closedList
                                closedList.add(node);

                                // Check if distance of node is zero
                                // If zero, break the while loop (Solution found)
                                // Else, proceed to the loop body
                                if (node.getDistance() == 0) {
                                    break;
                                }

                                GeoPoint nodePoint = new GeoPoint(node.getStop().getLat(), node.getStop().getLon());

                                // Generate successor/s
                                for (StopTime st : StopTime.getAllByStops(node.getStop())) {
                                    StopTime prev = node.getTime().getPrev();
                                    StopTime next = node.getTime().getNext();

                                    // Get previous stop in trip
                                    if (prev != null) {
                                        Stops prevStop = prev.getStop();
                                        GeoPoint prevPoint = new GeoPoint(prevStop.getLat(), prevStop.getLon());
                                        successors.add(new StopsNode(prevPoint.distanceTo(dest), node.getCost() + nodePoint.distanceTo(prevPoint), node, prev));
                                    }

                                    // Get next stop in trip
                                    if (next != null) {
                                        Stops nextStop = next.getStop();
                                        GeoPoint nextPoint = new GeoPoint(nextStop.getLat(), nextStop.getLon());
                                        successors.add(new StopsNode(nextPoint.distanceTo(dest), node.getCost() + nodePoint.distanceTo(nextPoint), node, next));
                                    }
                                }

                                // Add all stops within 500 meters to successors
                                for (Stops st : Stops.getAllWithinDistance(node.getStop())) {
                                    GeoPoint stPoint = new GeoPoint(st.getLat(), st.getLon());
                                    successors.add(new StopsNode(stPoint.distanceTo(dest), node.getCost() + nodePoint.distanceTo(stPoint), node, StopTime.getAllByStops(st).get(0)));
                                }

                                int size = successors.size();

                                // Add successors to openList if not found in both lists
                                for (int i = 0; i < size; i++) {

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

                            Log.i("SearchFragment - ", "Found path!");
                            Log.i("SearchFragment - ", "Tracing back...");
                            // Trace back computed path
                            final ArrayList<StopsNode> pathList = new ArrayList<StopsNode>();
                            StopsNode currNode = closedList.get(closedList.size() - 1);

                            while (currNode != null) {
                                pathList.add(currNode);
                                currNode = currNode.getParent();
                            }

                            // Redirect to map fragment
                            final MapFragment mf = (MapFragment) getFragmentManager().findFragmentByTag("map_frag");
                            mf.getView().post(new Runnable() {
                                @Override
                                public void run() {
                                    Collections.reverse(pathList);
                                    mf.showRoute(pathList);
                                }
                            });
                        }
                    }).start();
                }
            }

            // Helper method for finding where to put node in the given list
            public int indexInList(List<StopsNode> stopsList, StopsNode node) {
                for(int i = 0; i < stopsList.size(); i++) {
                    if(stopsList.get(i).getStop().getStopId().equals(node.getStop().getStopId()))
                        return i;
                }
                return -1;
            }

            // Helper method for inserting node in given list
            // Used for sorting purposes
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