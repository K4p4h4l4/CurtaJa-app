package com.example.tolavio.curta;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    EditText emailLoginET,senhaLoginET;
    Button login;
    String email_login,senha_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLoginET = (EditText) findViewById(R.id.emailLogin);
        senhaLoginET = (EditText) findViewById(R.id.senhaLogin);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario_login(v);
            }
        });
    }

    public void usuario_login(View view){
        email_login = emailLoginET.getText().toString();
        senha_login = senhaLoginET.getText().toString();
        String method = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,email_login,senha_login);
    }
}
