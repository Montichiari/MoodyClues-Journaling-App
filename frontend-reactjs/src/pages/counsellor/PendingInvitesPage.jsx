import React, { useState, useEffect } from 'react';
import {Box, Button, TextField, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Sidenav from '../../components/Sidenav';
import SidenavC from "../../components/SidenavC.jsx";

const drawerWidth = 200;

const PendingInvitesPage = () => {
    const navigate = useNavigate();
    const [searchTerm, setSearchTerm] = useState('');
    const [invites, setInvites] = useState([]);
    const [filteredInvites, setFilteredInvites] = useState([]);
    const [loading, setLoading] = useState(false);


    const counsellorId = localStorage.getItem('counsellorId');

    const fetchInvites = async () => {
        if (!counsellorId) {
            console.error('No counsellorId found in localStorage');
            setInvites([]);
            setFilteredInvites([]);
            return;
        }
        setLoading(true);
        try {
            //test
            const baseUrl = 'http://122.248.243.60:8080';

            const url = `${baseUrl}/api/linkrequest/counsellor/all-link-requests/${counsellorId}`;
            const res = await fetch(url, { credentials: 'include' });
            if (!res.ok) throw new Error('Failed to fetch invites');
            const data = await res.json();
            setInvites(data);
            setFilteredInvites(data);
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

    // 监听输入，实时过滤
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
                sx={{
                    flexGrow: 1,
                    ml: `${drawerWidth}px`,
                    p: 4,
                    pt: 2,
                    maxWidth: 800,
                }}
            >
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                    <Box sx={{ flexGrow: 1, textAlign: 'center' }}>
                        <h1 style={{ fontSize: '2.5rem', margin: 0 }}>Pending Invites</h1>
                    </Box>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={() => navigate('/invites/sendinvites')}
                    >
                        Invite
                    </Button>
                </Box>

                <TextField
                    label="Search by Email"
                    variant="outlined"
                    size="small"
                    value={searchTerm}
                    onChange={handleSearchChange}  // 输入时实时过滤
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
                                            <TableCell>{new Date(invite.requestedAt).toLocaleString()}</TableCell>
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
