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

    @Column(name="sequence")
    private int sequence;

    @Column(name="trip")
    private Trip trip;

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

    public static List<StopTime> getAll(){
        return new Select()
                .from(StopTime.class)
                .execute();
    }

    public static List<StopTime> getAllByTrip(Trip trip) {
        return new Select()
                .from(StopTime.class)
                .where("trip = ?", trip.getId())
                .orderBy("sequence asc")
                .execute();
    }

    public static List<StopTime> getAllByStops(Stops stop) {
        return new Select()
                .from(StopTime.class)
                .where("stop = ?", stop.getId())
                .execute();
    }

    public static StopTime getByTripAndStops(Trip trip, Stops stop) {
        return new Select()
                .from(StopTime.class)
                .where("trip = ? and stop = ?", trip.getId(), stop.getId())
                .executeSingle();
    }

    public static void populate(InputStream stopTimeInStream) {
        ActiveAndroid.beginTransaction();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(stopTimeInStream),',', '"', 1);
            StopTime checker;
            Stops stopChecker;
            Trip tripChecker;
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                stopChecker = Stops.getByStopId(nextLine[2]);

                if(stopChecker != null) {
                    tripChecker = Trip.getByTripId(nextLine[0]);
                    if(tripChecker != null) {
                        checker = getByTripAndStops(tripChecker, stopChecker);

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
        }
        catch(Exception ex) {
            Log.d("-------------StopTime", "Exception caught!", ex);
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

}
