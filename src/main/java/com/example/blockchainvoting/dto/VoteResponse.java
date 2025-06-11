package com.example.blockchainvoting.dto;

public class VoteResponse {
    private String status;
    private String message;
    private String blockHash;

    public VoteResponse() {}

    public VoteResponse(String status, String message, String blockHash) {
        this.status = status;
        this.message = message;
        this.blockHash = blockHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }
} 