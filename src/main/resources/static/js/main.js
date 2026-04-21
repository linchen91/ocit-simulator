import { Api } from './api.js';
const DATEX2_COLORS = {
    normal: '#22c55e',
    minor: '#eab308',
    major: '#f97316',
    severe: '#ef4444'
};
const OCITC_COLORS = {
    SUCCESS: '#22c55e',
    FAILURE: '#ef4444',
    PENDING: '#eab308',
    TIMEOUT: '#6b7280'
};
const STATUS_COLORS = {
    OPERATIONAL: '#22c55e',
    MAINTENANCE: '#eab308',
    OFFLINE: '#ef4444',
    ERROR: '#dc2626'
};
const SIGNAL_COLORS = {
    RED: '#ef4444',
    YELLOW: '#eab308',
    GREEN: '#22c55e',
    OFF: '#6b7280'
};
let controllers = [];
let refreshInterval = null;
let datex2RefreshInterval = null;
async function loadControllers() {
    try {
        controllers = await Api.getAllControllers();
        renderControllers();
    }
    catch (err) {
        console.error('Failed to load controllers:', err);
    }
}
async function loadSystemStatus() {
    try {
        return await Api.getSystemStatus();
    }
    catch {
        return null;
    }
}
function renderControllers() {
    const container = document.getElementById('controllers');
    if (!container)
        return;
    if (controllers.length === 0) {
        container.innerHTML = '<p class="text-gray-500">No controllers found</p>';
        return;
    }
    container.innerHTML = controllers.map(c => `
    <div class="controller-card" data-id="${c.controllerId}">
      <div class="card-header">
        <h3>${c.controllerId}</h3>
        <span class="status-badge" style="background: ${STATUS_COLORS[c.status] || '#6b7280'}">${c.status}</span>
      </div>
      <p class="location">${c.location || 'Unknown location'}</p>
      <div class="card-actions">
        <button class="btn" onclick="window.app.showDetails('${c.controllerId}')">Details</button>
        <select onchange="window.app.setStatus('${c.controllerId}', this.value)">
          <option value="" ${!c.status ? 'selected' : ''}>Change status</option>
          <option value="OPERATIONAL">OPERATIONAL</option>
          <option value="MAINTENANCE">MAINTENANCE</option>
          <option value="OFFLINE">OFFLINE</option>
          <option value="ERROR">ERROR</option>
        </select>
      </div>
    </div>
  `).join('');
}
async function showDetails(controllerId) {
    const modal = document.getElementById('modal');
    if (!modal)
        return;
    try {
        const [supply, traffic, signals] = await Promise.all([
            Api.getSupplyData(controllerId),
            Api.getTrafficData(controllerId).catch(() => null),
            Api.getSignalGroups(controllerId)
        ]);
        let signalsHtml = '';
        if (signals.length > 0) {
            signalsHtml = '<div class="signal-groups"><h4>Signal Groups</h4>' +
                signals.map(s => `
          <div class="signal-item">
            <span>Group ${s.groupId}</span>
            <span class="signal-color" style="background: ${SIGNAL_COLORS[s.color] || '#6b7280'}"></span>
            <span>Phase ${s.phase}</span>
          </div>
        `).join('') + '</div>';
        }
        const trafficInfo = traffic ? `
      <div class="traffic-info">
        <p>Phase: <strong>${traffic.currentPhase}</strong></p>
        <p>Cycle: <strong>${traffic.cycleTime}s</strong></p>
      </div>
    ` : '';
        modal.innerHTML = `
      <div class="modal-content">
        <div class="modal-header">
          <h2>${controllerId}</h2>
          <button onclick="window.app.closeModal()">&times;</button>
        </div>
        <div class="modal-body">
          <div class="supply-data">
            <h4>Supply Data</h4>
            <p>Manufacturer: ${supply.manufacturer}</p>
            <p>Model: ${supply.model}</p>
            <p>Software: ${supply.softwareVersion}</p>
            <p>Hardware: ${supply.hardwareVersion}</p>
            <p>Source: ${supply.dataSource}</p>
          </div>
          ${trafficInfo}
          ${signalsHtml}
        </div>
      </div>
    `;
        modal.style.display = 'flex';
    }
    catch (err) {
        console.error('Failed to load details:', err);
    }
}
function closeModal() {
    const modal = document.getElementById('modal');
    if (modal)
        modal.style.display = 'none';
}
async function setStatus(controllerId, status) {
    if (!status)
        return;
    try {
        await Api.setControllerStatus(controllerId, status);
        await loadControllers();
    }
    catch (err) {
        console.error('Failed to set status:', err);
    }
}
async function init() {
    const status = await loadSystemStatus();
    updateStatusPanel(status);
    await loadControllers();
    await loadDatex2Data();
    await loadOcitsData();
    refreshInterval = window.setInterval(async () => {
        const s = await loadSystemStatus();
        updateStatusPanel(s);
    }, 5000);
    datex2RefreshInterval = window.setInterval(async () => {
        await loadDatex2Data();
        await loadOcitsData();
    }, 10000);
}
function parseXmlDatex2(xml) {
    try {
        const parser = new DOMParser();
        const doc = parser.parseFromString(xml, 'application/xml');
        return doc.documentElement.textContent || xml.substring(0, 200);
    }
    catch {
        return xml.substring(0, 200);
    }
}
async function loadDatex2Data() {
    const panel = document.getElementById('datex2-panel');
    if (!panel)
        return;
    try {
        const [situation, measured, elaborated] = await Promise.all([
            Api.getDatex2Situation().catch(() => ''),
            Api.getDatex2Measured().catch(() => ''),
            Api.getDatex2Elaborated().catch(() => '')
        ]);
        const parser = new DOMParser();
        let severity = 'unknown';
        let lat = 0, lon = 0;
        try {
            const sitDoc = parser.parseFromString(situation, 'application/xml');
            const severityEl = sitDoc.getElementsByTagName('overallSeverity')[0] ||
                sitDoc.getElementsByTagName('OverallSeverity')[0] ||
                sitDoc.getElementsByTagName('overallseverity')[0] ||
                null;
            const latEl = sitDoc.getElementsByTagName('latitude')[0] ||
                sitDoc.getElementsByTagName('Latitude')[0] ||
                null;
            const lonEl = sitDoc.getElementsByTagName('longitude')[0] ||
                sitDoc.getElementsByTagName('Longitude')[0] ||
                null;
            severity = severityEl?.textContent?.trim() || 'unknown';
            lat = parseFloat(latEl?.textContent?.trim() || '0');
            lon = parseFloat(lonEl?.textContent?.trim() || '0');
        }
        catch { }
        let speed = 0, vehicles = 0;
        try {
            const measDoc = parser.parseFromString(measured, 'application/xml');
            const speedEl = measDoc.getElementsByTagName('speed')[0] ||
                measDoc.getElementsByTagName('Speed')[0] ||
                null;
            const vehEl = measDoc.getElementsByTagName('numberOfVehicles')[0] ||
                measDoc.getElementsByTagName('NumberOfVehicles')[0] ||
                null;
            speed = parseFloat(speedEl?.textContent?.trim() || '0');
            vehicles = parseInt(vehEl?.textContent?.trim() || '0');
        }
        catch { }
        let status = 'unknown';
        try {
            const elabDoc = parser.parseFromString(elaborated, 'application/xml');
            const statusEl = elabDoc.getElementsByTagName('elaboratedDataStatus')[0] ||
                elabDoc.getElementsByTagName('ElaboratedDataStatus')[0] ||
                elabDoc.getElementsByTagName('elaborateddatastatus')[0] ||
                null;
            status = statusEl?.textContent?.trim() || 'unknown';
        }
        catch { }
        panel.innerHTML = `
      <div class="datex2-card">
        <h4>DATEX II Status</h4>
        <p>Protocol: <span>3.1</span></p>
        <p>Supplier: <span>OCIT-SIMULATOR</span></p>
      </div>
      <div class="datex2-card">
        <h4>Situation</h4>
        <p>Severity: <span style="color: ${DATEX2_COLORS[severity] || '#6b7280'}">${(severity || 'unknown').toUpperCase()}</span></p>
        <p>Location: <span>${(lat || 0).toFixed(4)}, ${(lon || 0).toFixed(4)}</span></p>
      </div>
      <div class="datex2-card">
        <h4>Traffic Data</h4>
        <p>Speed: <span>${(speed || 0).toFixed(1)} km/h</span></p>
        <p>Vehicles: <span>${vehicles || 0}</span></p>
      </div>
      <div class="datex2-card">
        <h4>System Status</h4>
        <p>Status: <span style="color: ${status === 'operational' ? '#22c55e' : '#ef4444'}">${(status || 'unknown').toUpperCase()}</span></p>
        <p>Updated: <span>${new Date().toLocaleTimeString()}</span></p>
      </div>
    `;
    }
    catch (err) {
        console.error('Failed to load Datex2 data:', err);
        if (panel)
            panel.innerHTML = '<p>Failed to load DATEX II data</p>';
    }
}
async function loadOcitsData() {
    const panel = document.getElementById('ocitc-panel');
    if (!panel)
        return;
    try {
        const [supply, process, status, heartbeat] = await Promise.all([
            Api.getOcitsSupply().catch(() => ''),
            Api.getOcitsProcess().catch(() => ''),
            Api.getOcitsStatus().catch(() => ''),
            Api.getOcitsHeartbeat().catch(() => '')
        ]);
        const parser = new DOMParser();
        let objectType = 'unknown', manufacturer = 'unknown', statusC = 'unknown';
        try {
            const doc = parser.parseFromString(supply, 'application/xml');
            const objEl = doc.getElementsByTagName('ObjectType')[0] ||
                doc.getElementsByTagName('objectType')[0] ||
                doc.getElementsByTagName('objecttype')[0] ||
                null;
            const mfrEl = doc.getElementsByTagName('Manufacturer')[0] ||
                doc.getElementsByTagName('manufacturer')[0] ||
                doc.getElementsByTagName('MANUFACTURER')[0] ||
                null;
            const statEl = doc.getElementsByTagName('Status')[0] ||
                doc.getElementsByTagName('status')[0] ||
                doc.getElementsByTagName('STATUS')[0] ||
                null;
            objectType = objEl?.textContent?.trim() || 'unknown';
            manufacturer = mfrEl?.textContent?.trim() || 'unknown';
            statusC = statEl?.textContent?.trim() || 'unknown';
        }
        catch { }
        let cycleTime = 0, active = false;
        try {
            const doc = parser.parseFromString(process, 'application/xml');
            const cycleEl = doc.getElementsByTagName('CycleTime')[0] ||
                doc.getElementsByTagName('cycleTime')[0] ||
                doc.getElementsByTagName('cycletime')[0] ||
                null;
            const activeEl = doc.getElementsByTagName('Active')[0] ||
                doc.getElementsByTagName('active')[0] ||
                doc.getElementsByTagName('ACTIVE')[0] ||
                null;
            const ct = cycleEl && cycleEl.textContent ? cycleEl.textContent.trim() : '';
            cycleTime = ct ? parseInt(ct, 10) : 0;
            const at = activeEl && activeEl.textContent ? activeEl.textContent.trim() : '';
            active = (at === 'true');
        }
        catch { }
        let msgType = 'unknown';
        try {
            const doc = parser.parseFromString(heartbeat, 'application/xml');
            const typeEl = doc.getElementsByTagName('MessageType')[0] ||
                doc.getElementsByTagName('messageType')[0] ||
                doc.getElementsByTagName('MESSAGETYPE')[0] ||
                null;
            msgType = typeEl?.textContent?.trim() || 'unknown';
        }
        catch { }
        panel.innerHTML = `
      <div class="ocitc-card">
        <h4>OCIT-C</h4>
        <p>Protocol: <span>3.0</span></p>
        <p>Message: <span>${msgType ? msgType.toUpperCase() : 'N/A'}</span></p>
      </div>
      <div class="ocitc-card">
        <h4>Supply Data</h4>
        <p>Type: <span>${objectType ? objectType.toUpperCase() : 'N/A'}</span></p>
        <p>Manufacturer: <span>${manufacturer || 'N/A'}</span></p>
        <p>Status: <span style="color: ${STATUS_COLORS[statusC] || '#6b7280'}">${statusC || 'N/A'}</span></p>
      </div>
      <div class="ocitc-card">
        <h4>Process</h4>
        <p>Cycle: <span>${cycleTime || 0}s</span></p>
        <p>Active: <span style="color: ${active ? '#22c55e' : '#ef4444'}">${active ? 'YES' : 'NO'}</span></p>
      </div>
      <div class="ocitc-card">
        <h4>Connection</h4>
        <p>Server: <span>13000</span></p>
        <p>Updated: <span>${new Date().toLocaleTimeString()}</span></p>
      </div>
    `;
    }
    catch (err) {
        console.error('Failed to load OCIT-C data:', err);
        if (panel)
            panel.innerHTML = '<p>Failed to load OCIT-C data</p>';
    }
}
function updateStatusPanel(status) {
    const panel = document.getElementById('status-panel');
    if (!panel || !status)
        return;
    panel.innerHTML = `
    <div class="stat-card">
      <span class="stat-value">${status.totalControllers || 0}</span>
      <span class="stat-label">Total</span>
    </div>
    <div class="stat-card">
      <span class="stat-value">${status.activeControllers || 0}</span>
      <span class="stat-label">Active</span>
    </div>
    <div class="stat-card">
      <span class="stat-value">${status.controllersByStatus?.OPERATIONAL || 0}</span>
      <span class="stat-label">Operational</span>
    </div>
    <div class="stat-card">
      <span class="stat-value">${status.controllersByStatus?.MAINTENANCE || 0}</span>
      <span class="stat-label">Maintenance</span>
    </div>
  `;
}
window.app = { showDetails, setStatus, closeModal };
document.addEventListener('DOMContentLoaded', init);
