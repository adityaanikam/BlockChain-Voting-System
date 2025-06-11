package com.example.blockchainvoting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlockchainTest {

    @BeforeEach
    public void setUp() {
        // Clear blockchain before each test
        Blockchain.blockchain.clear();
        Blockchain.difficulty = 2; // Lower difficulty for faster tests
    }

    @Test
    public void testValidChainWithGenesisOnly() {
        Blockchain.addBlock(new Block("Genesis Block", "0"));
        Assertions.assertTrue(Blockchain.isChainValid(), "Chain with only genesis block should be valid");
    }

    @Test
    public void testValidChainWithOneVote() {
        // Add genesis
        Blockchain.addBlock(new Block("Genesis Block", "0"));
        // Add a vote block
        String prevHash = Blockchain.blockchain.get(Blockchain.blockchain.size() - 1).hash;
        Blockchain.addBlock(new Block("Candidate A", prevHash));

        Assertions.assertTrue(Blockchain.isChainValid(), "Chain with one vote block should be valid");
    }
} 