package com.example.blockchainvoting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blockchainvoting.Block;
import com.example.blockchainvoting.Blockchain;
import com.example.blockchainvoting.VoterRegistration;
import com.example.blockchainvoting.dto.BlockchainStatus;
import com.example.blockchainvoting.dto.RegistrationRequest;
import com.example.blockchainvoting.dto.VoteRequest;
import com.example.blockchainvoting.dto.VoteResponse;

@RestController
@RequestMapping("/api/voting")
@CrossOrigin(origins = "*") // For frontend integration
public class VotingController {

    private final VoterRegistration voterRegistration;

    public VotingController() {
        this.voterRegistration = new VoterRegistration();
        // Register some test voters
        voterRegistration.registerVoter("VOTER123");
        voterRegistration.registerVoter("VOTER456");
        voterRegistration.registerVoter("aditya12f5");
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerVoter(@RequestBody RegistrationRequest request) {
        Map<String, String> response = new HashMap<>();
        
        try {
            boolean success = voterRegistration.registerVoter(request.getVoterId());
            if (success) {
                response.put("status", "success");
                response.put("message", "Voter registered successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Voter already registered");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/cast")
    public ResponseEntity<VoteResponse> castVote(@RequestBody VoteRequest request) {
        try {
            if (!voterRegistration.isVoterRegistered(request.getVoterId())) {
                return ResponseEntity.badRequest()
                    .body(new VoteResponse("error", "Voter not registered", null));
            }

            // Create new block with vote
            String previousHash = Blockchain.blockchain.isEmpty() ? "0" : 
                Blockchain.blockchain.get(Blockchain.blockchain.size() - 1).hash;
            
            Block newBlock = new Block(request.getCandidate(), previousHash);
            Blockchain.addBlock(newBlock);

            return ResponseEntity.ok(
                new VoteResponse("success", "Vote cast successfully", newBlock.hash)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new VoteResponse("error", "Failed to cast vote: " + e.getMessage(), null));
        }
    }

    @GetMapping("/blockchain/status")
    public ResponseEntity<BlockchainStatus> getBlockchainStatus() {
        try {
            boolean isValid = Blockchain.isChainValid();
            int blockCount = Blockchain.blockchain.size();
            
            return ResponseEntity.ok(new BlockchainStatus(isValid, blockCount, "Blockchain status retrieved"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new BlockchainStatus(false, 0, "Failed to get blockchain status: " + e.getMessage()));
        }
    }

    @GetMapping("/blockchain/blocks")
    public ResponseEntity<List<Map<String, Object>>> getAllBlocks() {
        try {
            List<Map<String, Object>> blocks = Blockchain.blockchain.stream()
                .map(block -> {
                    Map<String, Object> blockData = new HashMap<>();
                    blockData.put("hash", block.hash);
                    blockData.put("previousHash", block.previousHash);
                    blockData.put("data", block.getData());
                    blockData.put("timestamp", block.getTimeStamp());
                    blockData.put("nonce", block.getNonce());
                    return blockData;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(blocks);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/results")
    public ResponseEntity<Map<String, Integer>> getVotingResults() {
        try {
            Map<String, Integer> results = new HashMap<>();
            
            // Skip genesis block and count votes
            for (int i = 1; i < Blockchain.blockchain.size(); i++) {
                Block block = Blockchain.blockchain.get(i);
                String candidate = block.getData();
                results.put(candidate, results.getOrDefault(candidate, 0) + 1);
            }
            
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new HashMap<>());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "Blockchain Voting System API");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/blockchain/validate")
    public ResponseEntity<Map<String, Object>> validateBlockchain() {
        Map<String, Object> response = new HashMap<>();
        boolean isValid = Blockchain.isChainValid();
        response.put("isValid", isValid);
        response.put("blockCount", Blockchain.blockchain.size());
        return ResponseEntity.ok(response);
    }
} 