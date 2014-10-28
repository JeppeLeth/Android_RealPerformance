package dk.bitbreakers.android.realmperformance.data;

public interface DbTask {

    boolean isSetupNeeded();
    void runSetup();
    boolean isRunning();
    void start();
    void stop();
    void dispose();
    void setCallback(Callback callback);
    void removeCallback();

    interface Callback<Progress, Result> {
        void onSetupFinished();
        void onPreExecute();
        void onProgressUpdate(Progress... values);
        void onPostExecute(Result result);
    }
}
