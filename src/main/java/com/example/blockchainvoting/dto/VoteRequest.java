package com.example.blockchainvoting.dto;

public class VoteRequest {
    private String voterId;
    private String candidate;

    public VoteRequest() {}

    public VoteRequest(String voterId, String candidate) {
        this.voterId = voterId;
        this.candidate = candidate;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }
} 