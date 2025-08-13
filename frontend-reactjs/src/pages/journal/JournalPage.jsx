import React, { useState, useEffect } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { Box } from '@mui/material';
import Sidenav from '../../components/Sidenav';

const drawerWidth = 200;

const JournalPage = () => {
    const { clientId } = useParams();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const clientName = queryParams.get('name');

    const [entries, setEntries] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchJournalEntries = async () => {
            try {
                setLoading(true);
                const res = await fetch(`/api/journal/all/${clientId}`, {
                    credentials: 'include',
                });
                if (!res.ok) throw new Error('Failed to fetch journal entries');
                const data = await res.json();
                setEntries(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchJournalEntries();
    }, [clientId]);

    const filteredEntries = entries.filter(entry =>
        entry.title.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav />
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    ml: `${drawerWidth}px`,
                    p: 4,
                    pt: 2,
                }}
            >
                <h1 style={{ marginBottom: 20 }}>
                    {clientName ? `${clientName}'s Journal` : `Client ${clientId}'s Journal`}
                </h1>

                <input
                    type="text"
                    placeholder="Search by title..."
                    value={searchTerm}
                    onChange={e => setSearchTerm(e.target.value)}
                    style={{
                        marginBottom: 20,
                        padding: '6px 10px',
                        fontSize: 16,
                        width: 300,
                        borderRadius: 4,
                        border: '1px solid #ccc',
                    }}
                />

                {loading && <p>Loading journal entries...</p>}
                {error && <p style={{ color: 'red' }}>Error: {error}</p>}

                {/* 表格：无论有没有数据，表头永远显示 */}
                <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 14 }}>
                    <thead>
                    <tr style={{ backgroundColor: '#f0f0f0' }}>
                        <th style={{ border: '1px solid #ddd', padding: 8, width: 150 }}>Date</th>
                        <th style={{ border: '1px solid #ddd', padding: 8, textAlign: 'center' }}>Title</th>
                    </tr>
                    </thead>
                    <tbody>
                    {!loading && !error && filteredEntries.length === 0 && (
                        <tr>
                            <td colSpan={2} style={{ border: '1px solid #ddd', padding: 8, textAlign: 'center' }}>
                                No entries found.
                            </td>
                        </tr>
                    )}
                    {!loading && !error && filteredEntries.map(entry => (
                        <tr key={entry.id || entry.entryId}>
                            <td style={{ border: '1px solid #ddd', padding: 8 }}>
                                {new Date(entry.createdAt || entry.date).toLocaleDateString('en-GB')}
                            </td>
                            <td style={{ border: '1px solid #ddd', padding: 8 }}>{entry.title}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </Box>
        </Box>
    );
};

export default JournalPage;
