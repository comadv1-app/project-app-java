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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CodeTimedActivity extends AppCompatActivity {
    private static final int EASY_COUNT = 3, MED_COUNT = 4, HARD_COUNT = 3;
    private static final int EASY_TIME = 60, MED_TIME = 45, HARD_TIME = 30;

    private TextView tvTitle, tvMode, tvQIndex, tvTime, tvScore, tvLevel;
    private MaterialCardView cardTask, cardSummary;
    private TextView tvSpec, tvTemplateLabel, tvOutputLabel, tvSummary;
    private EditText etCode, etOutput;
    private MaterialButton btnRun, btnNext, btnBack;

    private final List<CodeProblem> pool = new ArrayList<>();
    private final List<Item> timeline = new ArrayList<>();
    private int cur = 0;
    private int score = 0;
    private CountDownTimer timer;
    private long millisLeft = 0;

    private static class Item {
        final CodeProblem p; final String level; final int timeSec;
        Item(CodeProblem p, String level, int timeSec){ this.p=p; this.level=level; this.timeSec=timeSec; }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code_timed);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, 0); return insets;
        });

        bindViews();
        buildPool();
        buildTimeline();

        startGame();
    }

    private void bindViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvMode = findViewById(R.id.tvMode);
        tvQIndex = findViewById(R.id.tvQIndex);
        tvTime = findViewById(R.id.tvTime);
        tvScore = findViewById(R.id.tvScore);
        tvLevel = findViewById(R.id.tvLevel);

        cardTask = findViewById(R.id.cardTask);
        cardSummary = findViewById(R.id.cardSummary);

        tvSpec = findViewById(R.id.tvSpec);
        tvTemplateLabel = findViewById(R.id.tvTemplateLabel);
        tvOutputLabel = findViewById(R.id.tvOutputLabel);
        tvSummary = findViewById(R.id.tvSummary);

        etCode = findViewById(R.id.etCode);
        etOutput = findViewById(R.id.etOutput);

        btnRun = findViewById(R.id.btnRun);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnRun.setOnClickListener(v -> onRun());
        btnNext.setOnClickListener(v -> gotoNext());
    }

    private void startGame() {
        score = 0;
        cur = 0;
        tvMode.setText("เวลา");
        tvScore.setText(scoreText());
        cardSummary.setVisibility(View.GONE);
        cardTask.setVisibility(View.VISIBLE);
        showCurrent();
    }

    private void showCurrent() {
        if (cur >= timeline.size()) { showSummary(); return; }
        Item it = timeline.get(cur);

        tvTitle.setText("code solving game");
        tvLevel.setText(it.level);
        tvQIndex.setText(String.format(Locale.getDefault(),"ข้อที่ %d / %d", cur+1, timeline.size()));
        tvScore.setText(scoreText());

        tvSpec.setText(it.p.renderSpec());
        tvTemplateLabel.setText("Code Template");
        etCode.setText(it.p.template);
        etOutput.setText("");
        tvOutputLabel.setText("Output");

        btnNext.setEnabled(false);
        etCode.setEnabled(true);
        btnRun.setEnabled(true);

        startTimer(it.timeSec);
    }

    private void onRun() {
        if (cur >= timeline.size()) return;
        Item it = timeline.get(cur);

        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) return;

        btnRun.setEnabled(false);

        CodeProblem.RunResult r = it.p.judge(code);

        StringBuilder out = new StringBuilder();
        for (int i=0;i<r.cases.size();i++){
            CodeProblem.CaseResult cr = r.cases.get(i);
            out.append(String.format(Locale.getDefault(),
                    "#%d\nInput:\n%s\nExpected:\n%s\nYour output:\n%s\nResult: %s\n\n",
                    i+1, cr.inputShown, cr.expectedShown, cr.userShown, cr.passed? "PASS" : "FAIL"));
        }
        out.append(String.format(Locale.getDefault(),"สรุป: %d/%d เคส", r.passedCount, r.cases.size()));
        etOutput.setText(out.toString());

        if (r.allPassed) {
            score++;
            tvScore.setText(scoreText());
            stopTimer();
            etCode.setEnabled(false);
            btnNext.setEnabled(true);
        } else {
            btnRun.setEnabled(true);
        }
    }

    private void gotoNext() {
        stopTimer();
        cur++;
        showCurrent();
    }

    private void startTimer(int seconds) {
        stopTimer();
        tvTime.setText(String.format(Locale.getDefault(),"เวลา: %ds", seconds));
        timer = new CountDownTimer(seconds*1000L, 200) {
            @Override public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                int s = (int)Math.ceil(millisLeft/1000.0);
                tvTime.setText(String.format(Locale.getDefault(),"เวลา: %ds", s));
            }
            @Override public void onFinish() {
                millisLeft = 0;
                tvTime.setText("เวลา: 0s");
                etCode.setEnabled(false);
                btnRun.setEnabled(false);
                btnNext.setEnabled(true);
            }
        }.start();
    }

    private void stopTimer() {
        if (timer != null) { timer.cancel(); timer = null; }
    }

    private void showSummary() {
        stopTimer();
        cardTask.setVisibility(View.GONE);
        cardSummary.setVisibility(View.VISIBLE);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.getDefault(),"คะแนน %d/%d\n\n", score, timeline.size()));
        sb.append("Easy\t\t" ).append(EASY_COUNT).append("\n");
        sb.append("Medium\t").append(MED_COUNT).append("\n");
        sb.append("Hard\t\t").append(HARD_COUNT).append("\n\n");
        sb.append("เวลา/ข้อ\n");
        sb.append("Easy\t\t").append(EASY_TIME/60).append(" นาที\n");
        sb.append("Medium\t").append(MED_TIME/60).append(" นาที\n");
        sb.append("Hard\t\t").append(HARD_TIME/60).append(" นาที\n");
        tvSummary.setText(sb.toString());
    }

    private String scoreText(){ return String.format(Locale.getDefault(),"คะแนน: %d", score); }
    private void buildPool() {
        pool.clear();

        // Python
        pool.add(new CodeProblem(
                "ให้เขียนโปรแกรมรับค่าจำนวนเต็ม 1 ค่า แล้วตรวจสอบว่าเป็นเลขคู่หรือเลขคี่ (Python)",
                new String[]{"ถ้าเป็นเลขคู่ให้พิมพ์ \"Even\"", "ถ้าเป็นเลขคี่ให้พิมพ์ \"Odd\""},
                "n = int(input())\n# เขียนโค้ดของคุณที่นี่\n",
                "n = int(input())\nif n % 2 == 0:\n    print(\"Even\")\nelse:\n    print(\"Odd\")\n"
        ).addCase("7","Odd").addCase("10","Even")
                .setHeuristicKeywords(new String[]{"input","print"}));

        // Java
        pool.add(new CodeProblem(
                "Java: รับค่า N แล้วคำนวณผลรวม 1..N",
                new String[]{"อินพุต: จำนวนเต็ม N","เอาต์พุต: ผลรวม 1..N"},
                "import java.util.*;\npublic class Main{\n" +
                        "  public static void main(String[] args){\n" +
                        "    Scanner sc = new Scanner(System.in);\n" +
                        "    int n = sc.nextInt();\n" +
                        "    // เขียนโค้ดของคุณที่นี่\n" +
                        "  }\n}\n",
                "import java.util.*;\npublic class Main{\n" +
                        "  public static void main(String[] args){\n" +
                        "    Scanner sc = new Scanner(System.in);\n" +
                        "    int n = sc.nextInt(); long s=0;\n" +
                        "    for(int i=1;i<=n;i++) s+=i;\n" +
                        "    System.out.println(s);\n" +
                        "  }\n}\n"
        ).addCase("3","6").addCase("10","55")
                .setHeuristicKeywords(new String[]{"Scanner","System.out"}));

        // C
        pool.add(new CodeProblem(
                "C: รับค่า a b c (คั่นด้วยช่องว่าง) แล้วพิมพ์ค่ามากที่สุด",
                new String[]{"อินพุต: a b c (int)","เอาต์พุต: ค่ามากที่สุด"},
                "#include <stdio.h>\nint main(){\n" +
                        "  int a,b,c; scanf(\"%d %d %d\", &a,&b,&c);\n" +
                        "  // เขียนโค้ดของคุณที่นี่\n" +
                        "  return 0;\n}\n",
                "#include <stdio.h>\nint main(){\n" +
                        "  int a,b,c; scanf(\"%d %d %d\", &a,&b,&c);\n" +
                        "  int m=a; if(b>m) m=b; if(c>m) m=c;\n" +
                        "  printf(\"%d\\n\", m); return 0;\n}\n"
        ).addCase("2 7 5","7").addCase("9 3 9","9")
                .setHeuristicKeywords(new String[]{"scanf","printf"}));
    }

    private void buildTimeline() {
        timeline.clear();
        List<CodeProblem> copy = new ArrayList<>(pool);
        Collections.shuffle(copy);

        addItems(copy, EASY_COUNT, "Easy", EASY_TIME);
        addItems(copy, MED_COUNT, "Medium", MED_TIME);
        addItems(copy, HARD_COUNT, "Hard", HARD_TIME);
    }

    private void addItems(List<CodeProblem> copy, int count, String level, int timeSec) {
        int idx = 0;
        for (int i=0;i<count;i++){
            if (copy.isEmpty()) break;
            CodeProblem p = copy.get(idx);
            timeline.add(new Item(p, level, timeSec));
            idx = (idx+1) % copy.size();
        }
    }

    @Override protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }
}
