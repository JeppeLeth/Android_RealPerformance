package dk.bitbreakers.android.realmperformance.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dk.bitbreakers.android.realmperformance.Constants;
import dk.bitbreakers.android.realmperformance.R;
import dk.bitbreakers.android.realmperformance.util.AnimUtil;
import dk.bitbreakers.android.realmperformance.util.BundleArguments;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        int id = item.getItemId();

        switch (id){
            case R.id.action_homepage:
                i.setData(Uri.parse(Constants.URL_HOMEPAGE));
                startActivity(i);
                return true;
            case R.id.action_github:
                i.setData(Uri.parse(Constants.URL_GITHUB));
                startActivity(i);
                return true;
            case R.id.action_twitter:
                i.setData(Uri.parse(Constants.URL_TWITTER));
                startActivity(i);
                return true;
            case R.id.action_stackoverflow:
                i.setData(Uri.parse(Constants.URL_STACKOVERFLOW));
                startActivity(i);
                return true;
            case R.id.action_email:
                i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",Constants.EMAIL_ADDRESS, null));
                startActivity(i);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPerformanceTestClick(View view){
        Intent i = new Intent(this, PerformanceTestActivity.class);
        switch (view.getId()){
            case R.id.btn_sqlite_test:
                i.putExtra(BundleArguments.KEY_TEST_TYPE, BundleArguments.ARG_TEST_TYPE_SQLITE_READ_SINUS);
                startActivity(i);
                break;
            case R.id.btn_realm_test:
                i.putExtra(BundleArguments.KEY_TEST_TYPE, BundleArguments.ARG_TEST_TYPE_REALM_READ_SINUS);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    public void onLogoClick(View view){
        // I like to put in small hidden animations
        AnimUtil.tada(findViewById(R.id.realm_logo), 1.2f).start();
    }

}
