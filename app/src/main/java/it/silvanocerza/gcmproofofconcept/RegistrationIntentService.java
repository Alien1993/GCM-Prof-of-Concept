package it.silvanocerza.gcmproofofconcept;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {
    public static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = getSharedPreferences(Constants.TOKEN_PREFS, MODE_PRIVATE);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.project_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE);

            Log.i(TAG, "GCM Registration token: " + token);

            preferences.edit().putString(Constants.TOKEN, token).apply();

            sendRegistrationToServer(token);

            preferences.edit().putBoolean(Constants.TOKEN_IS_SENT, true).apply();

        } catch (IOException exc) {
            Log.d(TAG, "Failed to retrieve token", exc);

            preferences.edit().putBoolean(Constants.TOKEN_IS_SENT, false).apply();
        }

        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    private void sendRegistrationToServer(String token) {
        // noop
    }
}
