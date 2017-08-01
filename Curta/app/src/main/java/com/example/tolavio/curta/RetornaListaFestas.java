package com.example.tolavio.curta;

import com.example.tolavio.curta.models.FestasModel;

import java.util.List;

/**
 * Created by Tolavio on 29-03-2017.
 */

public class RetornaListaFestas {

    List<FestasModel> festasModels;

    public RetornaListaFestas(List<FestasModel> festas){

        this.festasModels = festas;
    }

    public List<FestasModel> RetornaListaFestas(){
        return festasModels;
    }
}
