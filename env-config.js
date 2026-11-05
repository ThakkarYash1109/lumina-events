// Lumina Dynamic Environment & API Endpoint Resolver
// Seamlessly switches between Local Development, Production Cloud API, and Hybrid Android App Mode

const LUMINA_CONFIG = {
    // Current runtime environment: 'development' | 'production' | 'auto'
    mode: 'auto',

    // Local microservice address
    localApiUrl: 'http://localhost:8080/api',

    // Live public cloud API endpoint (e.g. Render, Railway, AWS ECS)
    productionApiUrl: 'https://lumina-api.onrender.com/api',

    // Fallback Mock data enabled if remote backend is waking up from free-tier sleep
    enableGracefulFallback: true,

    // Resolve optimal API base URL
    getApiBase() {
        if (this.mode === 'production') return this.productionApiUrl;
        if (this.mode === 'development') return this.localApiUrl;

        // Auto mode detection:
        const isLocal = window.location.hostname === 'localhost' ||
                        window.location.hostname === '127.0.0.1' ||
                        window.location.protocol === 'file:';

        // Check if explicitly configured in localStorage or meta tag
        const overriddenUrl = localStorage.getItem('lumina_api_url');
        if (overriddenUrl) return overriddenUrl;

        // If accessed in local development, default to local port 8080
        // When deployed publicly or loaded in Capacitor Android App, default to production API
        return isLocal ? this.localApiUrl : this.productionApiUrl;
    }
};

const LUMINA_API_BASE = LUMINA_CONFIG.getApiBase();
console.log(`[Lumina Environment] Initialized API Base: ${LUMINA_API_BASE}`);
