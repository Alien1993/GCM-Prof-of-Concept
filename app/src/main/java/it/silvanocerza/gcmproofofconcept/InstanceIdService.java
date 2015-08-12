package it.silvanocerza.gcmproofofconcept;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

public class InstanceIdService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }

    private void refreshToken() {
        InstanceID.getInstance(this).getId();
    }
}
