import React, { useState, useEffect } from "react";
import { Box } from '@mui/material';
import Sidenav from '../../components/Sidenav';
import { useNavigate } from 'react-router-dom';
import SidenavC from "../../components/SidenavC.jsx";

const drawerWidth = 200;

const ClientsPage = () => {
    const navigate = useNavigate();

    const counsellorId =
        localStorage.getItem('counsellorId') ||
        localStorage.getItem('userId') ||
        null;

    const [searchTerm, setSearchTerm] = useState("");
    const [clients, setClients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchClients = async () => {
            try {
                if (!counsellorId) {
                    setError('Please login first.');
                    navigate('/counsellor/login', { replace: true });
                    return;
                }

                const res = await fetch(
                    `http://122.248.243.60:8080/api/linkrequest/counsellor/all-link-requests-accepted/${counsellorId}`,
                    { credentials: 'include' }
                );
                if (!res.ok) throw new Error(`Failed to fetch clients: ${res.status}`);

                const data = await res.json();

                console.log("Raw linkrequest data:", data);
                data.forEach((r, index) => {
                    console.log(`Item ${index} journalUser:`, r.journalUser);
                });

                if (!Array.isArray(data)) throw new Error('Invalid data format');

                const mapped = data.map(r => ({
                    id: r.journalUser.id,
                    dateLinked: new Date(r.requestedAt).toLocaleDateString('en-GB'),
                    name: `${r.journalUser.firstName} ${r.journalUser.lastName}`
                }));

                setClients(mapped);
            } catch (e) {
                setError(e.message || 'Something went wrong');
            } finally {
                setLoading(false);
            }
        };

        fetchClients();
    }, [counsellorId, navigate]);

    const filtered = clients.filter(c => c.name.toLowerCase().includes(searchTerm.toLowerCase()));

    return (
        <Box sx={{ display: 'flex' }}>
            <SidenavC />
            <Box component="main" sx={{ flexGrow: 1, mr: `${drawerWidth}px`, p: 4 }}>
                <h2 className="text-3xl font-semibold mb-6" style={{ fontWeight: 600, marginBottom: '1rem' }}>Your Linked Clients</h2>

                <input
                    type="text"
                    placeholder="Search..."
                    style={{ width: 250, padding: '6px 10px', fontSize: '0.875rem', borderRadius: 4, border: '1px solid #ccc', marginBottom: 16 }}
                    value={searchTerm}
                    onChange={e => setSearchTerm(e.target.value)}
                />

                <div style={{ border: '1px solid #ddd', borderRadius: 6, overflow: 'hidden' }}>
                    <div style={{ display: 'grid', gridTemplateColumns: '120px 1fr 200px', backgroundColor: '#f3f4f6', padding: 8, fontWeight: 600 }}>
                        <div>Date linked</div>
                        <div>Name</div>
                        <div>Actions</div>
                    </div>

                    {loading ? (
                        <div style={{ padding: 16, textAlign: 'center' }}>Loading clients...</div>
                    ) : error ? (
                        <div style={{ padding: 16, textAlign: 'center', color: 'red' }}>{error}</div>
                    ) : filtered.length === 0 ? (
                        <div style={{ padding: 16, textAlign: 'center' }}>{searchTerm ? 'No matching clients found' : 'No clients linked yet'}</div>
                    ) : (
                        filtered.map(client => (
                            <div
                                key={client.id}
                                style={{ display: 'grid', gridTemplateColumns: '120px 1fr 200px', padding: 8, borderTop: '1px solid #eee', alignItems: 'center', fontSize: '0.875rem' }}
                            >
                                <div>{client.dateLinked}</div>
                                <div>{client.name}</div>
                                <div style={{ display: 'flex', gap: 8 }}>
                                    <button
                                        onClick={() => {
                                            console.log("Navigating to /read/", client.id);
                                            navigate(`/counsellor/read/${client.id}`);
                                        }}
                                        style={{
                                            border: '1px solid #ccc',
                                            borderRadius: 9999,
                                            padding: '2px 10px',
                                            fontSize: '0.75rem',
                                            cursor: 'pointer',
                                            backgroundColor: '#fff',
                                            marginLeft: 70,
                                        }}
                                    >
                                        Journal
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </Box>
        </Box>
    );
};

export default ClientsPage;
