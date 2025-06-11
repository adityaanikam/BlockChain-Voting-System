package com.example.blockchainvoting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockchainStatus {
    @JsonProperty("isValid")
    private boolean valid;
    private int blockCount;
    private String message;

    public BlockchainStatus() {}

    public BlockchainStatus(boolean isValid, int blockCount, String message) {
        this.valid = isValid;
        this.blockCount = blockCount;
        this.message = message;
    }

    @JsonProperty("isValid")
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} 