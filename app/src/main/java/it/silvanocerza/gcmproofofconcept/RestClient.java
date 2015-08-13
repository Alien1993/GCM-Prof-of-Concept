package it.silvanocerza.gcmproofofconcept;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class RestClient {
    private Api mApi;

    public RestClient() {
        // Defining Serializer/Deserializer
        Gson converter = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // Defining Client and RestAdapter
        OkHttpClient client = new OkHttpClient();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(Constants.ROOT_ENDPOINT)
                .setConverter(new GsonConverter(converter))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mApi = restAdapter.create(Api.class);
    }

    public Api getApi() {
        return this.mApi;
    }
}
