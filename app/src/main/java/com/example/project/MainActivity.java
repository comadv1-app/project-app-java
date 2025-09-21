package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnContent = findViewById(R.id.btnExample);
        btnContent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
            startActivity(intent);
        });

        Button btnQuiz = findViewById(R.id.btnQuiz);
        btnQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
        });

        Button btnChallenge = findViewById(R.id.btnChallenge);
        btnChallenge.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChallengeActivity.class);
            startActivity(intent);
        });

        Button btnMajor = findViewById(R.id.btnMajor);
        btnMajor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MajorActivity.class);
            startActivity(intent);
        });

        Button btnOthers = findViewById(R.id.btnOthers);
        btnOthers.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OthersActivity.class);
            startActivity(intent);
        });

    }
}