package com.example.tolavio.curta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListView listView = (ListView) findViewById(R.id.list_msg);
        Button btnSend = (Button) findViewById(R.id.btnSend);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
    }
}
