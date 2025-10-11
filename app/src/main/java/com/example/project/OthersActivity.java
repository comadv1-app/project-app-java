package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OthersActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView auto;
    private RecyclerView rv;
    private DomainAdapter adapter;

    // panel detail
    private MaterialCardView cardDetail;
    private TextView tvDTitle, tvDDesc, tvDBullets;
    private MaterialButton btnMore;

    private final List<Domain> domains = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_others);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, 0);
            return insets;
        });

        bindViews();
        buildData();
        setupDropdown();
        setupList();
        setupBottomNav();
    }

    private void bindViews() {
        auto = findViewById(R.id.autoCategory);
        rv = findViewById(R.id.rvDomains);

        cardDetail = findViewById(R.id.cardDetail);
        tvDTitle = findViewById(R.id.tvDetailTitle);
        tvDDesc  = findViewById(R.id.tvDetailDesc);
        tvDBullets = findViewById(R.id.tvDetailBullets);
        btnMore = findViewById(R.id.btnMore);

        btnMore.setOnClickListener(v -> {
            // ตอนนี้ทำเป็นตัวอย่าง: กลับไปหน้า Content หรือเปิดลิงก์ก็ได้
            // คุณจะเปลี่ยนเป็นเปิดหน้า detail จริงในอนาคต
            // startActivity(new Intent(this, SomeDetailActivity.class));
        });
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.others);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0,0); finish(); return true;
            } else if (id == R.id.content) {
                startActivity(new Intent(this, ContentActivity.class));
                overridePendingTransition(0,0); finish(); return true;
            } else if (id == R.id.game) {
                startActivity(new Intent(this, ChallengeActivity.class));
                overridePendingTransition(0,0); finish(); return true;
            } else if (id == R.id.others) {
                return true;
            }
            return false;
        });
    }

    private void buildData() {
        domains.clear();
        domains.add(new Domain(
                "Computer Science",
                "พื้นฐานวิทยาการคอมพิวเตอร์ ครอบคลุมอัลกอริทึม โครงสร้างข้อมูล ทฤษฎีคอมพิวเตอร์",
                Arrays.asList("Software Engineer", "Algorithm Engineer", "Research Engineer")
        ));
        domains.add(new Domain(
                "Software Engineering",
                "การพัฒนาซอฟต์แวร์ให้มีคุณภาพ กระบวนการ SE, ออกแบบสถาปัตยกรรม, ทดสอบ, CI/CD",
                Arrays.asList("Full-stack Developer", "Software Architect", "DevOps Engineer")
        ));
        domains.add(new Domain(
                "Information Technology",
                "ระบบสารสนเทศ การดูแลโครงสร้างพื้นฐาน ระบบเครือข่าย เซิร์ฟเวอร์ และบริการไอที",
                Arrays.asList("System Administrator", "IT Support", "Cloud Engineer")
        ));
        domains.add(new Domain(
                "Data Science",
                "วิเคราะห์ข้อมูล สถิติ การเรียนรู้ของเครื่อง สร้างโมเดลและนำเสนออินไซต์",
                Arrays.asList("Data Scientist", "Data Analyst", "ML Engineer")
        ));
        domains.add(new Domain(
                "Artificial Intelligence",
                "ปัญญาประดิษฐ์และ Deep Learning สำหรับภาพ ภาษา และสัญญาณ",
                Arrays.asList("AI Engineer", "Computer Vision Engineer", "NLP Engineer")
        ));
        domains.add(new Domain(
                "Cybersecurity",
                "ปกป้องระบบและข้อมูล ตรวจจับ/ตอบสนองภัยคุกคาม ทดสอบเจาะระบบ",
                Arrays.asList("Security Analyst", "Pentester", "SOC Engineer")
        ));
        domains.add(new Domain(
                "Computer Graphics & Game Development",
                "กราฟิกคอมพิวเตอร์ เอนจินเกม ฟิสิกส์สำหรับเกม และการเรนเดอร์แบบเรียลไทม์",
                Arrays.asList("Game Programmer", "Technical Artist", "Graphics Engineer")
        ));
    }

    private void setupDropdown() {
        String[] opts = new String[]{
                "Software", "Security", "Data", "AI", "Graphics", "Infra/Network"
        };
        auto.setSimpleItems(opts);
        auto.setText("", false);

        auto.setOnItemClickListener((parent, view, position, id) -> {
            Domain picked = guessFirstByCategory(opts[position]);
            if (picked != null) showDetail(picked);
        });
    }

    private Domain guessFirstByCategory(String cat) {
        cat = cat.toLowerCase();
        for (Domain d : domains) {
            if (cat.contains("software") && d.title.contains("Software")) return d;
            if (cat.contains("security") && d.title.contains("Cybersecurity")) return d;
            if (cat.contains("data") && d.title.contains("Data Science")) return d;
            if (cat.equals("ai") && d.title.contains("Artificial Intelligence")) return d;
            if (cat.contains("graphics") && d.title.contains("Graphics")) return d;
            if (cat.contains("infra") || cat.contains("network")) {
                if (d.title.contains("Information Technology")) return d;
            }
        }
        return domains.isEmpty()? null : domains.get(0);
    }

    private void setupList() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DomainAdapter(domains, this::showDetail);
        rv.setAdapter(adapter);
    }

    private void showDetail(Domain d) {
        cardDetail.setVisibility(View.VISIBLE);
        tvDTitle.setText(d.title);
        tvDDesc.setText(d.desc);

        StringBuilder bullets = new StringBuilder("สายงานที่รองรับ\n");
        for (String b : d.roles) {
            bullets.append("• ").append(b).append("\n");
        }
        tvDBullets.setText(bullets.toString());
    }

    static class Domain {
        final String title;
        final String desc;
        final List<String> roles;

        Domain(String t, String d, List<String> r) {
            title = t; desc = d; roles = r;
        }
    }

    interface OnDomainClick { void onClick(Domain d); }

    static class DomainAdapter extends RecyclerView.Adapter<DomainAdapter.VH> {
        private final List<Domain> data;
        private final OnDomainClick cb;

        DomainAdapter(List<Domain> data, OnDomainClick cb) {
            this.data = data; this.cb = cb;
        }

        @NonNull @Override public VH onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View v = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_domain_row, parent, false);
            return new VH(v);
        }

        @Override public void onBindViewHolder(@NonNull VH h, int position) {
            Domain d = data.get(position);
            h.title.setText(d.title);
            h.itemView.setOnClickListener(v -> cb.onClick(d));
        }

        @Override public int getItemCount() { return data.size(); }

        static class VH extends RecyclerView.ViewHolder {
            final TextView title; final ImageView icon;
            VH(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tvRowTitle);
                icon  = itemView.findViewById(R.id.ivChevron);
            }
        }
    }
}
