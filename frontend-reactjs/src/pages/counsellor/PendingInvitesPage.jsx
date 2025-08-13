import React, { useState, useEffect } from 'react';
import {
    Box, Button, TextField, Table, TableBody, TableCell,
    TableContainer, TableHead, TableRow, Paper
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import SidenavC from "../../components/SidenavC.jsx";

const drawerWidth = 200;
const API_BASE = 'http://122.248.243.60:8080';
const TZ_SG = 'Asia/Singapore';


function normalizeUtcIso(value) {
    if (value == null) return null;

    if (value instanceof Date) return value.toISOString();
    if (typeof value === 'number') return new Date(value).toISOString();

    const s = String(value).trim();

    if (/[zZ]|[+\-]\d{2}:?\d{2}$/.test(s)) return s;

    if (/^\d{4}-\d{2}-\d{2}[ T]\d{2}:\d{2}(?::\d{2}(?:\.\d{1,9})?)?$/.test(s)) {
        const iso = s.includes('T') ? s : s.replace(' ', 'T');
        return iso + 'Z';
    }

    if (/^\d{4}-\d{2}-\d{2}$/.test(s)) {
        return s + 'T00:00:00Z';
    }

    return s;
}

function parseAsUtcDate(value) {
    const iso = normalizeUtcIso(value);
    if (!iso) return null;
    const d = new Date(iso);
    return Number.isNaN(d.getTime()) ? null : d;
}

function formatSG(value) {
    const d = parseAsUtcDate(value);
    if (!d) return '—';
    return new Intl.DateTimeFormat('en-SG', {
        timeZone: TZ_SG,
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(d);
}

const PendingInvitesPage = () => {
    const navigate = useNavigate();
    const [searchTerm, setSearchTerm] = useState('');
    const [invites, setInvites] = useState([]);
    const [filteredInvites, setFilteredInvites] = useState([]);
    const [loading, setLoading] = useState(false);

    const counsellorId = localStorage.getItem('counsellorId')
        || localStorage.getItem('userId') // fallback if your team reused the key
        || null;

    const fetchInvites = async () => {
        if (!counsellorId) {
            console.error('No counsellorId found in localStorage');
            setInvites([]);
            setFilteredInvites([]);
            return;
        }
        setLoading(true);
        try {
            const url = `${API_BASE}/api/linkrequest/counsellor/all-link-requests/${counsellorId}`;
            const res = await fetch(url, { credentials: 'include' });
            if (!res.ok) throw new Error('Failed to fetch invites');

            const data = await res.json();
            // Optional: map to add a preformatted SG string (saves work during render)
            const withDisplay = Array.isArray(data)
                ? data.map(inv => ({
                    ...inv,
                    requestedAtDisplay: formatSG(inv.requestedAt),
                }))
                : [];

            setInvites(withDisplay);
            setFilteredInvites(withDisplay);
        } catch (error) {
            console.error(error);
            setInvites([]);
            setFilteredInvites([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchInvites();
    }, []);

    const handleSearchChange = (e) => {
        const val = e.target.value;
        setSearchTerm(val);
        if (!val.trim()) {
            setFilteredInvites(invites);
        } else {
            const filtered = invites.filter(invite =>
                invite.journalUser?.email.toLowerCase().includes(val.trim().toLowerCase())
            );
            setFilteredInvites(filtered);
        }
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <SidenavC />
            <Box
                component="main"
                sx={{ flexGrow: 1, ml: `${drawerWidth}px`, p: 4, pt: 2, maxWidth: 800 }}
            >
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                    <Box sx={{ flexGrow: 1, textAlign: 'center' }}>
                        <h1 style={{ fontSize: '2.5rem', margin: 0 }}>Pending Invites</h1>
                    </Box>
                    <Button variant="contained" color="primary" onClick={() => navigate('/invites/sendinvites')}>
                        Invite
                    </Button>
                </Box>

                <TextField
                    label="Search by Email"
                    variant="outlined"
                    size="small"
                    value={searchTerm}
                    onChange={handleSearchChange}
                    sx={{ mb: 3, width: 300, ml: -10 }}
                />

                {loading ? (
                    <p>Loading...</p>
                ) : (
                    <TableContainer component={Paper}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Date Sent</TableCell>
                                    <TableCell>Email</TableCell>
                                    <TableCell>Status</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {filteredInvites.length === 0 ? (
                                    <TableRow>
                                        <TableCell colSpan={3} align="center">No pending invites found.</TableCell>
                                    </TableRow>
                                ) : (
                                    filteredInvites.map((invite) => (
                                        <TableRow key={invite.id}>
                                            {/* ✅ timezone-correct rendering */}
                                            <TableCell>{invite.requestedAtDisplay || formatSG(invite.requestedAt)}</TableCell>
                                            <TableCell>{invite.journalUser?.email || '—'}</TableCell>
                                            <TableCell>{invite.status}</TableCell>
                                        </TableRow>
                                    ))
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Box>
        </Box>
    );
};

export default PendingInvitesPage;
