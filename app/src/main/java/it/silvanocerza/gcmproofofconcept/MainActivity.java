package it.silvanocerza.gcmproofofconcept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private Button mRegisterButton;
    private TextView mRegistrationStatusTextView, mRegistrationTokenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setEnabled(checkPlayServices());

        mRegistrationStatusTextView = (TextView) findViewById(R.id.registration_status);
        mRegistrationStatusTextView.setText(getString(R.string.token_not_stored));

        mRegistrationTokenTextView = (TextView) findViewById(R.id.registration_token);

        mPreferences = getSharedPreferences(Constants.TOKEN_PREFS, MODE_PRIVATE);

        IntentFilter registrationFilter = new IntentFilter();
        registrationFilter.addAction(Constants.REGISTRATION_COMPLETE);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new RegistrationReceiver(), registrationFilter
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    public void deleteDevice(View view) {
        String deviceId = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mPreferences.edit().remove(Constants.TOKEN).apply();
        mRegistrationStatusTextView.setText(getString(R.string.token_not_stored));
        mRegistrationTokenTextView.setText("");

        new RestClient().getApi().deleteDeviceId(deviceId, new ResponseCallback() {
            @Override
            public void success(Response response) {
                Log.i("DeleteDeviceCall", "Status: " + response.getStatus());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        400).show();
            } else {
                Log.i("MainActivity", "Device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }

    private class RegistrationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mRegistrationStatusTextView.setText(getString(R.string.token_stored));
            mRegistrationTokenTextView.setText(mPreferences.getString(Constants.TOKEN, ""));
        }
    }
}
