package dk.bitbreakers.android.realmperformance.data.sqlite;

import android.provider.BaseColumns;

public class DataContract {

    private DataContract(){}

    public static final class PointEntry implements BaseColumns {

        private PointEntry(){}

        public static final String TABLE_NAME   = "point";

        public static final String COLUMN_X     = "x";
        public static final String COLUMN_Y     = "y";

        public static String buildCreateTableQuery(){
            String SQL_CREATE_POINT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_X + " INTEGER NOT NULL, " +
                    COLUMN_Y + " REAL NOT NULL " +
                    " );";
            return SQL_CREATE_POINT_TABLE;
        }

        public static String buildCreateIndexOnX(){
            String SQL_CREATE_INDEX_ON_X = "CREATE INDEX Point_Index_On_x ON "+ TABLE_NAME+ " (" +
                    COLUMN_X+" );";
            return SQL_CREATE_INDEX_ON_X;
        }

    }

}
