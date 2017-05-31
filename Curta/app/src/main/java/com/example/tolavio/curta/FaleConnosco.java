package com.example.tolavio.curta;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FaleConnosco extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fale_connosco);

        final EditText mNome = (EditText) findViewById(R.id.nome_fale_connosco);
        final EditText mEmail = (EditText) findViewById(R.id.email_fale_connosco);
        final EditText mMensagem = (EditText) findViewById(R.id.mensagem_fale_connosco);
        Button mEnviarEmail = (Button) findViewById(R.id.enviar_fale_connosco);
        mEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open email client using intent
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","contato@curtaja.com",null));

                String to = mEmail.getText().toString();
                String message = mMensagem.getText().toString();
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Email de um cliente do Curta Já");
                emailIntent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(emailIntent,"Enviando Email..."));
            }
        });
    }
}
