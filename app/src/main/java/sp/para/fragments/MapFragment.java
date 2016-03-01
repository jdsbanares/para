package sp.para.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

import sp.para.R;
import sp.para.models.Route;
import sp.para.models.StopTime;
import sp.para.models.Stops;
import sp.para.models.Trip;

/**
 * Created by Jd Banares on 3/1/2016.
 */
public class MapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        MapView map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMaxZoomLevel(19);

        // Zoom via pinch/expand
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(14.69161, 120.96928);
        mapController.setCenter(startPoint);

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

        Log.d("-------------APP", "Stops size = " + Stops.getAll().size());
        Log.d("-------------APP", "Route size = " + Route.getAll().size());
        Log.d("-------------APP", "Trip size = " + Trip.getAll().size());
        Log.d("-------------APP", "StopTime size = "+ StopTime.getAll().size());

        // Draw Lines
/*
        RoadManager roadManager = new OSRMRoadManager();

        List<StopTime> stopTimeList = StopTime.getAllByTrip(Trip.getByTripId("724628"));
//        List<StopTime> stopTimeList = StopTime.getAllByTrip(Trip.getByTripId("724674"));

        for(int i = 0; i < stopTimeList.size()-1; i++) {
            StopTime currLine = stopTimeList.get(i);
            StopTime nextLine = stopTimeList.get(i+1);
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            GeoPoint startLine = new GeoPoint(currLine.getStop().getLat(),currLine.getStop().getLon());
            waypoints.add(startLine);
            GeoPoint endLine = new GeoPoint(nextLine.getStop().getLat(),nextLine.getStop().getLon());
            waypoints.add(endLine);

            Road road = roadManager.getRoad(waypoints);

            Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);

            map.getOverlays().add(roadOverlay);
        }
*/

//        map.invalidate();

        // Place marker on each stop
        /*
        for(Stops currStop: Stops.getAll()){
            Marker stopMarker = new Marker(map);
            stopMarker.setPosition(new GeoPoint(currStop.getLat(), currStop.getLon()));
            stopMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(stopMarker);
        }
        map.invalidate();
        */

        return view;
    }

}
