package sp.para;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

public class Para extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize ActiveAndroid for ORM operations
        ActiveAndroid.initialize(this);
    }
}
