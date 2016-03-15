package sp.para.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.List;

import sp.para.R;
import sp.para.models.Stops;

public class SearchActivity extends FragmentActivity {

    AutoCompleteTextView originTxtFld;
    AutoCompleteTextView destTxtFld;
    Button findRouteBtn;
    Stops origin;
    Stops destination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        List<Stops> stopsList = Stops.getAll();

        originTxtFld = (AutoCompleteTextView) findViewById(R.id.searchOrigin);
        destTxtFld = (AutoCompleteTextView) findViewById(R.id.searchDestination);

        final ArrayAdapter<Stops> originAdapter = new ArrayAdapter<Stops>(this, android.R.layout.simple_list_item_1, stopsList);
        originTxtFld.setAdapter(originAdapter);

        originTxtFld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                origin = originAdapter.getItem(position);
            }
        });

        final ArrayAdapter<Stops> destAdapter = new ArrayAdapter<Stops>(this, android.R.layout.simple_list_item_1, stopsList);
        destTxtFld.setAdapter(destAdapter);

        destTxtFld.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destination = destAdapter.getItem(position);
            }
        });

        findRouteBtn = (Button) findViewById(R.id.findRouteBtn);

        findRouteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Do A* search using origin and destination
                Log.d("-------------APP", "ORIGIN SELECTED = " + origin.getLat());
                Log.d("-------------APP", "DESTIN SELECTED = " + destination.getLat());
            }
        });
    }

}