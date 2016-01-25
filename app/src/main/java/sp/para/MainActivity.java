package sp.para;

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

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //startMarker.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        startMarker.setTitle("Start point");

        map.getOverlays().add(startMarker);
        map.invalidate();

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

        populate();

        Log.d("---------------APP", "HELLOOOOO!");

        Log.d("--------------APP", "Size: --------------- "+Stops.getRandom().getName());
//        for(Stops currStop: Stops.list()){
//            Marker stopMarker = new Marker(map);
//            stopMarker.setPosition(new GeoPoint(currStop.getLat(), currStop.getLon()));
//            stopMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            map.getOverlays().add(stopMarker);
//        }
//        map.invalidate();
    }

    private void populate() {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.stops)),',', '"', 1);

//            CSVReader reader = new CSVReader(new FileReader(new File(Environment.getExternalStorageDirectory(),"GTFS/stops.txt")),',', '"', 1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                double newLat = Double.parseDouble(nextLine[4]);
                double newLon = Double.parseDouble(nextLine[5]);
                if(Stops.MIN_LAT <= newLat && newLat <= Stops.MAX_LAT && Stops.MIN_LON <= newLon && newLon <= Stops.MAX_LON) {
                    Log.d("------NEW","-----"+nextLine[2]+" -a-a- "+newLat+" --- "+newLon);
                    Stops stop = new Stops();
                    Log.d("------YAY", "Aaaaaaa");
                    stop.setStopId(nextLine[0]);
                    stop.setName(nextLine[2]);
                    stop.setLat(newLat);
                    stop.setLon(newLon);
                    Log.d("-------------APP","NAME --> "+stop.getName());
                    stop.save();
                }
            }
        }
        catch(Exception ex) {
            Log.d("-------------APP", "Exception caught!\n" + ex.getLocalizedMessage());
        }
    }
}