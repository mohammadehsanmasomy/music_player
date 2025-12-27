package com.example.masomi1;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSelectFile, btnPlayUrl;
    EditText edtUrl;

    public static final int PICK_AUDIO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSelectFile = findViewById(R.id.btnSelectFile);
        btnPlayUrl = findViewById(R.id.btnPlayUrl);
        edtUrl = findViewById(R.id.edtUrl);

        btnSelectFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_AUDIO);
        });

        btnPlayUrl.setOnClickListener(v -> {
            String url = edtUrl.getText().toString().trim();
            if (!url.isEmpty()) {
                goToPlayer(url);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                goToPlayer(uri.toString());
            }
        }
    }

    private void goToPlayer(String url) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("musicUrl", url);
        startActivity(intent);
    }
}