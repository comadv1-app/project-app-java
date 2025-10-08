package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private View panelCategory, panelQuiz;
    private String selectedCategory = null;

    private TextView tvQuestion, tvResult;
    private RadioGroup rgChoices;
    private RadioButton c1, c2, c3, c4;
    private Button btnPrev, btnNext;

    private int index = 0, score = 0;
    private final List<Question> questions = new ArrayList<>();
    private final Map<String, Integer> scoreByTrack = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        panelCategory = findViewById(R.id.panelCategory);
        panelQuiz     = findViewById(R.id.panelQuiz);

        View btnBasic  = findViewById(R.id.btnBasic);
        View btnMajor  = findViewById(R.id.btnMajor);
        View btnOthers = findViewById(R.id.btnOthers);
        Button btnStart = findViewById(R.id.btnStart);

        View.OnClickListener choose = v -> {
            if (v.getId() == R.id.btnBasic)  selectedCategory = "basic";
            if (v.getId() == R.id.btnMajor)  selectedCategory = "major";
            if (v.getId() == R.id.btnOthers) selectedCategory = "others";
            btnStart.setEnabled(true);

            btnBasic.setBackgroundResource("basic".equals(selectedCategory)  ? R.drawable.btn_blue_solid : R.drawable.btn_blue_outline);
            btnMajor.setBackgroundResource("major".equals(selectedCategory)  ? R.drawable.btn_blue_solid : R.drawable.btn_blue_outline);
            btnOthers.setBackgroundResource("others".equals(selectedCategory) ? R.drawable.btn_blue_solid : R.drawable.btn_blue_outline);
            ((Button) btnBasic ).setTextColor("basic".equals(selectedCategory)  ? 0xFFFFFFFF : 0xFF001A66);
            ((Button) btnMajor ).setTextColor("major".equals(selectedCategory)  ? 0xFFFFFFFF : 0xFF001A66);
            ((Button) btnOthers).setTextColor("others".equals(selectedCategory) ? 0xFFFFFFFF : 0xFF001A66);
        };
        btnBasic.setOnClickListener(choose);
        btnMajor.setOnClickListener(choose);
        btnOthers.setOnClickListener(choose);

        btnStart.setOnClickListener(v -> startQuiz(selectedCategory));

        // quiz widgets
        tvQuestion = findViewById(R.id.tvQuestion);
        tvResult   = findViewById(R.id.tvResult);
        rgChoices  = findViewById(R.id.rgChoices);
        c1 = findViewById(R.id.choice1);
        c2 = findViewById(R.id.choice2);
        c3 = findViewById(R.id.choice3);
        c4 = findViewById(R.id.choice4);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        btnPrev.setOnClickListener(v -> {
            if (index > 0) { index--; showQuestion(); }
        });
        btnNext.setOnClickListener(v -> onNextClicked());

        BottomNavigationView bottom = findViewById(R.id.bottomNavigationView);
        bottom.setSelectedItemId(R.id.game);
        bottom.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home)   { startActivity(new Intent(this, MainActivity.class));   return true; }
            if (id == R.id.content){ startActivity(new Intent(this, ContentActivity.class)); return true; }
            if (id == R.id.game)   { return true; }
            if (id == R.id.others) { startActivity(new Intent(this, OthersActivity.class)); return true; }
            return false;
        });
    }

    // ---------- START QUIZ ----------
    private void startQuiz(String category) {
        questions.clear();
        scoreByTrack.clear();
        score = 0; index = 0;
        tvResult.setText("");
        rgChoices.clearCheck();

        switch (category) {
            case "basic":
                loadBasicQuiz();
                break;

            case "major":
                scoreByTrack.put("Robotics & Automation", 0);
                scoreByTrack.put("Advanced Software Engineering", 0);
                scoreByTrack.put("Cybersecurity", 0);
                loadMajorQuiz();
                break;

            case "others":
                for (String k : new String[]{"Computer Science","Software Engineering","Information Technology","Data Science","Artificial Intelligence","Cybersecurity","Computer Graphics & Game Development"}) scoreByTrack.put(k, 0);
                loadOthersQuiz();
        }

        panelCategory.setVisibility(View.GONE);
        panelQuiz.setVisibility(View.VISIBLE);
        showQuestion();
    }

    private void showQuestion() {
        Question q = questions.get(index);
        tvQuestion.setText((index+1) + "/" + questions.size() + "  " + q.text);
        c1.setText(q.choices[0]);
        c2.setText(q.choices[1]);
        c3.setText(q.choices[2]);
        c4.setText(q.choices[3]);
        rgChoices.clearCheck();

        btnPrev.setEnabled(index > 0);
        btnNext.setText(index == questions.size()-1 ? "ส่งคำตอบ" : "ถัดไป");
    }

    private void onNextClicked() {
        int sel = rgChoices.getCheckedRadioButtonId();
        if (sel == -1) {
            Toast.makeText(this, "กรุณาเลือกคำตอบ", Toast.LENGTH_SHORT).show();
            return;
        }
        int ansIndex = (sel == R.id.choice1) ? 0 :
                (sel == R.id.choice2) ? 1 :
                        (sel == R.id.choice3) ? 2 : 3;

        Question q = questions.get(index);
        boolean correct = (ansIndex == q.correctIndex);

        if ("basic".equals(selectedCategory)) {
            if (correct) score++;
        } else {
            if (correct && q.target != null) {
                scoreByTrack.put(q.target, scoreByTrack.get(q.target) + 1);
            }
        }

        if (index < questions.size()-1) {
            index++;
            showQuestion();
        } else {
            if ("basic".equals(selectedCategory)) {
                int total = questions.size();
                int percent = (int)Math.round(100.0 * score / total);
                String verdict = percent >= 70 ? "เหมาะกับ Computer Engineering"
                        : (percent >= 50 ? "พอไปได้ (เสริมพื้นฐานอีกนิด)" : "ถ้าสู้ก็ไปต่อ หรือพิจารณาสาขาอื่น");
                tvResult.setText("ได้ " + score + " / " + total + " (" + percent + "%)\n" + verdict);
            } else {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String,Integer> e : scoreByTrack.entrySet()) {
                    int correctCnt = e.getValue();
                    sb.append(e.getKey()).append(": ")
                            .append(correctCnt).append("/5 (")
                            .append(correctCnt * 20).append("%)\n");
                }
                tvResult.setText(sb.toString());
            }

            btnNext.setText("เลือกหมวดใหม่");
            btnNext.setOnClickListener(v -> {
                panelQuiz.setVisibility(View.GONE);
                panelCategory.setVisibility(View.VISIBLE);
                findViewById(R.id.btnStart).setEnabled(false);
                selectedCategory = null;
            });
        }
    }

    // Basic
    private void loadBasicQuiz() {
        questions.add(new Question("หน่วยประมวลผลกลางของคอมพิวเตอร์คืออะไร",
                new String[]{"ALU","Cache","CPU","GPU"}, 2));
        questions.add(new Question("RAM จัดเป็นหน่วยความจำแบบใด",
                new String[]{"แบบชั่วคราว/ลบเมื่อปิดเครื่อง","แบบถาวร","แบบนำออกได้เท่านั้น","แบบสำรองเท่านั้น"}, 0));
        questions.add(new Question("โปรโตคอล TCP/IP ใช้สำหรับอะไร",
                new String[]{"เข้ารหัสไฟล์","สื่อสารเครือข่ายอินเทอร์เน็ต","ควบคุมการแสดงผลจอภาพ","จัดการไฟล์ระบบ"}, 1));
        questions.add(new Question("ภาษาใดจัดเป็นระดับต่ำสุดในตัวเลือกนี้",
                new String[]{"Java","Python","C","SQL"}, 2));
        questions.add(new Question("ระบบปฏิบัติการทำหน้าที่หลักใด",
                new String[]{"จัดการทรัพยากรฮาร์ดแวร์/กระบวนการ","แก้ไวยากรณ์ของโปรแกรม","ส่งอีเมล","เขียนเว็บเพจอัตโนมัติ"}, 0));
        questions.add(new Question("ไมโครคอนโทรลเลอร์เหมาะกับงานแบบใด",
                new String[]{"ควบคุมอุปกรณ์/เซนเซอร์ขนาดเล็ก","เรนเดอร์กราฟิก 3D หนักๆ","โฮสต์เว็บขนาดใหญ่","ฝึกโมเดลขนาดใหญ่"}, 0));
        questions.add(new Question("Git ใช้เพื่ออะไร",
                new String[]{"ควบคุมเวอร์ชันซอร์สโค้ด","สร้างไบนารี","เขียนเอกสาร PDF","ทดสอบหน่วยความจำ"}, 0));
        questions.add(new Question("การดีบักควรทำอย่างไรก่อน",
                new String[]{"ทำซ้ำปัญหาให้ได้/ระบุเงื่อนไขเกิด","แก้สุ่มไปก่อน","เปลี่ยนเครื่องคอมพิวเตอร์","ลบโค้ดทั้งหมด"}, 0));
        questions.add(new Question("วงจรไฟฟ้ากระแสตรง หน่วยแรงดันคือ",
                new String[]{"A","V","Ω","Hz"}, 1));
        questions.add(new Question("IoT ประกอบด้วยแนวคิดใด",
                new String[]{"อุปกรณ์เชื่อมต่อเครือข่าย ส่งข้อมูลสู่คลาวด์/แอป","อุปกรณ์ทำงานออฟไลน์เท่านั้น","เครือข่ายเฉพาะในห้องแลป","ใช้ได้เฉพาะคอมพิวเตอร์ตั้งโต๊ะ"}, 0));
    }

    // Major
    private void loadMajorQuiz() {
        // RA
        questions.add(new Question("ส่วนใดมักใช้ควบคุมมอเตอร์ในหุ่นยนต์",
                new String[]{"LDR","H-bridge / Motor driver","Heat sink","Optocoupler"}, 1, "RA"));
        questions.add(new Question("ROS ใช้หลัก ๆ เพื่อ",
                new String[]{"ดีไซน์ UI","สื่อสาร/ประสานงานระหว่าง node ของหุ่นยนต์","เก็บข้อมูล Big Data","ทดสอบเว็บ"}, 1, "RA"));
        questions.add(new Question("เซนเซอร์วัดระยะยอดนิยม",
                new String[]{"DHT11","Ultrasonic / LiDAR","RTC","XBee"}, 1, "RA"));
        questions.add(new Question("คำว่า Kinematics เกี่ยวข้องกับ",
                new String[]{"การเคลื่อนที่/ตำแหน่งแขนกล","ไฟฟ้ากำลัง","สถาปัตยกรรมซอฟต์แวร์","ความถี่สัญญาณนาฬิกา"}, 0, "RA"));
        questions.add(new Question("PLC มักใช้ในงาน",
                new String[]{"คลาวด์คอมพิวติ้ง","เกมมือถือ","อุตสาหกรรม/สายการผลิต","สื่อสังคมออนไลน์"}, 2, "RA"));

        // ASE
        questions.add(new Question("Design Pattern ที่ใช้สร้างออบเจ็กต์อย่างยืดหยุ่น",
                new String[]{"Strategy","Factory","Observer","Adapter"}, 1, "ASE"));
        questions.add(new Question("CI/CD ช่วยเรื่อง",
                new String[]{"ตกแต่ง UI","ทดสอบ/ดีพลอยอัตโนมัติอย่างต่อเนื่อง","เขียนเอกสาร","จัดการบัญชีผู้ใช้"}, 1, "ASE"));
        questions.add(new Question("การทดสอบระดับหน่วยคือ",
                new String[]{"ทดสอบฟังก์ชัน/คลาสเฉพาะจุด","ทดสอบระบบปลายทาง","ทดสอบประสิทธิภาพเครือข่าย","ทดสอบความปลอดภัย"}, 0, "ASE"));
        questions.add(new Question("แนวคิด Clean Architecture เน้น",
                new String[]{"แยกเลเยอร์/ลดการพึ่งพา/ทดสอบง่าย","ใช้ฐานข้อมูลเดียว","ใช้ UI เดียว","เขียนภาษาเดียว"}, 0, "ASE"));
        questions.add(new Question("การทำงานพร้อมกันควรระวัง",
                new String[]{"Race condition / Deadlock","สีปุ่ม","ขนาดโลโก้","ชื่อคลาสยาวไป"}, 0, "ASE"));

        // CYB
        questions.add(new Question("หลักการความปลอดภัย CIA หมายถึง",
                new String[]{"Confidentiality, Integrity, Availability","Control, Inspect, Alert","Code, Inject, Attack","Central, Internal, Admin"}, 0, "CYB"));
        questions.add(new Question("OWASP Top 10 เกี่ยวกับ",
                new String[]{"ความเสี่ยงแอปเว็บที่พบบ่อย","อัลกอริทึม ML","มาตรฐานฮาร์ดแวร์","โปรโตคอลสื่อสาร"}, 0, "CYB"));
        questions.add(new Question("เครื่องมือดักจับแพ็กเก็ต",
                new String[]{"Jenkins","Docker","Wireshark","Postman"}, 2, "CYB"));
        questions.add(new Question("การเข้ารหัสแบบสมมาตร",
                new String[]{"คีย์เดียวสำหรับเข้ารหัส/ถอดรหัส","ใช้คีย์คู่","ไม่ใช้คีย์","ใช้รหัสผ่านอย่างเดียว"}, 0, "CYB"));
        questions.add(new Question("Incident Response ขั้นแรกมักเป็น",
                new String[]{"Identify/Contain","Public Post","ลบระบบทิ้งทันที","เปลี่ยนทีม"}, 0, "CYB"));
    }

    private void loadOthersQuiz() {
        // CS
        questions.add(new Question("โครงสร้างข้อมูลที่เข้าถึงท้าย/ต้นเร็วและเรียงเชื่อมคือ",
                new String[]{"Linked List","Array","HashMap","Stack"}, 0, "CS"));
        questions.add(new Question("Big-O ของ Binary Search",
                new String[]{"O(n)","O(log n)","O(1)","O(n log n)"}, 1, "CS"));
        questions.add(new Question("DFA/NFA อยู่ในหัวข้อ",
                new String[]{"Automata/Formal Language","Cryptography","Graphics","OS"}, 0, "CS"));
        questions.add(new Question("Sorting แบบแบ่งแล้วรวม",
                new String[]{"Merge Sort","Insertion","Selection","Bubble"}, 0, "CS"));
        questions.add(new Question("Stack/Heap เป็นเรื่องของ",
                new String[]{"Program memory model","UI","Network","File system"}, 0, "CS"));

        // SE
        questions.add(new Question("เอกสารเก็บความต้องการคือ",
                new String[]{"SRS","ERD","BOM","SDK"}, 0, "SE"));
        questions.add(new Question("Agile เน้น",
                new String[]{"ส่งมอบเป็นช่วง ๆ / รับฟีดแบ็กเร็ว","สเปคตายตัว","ปล่อยปีละครั้ง","ไม่มีรีวิว"}, 0, "SE"));
        questions.add(new Question("Code Review ช่วย",
                new String[]{"คุณภาพ/ความปลอดภัย/ความรู้ร่วม","เพิ่มบั๊ก","ลดทีมเวิร์ก","ช้าลงเสมอ"}, 0, "SE"));
        questions.add(new Question("Unit test ต่างกับ Integration test ที่",
                new String[]{"ขอบเขตเล็ก/ไม่แตะระบบจริง","ใช้ผู้ใช้จริง","ทดสอบโหลด","ไม่ต้องเขียนโค้ด"}, 0, "SE"));
        questions.add(new Question("Issue Tracking ใช้เครื่องมือ",
                new String[]{"Jira / GitHub Issues","Photoshop","Figma","Slack อย่างเดียว"}, 0, "SE"));

        // IT
        questions.add(new Question("อุปกรณ์แจก IP ภายในเครือข่าย",
                new String[]{"DHCP Server","DNS","NTP","IDS"}, 0, "IT"));
        questions.add(new Question("โดเมน/กลุ่มนโยบายในองค์กร",
                new String[]{"Active Directory","FTP","CDN","VPN Client"}, 0, "IT"));
        questions.add(new Question("2FA ทำเพื่อ",
                new String[]{"เพิ่มความปลอดภัยการยืนยันตัวตน","เร่งอินเทอร์เน็ต","สำรองไฟล์","ปิดโฆษณา"}, 0, "IT"));
        questions.add(new Question("สำรองข้อมูลแบบจุดเวลา",
                new String[]{"Snapshot/Incremental Backup","CRC","NAT","Telnet"}, 0, "IT"));
        questions.add(new Question("ITIL เกี่ยวกับ",
                new String[]{"IT Service Management","Game Design","PCB Layout","Shader"}, 0, "IT"));

        // DS
        questions.add(new Question("ค่ากลางที่ไม่ไวต่อ outlier",
                new String[]{"Mean","Median","Mode","Variance"}, 1, "DS"));
        questions.add(new Question("ไลบรารี DataFrame ใน Python",
                new String[]{"Pandas","TensorFlow","Flask","OpenCV"}, 0, "DS"));
        questions.add(new Question("แผนภาพความหนาแน่นต่อเนื่อง",
                new String[]{"KDE / Density Plot","Bar Chart","Pie","Box"}, 0, "DS"));
        questions.add(new Question("Train/Validation/Test แยกเพื่อ",
                new String[]{"ประเมิน/กัน overfitting","เร็วขึ้นเสมอ","ใช้ GPU","แก้ scalability"}, 0, "DS"));
        questions.add(new Question("A/B Test ต้องมี",
                new String[]{"กลุ่มควบคุม, สุ่ม, เมตริก","แค่กราฟสวย","ทีมตลาดอย่างเดียว","ไม่มีสถิติ"}, 0, "DS"));

        // AI
        questions.add(new Question("Activation ไม่เชิงเส้นนิยมใน NN",
                new String[]{"ReLU / variants","Sigmoid เท่านั้น","Linear เท่านั้น","Softmax เท่านั้น"}, 0, "AI"));
        questions.add(new Question("Backpropagation ใช้เพื่อ",
                new String[]{"ปรับน้ำหนักด้วย gradient","สร้างข้อมูล","แปลงสีภาพ","เข้ารหัสข้อมูล"}, 0, "AI"));
        questions.add(new Question("แก้ Overfitting",
                new String[]{"Regularization/Data Aug/Early Stop","เพิ่มพารามิเตอร์เสมอ","ตัดข้อมูลออกหมด","ลบ validation"}, 0, "AI"));
        questions.add(new Question("Transformer ใช้กลไก",
                new String[]{"Self-Attention","RNN only","Hough Transform","SVM"}, 0, "AI"));
        questions.add(new Question("MLOps เกี่ยวกับ",
                new String[]{"นำโมเดลขึ้นใช้งาน/ติดตาม/เวอร์ชัน","แก้ UI","ออกแบบ PCB","สร้างเกม"}, 0, "AI"));

        // SEC
        questions.add(new Question("แนวคิด least privilege คือ",
                new String[]{"ให้สิทธิเท่าที่จำเป็น","ให้สิทธิสูงสุด","ใช้รหัสผ่านร่วมกัน","ปิดล็อกทั้งหมด"}, 0, "SEC"));
        questions.add(new Question("MFA หมายถึง",
                new String[]{"มากกว่า 2 ปัจจัย","1 ปัจจัย","ไม่มีปัจจัย","ใช้บุญ"}, 0, "SEC"));
        questions.add(new Question("Hash เหมาะเก็บรหัสผ่าน",
                new String[]{"bcrypt/argon2/scrypt","md5","crc32","sha1 ตรง ๆ"}, 0, "SEC"));
        questions.add(new Question("การจู่โจมส่งลิงก์ลวงอีเมล",
                new String[]{"Phishing","DDoS","MITM","Brute force"}, 0, "SEC"));
        questions.add(new Question("SIEM ทำอะไร",
                new String[]{"รวม Log วิเคราะห์เหตุผิดปกติ","ตัดต่อวิดีโอ","ดีไซน์เว็บ","เปิดพอร์ต"}, 0, "SEC"));

        // GAME
        questions.add(new Question("Engine เกมที่นิยม",
                new String[]{"Unity / Unreal","WordPress","Hadoop","Airflow"}, 0, "GAME"));
        questions.add(new Question("Shader ใช้ทำ",
                new String[]{"คำนวณแสง/สี/เอฟเฟกต์บน GPU","จัดการฐานข้อมูล","ทดสอบหน่วย","ควบคุมหุ่นยนต์"}, 0, "GAME"));
        questions.add(new Question("สาเหตุ FPS ต่ำ",
                new String[]{"GPU workload สูง/Draw call มาก","อินเทอร์เน็ตช้าเสมอ","ขาด unit test","ไม่มี favicon"}, 0, "GAME"));
        questions.add(new Question("Collider/Physics ใช้ใน",
                new String[]{"ระบบชน/แรง/การเคลื่อนที่","Rendering pipeline เท่านั้น","CI/CD","SIEM"}, 0, "GAME"));
        questions.add(new Question("แนวทาง Optimize เกม",
                new String[]{"Profile ก่อนค่อยแก้/ลด overdraw/ batching","เปลี่ยนชื่อไฟล์","ลบ README","เพิ่ม texture ใหญ่ขึ้น"}, 0, "GAME"));
    }

    // ---------- data model ----------
    static class Question {
        String text;
        String[] choices;
        int correctIndex;
        String target;
        Question(String t, String[] c, int correct) { this(t, c, correct, null); }
        Question(String t, String[] c, int correct, String target) {
            text = t; choices = c; correctIndex = correct; this.target = target;
        }
    }
}
