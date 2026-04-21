const API_BASE = '/api/v1/controllers';
const DATEX2_BASE = '/api/v1/datex2';
const OCITC_BASE = '/api/v1/ocit-c';

export interface TrafficSignalController {
  controllerId: string;
  status: string;
  location: string;
  installationDate: string;
}

export interface SupplyData {
  controllerId: string;
  manufacturer: string;
  model: string;
  softwareVersion: string;
  hardwareVersion: string;
  dataSource: string;
}

export interface TrafficData {
  controllerId: string;
  timestamp: string;
  currentPhase: string;
  cycleTime: number;
  measurements: Record<number, number>;
}

export interface SignalGroup {
  controllerId: string;
  groupId: string;
  color: string;
  phase: number;
}

export interface SystemStatus {
  totalControllers: number;
  activeControllers: number;
  controllersByStatus: Record<string, number>;
}

async function fetchJson<T>(url: string): Promise<T> {
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
  }
  return response.json();
}

export const Api = {
  async getAllControllers(): Promise<TrafficSignalController[]> {
    return fetchJson<TrafficSignalController[]>(API_BASE);
  },

  async getController(id: string): Promise<TrafficSignalController | null> {
    return fetchJson<TrafficSignalController | null>(`${API_BASE}/${id}`);
  },

  async getSupplyData(id: string): Promise<SupplyData> {
    return fetchJson<SupplyData>(`${API_BASE}/${id}/supply`);
  },

  async getTrafficData(id: string): Promise<TrafficData> {
    return fetchJson<TrafficData>(`${API_BASE}/${id}/traffic`);
  },

  async getSignalGroups(id: string): Promise<SignalGroup[]> {
    return fetchJson<SignalGroup[]>(`${API_BASE}/${id}/signals`);
  },

  async getSystemStatus(): Promise<SystemStatus> {
    return fetchJson<SystemStatus>(`${API_BASE}/status`);
  },

  async setControllerStatus(id: string, status: string): Promise<void> {
    await fetch(`${API_BASE}/${id}/status`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status })
    });
  },

  async setSignalColor(controllerId: string, groupId: string, color: string): Promise<void> {
    await fetch(`${API_BASE}/${controllerId}/signals/${groupId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ color })
    });
  },

  async getDatex2Situation(): Promise<string> {
    const response = await fetch(`${DATEX2_BASE}/situation`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.text();
  },

  async getDatex2Measured(): Promise<string> {
    const response = await fetch(`${DATEX2_BASE}/measured`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.text();
  },

  async getDatex2Elaborated(): Promise<string> {
    const response = await fetch(`${DATEX2_BASE}/elaborated`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.text();
  },

  async getOcitsSupply(): Promise<string> {
    const response = await fetch(`${OCITC_BASE}/supply`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.text();
  },

  async getOcitsProcess(): Promise<string> {
    const response = await fetch(`${OCITC_BASE}/process`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.text();
  },

  async getOcitsStatus(): Promise<string> {
    const response = await fetch(`${OCITC_BASE}/status`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.text();
  },

  async getOcitsHeartbeat(): Promise<string> {
    const response = await fetch(`${OCITC_BASE}/heartbeat`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.text();
  }
};