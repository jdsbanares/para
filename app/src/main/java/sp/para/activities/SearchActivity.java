package sp.para.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import sp.para.R;
import sp.para.models.Stops;

public class SearchActivity extends FragmentActivity {

    AutoCompleteTextView originTxtFld;
    AutoCompleteTextView destTxtFld;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

//        List<String> stopsList = new ArrayList<String>();

        List<Stops> stopsList = Stops.getAll();

//        for(Stops currStop : Stops.getAll()) {
//            stopsList.add(currStop.getName());
//        }

        originTxtFld = (AutoCompleteTextView) findViewById(R.id.searchOrigin);
        destTxtFld = (AutoCompleteTextView) findViewById(R.id.searchDestination);

        ArrayAdapter<Stops> originAdapter = new ArrayAdapter<Stops>(this, android.R.layout.simple_list_item_1, stopsList);
        originTxtFld.setAdapter(originAdapter);

        ArrayAdapter<Stops> destAdapter = new ArrayAdapter<Stops>(this, android.R.layout.simple_list_item_1, stopsList);
        destTxtFld.setAdapter(destAdapter);
    }

}