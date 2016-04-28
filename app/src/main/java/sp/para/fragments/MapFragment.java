package sp.para.fragments;

import android.app.Fragment;
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

import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.android.util.AndroidUtil;
//import org.mapsforge.map.android.view.MapView;
//import org.mapsforge.map.layer.overlay.Polyline;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sp.para.R;
import sp.para.activities.MainActivity;
import sp.para.models.Route;
import sp.para.models.StopTime;
import sp.para.models.Stops;
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

//        tileCache = AndroidUtil.createTileCache(getActivity().getBaseContext(), getClass().getSimpleName(), map.getModel().displayModel.getTileSize(),
//                1f, map.getModel().frameBufferModel.getOverdrawFactor());
//
//        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"graphhopper/maps/philippines-gh");
//        File phMaps = new File(path.getAbsolutePath(), "philippines.map");
//
//        MapDataStore mapDataStore = new MapFile(phMaps);
//
//        map.getLayerManager().getLayers().clear();
//
//        tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, map.getModel().mapViewPosition, false, true, AndroidGraphicFactory.INSTANCE);
//        tileRendererLayer.setTextScale(1.5f);
//        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
//        map.getModel().mapViewPosition.setMapPosition(new MapPosition(mapDataStore.boundingBox().getCenterPoint(), (byte) 15));
//        map.getLayerManager().getLayers().add(tileRendererLayer);

        map.setTileSource(TileSourceFactory.MAPQUESTOSM);
        map.setMultiTouchControls(true);
        map.setMaxZoomLevel(17);
        map.setMinZoomLevel(13);
        map.setUseDataConnection(false);

        IMapController mapController = map.getController();
        mapController.setZoom(17);
        mapController.setCenter(new GeoPoint(14.691719, 120.969944));

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

        for(Stops currStop: Stops.getAll()){
            Marker stopMarker = new Marker(map);
            stopMarker.setPosition(new GeoPoint(currStop.getLat(), currStop.getLon()));
            stopMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(stopMarker);
        }
        map.invalidate();

        searchBtn = (Button) view.findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment searchFragment = new SearchFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_activity, searchFragment, "search_frag");
                ft.addToBackStack("search_frag");
                ft.commit();
//                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
//                startActivity(intent);
            }
        });

        return view;
    }

    public void showRoute(ArrayList<Stops> waypoints) {
        GraphHopper hopper = new GraphHopper().forMobile();

        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"graphhopper/maps");
        File phMaps = new File(path.getAbsolutePath(), "philippines-gh");

//        Log.d("-------------APP", "maps absolute "+ phMaps.getAbsolutePath());

        hopper.load(phMaps.getAbsolutePath());

        Log.d("-------------APP", "found graph " + hopper.getGraphHopperStorage().toString() + ", nodes:" + hopper.getGraphHopperStorage().getNodes());

//        Log.d("-------------APP", "Road size = "+ road);

//        for (int i=0; i<road.mNodes.size(); i++){
//            RoadNode node = road.mNodes.get(i);
//            Marker nodeMarker = new Marker(map);
//            nodeMarker.setPosition(node.mLocation);
//            nodeMarker.setIcon(getResources().getDrawable(R.drawable.bonuspack_bubble));
//            nodeMarker.setTitle("Step "+i);
//            map.getOverlays().add(nodeMarker);
//        }

//        Paint paintStroke = AndroidGraphicFactory.INSTANCE.createPaint();
//        paintStroke.setStyle(Style.STROKE);
//        paintStroke.setColor(Color.argb(128, 0, 0xCC, 0x33));
//        paintStroke.setDashPathEffect(new float[]
//                {
//                        25, 15
//                });
//        paintStroke.setStrokeWidth(8);

//        map.getLayerManager().getLayers().clear();
        map.getOverlays().clear();

        ArrayList<GeoPoint> geopoints = new ArrayList<GeoPoint>();

//        for(Stops st : waypoints) {
//            geopoints.add(new GeoPoint(st.getLat(), st.getLon()));
//        }

        for(int i=0; i < waypoints.size() - 1; i++) {
            GHRequest req = new GHRequest(waypoints.get(i).getLat(), waypoints.get(i).getLon(),
                    waypoints.get(i+1).getLat(), waypoints.get(i+1).getLon())
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

        RoadManager roadManager = new OSRMRoadManager();
        Road road = roadManager.getRoad(geopoints);

        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, getActivity());
        map.getOverlays().add(roadOverlay);
        map.getController().setCenter(geopoints.get(0));
        map.invalidate();

        getFragmentManager().popBackStack();

        StepsFragment stepsFragment = StepsFragment.newInstance(waypoints.get(0), waypoints.get(waypoints.size() - 1));

        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.remove(getFragmentManager().findFragmentByTag("steps_frag"));
        ft.add(R.id.main_activity, stepsFragment, "steps_frag");
        ft.addToBackStack("steps_frag");
        ft.commit();
    }

}
