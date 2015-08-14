package it.silvanocerza.gcmproofofconcept;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class GcmMessageListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d("GcmMessageListener", data.toString());
    }
}
