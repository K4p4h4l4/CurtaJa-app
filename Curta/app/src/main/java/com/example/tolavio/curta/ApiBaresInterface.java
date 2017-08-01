package com.example.tolavio.curta;

import com.example.tolavio.curta.models.BaresModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Tolavio on 19-07-2017.
 */

public interface ApiBaresInterface {

    @POST("search?column=categorias_id&operator==&search=5")
    Call<List<BaresModel>> getBaresModel();
}
