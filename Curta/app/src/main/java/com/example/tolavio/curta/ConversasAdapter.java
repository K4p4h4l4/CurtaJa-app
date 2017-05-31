package com.example.tolavio.curta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Tolavio on 20-11-2016.
 */

public class ConversasAdapter extends BaseAdapter {

    private Context c;
    public String[] usuarios;


    public ConversasAdapter(Context c, String[] usuarios){

        this.c = c;
        this.usuarios = usuarios;

    }

    @Override
    public int getCount() {
        return usuarios.length;
    }

    @Override
    public Object getItem(int position) {
        return usuarios[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){

            LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chat_item,null);
        }

        //Pegando o elemento na estrutura
        TextView nomeUsuario = (TextView)convertView.findViewById(R.id.chat_item);

        //settando os dados
        nomeUsuario.setText(usuarios[position]);

        return convertView;
    }
}
