package sp.para.models;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Table(name="route")
public class Route extends Model {

    // Represents a route
    // Extracted from the GTFS feed

    // Route ID from GTFS
    @Column(name="route_id")
    private String route_id;

    // Route name from GTFS
    @Column(name="name")
    private String name;

    // Route description from GTFS
    @Column(name="description")
    private String description;

    public Route() { super(); }

    public Route(String route_id, String name, String description) {
        super();
        this.route_id = route_id;
        this.name = name;
        this.description = description;
    }

    public void setRouteId(String route_id) { this.route_id = route_id; }

    public String getRouteId() { return this.route_id; }

    public void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    public void setDescription(String name) { this.description = description; }

    public String getDescription() { return this.description; }

    // Override to show route name when toString() is called
    @Override
    public String toString() {
        return this.name;
    }

    // Gets all stored routes ordered by name
    public static List<Route> getAll(){
        return new Select()
                .from(Route.class)
                .orderBy("name asc")
                .execute();
    }

    // Get route by route_id
    public static Route getByRouteId(String route_id){
        return new Select()
                .from(Route.class)
                .where("route_id = ?", route_id)
                .executeSingle();
    }

    // Population for routes
    public static void populate(InputStream routeInStream) {
        ActiveAndroid.beginTransaction();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(routeInStream),',', '"', 1);
            Route checker;
            String[] nextLine;

            Log.i("Route - ", "Populating Routes...");

            while ((nextLine = reader.readNext()) != null) {
                // Check if route has already been added
                checker = getByRouteId(nextLine[9]);

                // If route exists, update data
                // Else, create new route object to be saved
                if(checker != null) {
                    checker.setName(nextLine[2]);
                    checker.setDescription(nextLine[3]);
                    checker.save();
                }
                else {
                    Route route = new Route(nextLine[9],nextLine[2],nextLine[3]);
                    route.save();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.i("Route - ", "Successfully populated Routes");
        }
        catch(Exception ex) {
            Log.e("Route - ", ex.toString(), ex);
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

}
