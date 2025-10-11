package com.example.project;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
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

public class CodePracticeActivity extends AppCompatActivity {

    private TextView tvTitle, tvMode, tvSpec, tvTemplateLabel, tvOutputLabel, tvScore;
    private EditText etCode, etOutput;
    private MaterialButton btnRun, btnNext, btnReveal, btnBack;
    private MaterialCardView cardTask, cardSummary;
    private TextView tvSummary;

    private final List<CodeProblem> problems = new ArrayList<>();
    private int index = 0;
    private int passed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code_practice);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(b.left, b.top, b.right, 0);
            return insets;
        });

        bind();
        buildProblems();

        startPractice();
    }

    private void bind() {
        tvTitle = findViewById(R.id.tvTitle);
        tvMode = findViewById(R.id.tvMode);
        tvSpec = findViewById(R.id.tvSpec);
        tvTemplateLabel = findViewById(R.id.tvTemplateLabel);
        tvOutputLabel = findViewById(R.id.tvOutputLabel);
        tvScore = findViewById(R.id.tvScore);

        etCode = findViewById(R.id.etCode);
        etOutput = findViewById(R.id.etOutput);

        btnRun = findViewById(R.id.btnRun);
        btnNext = findViewById(R.id.btnNext);
        btnReveal = findViewById(R.id.btnReveal);
        btnBack = findViewById(R.id.btnBack);

        cardTask = findViewById(R.id.cardTask);
        cardSummary = findViewById(R.id.cardSummary);
        tvSummary = findViewById(R.id.tvSummary);

        btnBack.setOnClickListener(v -> finish());
        btnRun.setOnClickListener(v -> onRun());
        btnNext.setOnClickListener(v -> onNext());
        btnReveal.setOnClickListener(v -> onReveal());
    }

    private void startPractice() {
        index = 0;
        passed = 0;
        cardSummary.setVisibility(View.GONE);
        cardTask.setVisibility(View.VISIBLE);
        tvMode.setText("ฝึก");
        tvScore.setText(scoreText());
        showProblem();
    }

    private void showProblem() {
        if (index >= problems.size()) {
            showSummary();
            return;
        }
        CodeProblem p = problems.get(index);

        tvTitle.setText("code solving game");
        tvSpec.setText(p.renderSpec());
        tvTemplateLabel.setText("Code Template");
        etCode.setText(p.template);
        etOutput.setText("");
        tvOutputLabel.setText("Output");
    }

    private void onRun() {
        CodeProblem p = problems.get(index);
        String userCode = etCode.getText().toString();
        if (TextUtils.isEmpty(userCode)) {
            etOutput.setText("กรุณาใส่โค้ดก่อนครับ");
            return;
        }
        CodeProblem.RunResult result = p.judge(userCode);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < result.cases.size(); i++) {
            CodeProblem.CaseResult cr = result.cases.get(i);
            out.append(String.format(Locale.getDefault(),
                    "#%d\nInput:\n%s\nExpected:\n%s\nYour output:\n%s\nResult: %s\n\n",
                    i + 1, cr.inputShown, cr.expectedShown, cr.userShown, cr.passed ? "PASS" : "FAIL"));
        }
        out.append(String.format(Locale.getDefault(), "สรุป: %d/%d เคส",
                result.passedCount, result.cases.size()));
        etOutput.setText(out.toString());

        if (result.allPassed) {
            passed++;
            tvScore.setText(scoreText());
            p.lockPassed = true;
        }
    }

    private void onNext() {
        index++;
        if (index >= problems.size()) {
            showSummary();
        } else {
            showProblem();
        }
    }

    private void onReveal() {
        CodeProblem p = problems.get(index);
        etCode.setText(p.solutionExample);
    }

    private void showSummary() {
        cardTask.setVisibility(View.GONE);
        cardSummary.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.getDefault(), "คะแนน %d/%d\n\n", passed, problems.size()));
        sb.append("เวลาจำลองสำหรับโหมดฝึก: ไม่มีจับเวลา\n");
        sb.append("แนะนำ: ลองแก้ให้ผ่านทุกเคสก่อน แล้วค่อยไปโหมดจับเวลา");
        tvSummary.setText(sb.toString());
    }

    private String scoreText() {
        return String.format(Locale.getDefault(), "คะแนน: %d/%d", passed, problems.size());
    }

    private void buildProblems() {
        problems.clear();

        // ---------- Python ----------
        problems.add(new CodeProblem(
                "Python: รับจำนวนเต็ม 1 ค่า แล้วพิมพ์ว่าเป็นเลขคู่หรือคี่",
                new String[]{
                        "อินพุต: จำนวนเต็ม 1 ตัว",
                        "ถ้าเป็นเลขคู่ให้พิมพ์ \"Even\"",
                        "ถ้าเป็นเลขคี่ให้พิมพ์ \"Odd\""
                },
                "n = int(input())\n"
                        + "# เขียนโค้ดของคุณที่นี่\n",
                "n = int(input())\n"
                        + "if n % 2 == 0:\n"
                        + "    print(\"Even\")\n"
                        + "else:\n"
                        + "    print(\"Odd\")\n"
        )
                .addCase("7", "Odd")
                .addCase("10", "Even")
                .setHeuristicKeywords(new String[]{ "input", "print" }));

        // ---------- Java ----------
        problems.add(new CodeProblem(
                "Java: รับค่า N แล้วคำนวณผลรวม 1..N",
                new String[]{
                        "อินพุต: จำนวนเต็ม N",
                        "เอาต์พุต: ผลรวมตั้งแต่ 1 ถึง N"
                },
                "import java.util.*;\n"
                        + "public class Main {\n"
                        + "    public static void main(String[] args) {\n"
                        + "        Scanner sc = new Scanner(System.in);\n"
                        + "        int n = sc.nextInt();\n"
                        + "        // เขียนโค้ดของคุณที่นี่\n"
                        + "    }\n"
                        + "}\n",
                "import java.util.*;\n"
                        + "public class Main {\n"
                        + "    public static void main(String[] args) {\n"
                        + "        Scanner sc = new Scanner(System.in);\n"
                        + "        int n = sc.nextInt();\n"
                        + "        long s = 0;\n"
                        + "        for (int i = 1; i <= n; i++) s += i;\n"
                        + "        System.out.println(s);\n"
                        + "    }\n"
                        + "}\n"
        )
                .addCase("3", "6")
                .addCase("10", "55")
                .setHeuristicKeywords(new String[]{ "Scanner", "System.out" }));

        // ---------- C ----------
        problems.add(new CodeProblem(
                "C: รับค่า a b c (คั่นด้วยช่องว่าง) แล้วพิมพ์ค่ามากที่สุด",
                new String[]{
                        "อินพุต: a b c (int)",
                        "เอาต์พุต: ค่ามากที่สุดเพียงตัวเดียว"
                },
                "#include <stdio.h>\n\n"
                        + "int main(){\n"
                        + "    int a,b,c;\n"
                        + "    scanf(\"%d %d %d\", &a, &b, &c);\n"
                        + "    // เขียนโค้ดของคุณที่นี่\n"
                        + "    return 0;\n"
                        + "}\n",
                "#include <stdio.h>\n\n"
                        + "int main(){\n"
                        + "    int a,b,c;\n"
                        + "    scanf(\"%d %d %d\", &a, &b, &c);\n"
                        + "    int m = a;\n"
                        + "    if (b > m) m = b;\n"
                        + "    if (c > m) m = c;\n"
                        + "    printf(\"%d\\n\", m);\n"
                        + "    return 0;\n"
                        + "}\n"
        )
                .addCase("2 7 5", "7")
                .addCase("9 3 9", "9")
                .setHeuristicKeywords(new String[]{ "scanf", "printf" }));
    }

}
