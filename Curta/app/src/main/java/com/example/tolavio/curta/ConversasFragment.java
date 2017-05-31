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
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ConversasFragment extends Fragment implements AdapterView.OnItemClickListener {

    String[] usuarios = {
            "Mabeko",
            "Sugo",
            "Kapahala",
            "Cadinda",
            "Dhalo",
            "Mafalda",
            "Maria",
            "LeBron"};

    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.list_chat);
        ConversasAdapter conversasAdapter = new ConversasAdapter(getActivity(),usuarios);
        listView.setAdapter(conversasAdapter);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(),Chat.class);

        startActivity(i);
    }
}
