package com.example.blockchainvoting.dto;

public class RegistrationRequest {
    private String voterId;

    public RegistrationRequest() {}

    public RegistrationRequest(String voterId) {
        this.voterId = voterId;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }
} 