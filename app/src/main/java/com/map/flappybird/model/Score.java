package com.map.flappybird.model;

public class Score {
    private final String username;
    private final int score;
    private final String createdAt;
    private int userId;

    public Score(String username, int score, String createdAt) {
        this.username = username;
        this.score = score;
        this.createdAt = createdAt;
    }

    public Score(String username, int score, String createdAt, int userId) {
        this.username = username;
        this.score = score;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getUserId() {
        return userId;
    }
}

