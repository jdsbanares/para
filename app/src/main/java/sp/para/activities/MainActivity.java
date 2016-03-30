package sp.para.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import sp.para.R;
import sp.para.fragments.MapFragment;
import sp.para.fragments.SearchFragment;

public class MainActivity extends FragmentActivity {

    Button searchBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.main);

        if(savedInstanceState == null) {
            Fragment mapFragment = new MapFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frag_placeholder, mapFragment, "map_frag").commit();
        }

        searchBtn = (Button) findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment searchFragment = new SearchFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_activity, searchFragment, "search_frag");
                ft.addToBackStack(null);
                ft.commit();
//                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
//                startActivity(intent);
            }
        });

        /*
        MapView map = (MapView) findViewById(R.id.map);
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
        Log.d("-------------APP", "StopTime size = "+StopTime.getAll().size());

        // Draw Lines

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
    }

}