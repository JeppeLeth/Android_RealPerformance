package dk.bitbreakers.android.realmperformance.ui.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static com.jjoe64.graphview.GraphView.*;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.text.NumberFormat;

import dk.bitbreakers.android.realmperformance.Constants;
import dk.bitbreakers.android.realmperformance.R;
import dk.bitbreakers.android.realmperformance.data.DbTask;
import dk.bitbreakers.android.realmperformance.data.Point;
import dk.bitbreakers.android.realmperformance.data.realm.RealmSinusQueryTask;
import dk.bitbreakers.android.realmperformance.data.sqlite.SqliteSinusQueryTask;
import dk.bitbreakers.android.realmperformance.util.BundleArguments;


public class PerformanceTestFragment extends Fragment implements DbTask.Callback<Point, Void> {

    private static final String TAG = PerformanceTestFragment.class.getSimpleName();

    //private final Handler mHandler = new Handler();
    private DbTask                  mDbTask;
    private GraphView               mGraphView;
    private GraphViewSeries         mGraphDataSeries;
    private int                     mScaleX;
    private String                  mTestType;
    private View                    mProgressContainer;
    private TextView                mProgressText;
    private Button                  mStartStop;
    private ViewGroup               mGraphContainer;
    private OnClickListener         mStartClick = new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!mDbTask.isRunning()) {
                                                mDbTask.start();
                                            }
                                        }
                                    };
    private OnClickListener         mStopClick = new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDbTask.stop();
                                        }
                                    };


    public PerformanceTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setRetainInstance(true);
        mTestType = getActivity().getIntent().getStringExtra(BundleArguments.KEY_TEST_TYPE);
        setupGraph();
        setupTask();
    }

    private void setupTask() {
        switch (mTestType){
            case BundleArguments.ARG_TEST_TYPE_SQLITE_READ_SINUS:
                mDbTask = SqliteSinusQueryTask.getInstance(getActivity());
                Log.d(TAG, "setupTask sqlite");
                break;
            case BundleArguments.ARG_TEST_TYPE_REALM_READ_SINUS:
                mDbTask = RealmSinusQueryTask.getInstance(getActivity());
                Log.d(TAG, "setupTask realm");
                break;
            default:
               throw new IllegalArgumentException("Bundle must contain valid argument for key = "
               +BundleArguments.KEY_TEST_TYPE + ", the current was "+mTestType);

        }
    }

    private void setupGraph(){
        mGraphView = new LineGraphView(getActivity().getApplicationContext(), "");
        resetGraph();

        mGraphView.getGraphViewStyle().setNumHorizontalLabels(6);
        mGraphView.getGraphViewStyle().setGridColor(Color.LTGRAY);
        //mGraphView.setViewPort(0, 520);
        mGraphView.setScrollable(true);
        mGraphView.setId(View.NO_ID);
        switch (mTestType){
            case BundleArguments.ARG_TEST_TYPE_SQLITE_READ_SINUS:
            case BundleArguments.ARG_TEST_TYPE_REALM_READ_SINUS:
                mGraphView.setManualYAxisBounds(60, -60);
                mScaleX = 225;
                break;
            default:

        }
    }

    private void resetGraph(){
        mGraphView.removeAllSeries();
        mGraphDataSeries = new GraphViewSeries( new GraphViewData[] {});
        mGraphDataSeries.getStyle().color = getResources().getColor(R.color.realm_text_highlight);
        mGraphView.addSeries(mGraphDataSeries); // data
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_performance_test, container, false);
        if (savedInstanceState == null) {
            ViewGroup layout = (ViewGroup) view.findViewById(R.id.graphView);
            if (layout.getChildCount() == 0)
            layout.addView(mGraphView);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView headline = (TextView) view.findViewById(R.id.textHeadline);
        TextView description = (TextView) view.findViewById(R.id.textDescription);
        mProgressContainer = view.findViewById(R.id.ui_progress_container);
        mProgressText = (TextView) view.findViewById(R.id.progressText);
        mStartStop = (Button) view.findViewById(R.id.btn_start_stop);
        mGraphContainer = (ViewGroup) view.findViewById(R.id.graphView);

        String name = null;
        String iterationCount = null;
        switch (mTestType){
            case BundleArguments.ARG_TEST_TYPE_SQLITE_READ_SINUS:
                name = getString(R.string.sqlite);
                iterationCount = NumberFormat.getIntegerInstance().format(Constants.TEST_QUERIES_COUNT);
                headline.setText(getString(R.string.sinus_test_header, name, iterationCount));
                description.setText(getString(R.string.sinus_test_desc, name, iterationCount));
                break;
            case BundleArguments.ARG_TEST_TYPE_REALM_READ_SINUS:
                name = getString(R.string.realm);
                iterationCount = NumberFormat.getIntegerInstance().format(Constants.TEST_QUERIES_COUNT);
                headline.setText(getString(R.string.sinus_test_header, name, iterationCount));
                description.setText(getString(R.string.sinus_test_desc, name, iterationCount));
                break;
            default:
        }

        invalidateStartStopButton(!mDbTask.isRunning());
    }

    @Override
    public void onResume() {
        super.onResume();
        mDbTask.setCallback(this);
        if (mDbTask.isSetupNeeded()) {
            showLoading(true, R.string.loading_database_setup);
            if (!mDbTask.isRunning()) {
                mDbTask.runSetup();
            }
        } else {
            showLoading(false, 0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mGraphView != null && mGraphView.getParent() != null) {
            ((ViewGroup) mGraphView.getParent()).removeAllViews();
        }
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mDbTask.dispose();
        mDbTask = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void onSetupFinished() {
        showLoading(false, 0);
        invalidateStartStopButton(true);
    }

    private void showLoading(boolean show, int loadingText){
        if (mProgressContainer == null) {
            return;
        }
        mProgressContainer.setVisibility(show ? VISIBLE: GONE);
        mStartStop.setEnabled(!show);
        mGraphContainer.setVisibility(!show ? VISIBLE : GONE);
        if (show) {
            mProgressText.setText(loadingText);
        }
    }

    private void invalidateStartStopButton(boolean start){
        mStartStop.setText(start ? R.string.start : R.string.stop);
        mStartStop.setOnClickListener(start ? mStartClick : mStopClick);
    }

    @Override
    public void onPreExecute() {
        resetGraph();
        invalidateStartStopButton(false);
    }

    @Override
    public void onProgressUpdate(Point... values) {
        mGraphDataSeries.appendData(new GraphViewData(values[0].getX(), values[0].getY()), true, mScaleX);
        mGraphView.setViewPort(values[0].getX() - mScaleX, mScaleX);
    }

    @Override
    public void onPostExecute(Void aVoid) {
        Log.d(TAG, "Done");
        invalidateStartStopButton(true);
    }
}
