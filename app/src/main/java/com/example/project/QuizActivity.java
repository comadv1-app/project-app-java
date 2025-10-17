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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private enum QuizCategory { BASIC, MAJOR, OTHERS }
    private QuizCategory selectedCategory = null;

    private View panelCategory, panelQuiz;
    private TextView tvQuestion, tvResult;
    private RadioGroup rgChoices;
    private Button btnPrev, btnNext, btnStart;
    private Button btnBasic, btnMajor, btnOthers;
    private List<RadioButton> choiceButtons;

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

        initViews();
        setupCategoryListeners();
        setupQuizNavigators();
        setupBottomNavigation();
    }

    private void initViews() {
        panelCategory = findViewById(R.id.panelCategory);
        panelQuiz     = findViewById(R.id.panelQuiz);

        btnBasic  = findViewById(R.id.btnBasic);
        btnMajor  = findViewById(R.id.btnMajor);
        btnOthers = findViewById(R.id.btnOthers);
        btnStart = findViewById(R.id.btnStart);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvResult   = findViewById(R.id.tvResult);
        rgChoices  = findViewById(R.id.rgChoices);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        choiceButtons = Arrays.asList(
                findViewById(R.id.choice1),
                findViewById(R.id.choice2),
                findViewById(R.id.choice3),
                findViewById(R.id.choice4)
        );
    }

    private void setupCategoryListeners() {
        View.OnClickListener choose = v -> {
            int id = v.getId();
            if (id == R.id.btnBasic) selectedCategory = QuizCategory.BASIC;
            else if (id == R.id.btnMajor) selectedCategory = QuizCategory.MAJOR;
            else if (id == R.id.btnOthers) selectedCategory = QuizCategory.OTHERS;

            updateCategoryButtonsUI();
            btnStart.setEnabled(true);
        };
        btnBasic.setOnClickListener(choose);
        btnMajor.setOnClickListener(choose);
        btnOthers.setOnClickListener(choose);
        btnStart.setOnClickListener(v -> startQuiz());
    }

    private void updateCategoryButtonsUI() {
        updateButtonState(btnBasic, selectedCategory == QuizCategory.BASIC);
        updateButtonState(btnMajor, selectedCategory == QuizCategory.MAJOR);
        updateButtonState(btnOthers, selectedCategory == QuizCategory.OTHERS);
    }

    private void updateButtonState(Button button, boolean isSelected) {
        button.setBackgroundResource(isSelected ? R.drawable.btn_blue_solid : R.drawable.btn_blue_outline);
        button.setTextColor(isSelected ? 0xFFFFFFFF : 0xFF001A66);
    }

    private void setupQuizNavigators() {
        btnPrev.setOnClickListener(v -> {
            if (index > 0) {
                index--;
                showQuestion();
            }
        });
        btnNext.setOnClickListener(v -> onNextClicked());
    }

    private void startQuiz() {
        resetQuizState();

        switch (selectedCategory) {
            case BASIC:
                questions.addAll(QuizRepo.getBasicQuestions());
                break;
            case MAJOR:
                scoreByTrack.put("Robotics & Automation", 0);
                scoreByTrack.put("Advanced Software Engineering", 0);
                scoreByTrack.put("Cybersecurity", 0);
                questions.addAll(QuizRepo.getMajorQuestions());
                break;
            case OTHERS:
                for (String k : new String[]{"Computer Science","Software Engineering","Information Technology","Data Science","Artificial Intelligence","Cybersecurity","Computer Graphics & Game Development"}) {
                    scoreByTrack.put(k, 0);
                }
                questions.addAll(QuizRepo.getOthersQuestions());
        }

        // *** FIX POINT 2 (B): คืนค่า Listener และข้อความของปุ่มสำหรับควิซรอบใหม่ที่นี่ ***
        btnNext.setText("ถัดไป");
        btnNext.setOnClickListener(v -> onNextClicked());

        panelCategory.setVisibility(View.GONE);
        panelQuiz.setVisibility(View.VISIBLE);
        showQuestion();
    }

    private void resetQuizState() {
        questions.clear();
        scoreByTrack.clear();
        score = 0;
        index = 0;
        tvResult.setText("");
        rgChoices.clearCheck();
    }

    private void showQuestion() {
        Question q = questions.get(index);
        tvQuestion.setText(String.format("%d/%d  %s", index + 1, questions.size(), q.text));

        for (int i = 0; i < choiceButtons.size(); i++) {
            choiceButtons.get(i).setText(q.choices[i]);
        }

        rgChoices.clearCheck();
        btnPrev.setEnabled(index > 0);
        btnNext.setText(index == questions.size() - 1 ? "ส่งคำตอบ" : "ถัดไป");
    }

    private void onNextClicked() {
        int selectedRadioButtonId = rgChoices.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "กรุณาเลือกคำตอบ", Toast.LENGTH_SHORT).show();
            return;
        }

        processAnswer(selectedRadioButtonId);

        if (index < questions.size() - 1) {
            moveToNextQuestion();
        } else {
            showResults();
        }
    }

    private void processAnswer(int selectedRadioButtonId) {
        int ansIndex = -1;
        for (int i = 0; i < choiceButtons.size(); i++) {
            if (choiceButtons.get(i).getId() == selectedRadioButtonId) {
                ansIndex = i;
                break;
            }
        }

        Question q = questions.get(index);
        boolean isCorrect = (ansIndex == q.correctIndex);

        if (selectedCategory == QuizCategory.BASIC) {
            if (isCorrect) score++;
        } else {
            if (isCorrect && q.target != null) {
                int currentScore = scoreByTrack.getOrDefault(q.target, 0);
                scoreByTrack.put(q.target, currentScore + 1);
            }
        }
    }

    private void moveToNextQuestion() {
        index++;
        showQuestion();
    }

    private void showResults() {
        if (selectedCategory == QuizCategory.BASIC) {
            int total = questions.size();
            int percent = (int) Math.round(100.0 * score / total);
            String verdict = percent >= 70 ? "เหมาะกับ Computer Engineering"
                    : (percent >= 50 ? "พอไปได้ (เสริมพื้นฐานอีกนิด)" : "ถ้าสู้ก็ไปต่อ หรือพิจารณาสาขาอื่น");
            String resultText = "ได้ " + score + " / " + total + " (" + percent + "%)\n" + verdict;
            tvResult.setText(resultText);
        } else {
            StringBuilder sb = new StringBuilder();
            Map<String, Long> questionCountPerTrack = new HashMap<>();
            for(Question q : questions) {
                if(q.target != null) {
                    questionCountPerTrack.put(q.target, questionCountPerTrack.getOrDefault(q.target, 0L) + 1);
                }
            }

            for (Map.Entry<String, Integer> e : scoreByTrack.entrySet()) {
                int correctCnt = e.getValue();
                long totalQuestions = questionCountPerTrack.getOrDefault(e.getKey(), 1L);
                sb.append(e.getKey()).append(": ")
                        .append(correctCnt).append("/").append(totalQuestions)
                        .append(" (").append((int)(100.0 * correctCnt / totalQuestions)).append("%)\n");
            }
            tvResult.setText(sb.toString());
        }

        btnNext.setText("เลือกหมวดใหม่");
        btnNext.setOnClickListener(v -> resetToCategorySelection());
    }

    private void resetToCategorySelection() {
        panelQuiz.setVisibility(View.GONE);
        panelCategory.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        selectedCategory = null;
        updateCategoryButtonsUI();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottom = findViewById(R.id.bottomNavigationView);
        bottom.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home)   { startActivity(new Intent(this, MainActivity.class));   return true; }
            if (id == R.id.content){ startActivity(new Intent(this, ContentActivity.class)); return true; }
            if (id == R.id.game)   { startActivity(new Intent(this, ChallengeActivity.class)); return true; }
            if (id == R.id.others) { startActivity(new Intent(this, OthersActivity.class)); return true; }
            return false;
        });
    }

    static class Question {
        String text;
        String[] choices;
        int correctIndex;
        String target;
        Question(String t, String[] c, int correct) { this(t, c, correct, null); }
        Question(String t, String[] c, int correct, String target) {
            this.text = t;
            this.choices = c;
            this.correctIndex = correct;
            this.target = target;
        }
    }
}