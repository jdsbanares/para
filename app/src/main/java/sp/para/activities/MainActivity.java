package sp.para.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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

        setContentView(R.layout.main);

        copyFiles();

        if(savedInstanceState == null) {
            Fragment mapFragment = new MapFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frag_placeholder, mapFragment, "map_frag").commit();
        }
    }

    public void copyFiles() {
        String mapFileName = "MapquestOSM.zip";
        String mapPath = "/sdcard/osmdroid/MapquestOSM.zip";
        String dbFileName = "Para.db";
        String dbPath = "/data/data/sp.para/databases/Para.db";

        Log.d("-------------APP", "COPYING FILES");

        try {
            AssetManager assetManager = getAssets();
            InputStream in = null;
            OutputStream out = null;
            byte[] buffer = new byte[1024];
            int read;

            if(Stops.getCount() == 0) {
                Log.d("-------------APP", " STOPS EMPTY!");
                in = assetManager.open(dbFileName);
                out = new FileOutputStream(dbPath);

                while((read = in.read(buffer)) != -1){
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();
            }

            File osmDir = new File("/sdcard/osmdroid");

            if(!osmDir.exists())
                osmDir.mkdirs();

            File mapFile = new File(mapPath);
            if(!mapFile.exists()) {
                Log.d("-------------APP", " MAP FILE DOES NOT EXIST!");
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
            Log.d("-------------APP", " -- "+ex.toString());
        }
    }

}