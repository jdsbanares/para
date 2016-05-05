package sp.para.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import sp.para.R;
import sp.para.models.InstructionNode;
import sp.para.models.Route;
import sp.para.models.StopTime;
import sp.para.models.Stops;
import sp.para.models.Trip;

public class UpdateFragment extends Fragment {

    Button backBtn;
    Button updateBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_fragment, container, false);

        backBtn = (Button) view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        updateBtn = (Button) view.findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileInputStream stopsFile = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "gtfs/stops.txt"));
                            FileInputStream routesFile = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "gtfs/routes.txt"));
                            FileInputStream tripFile = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "gtfs/trips.txt"));
                            FileInputStream stopTimeFile = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "gtfs/stop_times.txt"));

                            Stops.populate(stopsFile);
                            Route.populate(routesFile);
                            Trip.populate(tripFile);
                            StopTime.populate(stopTimeFile);

                            Log.d("-------------APP", "Successfully updated data!");
                        }
                        catch(FileNotFoundException ex) {
                            Log.d("-------------APP", "File not found exception!");
                        }
                    }
                }).start();
                Log.d("-------------APP", "UPDATE THREAD STARTED!");
            }
        });

        return view;
    }

}