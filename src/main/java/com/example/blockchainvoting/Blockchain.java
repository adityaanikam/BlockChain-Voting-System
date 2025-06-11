package com.example.blockchainvoting;

import java.util.ArrayList;

public class Blockchain {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static int difficulty = 5;

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // Check if the current block's hash is correct
<<<<<<< HEAD
            String recalculatedHash = currentBlock.calculateHash();
            if (!currentBlock.hash.equals(recalculatedHash)) {
                System.err.println("⛔ Hash mismatch at block index " + i + ": stored=" + currentBlock.hash + " recalculated=" + recalculatedHash);
=======
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
>>>>>>> 8182c09528479f3a460c27028fa1c3125e8e78aa
                return false;
            }

            // Check if the previous block's hash is correctly referenced
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
<<<<<<< HEAD
                System.err.println("⛔ Previous hash mismatch at block index " + i + ": storedPrevHash=" + currentBlock.previousHash + " actualPrevHash=" + previousBlock.hash);
=======
>>>>>>> 8182c09528479f3a460c27028fa1c3125e8e78aa
                return false;
            }
        }
        return true;
    }
}

