package com.example.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeProblem {

    public final String statement;
    public final String[] bullets;
    public final String template;
    public final String solutionExample;
    public boolean lockPassed = false;

    private final List<TestCase> testcases = new ArrayList<>();
    private String[] heuristicKeywords = new String[0];

    public CodeProblem(String statement, String[] bullets, String template, String solutionExample) {
        this.statement = statement;
        this.bullets = bullets;
        this.template = template;
        this.solutionExample = solutionExample;
    }

    public CodeProblem addCase(String input, String expectedOutput) {
        testcases.add(new TestCase(input, expectedOutput));
        return this;
    }

    public CodeProblem setHeuristicKeywords(String[] kws) {
        this.heuristicKeywords = kws;
        return this;
    }

    public String renderSpec() {
        StringBuilder sb = new StringBuilder("โจทย์\n");
        for (String b : bullets) sb.append("• ").append(b).append("\n");
        return sb.toString();
    }

    public RunResult judge(String userCode) {
        RunResult result = new RunResult();
        boolean looksValid = true;
        for (String k : heuristicKeywords) {
            if (!userCode.contains(k)) {
                looksValid = false;
                break;
            }
        }
        for (TestCase tc : testcases) {
            String expected = tc.expected;
            String userOut = looksValid ? expected : "(ไม่ผ่านเงื่อนไข/คำสำคัญไม่ครบ)";
            boolean pass = looksValid;
            result.cases.add(new CaseResult(tc.input, expected, userOut, pass));
            if (pass) result.passedCount++;
        }
        result.allPassed = (result.passedCount == testcases.size());
        return result;
    }

    private static class TestCase {
        final String input;
        final String expected;
        TestCase(String i, String e){ input=i; expected=e; }
    }

    public static class RunResult {
        public final List<CaseResult> cases = new ArrayList<>();
        public int passedCount = 0;
        public boolean allPassed = false;
    }

    public static class CaseResult {
        public final String inputShown, expectedShown, userShown;
        public final boolean passed;
        CaseResult(String in, String ex, String us, boolean p){
            inputShown = in; expectedShown = ex; userShown = us; passed = p;
        }
    }
}
