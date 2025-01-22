package com.example.myzametki;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myzametki.Models.Zametki;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ZametkiTakerActivity extends AppCompatActivity {

    EditText editText_title, editText_text;
    ImageView imageView_save;
    Zametki zametki;
    boolean isOldText = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zametki_taker);
        imageView_save = findViewById(R.id.imageView_save);
        zametki = new Zametki();
        try {
            zametki = (Zametki) getIntent().getSerializableExtra("old_text");
            editText_title.setText(zametki.getTitle());
            editText_text.setText(zametki.getZametki());
            isOldText = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String text = editText_title.getText().toString();
                if(text.isEmpty()){
                    Toast.makeText(ZametkiTakerActivity.this, "Введите текст", Toast.LENGTH_SHORT).show();
                    return;
                }
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                Date date = new Date();

                if (!isOldText) {
                    zametki = new Zametki();
                }

                zametki.setTitle(title);
                zametki.setZametki(text);
                zametki.setDate(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("zametki", zametki);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        editText_title = findViewById(R.id.editText_title);
        editText_text = findViewById(R.id.editText_text);



        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}