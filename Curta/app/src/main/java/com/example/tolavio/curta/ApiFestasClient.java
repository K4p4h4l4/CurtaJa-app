package com.example.tolavio.curta;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tolavio on 19-07-2017.
 */

public class ApiFestasClient {

    public static final String BASE_URL = "http://www.curtaja.com/api/v1/eventos/";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }
}
