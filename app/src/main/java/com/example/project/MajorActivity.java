package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MajorActivity extends AppCompatActivity {

    private MajorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_major);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.content) {
                startActivity(new Intent(this, ContentActivity.class));
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

        List<MajorTrack> data = new ArrayList<>();
        data.add(new MajorTrack("RA","Robotics & Automation",
                "ศึกษาการออกแบบ พัฒนา และควบคุมระบบหุ่นยนต์และระบบอัตโนมัติ (เช่น ROS, PLC, Motor driver)",
                "Hardware",
                "\nCoding ★★ \nHardware ★★★ \nMathematics ★★",
                "\nROS\nPLC\nControl Systems\nSensors\nActuators",
                "\nAutomation Engineer\nRobotics Engineer\nEmbedded Dev"));
        data.add(new MajorTrack("ASE","Advanced Software Engineering",
                "พัฒนาซอฟต์แวร์ระดับองค์กร ออกแบบสถาปัตยกรรม ทดสอบอัตโนมัติ CI/CD โค้ดคุณภาพ",
                "Software",
                "\nCoding ★★★ \nHardware ★ \nMathematics ★★",
                "\nDesign Patterns\nClean Architecture\nTesting\nCI/CD\nAPI",
                "\nFull-stack Dev\nSoftware Architect\nDevOps Engineer"));
        data.add(new MajorTrack("CYB","Cybersecurity",
                "ปกป้องข้อมูลและระบบไอที ตรวจจับ/ป้องกันช่องโหว่ เฝ้าระวังภัยคุกคาม",
                "Security",
                "\nCoding ★★ \nHardware ★ \nMathematics ★★★",
                "\nNetwork Security\nPentest\nForensics\nIncident Response",
                "\nSecurity Analyst\nPentester\nThreat Hunter"));

        RecyclerView rv = findViewById(R.id.rvTracks);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MajorAdapter(data, trackId -> {
            Intent i = new Intent(MajorActivity.this, QuizActivity.class);
            i.putExtra("start_category", "major");
            i.putExtra("major_track", trackId);
            startActivity(i);
        });
        rv.setAdapter(adapter);

        MaterialAutoCompleteTextView auto = findViewById(R.id.autoCategory);
        String[] options = new String[]{"ทั้งหมด","Hardware","Software","Security","AI and Automation"};
        auto.setSimpleItems(options);
        auto.setText("ทั้งหมด", false);
        auto.setOnItemClickListener((p, v, pos, id) -> adapter.setCategoryFilter(options[pos]));
    }
}