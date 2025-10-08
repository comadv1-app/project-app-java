package com.example.project;

public class MajorTrack {
    public String id;
    public String name;
    public String brief;
    public String category;
    public String skills;
    public String topics;
    public String jobs;

    public MajorTrack(String id, String name, String brief,
                      String category, String skills, String topics, String jobs) {
        this.id = id;
        this.name = name;
        this.brief = brief;
        this.category = category;
        this.skills = skills;
        this.topics = topics;
        this.jobs = jobs;
    }
}
