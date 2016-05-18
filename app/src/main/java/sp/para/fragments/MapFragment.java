package sp.para.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.util.PointList;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sp.para.R;
import sp.para.models.InstructionNode;
import sp.para.models.Route;
import sp.para.models.StopTime;
import sp.para.models.Stops;
import sp.para.models.StopsNode;
import sp.para.models.Trip;

public class MapFragment extends Fragment {

    Button searchBtn;
    MapView map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for map fragment
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        Log.i("MapFragment - ","Setting up MapView...");

        // Instantiate osmdroid MapView via id
        map = (MapView) view.findViewById(R.id.map);

        // Zoom via pinch/expand
        map.setClickable(true);

        // Zoom via on screen buttons
        map.setBuiltInZoomControls(true);

        // Set tile source to MapQuestOSM
        map.setTileSource(TileSourceFactory.MAPQUESTOSM);

        // Set multi-touch controls to true
        map.setMultiTouchControls(true);

        // Set maximum and minimum zoom levels
        map.setMaxZoomLevel(17);
        map.setMinZoomLevel(13);

        // Disable connection to the internet
        map.setUseDataConnection(false);

        // Initialize map view to zoom level 17 with the given coordinates
        IMapController mapController = map.getController();
        mapController.setZoom(17);
        mapController.setCenter(new GeoPoint(14.6922, 120.971));

        // Instantiate search button via id
        searchBtn = (Button) view.findViewById(R.id.searchBtn);

        // Add a listener when search button is clicked/tapped
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MapFragment - ","Search button clicked!");
                // Redirect to search fragment
                Fragment searchFragment = new SearchFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_activity, searchFragment, "search_frag");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    // Show existing route and its stops in the Map View
    public void showExisting(Route currRoute) {
        // Load graphhopper for assistance in route drawing
        GraphHopper hopper = new GraphHopper().forMobile();

        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"graphhopper/maps");
        File phMaps = new File(path.getAbsolutePath(), "philippines-gh");

        hopper.load(phMaps.getAbsolutePath());

        // Clear existing map overlays
        map.getOverlays().clear();

        // Will be used for plotting the stops in the map
        ArrayList<Stops> waypoints = new ArrayList<Stops>();

        // Get all trips from route
        List<Trip> trips = Trip.getAllByRoute(currRoute);

        // If trips is not empty
        // Get the stops in the trip and add to waypoints
        if(!trips.isEmpty()) {
            Log.i("MapFragment - ", "Getting stops from trip "+trips.get(0).getTripId());
            for(StopTime st: StopTime.getAllByTrip(trips.get(0))) {
                waypoints.add(st.getStop());
            }
        }

        Log.i("MapFragment - ", "Putting markers for each stop...");

        // Put marker for every stop
        for(Stops currWay: waypoints){
            Marker stopMarker = new Marker(map);
            stopMarker.setTitle(currWay.getName());
            stopMarker.setPosition(new GeoPoint(currWay.getLat(), currWay.getLon()));
            stopMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            stopMarker.setIcon(getResources().getDrawable(R.drawable.stop));
            map.getOverlays().add(stopMarker);
        }

        Log.i("MapFragment - ", "Getting route for each road segment...");

        ArrayList<IGeoPoint> geopoints = new ArrayList<IGeoPoint>();

        // Get geopoints for road segments in between Stops stored in waypoints
        for(int i=0; i < waypoints.size() - 1; i++) {
            GHRequest req = new GHRequest(waypoints.get(i).getLat(), waypoints.get(i).getLon(),
                    waypoints.get(i+1).getLat(), waypoints.get(i+1).getLon())
                    .setAlgorithm(AlgorithmOptions.ASTAR_BI);

            GHResponse resp = hopper.route(req);

            if(!resp.hasErrors()) {
                PathWrapper pathResp = resp.getBest();

                PointList tmp = pathResp.getPoints();
                for (int j = 0; j < pathResp.getPoints().getSize(); j++) {
                    geopoints.add(new GeoPoint(tmp.getLatitude(j), tmp.getLongitude(j)));
                }
            }
            else {
                for(Throwable error: resp.getErrors()) {
                    Log.e("MapFragment - ", error.getMessage(), error);
                }
            }
        }

        Log.i("MapFragment - ", "Drawing path...");

        // Create path from geopoints and draw in map
        PathOverlay geoPath = new PathOverlay(Color.BLUE, getActivity().getApplicationContext());
        geoPath.getPaint().setStrokeWidth(5);
        geoPath.addPoints(geopoints);
        map.getOverlays().add(geoPath);

        map.getController().setCenter(geopoints.get(0));
        map.invalidate();

        // Removes any fragment on top of the MapView
        getFragmentManager().popBackStack(getFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Log.i("MapFragment - ", "Adding label for route...");

        // Add a label for the route
        RouteLabelFragment routeLabelFragment = RouteLabelFragment.newInstance(currRoute);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.main_activity, routeLabelFragment, "route_label_frag");
        ft.addToBackStack(null);
        ft.commit();
    }

    // Show the computed route in the MapView
    public void showRoute(ArrayList<StopsNode> waypoints) {
        // Load graphhopper for asssitance in route drawing
        GraphHopper hopper = new GraphHopper().forMobile();

        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"graphhopper/maps");
        File phMaps = new File(path.getAbsolutePath(), "philippines-gh");

        hopper.load(phMaps.getAbsolutePath());

        // Clear existing map overlays
        map.getOverlays().clear();

        Log.i("MapFragment - ", "Getting route for each road segment...");

        ArrayList<IGeoPoint> geopoints = new ArrayList<IGeoPoint>();

        // Get geopoints for road segments in between Stops stored in waypoints
        for(int i=0; i < waypoints.size() - 1; i++) {
            GHRequest req = new GHRequest(waypoints.get(i).getStop().getLat(), waypoints.get(i).getStop().getLon(),
                    waypoints.get(i+1).getStop().getLat(), waypoints.get(i+1).getStop().getLon())
                    .setAlgorithm(AlgorithmOptions.ASTAR_BI);
            req.getHints().put("instructions", "true");

            GHResponse resp = hopper.route(req);

            if(!resp.hasErrors()) {
                PathWrapper pathResp = resp.getBest();

                PointList tmp = pathResp.getPoints();
                for (int j = 0; j < pathResp.getPoints().getSize(); j++) {
                    geopoints.add(new GeoPoint(tmp.getLatitude(j), tmp.getLongitude(j)));
                }
            }
            else {
                for(Throwable error: resp.getErrors()) {
                    Log.e("MapFragment - ", error.getMessage(), error);
                }
            }
        }

        Log.i("MapFragment - ", "Putting markers in the map...");

        // Put a marker in the route origin in the map
        Marker startMarker = new Marker(map);
        startMarker.setTitle(waypoints.get(0).getStop().getName());
        startMarker.setPosition(new GeoPoint(waypoints.get(0).getStop().getLat(), waypoints.get(0).getStop().getLon()));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(getResources().getDrawable(R.drawable.origin));
        map.getOverlays().add(startMarker);

        // Put a marker in the route destination in the map
        Marker endMarker = new Marker(map);
        endMarker.setTitle(waypoints.get(waypoints.size()-1).getStop().getName());
        endMarker.setPosition(new GeoPoint(waypoints.get(waypoints.size()-1).getStop().getLat(), waypoints.get(waypoints.size()-1).getStop().getLon()));
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setIcon(getResources().getDrawable(R.drawable.destination));
        map.getOverlays().add(endMarker);

        Log.i("MapFragment - ", "Drawing path...");

        // Create path from geopoints and draw in map
        PathOverlay geoPath = new PathOverlay(Color.BLUE, getActivity().getApplicationContext());
        geoPath.getPaint().setStrokeWidth(5);
        geoPath.addPoints(geopoints);
        map.getOverlays().add(geoPath);

        map.getController().setCenter(geopoints.get(0));

        Log.i("MapFragment - ", "Creating instructions...");

        // Create instruction nodes to be passed in the steps fragment
        ArrayList<InstructionNode> instList = new ArrayList<InstructionNode>();
        InstructionNode startIns = null;
        InstructionNode currIns = null;

        // Iterate waypoints for instructions
        // If the next waypoint has the same route as the current waypoint,
        // update the current instruction node's end stop to the current waypoint
        // Else, create a new instruction node
        for(StopsNode way: waypoints) {
            if(startIns == null) {
                startIns = new InstructionNode(way.getTime(), way.getTime(), way.getTime().getTrip().getRoute(), null);
                instList.add(startIns);
                currIns = startIns;
            }
            else if(currIns.getEndStop().getTrip().getRoute().getRouteId().equals(way.getTime().getTrip().getRoute().getRouteId())) {
                currIns.setEndStop(way.getTime());
            }
            else {
                InstructionNode newIns = new InstructionNode(way.getTime(), way.getTime(), way.getTime().getTrip().getRoute(), currIns);
                instList.add(newIns);
                currIns = newIns;
            }
        }

        // Removes any fragment on top of the MapView
        getFragmentManager().popBackStack(getFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Log.i("MapFragment - ", "Adding label for origin and destination...");

        // Add a fragment that shows the origin and destination
        PartialStepsFragment partialStepsFragment = PartialStepsFragment.newInstance(waypoints.get(0).getStop(), waypoints.get(waypoints.size() - 1).getStop(), instList);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.main_activity, partialStepsFragment, "partial_steps_frag");
        ft.addToBackStack(null);
        ft.commit();
    }

}
