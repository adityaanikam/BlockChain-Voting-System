package com.example.blockchainvoting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlockchainVotingApplication {

    public static void main(String[] args) {
        // Check if we're running in headless mode (for Render deployment)
        String headless = System.getProperty("java.awt.headless", "false");
        
        if ("true".equals(headless)) {
            System.out.println("🚀 Starting Blockchain Voting System - Backend Only Mode");
            System.out.println("📡 Server will be available on port 8080");
        } else {
            System.out.println("🚀 Starting Blockchain Voting System - Full Stack Mode");
        }
        
        // Initialize blockchain with genesis block
        initializeBlockchain();
        
        SpringApplication.run(BlockchainVotingApplication.class, args);
    }
    
    private static void initializeBlockchain() {
        if (Blockchain.blockchain.isEmpty()) {
            Blockchain.addBlock(new Block("Genesis Block", "0"));
            System.out.println("✅ Blockchain initialized with Genesis block");
        }
    }
} 