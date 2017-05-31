package com.example.tolavio.curta;

import com.example.tolavio.curta.models.BaresModel;

import java.util.List;

/**
 * Created by Tolavio on 03-03-2017.
 */

public class RetornaListaBares{

    List<BaresModel> baresModels;

    public RetornaListaBares(List<BaresModel> bares){
        this.baresModels = bares;
    }

    public List<BaresModel> RetornaListaBares(){
        return baresModels;
    }
}
