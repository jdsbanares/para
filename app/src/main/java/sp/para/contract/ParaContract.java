package sp.para.contract;

import android.provider.BaseColumns;

public final class ParaContract {

    public ParaContract() {
    }

    public static abstract class Stops implements BaseColumns {
        public static final String TABLE_NAME = "stops";
        public static final String COLUMN_NAME_STOP_ID = "stop_id";
        public static final String COLUMN_NAME_STOP_NAME = "name";
        public static final String COLUMN_NAME_STOP_LAT = "lat";
        public static final String COLUMN_NAME_STOP_LON = "lon";
        private static final String COORD_TYPE = "NUMBER(9,6)";
        private static final String LONG_TYPE = "LONG";
        private static final String TEXT_TYPE = "TEXT";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Stops.TABLE_NAME + " (" +
                Stops._ID + " INTEGER PRIMARY KEY," +
                Stops.COLUMN_NAME_STOP_ID + LONG_TYPE + COMMA_SEP +
                Stops.COLUMN_NAME_STOP_NAME + TEXT_TYPE + COMMA_SEP +
                Stops.COLUMN_NAME_STOP_LAT + COORD_TYPE + COMMA_SEP +
                Stops.COLUMN_NAME_STOP_LON + COORD_TYPE + COMMA_SEP +
            " )";

        private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Stops.TABLE_NAME;
    }
}
