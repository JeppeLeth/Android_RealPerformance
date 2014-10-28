package dk.bitbreakers.android.realmperformance.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import dk.bitbreakers.android.realmperformance.R;
import dk.bitbreakers.android.realmperformance.util.BundleArguments;

public class PerformanceTestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String testType = getIntent().getStringExtra(BundleArguments.KEY_TEST_TYPE);
        if (savedInstanceState == null) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.performance_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
