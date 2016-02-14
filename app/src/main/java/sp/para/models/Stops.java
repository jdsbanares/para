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

@Table(name="stops")
public class Stops extends Model {

    private static double MIN_LAT = 14.668743;
    private static double MAX_LAT = 14.751737;
    private static double MIN_LON = 120.926378;
    private static double MAX_LON = 121.024578;

    @Column(name="stop_id")
    private String stop_id;

    @Column(name="name")
    private String name;

    @Column(name="lat")
    private double lat;

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

    public static List<Stops> getAll(){
        return new Select()
                .from(Stops.class)
                .execute();
    }

    public static Stops getByStopId(String stop_id){
        return new Select()
                .from(Stops.class)
                .where("stop_id = ?", stop_id)
                .executeSingle();
    }

    public static void populate(InputStream stopInStream) {
        ActiveAndroid.beginTransaction();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(stopInStream),',', '"', 1);
            Stops checker;
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                double newLat = Double.parseDouble(nextLine[4]);
                double newLon = Double.parseDouble(nextLine[5]);

                if(Stops.MIN_LAT <= newLat && newLat <= Stops.MAX_LAT && Stops.MIN_LON <= newLon && newLon <= Stops.MAX_LON) {
                    checker = getByStopId(nextLine[0]);

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
        }
        catch(Exception ex) {
            Log.d("-------------Stops", "Exception caught!\n" + ex);
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
}
