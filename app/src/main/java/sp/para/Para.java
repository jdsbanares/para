package sp.para;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

public class Para extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize ActiveAndroid for ORM operations
        Log.i("Para - ", "Initializing ActiveAndroid...");
        ActiveAndroid.initialize(this);
    }
}
