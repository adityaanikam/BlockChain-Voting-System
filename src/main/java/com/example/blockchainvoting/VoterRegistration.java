package com.example.blockchainvoting;

import java.util.HashSet;



public class VoterRegistration {
    private HashSet<String> registeredVoters = new HashSet<>();

    // Register a voter by their voter ID
    public boolean registerVoter(String voterId) {
        if (registeredVoters.contains(voterId)) {
            System.out.println("Voter already registered.");
            return false;
        }
        registeredVoters.add(voterId);
        System.out.println("Voter registered successfully.");
        return true;
    }

    // Check if a voter is registered
    public boolean isVoterRegistered(String voterId) {
        return registeredVoters.contains(voterId);
    }
}
