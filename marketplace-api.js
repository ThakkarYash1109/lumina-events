// Lumina E-Commerce & Java Backend Integration Layer
// Depends on env-config.js (or defaults to auto-resolved endpoint)
if (typeof LUMINA_API_BASE === 'undefined') {
    var LUMINA_API_BASE = (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1' || window.location.protocol === 'file:')
        ? 'http://localhost:8080/api'
        : 'https://lumina-api.onrender.com/api';
}

// Check backend health
async function checkLuminaBackend() {
    try {
        const res = await fetch(`${LUMINA_API_BASE}/health`);
        if (res.ok) {
            const data = await res.json();
            return { online: true, data };
        }
    } catch (e) {
        // Backend not currently reachable
    }
    return { online: false };
}

// Fetch all marketplace vendors with category & budget filters
async function fetchLuminaVendors(category = 'all', city = '', budget = '') {
    try {
        let url = `${LUMINA_API_BASE}/vendors?category=${encodeURIComponent(category)}`;
        if (city) url += `&city=${encodeURIComponent(city)}`;
        if (budget) url += `&budget=${encodeURIComponent(budget)}`;
        const res = await fetch(url);
        if (res.ok) return await res.json();
    } catch (e) {
        console.warn('Using client-side fallback vendor data:', e);
    }
    return null;
}

// Create new Lumina booking with Escrow workflow
async function createLuminaBooking(bookingPayload) {
    try {
        const res = await fetch(`${LUMINA_API_BASE}/bookings`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookingPayload)
        });
        return await res.json();
    } catch (e) {
        return { success: false, error: e.message };
    }
}

// Fetch all bookings for Host or Vendor dashboard
async function fetchLuminaBookings() {
    try {
        const res = await fetch(`${LUMINA_API_BASE}/bookings`);
        if (res.ok) return await res.json();
    } catch (e) {
        console.error('Error fetching bookings:', e);
    }
    return [];
}

// Run 30-Test Suite from Testing Dashboard
async function runLuminaAutomatedTests() {
    try {
        const res = await fetch(`${LUMINA_API_BASE}/tests/run`);
        if (res.ok) return await res.json();
    } catch (e) {
        return { error: 'Failed connecting to Java test runner: ' + e.message };
    }
}

// Escrow Action: Release or Refund
async function updateLuminaEscrow(transactionId, action = 'release') {
    try {
        const res = await fetch(`${LUMINA_API_BASE}/payments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ transactionId, action })
        });
        return await res.json();
    } catch (e) {
        return { success: false, error: e.message };
    }
}

