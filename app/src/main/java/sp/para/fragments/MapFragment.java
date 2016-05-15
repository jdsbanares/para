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

import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
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

/**
 * Created by Jd Banares on 3/1/2016.
 */
public class MapFragment extends Fragment {

    Button searchBtn;
    MapView map;
    TileCache tileCache;
    TileRendererLayer tileRendererLayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        map = (MapView) view.findViewById(R.id.map);

        // Zoom via pinch/expand
        map.setClickable(true);
        map.setBuiltInZoomControls(true);

        map.setTileSource(TileSourceFactory.MAPQUESTOSM);
        map.setMultiTouchControls(true);
        map.setMaxZoomLevel(17);
        map.setMinZoomLevel(13);
        map.setUseDataConnection(false);

        IMapController mapController = map.getController();
        mapController.setZoom(17);
        mapController.setCenter(new GeoPoint(14.6922, 120.971));

//        Marker startMarker = new Marker(map);
//        startMarker.setPosition(startPoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        startMarker.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
//        startMarker.setTitle("Start point");

//        map.getOverlays().add(startMarker);
//        map.invalidate();

        // Population of Stops
//        Stops.populate(getResources().openRawResource(R.raw.stops));

        // Population of Routes
//        Route.populate(getResources().openRawResource(R.raw.routes));

        // Population of Trips
//        Trip.populate(getResources().openRawResource(R.raw.trips));

        // Population of StopTimes
//        StopTime.populate(getResources().openRawResource(R.raw.stop_times));

//        Log.d("-------------APP", "Stops size = " + Stops.getAll().size());
//        Log.d("-------------APP", "Route size = " + Route.getAll().size());
//        Log.d("-------------APP", "Trip size = " + Trip.getAll().size());
//        Log.d("-------------APP", "StopTime size = "+ StopTime.getAll().size());

        // Place marker on each stop

//        for(Stops currStop: Stops.getAll()){
//            Marker stopMarker = new Marker(map);
//            stopMarker.setPosition(new GeoPoint(currStop.getLat(), currStop.getLon()));
//            stopMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            map.getOverlays().add(stopMarker);
//        }
//        map.invalidate();

        searchBtn = (Button) view.findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment searchFragment = new SearchFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_activity, searchFragment, "search_frag");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    public void showExisting(Route currRoute) {
        GraphHopper hopper = new GraphHopper().forMobile();

        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"graphhopper/maps");
        File phMaps = new File(path.getAbsolutePath(), "philippines-gh");

        hopper.load(phMaps.getAbsolutePath());

        map.getOverlays().clear();

        ArrayList<Stops> waypoints = new ArrayList<Stops>();

        List<Trip> trips = Trip.getAllByRoute(currRoute);

        if(!trips.isEmpty()) {
            for(StopTime st: StopTime.getAllByTrip(trips.get(0))) {
                waypoints.add(st.getStop());
            }
        }

        Log.d("-------------APP", "waypoints -- "+waypoints.size());

        ArrayList<IGeoPoint> geopoints = new ArrayList<IGeoPoint>();

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
                    Log.d("-------------APP", ""+error.getMessage());
                }
            }

        }

        Log.d("-------------APP", "geopoints -- "+geopoints.size());

        Marker startMarker = new Marker(map);
        startMarker.setTitle("Origin");
        startMarker.setPosition(new GeoPoint(waypoints.get(0).getLat(), waypoints.get(0).getLon()));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        Marker endMarker = new Marker(map);
        endMarker.setTitle("Destination");
        endMarker.setPosition(new GeoPoint(waypoints.get(waypoints.size()-1).getLat(), waypoints.get(waypoints.size()-1).getLon()));
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(endMarker);

        PathOverlay geoPath = new PathOverlay(Color.BLUE, getActivity().getApplicationContext());
        geoPath.getPaint().setStrokeWidth(5);
        geoPath.addPoints(geopoints);
        map.getOverlays().add(geoPath);

        map.getController().setCenter(geopoints.get(0));
        map.invalidate();

        getFragmentManager().popBackStack(getFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        RouteLabelFragment routeLabelFragment = RouteLabelFragment.newInstance(currRoute);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.main_activity, routeLabelFragment, "route_label_frag");
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showRoute(ArrayList<StopsNode> waypoints) {
        GraphHopper hopper = new GraphHopper().forMobile();

        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"graphhopper/maps");
        File phMaps = new File(path.getAbsolutePath(), "philippines-gh");

        hopper.load(phMaps.getAbsolutePath());

        Log.d("-------------APP", "found graph " + hopper.getGraphHopperStorage().toString() + ", nodes:" + hopper.getGraphHopperStorage().getNodes());

        map.getOverlays().clear();

        ArrayList<IGeoPoint> geopoints = new ArrayList<IGeoPoint>();

        for(int i=0; i < waypoints.size() - 1; i++) {
            GHRequest req = new GHRequest(waypoints.get(i).getStop().getLat(), waypoints.get(i).getStop().getLon(),
                    waypoints.get(i+1).getStop().getLat(), waypoints.get(i+1).getStop().getLon())
                    .setAlgorithm(AlgorithmOptions.ASTAR_BI);
            req.getHints().put("instructions", "true");

            GHResponse resp = hopper.route(req);

            if(!resp.hasErrors()) {
                PathWrapper pathResp = resp.getBest();

                Log.d("-------------APP", "PATH INSTRUCTIONS "+pathResp.getInstructions().toString());

                PointList tmp = pathResp.getPoints();
                for (int j = 0; j < pathResp.getPoints().getSize(); j++) {
                    geopoints.add(new GeoPoint(tmp.getLatitude(j), tmp.getLongitude(j)));
                }
            }
            else {
                for(Throwable error: resp.getErrors()) {
                    Log.d("-------------APP", ""+error.getMessage());
                }
            }

        }

        Marker startMarker = new Marker(map);
        startMarker.setTitle("Origin");
        startMarker.setPosition(new GeoPoint(waypoints.get(0).getStop().getLat(), waypoints.get(0).getStop().getLon()));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        Marker endMarker = new Marker(map);
        endMarker.setTitle("Destination");
        endMarker.setPosition(new GeoPoint(waypoints.get(waypoints.size()-1).getStop().getLat(), waypoints.get(waypoints.size()-1).getStop().getLon()));
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(endMarker);

        PathOverlay geoPath = new PathOverlay(Color.BLUE, getActivity().getApplicationContext());
        geoPath.getPaint().setStrokeWidth(5);
        geoPath.addPoints(geopoints);
        map.getOverlays().add(geoPath);

        map.getController().setCenter(geopoints.get(0));

        ArrayList<InstructionNode> instList = new ArrayList<InstructionNode>();
        InstructionNode startIns = null;
        InstructionNode currIns = null;

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

        getFragmentManager().popBackStack(getFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        PartialStepsFragment partialStepsFragment = PartialStepsFragment.newInstance(waypoints.get(0).getStop(), waypoints.get(waypoints.size() - 1).getStop(), instList);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.main_activity, partialStepsFragment, "partial_steps_frag");
        ft.addToBackStack(null);
        ft.commit();
    }

}
