package sp.para.models;

import android.util.Log;

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

    @Column(name="trip_id")
    private String trip_id;

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

    public static List<Trip> getAll(){
        return new Select()
                .from(Trip.class)
                .execute();
    }

    public static Trip getByTripId(String trip_id){
        return new Select()
                .from(Trip.class)
                .where("trip_id = ?", trip_id)
                .executeSingle();
    }

    public static void populate(InputStream tripInStream) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(tripInStream),',', '"', 1);
            Trip checker;
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {

                checker = getByTripId(nextLine[10]);

                if(checker != null) {
                    checker.setRoute(Route.getByRouteId(nextLine[0]));
                    checker.save();
                }
                else {
                    Trip trip = new Trip(nextLine[10],Route.getByRouteId(nextLine[0]));
                    trip.save();
                }
            }
        }
        catch(Exception ex) {
            Log.d("-------------APP", "Exception caught!\n" + ex);
        }
    }

}
