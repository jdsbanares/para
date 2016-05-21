package sp.para.models;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Table(name="trip")
public class Trip extends Model {

    // Represents a single trip

    // Trip ID from GTFS
    @Column(name="trip_id")
    private String trip_id;

    // Route where trip belongs from GTFS
    @Column(name="route")
    private Route route;

    public Trip() { super(); }

    public Trip(String trip_id, Route route) {
        super();
        this.trip_id = trip_id;
        this.route = route;
    }

    public void setTripId(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getTripId() {
        return this.trip_id;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return this.route;
    }

    // Get all stored trips
    public static List<Trip> getAll(){
        return new Select()
                .from(Trip.class)
                .execute();
    }

    // Get all trips from a route
    public static List<Trip> getAllByRoute(Route route){
        return new Select()
                .from(Trip.class)
                .where("route = ?", route.getId())
                .execute();
    }

    // Get trip by trip id
    public static Trip getByTripId(String trip_id){
        return new Select()
                .from(Trip.class)
                .where("trip_id = ?", trip_id)
                .executeSingle();
    }

    // Population for trips
    public static void populate(InputStream tripInStream) {
        ActiveAndroid.beginTransaction();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(tripInStream),',', '"', 1);
            Trip checker;
            String[] nextLine;

            Log.i("Trip - ", "Populating Trip...");

            while ((nextLine = reader.readNext()) != null) {
                // Check if trip has already been added
                checker = getByTripId(nextLine[10]);

                // If trip exists, update data
                // Else, create new trip object to be saved
                if(checker != null) {
                    checker.setRoute(Route.getByRouteId(nextLine[0]));
                    checker.save();
                }
                else {
                    Trip trip = new Trip(nextLine[10],Route.getByRouteId(nextLine[0]));
                    trip.save();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.i("Trip - ", "Successfully populated Trip");
        }
        catch(Exception ex) {
            Log.e("Trip - ", ex.toString(), ex);
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

}
