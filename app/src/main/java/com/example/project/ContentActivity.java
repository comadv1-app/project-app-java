package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.project.R;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, sys.top, 0, 0);
            return insets;
        });

        View secBasic = findViewById(R.id.secBasic);
        View secCore = findViewById(R.id.secCore);
        View secElec = findViewById(R.id.secElective);

        setTitle(secBasic, "วิชาพื้นฐาน");
        addItem(secBasic, "calculus (I, II)");
        addItem(secBasic, "physics (I, II Emphasis on electrical circuits)");
        addItem(secBasic, "Chemistry");
        addItem(secBasic, "Basic programming (C, Python)");

        setTitle(secCore, "วิชาแกน");
        addItem(secCore, "Data Structures and Algorithms");
        addItem(secCore, "Digital Systems");
        addItem(secCore, "Electrical and Electronic Circuits");
        addItem(secCore, "Computer Architecture");
        addItem(secCore, "Operating Systems");
        addItem(secCore, "Database Systems");
        addItem(secCore, "Computer Networks");
        addItem(secCore, "Probability and Statistics for Engineers");
        addItem(secCore, "Engineering Mathematics");
        addItem(secCore, "Microprocessor and Microcontroller Systems");

        setTitle(secElec, "วิชาเอก");
        addItem(secElec, "Robotics & Automation");
        addItem(secElec, "Advanced Software Engineering");
        addItem(secElec, "Cybersecurity");

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.content);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.content) {
                return true;
            } else if (id == R.id.game) {
                startActivity(new Intent(this, ChallengeActivity.class));
                return true;
            } else if (id == R.id.others) {
                startActivity(new Intent(this, OthersActivity.class));
                return true;
            }

            return false;
        });
    }


    private void setTitle(View section, String title) {
        TextView tv = section.findViewById(R.id.tvSectionTitle);
        tv.setText(title);
    }

    private void addItem(View section, String text) {
        LinearLayout ll = section.findViewById(R.id.llContent);
        View row = getLayoutInflater().inflate(R.layout.row_bullet, ll, false);
        ((TextView) row.findViewById(R.id.tvItem)).setText(text);
        ll.addView(row);
    }

}
