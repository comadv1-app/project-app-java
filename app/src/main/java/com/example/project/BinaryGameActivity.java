package com.example.project;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BinaryGameActivity extends AppCompatActivity {


    private static final int TOTAL_QUESTIONS = 20;
    private static final int TIME_PER_QUESTION_SEC = 60;

    // Views
    private TextView tvTitle, tvQIdx, tvTime, tvScore, tvQuestion;
    private EditText etAnswer;
    private MaterialButton btnSubmit, btnBack;
    private MaterialCardView cardPlay, cardSummary;
    private TextView tvSummary;

    // State
    private final List<Bq> questions = new ArrayList<>();
    private int index = 0;
    private int score = 0;
    private CountDownTimer timer;
    private long gameStartMs = 0L;
    private long gameEndMs = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_binary_game);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, 0);
            return insets;
        });

        bindViews();
        buildQuestions();
        startGame();
    }

    private void bindViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvQIdx = findViewById(R.id.tvQIndex);
        tvTime = findViewById(R.id.tvTime);
        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);

        etAnswer = findViewById(R.id.etAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        cardPlay = findViewById(R.id.cardPlay);
        cardSummary = findViewById(R.id.cardSummary);
        tvSummary = findViewById(R.id.tvSummary);

        btnSubmit.setOnClickListener(v -> onSubmit());
        btnBack.setOnClickListener(v -> finish());
    }

    private void startGame() {
        score = 0;
        index = 0;
        gameStartMs = System.currentTimeMillis();
        gameEndMs = 0L;

        cardSummary.setVisibility(View.GONE);
        cardPlay.setVisibility(View.VISIBLE);
        showQuestion();
    }

    private void showQuestion() {
        if (index >= questions.size()) {
            finishGame();
            return;
        }
        Bq q = questions.get(index);

        tvTitle.setText("Binary");
        tvQIdx.setText(String.format(Locale.getDefault(), "ข้อที่ %d/%d", index + 1, TOTAL_QUESTIONS));
        tvScore.setText(String.format(Locale.getDefault(), "คะแนน: %d", score));
        tvQuestion.setText(q.renderPrompt());

        etAnswer.setText("");
        etAnswer.requestFocus();

        startTimer(TIME_PER_QUESTION_SEC);
    }

    private void onSubmit() {
        if (index >= questions.size()) return;
        stopTimer();
        Bq q = questions.get(index);

        String ans = etAnswer.getText().toString().trim();
        boolean correct = q.check(ans);

        if (correct) score++;

        index++;
        showQuestion();
    }

    private void startTimer(int seconds) {
        stopTimer();
        tvTime.setText(String.format(Locale.getDefault(), "เวลา: %ds", seconds));

        timer = new CountDownTimer(seconds * 1000L, 200) {
            @Override public void onTick(long millisUntilFinished) {
                int s = (int) Math.ceil(millisUntilFinished / 1000.0);
                tvTime.setText(String.format(Locale.getDefault(), "เวลา: %ds", s));
            }
            @Override public void onFinish() {
                tvTime.setText("เวลา: 0s");
                index++;
                showQuestion();
            }
        }.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void finishGame() {
        stopTimer();
        gameEndMs = System.currentTimeMillis();

        long secs = Math.max(0L, (gameEndMs - gameStartMs) / 1000);
        long mm = secs / 60;
        long ss = secs % 60;

        cardPlay.setVisibility(View.GONE);
        cardSummary.setVisibility(View.VISIBLE);

        String sum = String.format(Locale.getDefault(),
                "ทำได้ %d/%d ข้อ\nเวลา: %02d:%02d นาที",
                score, TOTAL_QUESTIONS, mm, ss);
        tvSummary.setText(sum);
    }

    private void buildQuestions() {
        questions.clear();
        Random r = new Random();
        int[][] pairs = new int[][]{
                {2,10},{10,2},{2,16},{16,2},{10,16},{16,10}
        };

        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            int[] p = pairs[r.nextInt(pairs.length)];
            int from = p[0], to = p[1];
            int value = r.nextInt(256) + 1; // 1..256

            String src = toBaseString(value, from);
            questions.add(new Bq(src, from, to));
        }
    }

    private static String toBaseString(int value, int base) {
        String s = Integer.toString(value, base);
        if (base == 16) s = s.toUpperCase(Locale.ROOT);
        return s;
    }

    static class Bq {
        final String src;
        final int fromBase;
        final int toBase;
        final int value10;

        Bq(String src, int from, int to) {
            this.src = src;
            this.fromBase = from;
            this.toBase = to;
            this.value10 = Integer.parseInt(src, from);
        }

        String renderPrompt() {
            return String.format(Locale.getDefault(),
                    "แปลง %s (ฐาน %d) ➜ ฐาน %d",
                    src, fromBase, toBase);
        }

        boolean check(String user) {
            if (TextUtils.isEmpty(user)) return false;
            try {
                int userVal = Integer.parseInt(user.replace(" ", "")
                                .toUpperCase(Locale.ROOT),
                        toBase);
                return userVal == value10;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }
}
