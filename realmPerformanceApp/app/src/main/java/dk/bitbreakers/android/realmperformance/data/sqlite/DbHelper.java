package dk.bitbreakers.android.realmperformance.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for SQLite data.
 */
public class DbHelper extends SQLiteOpenHelper {

    // If there is change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION   = 1;

    public static final String DATABASE_NAME    = "sqlite.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POINT_TABLE = DataContract.PointEntry.buildCreateTableQuery();

        sqLiteDatabase.execSQL(SQL_CREATE_POINT_TABLE);

        // Try to remove the comment slashes in next line. Then uninstall and install the app again.
        // You will see a astronomical increase in the query time. -- When faster that using Realm!!!

        //sqLiteDatabase.execSQL(DataContract.PointEntry.buildCreateIndexOnX());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.PointEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}