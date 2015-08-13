package it.silvanocerza.gcmproofofconcept;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit.client.Response;

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
            String token = instanceID.getToken(getString(R.string.project_number),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE);

            String savedToken = preferences.getString(Constants.TOKEN, "");

            Log.i(TAG, "GCM Registration token: " + token);

            if (savedToken.equals("")) {

                preferences.edit().putString(Constants.TOKEN, token).apply();

                sendRegistrationToServer(token);

                preferences.edit().putBoolean(Constants.TOKEN_IS_SENT, true).apply();

            } else if (!token.equals(savedToken)) {

                preferences.edit().putString(Constants.TOKEN, token).apply();

                updateRegistrationToServer(token);

                preferences.edit().putBoolean(Constants.TOKEN_IS_SENT, true).apply();

            }

            Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

        } catch (IOException exc) {
            Log.d(TAG, "Failed to retrieve token", exc);

            preferences.edit().putBoolean(Constants.TOKEN_IS_SENT, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) {
        JsonObject json = new JsonObject();

        String deviceId = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        json.addProperty("device_id", deviceId);
        json.addProperty("registration_id", token);

        Response response = new RestClient().getApi().postRegistrationId(json);

        if (response.getStatus() != 201) {
            sendRegistrationToServer(token);
        }
    }

    private void updateRegistrationToServer(String token) {
        JsonObject json = new JsonObject();

        String deviceId = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        json.addProperty("registration_id", token);

        Response response = new RestClient().getApi().updateRegistrationId(deviceId, json);

        if (response.getStatus() != 200) {
            sendRegistrationToServer(token);
        }
    }
}
