// Blockchain Voting System - Frontend JavaScript

const API_BASE = '/api/voting';

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    loadBlockchainStatus();
    loadResults();
    setupEventListeners();
});

function setupEventListeners() {
    // Voting form
    document.getElementById('votingForm').addEventListener('submit', handleVoting);
    
    // Registration form
    document.getElementById('registrationForm').addEventListener('submit', handleRegistration);
    
    // Auto-refresh every 30 seconds
    setInterval(() => {
        loadBlockchainStatus();
        loadResults();
    }, 30000);
}

async function handleVoting(event) {
    event.preventDefault();
    
    const voterId = document.getElementById('voterId').value.trim();
    const candidate = document.getElementById('candidate').value;
    
    if (!voterId || !candidate) {
        showAlert('Please fill in all fields', 'warning');
        return;
    }
    
    showLoading(true);
    
    try {
        const response = await fetch(`${API_BASE}/cast`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                voterId: voterId,
                candidate: candidate
            })
        });
        
        const result = await response.json();
        
        if (response.ok && result.status === 'success') {
            showAlert('✅ Vote cast successfully! Your vote has been recorded on the blockchain.', 'success');
            document.getElementById('votingForm').reset();
            
            // Refresh results and blockchain status
            setTimeout(() => {
                loadResults();
                loadBlockchainStatus();
            }, 1000);
        } else {
            showAlert(`❌ ${result.message || 'Failed to cast vote'}`, 'danger');
        }
    } catch (error) {
        console.error('Voting error:', error);
        showAlert('❌ Network error. Please try again.', 'danger');
    } finally {
        showLoading(false);
    }
}

async function handleRegistration(event) {
    event.preventDefault();
    
    const voterId = document.getElementById('newVoterId').value.trim();
    
    if (!voterId) {
        showAlert('Please enter a Voter ID', 'warning');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                voterId: voterId
            })
        });
        
        const result = await response.json();
        
        if (response.ok && result.status === 'success') {
            showAlert('✅ Voter registered successfully!', 'success');
            document.getElementById('registrationForm').reset();
        } else {
            showAlert(`❌ ${result.message || 'Registration failed'}`, 'danger');
        }
    } catch (error) {
        console.error('Registration error:', error);
        showAlert('❌ Network error. Please try again.', 'danger');
    }
}

async function loadBlockchainStatus() {
    try {
        const response = await fetch(`${API_BASE}/blockchain/status`);
        const status = await response.json();
        
        const statusDiv = document.getElementById('blockchainStatus');
        
        if (response.ok) {
            const isValid = status.isValid;
            const blockCount = status.blockCount;
            
            statusDiv.innerHTML = `
                <div class="d-flex align-items-center justify-content-center mb-3">
                    <i class="fas fa-${isValid ? 'check-circle text-success' : 'exclamation-triangle text-danger'} fs-1 me-3"></i>
                    <div>
                        <h4 class="mb-1">${isValid ? 'Valid' : 'Invalid'}</h4>
                        <p class="mb-0 text-muted">${blockCount} blocks</p>
                    </div>
                </div>
                <p class="text-muted small">${status.message}</p>
            `;
        } else {
            statusDiv.innerHTML = `
                <div class="text-center text-danger">
                    <i class="fas fa-exclamation-triangle fs-1 mb-2"></i>
                    <p>Failed to load blockchain status</p>
                </div>
            `;
        }
    } catch (error) {
        console.error('Error loading blockchain status:', error);
        document.getElementById('blockchainStatus').innerHTML = `
            <div class="text-center text-danger">
                <i class="fas fa-times-circle fs-1 mb-2"></i>
                <p>Network error</p>
            </div>
        `;
    }
}

async function loadResults() {
    try {
        const response = await fetch(`${API_BASE}/results`);
        const results = await response.json();
        
        const resultsDiv = document.getElementById('votingResults');
        
        if (response.ok) {
            let totalVotes = Object.values(results).reduce((sum, count) => sum + count, 0);
            
            if (totalVotes === 0) {
                resultsDiv.innerHTML = `
                    <div class="text-center text-muted">
                        <i class="fas fa-inbox fs-1 mb-3"></i>
                        <p>No votes cast yet</p>
                    </div>
                `;
                return;
            }
            
            let html = '';
            const candidates = ['Candidate A', 'Candidate B', 'Candidate C'];
            const colors = ['#007bff', '#dc3545', '#28a745'];
            
            candidates.forEach((candidate, index) => {
                const votes = results[candidate] || 0;
                const percentage = totalVotes > 0 ? (votes / totalVotes * 100) : 0;
                
                html += `
                    <div class="mb-3">
                        <div class="d-flex justify-content-between align-items-center mb-1">
                            <span class="fw-bold">${candidate}</span>
                            <span class="badge" style="background-color: ${colors[index]}">${votes} votes</span>
                        </div>
                        <div class="progress" style="height: 10px;">
                            <div class="progress-bar" 
                                 style="width: ${percentage}%; background-color: ${colors[index]}"
                                 role="progressbar" 
                                 aria-valuenow="${percentage}" 
                                 aria-valuemin="0" 
                                 aria-valuemax="100">
                            </div>
                        </div>
                        <small class="text-muted">${percentage.toFixed(1)}%</small>
                    </div>
                `;
            });
            
            html += `
                <hr>
                <div class="text-center">
                    <strong>Total Votes: ${totalVotes}</strong>
                </div>
            `;
            
            resultsDiv.innerHTML = html;
        } else {
            resultsDiv.innerHTML = `
                <div class="text-center text-danger">
                    <i class="fas fa-exclamation-triangle fs-1 mb-2"></i>
                    <p>Failed to load results</p>
                </div>
            `;
        }
    } catch (error) {
        console.error('Error loading results:', error);
        document.getElementById('votingResults').innerHTML = `
            <div class="text-center text-danger">
                <i class="fas fa-times-circle fs-1 mb-2"></i>
                <p>Network error</p>
            </div>
        `;
    }
}

async function checkBlockchain() {
    showAlert('🔍 Verifying blockchain integrity...', 'info');
    await loadBlockchainStatus();
    
    // Simulate verification delay for user feedback
    setTimeout(() => {
        const statusDiv = document.getElementById('blockchainStatus');
        if (statusDiv.innerHTML.includes('Valid')) {
            showAlert('✅ Blockchain verification complete - All blocks are valid!', 'success');
        } else {
            showAlert('⚠️ Blockchain verification failed - Integrity compromised!', 'danger');
        }
    }, 1000);
}

function showLoading(show) {
    const loading = document.querySelector('.loading');
    const form = document.getElementById('votingForm');
    
    if (show) {
        loading.style.display = 'block';
        form.style.display = 'none';
    } else {
        loading.style.display = 'none';
        form.style.display = 'block';
    }
}

function showAlert(message, type) {
    const alertContainer = document.getElementById('alertContainer');
    const alertId = 'alert-' + Date.now();
    
    const alertHtml = `
        <div id="${alertId}" class="alert alert-${type} alert-dismissible fade show shadow" role="alert">
            <strong>${message}</strong>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
    
    alertContainer.insertAdjacentHTML('afterbegin', alertHtml);
    
    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        const alert = document.getElementById(alertId);
        if (alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }
    }, 5000);
}

// Utility function to test API connectivity
async function testApiConnection() {
    try {
        const response = await fetch(`${API_BASE}/health`);
        const result = await response.json();
        
        if (response.ok) {
            console.log('✅ API connection successful:', result);
            return true;
        } else {
            console.error('❌ API connection failed:', response.status);
            return false;
        }
    } catch (error) {
        console.error('❌ API connection error:', error);
        return false;
    }
}

// Test API connection on load
testApiConnection().then(connected => {
    if (!connected) {
        showAlert('⚠️ Backend API not available. Some features may not work.', 'warning');
    }
}); 