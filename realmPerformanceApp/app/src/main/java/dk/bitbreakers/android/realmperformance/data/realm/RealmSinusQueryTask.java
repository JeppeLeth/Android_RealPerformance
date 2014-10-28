package dk.bitbreakers.android.realmperformance.data.realm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.text.NumberFormat;

import dk.bitbreakers.android.realmperformance.Constants;
import dk.bitbreakers.android.realmperformance.R;
import dk.bitbreakers.android.realmperformance.data.DbTask;
import dk.bitbreakers.android.realmperformance.data.Point;
import dk.bitbreakers.android.realmperformance.util.MockUtil;
import io.realm.Realm;

public class RealmSinusQueryTask implements DbTask {
    private static final String TAG             = RealmSinusQueryTask.class.getSimpleName();
    private static final String DB_FILE_NAME    = "realm_point_db";
    private static final int QUERIES_COUNT      = Constants.TEST_QUERIES_COUNT;

    private static RealmSinusQueryTask          ourInstance;

    private Context                             mContext;
    private boolean                             running;
    private Callback                            mCallback;
    private AsyncTask                           mSetupTask;
    private AsyncTask                           mQueryTask;

    public static RealmSinusQueryTask getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new RealmSinusQueryTask(context.getApplicationContext());
        }
        return ourInstance;
    }


    private RealmSinusQueryTask(Context applicationContext) {
        mContext = applicationContext;
    }

    private Realm getRealm(){
        return Realm.getInstance(mContext, DB_FILE_NAME);
    }

    @Override
    public boolean isSetupNeeded() {
        if (mSetupTask != null ) {
            return true;
        } else if (running){
            return false;
        }

        Realm realm = getRealm();
        realm.beginTransaction();  // Not doing this will create a memory leak - see screen shot
        RealmPoint p = realm.where(RealmPoint.class).findFirst();
        realm.commitTransaction(); // Remember to commit
        return p == null;
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
                Realm realm = getRealm();
                long start = System.currentTimeMillis();
                Log.d(TAG, "Start insert");
                realm.beginTransaction();
                for (int i = 1 ; i <= QUERIES_COUNT ; i++){
                    RealmPoint p = realm.createObject(RealmPoint.class);
                    p.setX(i);
                    p.setY( MockUtil.sinCurveCalc(i));
                }
                realm.commitTransaction();
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
                Realm realm = getRealm();
                for (int i = 1 ; i <= QUERIES_COUNT && running ; i++){
                    //realm.beginTransaction();
                    RealmPoint p = realm.where(RealmPoint.class).equalTo("x", i).findFirst();
                    //realm.commitTransaction();
                    publishProgress(p);
                }
                realm = null; // Gets very nasty Out of Memory exception if this is not done. Reproduce via start/stop repeatedly
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
