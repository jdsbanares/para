package sp.para.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sp.para.R;
import sp.para.fragments.MapFragment;
import sp.para.models.Route;
import sp.para.models.Stops;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set initial layout for MainActivity
        setContentView(R.layout.main);

        // Copy files on initial run
        copyFiles();

        if(savedInstanceState == null) {
            Fragment mapFragment = new MapFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frag_placeholder, mapFragment, "map_frag").commit();
        }
    }

    public void copyFiles() {
        // Zip file for offline map tiles
        String mapFileName = "MapquestOSM.zip";

        // Save path for offline map tiles in the device
        String mapPath = "/sdcard/osmdroid/MapquestOSM.zip";

        // DB File for database
        String dbFileName = "Para.db";

        // Save path for the database in the device
        String dbPath = "/data/data/sp.para/databases/Para.db";

        Log.i("MainActivity - ", "Copying files...");

        try {
            // Initialize buffer, in and out streams, assets
            AssetManager assetManager = getAssets();
            InputStream in = null;
            OutputStream out = null;
            byte[] buffer = new byte[1024];
            int read;

            // If there are no Stops in the database,
            // copy the database file to the save path
            if(Stops.getCount() == 0) {
                Log.i("MainActivity - ", "No stops found.");
                Log.i("MainActivity - ", "Copying db file...");

                in = assetManager.open(dbFileName);
                out = new FileOutputStream(dbPath);

                while((read = in.read(buffer)) != -1){
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();
            }

            // For checking if osmdroid directory exists in the device
            File osmDir = new File("/sdcard/osmdroid");

            // Checks if the osmdroid folder does not exist, create the directory
            if(!osmDir.exists()) {
                Log.i("MainActivity - ", "osmdroid directory does not exist.");
                Log.i("MainActivity - ", "Making directories...");
                osmDir.mkdirs();
            }

            // If map tiles does not exists in the device,
            // copy the zip file containing the images of the map tiles
            File mapFile = new File(mapPath);
            if(!mapFile.exists()) {
                Log.i("MainActivity - ", "Map tiles does not exist.");
                Log.i("MainActivity - ", "Copying map tiles...");
                in = assetManager.open(mapFileName);
                out = new FileOutputStream(mapPath);

                while((read = in.read(buffer)) != -1){
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();
            }
        }
        catch(IOException ex){
            Log.e("MainActivity - ", ex.toString(), ex);
            Toast.makeText(this, R.string.error_initialization_files, Toast.LENGTH_LONG).show();
        }
    }

}