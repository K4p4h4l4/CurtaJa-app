package com.example.tolavio.curta;

import com.example.tolavio.curta.models.FestasModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Tolavio on 19-07-2017.
 */

public interface ApiFestasInterface {

    @POST("search?column=categorias_id&operator==&search=4")
    Call<List<FestasModel>> getFestasModel();
}
