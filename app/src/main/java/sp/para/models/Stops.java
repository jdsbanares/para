package sp.para.models;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.opencsv.CSVReader;

import org.osmdroid.util.GeoPoint;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Table(name="stops")
public class Stops extends Model {

    // Represents a single stop
    // Extracted from the GTFS feed

    // Bounds for the stop coordinates
    // Used for data population
    private static double MIN_LAT = 14.668743;
    private static double MAX_LAT = 14.751737;
    private static double MIN_LON = 120.926378;
    private static double MAX_LON = 121.024578;

    // Stop ID from GTFS
    @Column(name="stop_id")
    private String stop_id;

    // Stop name from GTFS
    @Column(name="name")
    private String name;

    // Stop latitude from GTFS
    @Column(name="lat")
    private double lat;

    // Stop longitude from GTFS
    @Column(name="lon")
    private double lon;

    public Stops(){
        super();
    }

    public Stops(String stop_id, String name, double lat, double lon){
        super();
        this.stop_id = stop_id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public void setStopId(String stop_id) {
        this.stop_id = stop_id;
    }

    public String getStopId() {
        return this.stop_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat(){
        return this.lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return this.lon;
    }

    // Override to show stop name when toString() is called
    @Override
    public String toString() {
        return this.name;
    }

    // Get total count of stored stops
    public static int getCount(){
        return new Select()
                .from(Stops.class)
                .execute()
                .size();
    }

    // Get all stored stops
    public static List<Stops> getAll(){
        return new Select()
                .from(Stops.class)
                .execute();
    }

    // Get stop by stop id
    public static Stops getByStopId(String stop_id){
        return new Select()
                .from(Stops.class)
                .where("stop_id = ?", stop_id)
                .executeSingle();
    }

    // Get all stops within 500 meters of the given stop
    public static List<Stops> getAllWithinDistance(Stops arg) {
        ArrayList<Stops> stopsList = new ArrayList<Stops>();
        GeoPoint argPoint = new GeoPoint(arg.getLat(), arg.getLon());
        for(Stops currStop: Stops.getAll()) {
            GeoPoint currPoint = new GeoPoint(currStop.getLat(), currStop.getLon());
            if(argPoint.distanceTo(currPoint) <= 500)
                stopsList.add(currStop);
        }
        return stopsList;
    }

    // Population for stops
    public static void populate(InputStream stopInStream) {
        ActiveAndroid.beginTransaction();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(stopInStream),',', '"', 1);
            Stops checker;
            String[] nextLine;

            Log.i("Stops - ", "Populating Stops...");

            while ((nextLine = reader.readNext()) != null) {
                double newLat = Double.parseDouble(nextLine[4]);
                double newLon = Double.parseDouble(nextLine[5]);

                // Check if current stop is within bounds
                if(Stops.MIN_LAT <= newLat && newLat <= Stops.MAX_LAT && Stops.MIN_LON <= newLon && newLon <= Stops.MAX_LON) {
                    // Check if stop has already been added
                    checker = getByStopId(nextLine[0]);

                    // If stop exists, update data
                    // Else, create new stop object to be saved
                    if(checker != null) {
                        checker.setName(nextLine[2]);
                        checker.setLat(newLat);
                        checker.setLon(newLon);
                        checker.save();
                    }
                    else {
                        Stops stop = new Stops(nextLine[0],nextLine[2],newLat,newLon);
                        stop.save();
                    }
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.i("Stops - ", "Successfully populated Stops");
        }
        catch(Exception ex) {
            Log.e("Stops - ", ex.toString(), ex);
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
}
