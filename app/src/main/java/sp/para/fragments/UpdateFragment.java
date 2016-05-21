package sp.para.fragments;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

        Log.i("UpdateFragment - ", "Setting up Update Fragment...");

        // Goes back to previous fragment
        backBtn = (Button) view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        // Updates the date when clicked
        updateBtn = (Button) view.findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("UpdateFragment - ", "Update button clicked!");
                Log.i("UpdateFragment - ", "Update starting...");

                // Creates a toast to inform the user that update has been started
                Toast.makeText(getActivity(), R.string.update_start, Toast.LENGTH_LONG).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Get files for each model
                            FileInputStream stopsFile = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "gtfs/stops.txt"));
                            FileInputStream routesFile = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "gtfs/routes.txt"));
                            FileInputStream tripFile = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "gtfs/trips.txt"));
                            FileInputStream stopTimeFile = new FileInputStream(new File(Environment.getExternalStorageDirectory(), "gtfs/stop_times.txt"));

                            // Start population for each model
                            Stops.populate(stopsFile);
                            Route.populate(routesFile);
                            Trip.populate(tripFile);
                            StopTime.populate(stopTimeFile);

                            Log.i("UpdateFragment - ", "Update finished!");

                            // Creates a notification to to inform the user that update has been finished
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(getActivity())
                                    .setSmallIcon(R.drawable.ic_directions_white_36dp)
                                    .setContentTitle("Para")
                                    .setContentText("Data update has been completed.");

                            mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                            // Send the notification
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotifyMgr.notify(001, mBuilder.build());
                        }
                        catch(FileNotFoundException ex) {
                            Log.e("UpdateFragment - ", ex.toString(), ex);

                            // Creates a notification to to inform the user that update has failed
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(getActivity())
                                            .setSmallIcon(R.drawable.ic_directions_white_36dp)
                                            .setContentTitle("Para")
                                            .setContentText("Update failed! Please make sure you have the GTFS feed in the proper directory.");

                            mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                            // Send the notification
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotifyMgr.notify(002, mBuilder.build());
                        }
                    }
                }).start();
            }
        });

        return view;
    }

}