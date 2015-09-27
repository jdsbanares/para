package sp.para;

import android.app.Activity;
import android.os.Bundle;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.bonuspack.overlays.Marker;

import java.util.ArrayList;

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

        RoadManager roadManager = new OSRMRoadManager();

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        GeoPoint endPoint = new GeoPoint(14.695615, 120.971597);
        waypoints.add(endPoint);

        Road road = roadManager.getRoad(waypoints);

        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);

        map.getOverlays().add(roadOverlay);

        map.invalidate();

    }
}