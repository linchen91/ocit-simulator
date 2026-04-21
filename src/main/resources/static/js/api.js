const API_BASE = '/api/v1/controllers';
const DATEX2_BASE = '/api/v1/datex2';
const OCITC_BASE = '/api/v1/ocit-c';
async function fetchJson(url) {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
    return response.json();
}
export const Api = {
    async getAllControllers() {
        return fetchJson(API_BASE);
    },
    async getController(id) {
        return fetchJson(`${API_BASE}/${id}`);
    },
    async getSupplyData(id) {
        return fetchJson(`${API_BASE}/${id}/supply`);
    },
    async getTrafficData(id) {
        return fetchJson(`${API_BASE}/${id}/traffic`);
    },
    async getSignalGroups(id) {
        return fetchJson(`${API_BASE}/${id}/signals`);
    },
    async getSystemStatus() {
        return fetchJson(`${API_BASE}/status`);
    },
    async setControllerStatus(id, status) {
        await fetch(`${API_BASE}/${id}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status })
        });
    },
    async setSignalColor(controllerId, groupId, color) {
        await fetch(`${API_BASE}/${controllerId}/signals/${groupId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ color })
        });
    },
    async getDatex2Situation() {
        const response = await fetch(`${DATEX2_BASE}/situation`);
        if (!response.ok)
            throw new Error(`HTTP ${response.status}`);
        return response.text();
    },
    async getDatex2Measured() {
        const response = await fetch(`${DATEX2_BASE}/measured`);
        if (!response.ok)
            throw new Error(`HTTP ${response.status}`);
        return response.text();
    },
    async getDatex2Elaborated() {
        const response = await fetch(`${DATEX2_BASE}/elaborated`);
        if (!response.ok)
            throw new Error(`HTTP ${response.status}`);
        return response.text();
    },
    async getOcitsSupply() {
        const response = await fetch(`${OCITC_BASE}/supply`);
        if (!response.ok)
            throw new Error(`HTTP ${response.status}`);
        return response.text();
    },
    async getOcitsProcess() {
        const response = await fetch(`${OCITC_BASE}/process`);
        if (!response.ok)
            throw new Error(`HTTP ${response.status}`);
        return response.text();
    },
    async getOcitsStatus() {
        const response = await fetch(`${OCITC_BASE}/status`);
        if (!response.ok)
            throw new Error(`HTTP ${response.status}`);
        return response.text();
    },
    async getOcitsHeartbeat() {
        const response = await fetch(`${OCITC_BASE}/heartbeat`);
        if (!response.ok)
            throw new Error(`HTTP ${response.status}`);
        return response.text();
    }
};
