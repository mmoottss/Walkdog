package com.example.dog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Writecommunity extends AppCompatActivity {

    Button btnSave;
    EditText edtContent;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communtiy_write);

        edtContent = (EditText) findViewById(R.id.content_et);
        btnSave = (Button) findViewById(R.id.btnSave);


    }

}
