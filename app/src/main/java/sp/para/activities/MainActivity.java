package sp.para.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.opencsv.CSVReader;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.bonuspack.overlays.Marker;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sp.para.R;
import sp.para.models.*;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
        //startMarker.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
//        startMarker.setTitle("Start point");

//        map.getOverlays().add(startMarker);
//        map.invalidate();

//        RoadManager roadManager = new OSRMRoadManager();
//
//        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
//        waypoints.add(startPoint);
//        GeoPoint endPoint = new GeoPoint(14.695615, 120.971597);
//        waypoints.add(endPoint);
//
//        Road road = roadManager.getRoad(waypoints);
//
//        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);

//        map.getOverlays().add(roadOverlay);

//        map.invalidate();

        // Population of Stops
        Stops.populate(getResources().openRawResource(R.raw.stops));

//        Route.populate(getResources().openRawResource(R.raw.routes));

//        Log.d("-------------APP", "Route size = "+Route.getAll().size());

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