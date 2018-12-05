package com.example.aggarwalswati.providersob;

/**
 * Created by aggarwal.swati on 12/5/18.
 */

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface ApiInterface {
    @GET("api/{email}/{password}")
    Call<JSONObject> authenticate(@Path("email") String email, @Path("password") String password);

}
