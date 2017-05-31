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

public class ContactosAdapter extends BaseAdapter {


    private Context c;
    public String[] usuarios;
    public String[] moral;

    public ContactosAdapter(Context c, String[] usuarios, String[] moral){

        this.c = c;
        this.usuarios = usuarios;
        this.moral = moral;
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
            convertView = layoutInflater.inflate(R.layout.contactos_item,null);
        }

        //Pegando o elemento na estrutura
        TextView nomeUsuario = (TextView)convertView.findViewById(R.id.nome_usuario);
        TextView frase = (TextView)convertView.findViewById(R.id.frase_cliente);

        //settando os dados
        nomeUsuario.setText(usuarios[position]);
        frase.setText(moral[position]);
        return convertView;
    }
}
