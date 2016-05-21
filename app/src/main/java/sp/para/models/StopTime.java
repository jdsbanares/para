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

@Table(name="stop_time")
public class StopTime extends Model {

    // Represents a stop time
    // Extracted from the GTFS feed
    // Contains data regarding stop, trip, and the ordering of the stop in the trip

    // Denotes the order of the stop in the trip
    @Column(name="sequence")
    private int sequence;

    // Trip from GTFS
    @Column(name="trip")
    private Trip trip;

    // Stop from GTFS
    @Column(name="stop")
    private Stops stop;

    public StopTime() { super(); }

    public StopTime(Trip trip, Stops stop, int sequence) {
        super();
        this.trip = trip;
        this.stop = stop;
        this.sequence = sequence;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setStop(Stops stop) {
        this.stop = stop;
    }

    public Stops getStop() {
        return this.stop;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {
        return this.sequence;
    }

    // Get next StopTime for the trip
    public StopTime getNext() {
        return new Select()
                .from(StopTime.class)
                .where("trip = ? and sequence = ?", this.getTrip().getId(), sequence+1)
                .executeSingle();
    }

    // Get previous StopTime for the trip
    public StopTime getPrev() {
        return new Select()
                .from(StopTime.class)
                .where("trip = ? and sequence = ?", this.getTrip().getId(), sequence-1)
                .executeSingle();
    }

    // Get all StopTimes
    public static List<StopTime> getAll(){
        return new Select()
                .from(StopTime.class)
                .execute();
    }

    // Get all StopTimes by trip
    public static List<StopTime> getAllByTrip(Trip trip) {
        return new Select()
                .from(StopTime.class)
                .where("trip = ?", trip.getId())
                .orderBy("sequence asc")
                .execute();
    }

    // Get all StopTimes by Stop
    public static List<StopTime> getAllByStops(Stops stop) {
        return new Select()
                .from(StopTime.class)
                .where("stop = ?", stop.getId())
                .execute();
    }

    // Get all StopTimes by both trip and stop
    public static StopTime getByTripAndStops(Trip trip, Stops stop) {
        return new Select()
                .from(StopTime.class)
                .where("trip = ? and stop = ?", trip.getId(), stop.getId())
                .executeSingle();
    }

    // Population for StopTime
    public static void populate(InputStream stopTimeInStream) {
        ActiveAndroid.beginTransaction();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(stopTimeInStream),',', '"', 1);
            StopTime checker;
            Stops stopChecker;
            Trip tripChecker;
            String[] nextLine;

            Log.i("StopTime - ", "Populating StopTime...");

            while ((nextLine = reader.readNext()) != null) {
                // Checks if stop is saved
                stopChecker = Stops.getByStopId(nextLine[2]);

                // If stop is saved, proceed
                if(stopChecker != null) {
                    // Checks if trip is saved
                    tripChecker = Trip.getByTripId(nextLine[0]);

                    // If trip is saved, proceed
                    if(tripChecker != null) {
                        // Check if StopTime has already been added
                        checker = getByTripAndStops(tripChecker, stopChecker);

                        // If StopTime exists, update data
                        // Else, create new StopTime object to be saved
                        if(checker != null) {
                            checker.setTrip(tripChecker);
                            checker.setStop(stopChecker);
                            checker.setSequence(Integer.parseInt(nextLine[1]));
                            checker.save();
                        }
                        else {
                            StopTime stopTime = new StopTime(tripChecker, stopChecker, Integer.parseInt(nextLine[1]));
                            stopTime.save();
                        }
                    }
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.i("StopTime - ", "Successfully populated StopTime");
        }
        catch(Exception ex) {
            Log.e("StopTime - ", ex.toString(), ex);
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

}
