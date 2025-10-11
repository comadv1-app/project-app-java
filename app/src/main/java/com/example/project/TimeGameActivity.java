package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimeGameActivity extends AppCompatActivity {

    private static final int TOTAL_QUESTIONS = 20;
    private static final int BASE_TIME_SEC = 10;
    private static final int MAX_TIME_PER_QUESTION = 30;

    private TextView tvTitle;
    private TextView tvQIndex, tvTime, tvScore;
    private LinearProgressIndicator progress;
    private TextView tvQuestion;
    private RadioGroup group;
    private RadioButton rb1, rb2, rb3, rb4;
    private MaterialButton btnSubmit, btnSkip;
    private MaterialCardView cardQuestion;

    // result panel
    private MaterialCardView cardResult;
    private TextView tvResult;
    private MaterialButton btnPlayAgain, btnPickGame;

    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    // เวลา
    private CountDownTimer timer;
    private int carriedSeconds = 0;
    private int currentTimeLimit = BASE_TIME_SEC;
    private long millisLeft = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_time_game);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, 0);
            return insets;
        });

        bindViews();
        buildDemoQuestions();

        startGame();
    }

    private void bindViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvQIndex = findViewById(R.id.tvQIndex);
        tvTime = findViewById(R.id.tvTime);
        tvScore = findViewById(R.id.tvScore);
        progress = findViewById(R.id.progress);

        cardQuestion = findViewById(R.id.cardQuestion);
        tvQuestion = findViewById(R.id.tvQuestion);
        group = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSkip = findViewById(R.id.btnSkip);

        cardResult = findViewById(R.id.cardResult);
        tvResult = findViewById(R.id.tvResult);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnPickGame = findViewById(R.id.btnPickGame);

        btnSubmit.setOnClickListener(v -> onSubmit());
        btnSkip.setOnClickListener(v -> onSkip());
        btnPlayAgain.setOnClickListener(v -> startGame());
        btnPickGame.setOnClickListener(v -> {
            finish();
        });
    }

    private void startGame() {
        // reset
        cancelTimer();
        currentIndex = 0;
        score = 0;
        carriedSeconds = 0;
        tvTitle.setText("Time challenge game");
        cardResult.setVisibility(View.GONE);
        cardQuestion.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(true);
        btnSkip.setEnabled(true);

        progress.setMax(TOTAL_QUESTIONS);
        progress.setProgress(0);

        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= TOTAL_QUESTIONS || currentIndex >= questions.size()) {
            finishGame();
            return;
        }
        Question q = questions.get(currentIndex);

        // UI: header
        tvQIndex.setText(String.format(Locale.getDefault(), "ข้อที่ %d / %d", currentIndex + 1, TOTAL_QUESTIONS));
        tvScore.setText(String.format(Locale.getDefault(), "คะแนน: %d", score));

        currentTimeLimit = Math.min(MAX_TIME_PER_QUESTION, BASE_TIME_SEC + carriedSeconds);
        startTimerSec(currentTimeLimit);

        tvQuestion.setText(q.text);
        rb1.setText(q.options[0]);
        rb2.setText(q.options[1]);
        rb3.setText(q.options[2]);
        rb4.setText(q.options[3]);
        group.clearCheck();

        progress.setProgress(currentIndex);
    }

    private void onSubmit() {
        if (currentIndex >= questions.size()) return;

        int checkedId = group.getCheckedRadioButtonId();
        if (checkedId == -1) {
            return;
        }

        cancelTimer();
        int timeSpent = currentTimeLimit - (int) Math.ceil(millisLeft / 1000.0);
        int leftover = Math.max(0, currentTimeLimit - timeSpent);
        carriedSeconds = leftover;

        int pickedIndex = checkedId == R.id.rb1 ? 0 :
                checkedId == R.id.rb2 ? 1 :
                        checkedId == R.id.rb3 ? 2 : 3;

        if (pickedIndex == questions.get(currentIndex).answerIndex) {
            score++;
        }

        currentIndex++;
        progress.setProgress(currentIndex);
        showQuestion();
    }

    private void onSkip() {
        cancelTimer();
        carriedSeconds = 0;
        currentIndex++;
        progress.setProgress(currentIndex);
        showQuestion();
    }

    private void finishGame() {
        cancelTimer();

        cardQuestion.setVisibility(View.GONE);
        cardResult.setVisibility(View.VISIBLE);

        String result = String.format(Locale.getDefault(), "จบเกม\nคะแนน %d/%d", score, TOTAL_QUESTIONS);
        tvResult.setText(result);
    }

    private void startTimerSec(int seconds) {
        cancelTimer();

        long total = seconds * 1000L;
        timer = new CountDownTimer(total, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                int sec = (int) Math.ceil(millisUntilFinished / 1000.0);
                tvTime.setText(String.format(Locale.getDefault(), "เวลา: %ds", sec));
            }

            @Override
            public void onFinish() {
                millisLeft = 0;
                tvTime.setText("เวลา: 0s");
                carriedSeconds = 0;
                currentIndex++;
                progress.setProgress(currentIndex);
                showQuestion();
            }
        }.start();
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void buildDemoQuestions() {
        questions.clear();
        questions.add(new Question(
                "1. โครงสร้างข้อมูลใดใช้หลักการ LIFO (Last In, First Out)?",
                new String[]{"Queue", "Linked List", "Stack", "Tree"},
                2
        ));
        questions.add(new Question(
                "2. ระบบดิจิทัลใดที่ใช้เลขฐานสองเป็นหลักในการทำงาน?",
                new String[]{"Analog System", "Binary System", "Decimal System", "Hexadecimal System"},
                1
        ));
        questions.add(new Question(
                "3. ในวงจรไฟฟ้าแบบอนุกรม ถ้าเพิ่มตัวต้านทานเข้าไปอีก 1 ตัว จะเกิดผลอย่างไร?",
                new String[]{"กระแสไฟเพิ่มขึ้น", "กระแสไฟลดลง", "แรงดันลดลง", "ความต้านทานคงที่"},
                1
        ));
        questions.add(new Question(
                "4. หน่วยประมวลผลกลาง (CPU) ทำหน้าที่ใด?",
                new String[]{"เก็บข้อมูลชั่วคราว", "คำนวณและควบคุมการทำงานของระบบ", "จัดเก็บไฟล์ถาวร", "แสดงผลภาพ"},
                1
        ));
        questions.add(new Question(
                "5. ระบบปฏิบัติการ (Operating System) มีหน้าที่หลักใด?",
                new String[]{"ควบคุมฮาร์ดแวร์และจัดการทรัพยากร", "เขียนโปรแกรม", "ป้องกันไวรัส", "จัดทำเอกสาร"},
                0
        ));
        questions.add(new Question(
                "6. ระบบฐานข้อมูล (Database System) ใช้ภาษาใดในการจัดการข้อมูล?",
                new String[]{"HTML", "SQL", "C#", "Python"},
                1
        ));
        questions.add(new Question(
                "7. ข้อใดไม่ใช่ชั้นของแบบจำลองเครือข่าย OSI?",
                new String[]{"Application", "Session", "Internet", "Physical"},
                2
        ));
        questions.add(new Question(
                "8. ในวิชาสถิติสำหรับวิศวกร ค่าเฉลี่ยเลขคณิต (Arithmetic Mean) หมายถึงอะไร?",
                new String[]{"ผลรวมของข้อมูลทั้งหมดหารด้วยจำนวนข้อมูล", "ค่ากลางที่พบมากที่สุด", "ค่ากลางที่อยู่ตรงกลาง", "ค่ามากสุดและค่าน้อยสุด"},
                0
        ));
        questions.add(new Question(
                "9. สมการเชิงเส้น y = mx + c ใช้ในวิชาใด?",
                new String[]{"วิศวกรรมไฟฟ้า", "คณิตศาสตร์วิศวกรรม", "ระบบปฏิบัติการ", "ฐานข้อมูล"},
                1
        ));
        questions.add(new Question(
                "10. หน่วยประมวลผลย่อยในไมโครคอนโทรลเลอร์ เรียกว่าอะไร?",
                new String[]{"ALU", "GPU", "CU", "IOP"},
                0
        ));
        questions.add(new Question(
                "11. การใช้ Binary Tree ในการค้นหาข้อมูล เรียกว่าอะไร?",
                new String[]{"Linear Search", "Bubble Sort", "Binary Search Tree", "Hashing"},
                2
        ));
        questions.add(new Question(
                "12. การส่งข้อมูลในเครือข่ายที่ส่งทีละบิตแบบมีการซิงโครไนซ์เรียกว่าอะไร?",
                new String[]{"Parallel Transmission", "Serial Transmission", "Packet Transmission", "Bus Transmission"},
                1
        ));
        questions.add(new Question(
                "13. Which component is essential for controlling motors in a robotics system?",
                new String[]{"Motor driver", "Transistor", "Resistor", "Battery"},
                0
        ));
        questions.add(new Question(
                "14. What does PLC stand for in industrial automation?",
                new String[]{"Programmable Logic Controller", "Power Line Communication", "Process Load Circuit", "Parallel Logic Chip"},
                0
        ));
        questions.add(new Question(
                "15. Which of the following is a continuous integration tool?",
                new String[]{"GitHub", "Docker", "Jenkins", "Kubernetes"},
                2
        ));
        questions.add(new Question(
                "16. In software architecture, which principle encourages loose coupling between modules?",
                new String[]{"Encapsulation", "Inheritance", "Polymorphism", "Abstraction"},
                0
        ));
        questions.add(new Question(
                "17. Which algorithm is commonly used for encryption in cybersecurity?",
                new String[]{"AES", "FIFO", "Dijkstra", "SHA-1"},
                0
        ));
        questions.add(new Question(
                "18. What does a firewall primarily do?",
                new String[]{"Encrypt data", "Filter incoming and outgoing network traffic", "Compress files", "Detect malware in software"},
                1
        ));
        questions.add(new Question(
                "19. In machine learning, which activation function is commonly used in neural networks?",
                new String[]{"ReLU", "Sigmoid", "Softmax", "All of the above"},
                3
        ));
        questions.add(new Question(
                "20. Which microcontroller family is popular for educational robotics?",
                new String[]{"8051", "Arduino", "Raspberry Pi", "ESP8266"},
                1
        ));
    }


    @Override
    protected void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }
}
