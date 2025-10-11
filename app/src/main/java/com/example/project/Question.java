package com.example.project;

public class Question {
    public final String text;
    public final String[] options;
    public final int answerIndex;

    public Question(String text, String[] options, int answerIndex) {
        this.text = text;
        this.options = options;
        this.answerIndex = answerIndex;
    }
}
