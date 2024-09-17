package com.example.blockchainvoting;

import java.util.HashSet;

<<<<<<< HEAD
import java.util.HashSet;

public class VoterRegistration {
    private HashSet<String> registeredVoters = new HashSet<>();

    // Register a voter by their voter ID
=======
public class VoterRegistration {
    private HashSet<String> registeredVoters = new HashSet<>();

>>>>>>> 6539ad1557721f499e57d578f03a82981f6cf265
    public boolean registerVoter(String voterId) {
        if (registeredVoters.contains(voterId)) {
            System.out.println("Voter already registered.");
            return false;
        }
        registeredVoters.add(voterId);
        System.out.println("Voter registered successfully.");
        return true;
    }

<<<<<<< HEAD
    // Check if a voter is registered
=======
>>>>>>> 6539ad1557721f499e57d578f03a82981f6cf265
    public boolean isVoterRegistered(String voterId) {
        return registeredVoters.contains(voterId);
    }
}
