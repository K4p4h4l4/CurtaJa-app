package com.example.tolavio.curta;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class ContactosFragment extends Fragment implements AdapterView.OnItemClickListener {


    String[] usuarios = {
            "Mabeko",
            "Sugo",
            "Kapahala",
            "Cadinda",
            "Dhalo",
            "Mafalda",
            "Maria",
            "LeBron"};

    String[] moral = {
            "Toté vais te queimar",
            "Se lixaste!!",
            "Vaais apanhar",
            "blablalalbalablab",
            "Toté vais te queimar",
            "Se lixaste!!",
            "Vaais apanhar",
            "blablalalbalablab"};

    public ContactosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contactos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.list_contactos);
        ContactosAdapter contactosAdapter = new ContactosAdapter(getActivity(),usuarios,moral);
        listView.setAdapter(contactosAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(view.getContext(),Contato.class);

        startActivity(intent);
    }
}
