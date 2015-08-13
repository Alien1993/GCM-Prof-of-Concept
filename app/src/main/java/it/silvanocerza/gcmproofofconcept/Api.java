package it.silvanocerza.gcmproofofconcept;

import com.google.gson.JsonElement;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;

public interface Api {

    @POST("/gcm/")
    Response postRegistrationId(@Body JsonElement jsonBody);

    @PATCH("/gcm/{deviceId}/")
    Response updateRegistrationId(@Path("deviceId") String deviceId, @Body JsonElement jsonBody);

    @DELETE("/gcm/{deviceId}/")
    void deleteDeviceId(@Path("deviceId") String deviceId);

}
