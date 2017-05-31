package com.example.tolavio.curta;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CadastrarActivity extends AppCompatActivity {

    EditText nomeEd,emailEd,senhaEd,confSenhaEd,contatoEd;
    Button cadastrar;
    //RequestQueue requestQueue;

    String nome,contato,email,senha, confSenha;

    /*String url_de_cadastro = "http://www.appear.in/2mind";
    String url_de_login = "http://www.curtaja.com/api/v1/login";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        nomeEd = (EditText) findViewById(R.id.Nome);
        contatoEd=(EditText) findViewById(R.id.contato);
        emailEd = (EditText) findViewById(R.id.Email);
        senhaEd = (EditText)findViewById(R.id.Senha);
        confSenhaEd = (EditText)findViewById(R.id.ConfSenha);
        cadastrar = (Button) findViewById(R.id.cadastrar);

        //requestQueue = Volley.newRequestQueue(getApplicationContext());

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registo_usuario(v);
            }
        });

    }

    public void registo_usuario(View view){

        nome = nomeEd.getText().toString();
        contato = contatoEd.getText().toString();
        email = emailEd.getText().toString();
        senha = senhaEd.getText().toString();
        confSenha = confSenhaEd.getText().toString();
        String method = "registrar";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,nome,email,senha,confSenha,contato);
        finish();
    }


}
