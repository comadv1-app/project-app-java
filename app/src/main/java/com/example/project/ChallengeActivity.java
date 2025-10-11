package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class ChallengeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_challenge);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, 0);
            return insets;
        });

        setupTimeCard(findViewById(R.id.cardTime));
        setupCodeCard(findViewById(R.id.cardCode));
        setupBinaryCard(findViewById(R.id.cardBinary));

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.game);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            if (id == R.id.content) {
                startActivity(new Intent(this, ContentActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            if (id == R.id.game) return true;
            if (id == R.id.others) {
                startActivity(new Intent(this, OthersActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupTimeCard(View card) {
        UiCard ui = new UiCard(card);
        ui.setTitle("Time challenge game");
        ui.setDesc("ตอบคำถาม 20 ข้อ ข้อละ 10 วินาที ตอบเร็วได้ “พกเวลา” ไปข้อต่อไป (สูงสุด 30 วินาที/ข้อ)");

        ui.useInfoDropdown();
        ui.setInfo(
                "• เริ่มที่ 10 วินาทีต่อข้อ\n" +
                        "• ตอบเร็วกว่ากำหนด จะพกเวลาสะสมไปข้อถัดไป (สูงสุด 30 วิ/ข้อ)\n" +
                        "• กดข้ามได้ แต่เสียเวลา"
        );

        ui.setAltButtonVisible(false);
        ui.onStart(() -> {
            Intent intent = new Intent(this, TimeGameActivity.class);
            startActivity(intent);
        });
    }

    private void setupCodeCard(View card) {
        UiCard ui = new UiCard(card);
        ui.setTitle("code solving game");
        ui.setDesc("โอกาสเขียนและแก้โค้ด Java, Python, C พร้อมทดสอบอัตโนมัติ มีทั้งง่ายและยาก โหมดฝึก (5 ข้อ) และโหมดจับเวลา (10 ข้อ)");

        ui.useInfoDropdown();
        ui.setInfo(
                "• เริ่มแบบฝึก: แก้ที่ผิดให้ผ่าน → ~20 ทดสอบย่อย\n" +
                        "• ผ่านครบปลดล็อกโจทย์ยากขึ้น\n" +
                        "• โหมดจับเวลา: 1 นาที/ข้อ รวม 10 ข้อ"
        );

        ui.setAltButtonVisible(true);
        ui.setMainText("ฝึก");
        ui.setAltText("จับเวลา");
        ui.onStart(() ->
                Toast.makeText(this, "เริ่มโหมดฝึก (Code solving)", Toast.LENGTH_SHORT).show()
        );
        ui.onAlt(() ->
                Toast.makeText(this, "เริ่มโหมดจับเวลา (Code solving)", Toast.LENGTH_SHORT).show()
        );
    }

    private void setupBinaryCard(View card) {
        UiCard ui = new UiCard(card);
        ui.setTitle("Binary");
        ui.setDesc("แข่งแปลงเลขฐาน 2/10/16 (ตอบเร็วได้โบนัสเวลา) — ฝึกพื้นฐาน bit & base ที่ใช้จริงใน CPE");

        ui.useInfoDropdown();
        ui.setInfo(
                "• เริ่มจากฐานสิบ → ฐานสอง/ฐานสิบหก\n" +
                        "• ตอบเร็ว พกเวลาเข้าโจทย์ถัดไป (สูงสุด 30 วิ/ข้อ)"
        );

        ui.setAltButtonVisible(false);
        ui.onStart(() ->
                Toast.makeText(this, "เริ่ม Binary", Toast.LENGTH_SHORT).show()
        );
    }

    static class UiCard {
        private final View root, infoBox, btnStart, btnAlt;
        private final MaterialAutoCompleteTextView auto;
        private final android.widget.TextView tvTitle, tvDesc, tvInfo;

        UiCard(View root) {
            this.root = root;
            tvTitle = root.findViewById(R.id.tvGameTitle);
            tvDesc  = root.findViewById(R.id.tvGameDesc);
            tvInfo  = root.findViewById(R.id.tvInfo);
            infoBox = root.findViewById(R.id.infoBox);
            auto    = root.findViewById(R.id.autoLevel);
            btnStart= root.findViewById(R.id.btnStart);
            btnAlt  = root.findViewById(R.id.btnAlt);
        }

        void setTitle(String t){ tvTitle.setText(t); }
        void setDesc(String d){ tvDesc.setText(d); }

        void useInfoDropdown() {
            String[] one = new String[]{"วิธีเล่น"};
            auto.setSimpleItems(one);
            auto.setText(one[0], false);
            auto.setOnItemClickListener((p,v,pos,id) -> {
                boolean show = infoBox.getVisibility() != View.VISIBLE;
                showInfo(show);
                auto.clearFocus();
            });
        }

        void showInfo(boolean show){ infoBox.setVisibility(show ? View.VISIBLE : View.GONE); }
        void setInfo(String s){ tvInfo.setText(s); }

        void setMainText(String s){ ((android.widget.TextView)btnStart).setText(s); }
        void onStart(Runnable r){ btnStart.setOnClickListener(v-> r.run()); }

        void setAltButtonVisible(boolean vis){ btnAlt.setVisibility(vis ? View.VISIBLE : View.GONE); }
        void setAltText(String t){ ((android.widget.TextView)btnAlt).setText(t); }
        void onAlt(Runnable r){ btnAlt.setOnClickListener(v -> r.run()); }
    }
}
