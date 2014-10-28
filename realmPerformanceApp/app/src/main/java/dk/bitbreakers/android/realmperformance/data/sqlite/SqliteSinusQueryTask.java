package dk.bitbreakers.android.realmperformance.data.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static dk.bitbreakers.android.realmperformance.data.sqlite.DataContract.PointEntry;

import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.text.NumberFormat;

import dk.bitbreakers.android.realmperformance.Constants;
import dk.bitbreakers.android.realmperformance.R;
import dk.bitbreakers.android.realmperformance.data.DbTask;
import dk.bitbreakers.android.realmperformance.data.Point;
import dk.bitbreakers.android.realmperformance.util.MockUtil;


public class SqliteSinusQueryTask implements DbTask {
    private static final String TAG             = SqliteSinusQueryTask.class.getSimpleName();
    private static final int QUERIES_COUNT      = Constants.TEST_QUERIES_COUNT;

    private static SqliteSinusQueryTask ourInstance;

    public static SqliteSinusQueryTask getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SqliteSinusQueryTask(context.getApplicationContext());
        }
        return ourInstance;
    }

    private Context mContext;
    private SQLiteOpenHelper mDatabase;


    private boolean running;
    private Callback mCallback;
    private AsyncTask mSetupTask;
    private AsyncTask mQueryTask;

    private SqliteSinusQueryTask(Context applicationContext) {
        mContext = applicationContext;
        mDatabase = new DbHelper(mContext);
    }


    @Override
    public boolean isSetupNeeded() {
        if (mSetupTask != null ) {
            return true;
        } else if (running){
            return false;
        }

        SQLiteDatabase db = mDatabase.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + PointEntry.TABLE_NAME, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count == 0;
    }

    @Override
    public void runSetup() {
        if (mSetupTask != null) {
            return;
        }
        running = true;
        mSetupTask = new AsyncTask<Void, Void, Long>(){
            @Override
            protected Long doInBackground(Void... params) {
                SQLiteDatabase db = mDatabase.getWritableDatabase();
                long start = System.currentTimeMillis();
                Log.d(TAG, "Start insert");
                try {
                    db.beginTransaction();
                    String stmt = "INSERT INTO " + PointEntry.TABLE_NAME + "("+
                            PointEntry.COLUMN_X + ", " +
                            PointEntry.COLUMN_Y + ") " +
                            "VALUES (?, ?)";
                    SQLiteStatement preparedStmt = db.compileStatement(stmt);
                    for (int i = 1 ; i <= QUERIES_COUNT ; i++){
                        preparedStmt.bindLong(1, i);
                        preparedStmt.bindDouble(2, MockUtil.sinCurveCalc(i));
                        preparedStmt.execute();
                    }
                    db.setTransactionSuccessful();
                } catch (SQLException e) {
                    Log.e(TAG,"SQLite setup failure ", e);
                } finally {
                    db.endTransaction();
                }
                long duration = System.currentTimeMillis()-start;
                Log.d(TAG, "End insert, time in millis = "+duration);
                if (running){
                    running = false;
                }
                mSetupTask = null;
                return duration;
            }

            @Override
            protected void onPostExecute(Long duration) {
                super.onPostExecute(duration);
                if (mCallback != null) {
                    Toast.makeText(mContext, mContext.getString(R.string.test_setup_finished,
                            NumberFormat.getIntegerInstance().format(duration)), Toast.LENGTH_LONG).show();
                    mCallback.onSetupFinished();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        if (running) {
            return;
        }
        running = true;
        mQueryTask = new AsyncTask<Void, Point, Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (mCallback != null) {
                    mCallback.onPreExecute();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = mDatabase.getReadableDatabase();
                String query = "SELECT " +
                        PointEntry.COLUMN_X + ", "+
                        PointEntry.COLUMN_Y +
                        " FROM "+PointEntry.TABLE_NAME +
                        " WHERE " + PointEntry.COLUMN_X + " = ?";
                String[] args = new String[1];
                for (int i = 1 ; i <= QUERIES_COUNT && running ; i++){
                    args[0] = String.valueOf(i);
                    Cursor c = db.query(PointEntry.TABLE_NAME, new String[]{PointEntry.COLUMN_X ,PointEntry.COLUMN_Y}, "x = "+i,null,null,null,null);
                    if (c.moveToFirst()) {
                        Point p = new SqlitePoint(c.getInt(0), c.getDouble(1));
                        publishProgress(p);
                    }
                    c.close();
                }
                mQueryTask = null;
                running = false;
                return null;
            }

            @Override
            protected void onProgressUpdate(Point... values) {
                super.onProgressUpdate(values);
                if (mCallback != null) {
                    mCallback.onProgressUpdate(values);
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mCallback != null) {
                    mCallback.onPostExecute(aVoid);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }
        running = false;
    }

    @Override
    public void dispose() {
        mContext = null;
        mCallback = null;
        running = false;
        ourInstance = null;
    }

    @Override
    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void removeCallback() {
        mCallback = null;
    }
}
