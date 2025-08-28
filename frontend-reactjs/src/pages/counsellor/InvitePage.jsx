import React, { useState } from 'react';
import { Box } from '@mui/material';
import Sidenav from '../../components/Sidenav';
import axios from 'axios';

const drawerWidth = 200;

const InvitePage = () => {
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState(null);

    const handleSendInvite = async () => {
        if (!email) {
            setMessage({ type: 'error', text: 'Please enter an email address.' });
            return;
        }

        setLoading(true);
        setMessage(null);

        try {
            const response = await axios.post(
                'http://localhost:8080/api/linkrequest',
                { clientEmail: email },
                { withCredentials: true }
            );

            if (response.status !== 200) {
                throw new Error(`Request failed with status ${response.status}`);
            }

            setMessage({ type: 'success', text: 'Invite sent successfully!' });
            setEmail('');
        } catch (err) {
            const errMsg = err.response?.data?.message || err.message || 'Failed to send invite';
            setMessage({ type: 'error', text: errMsg });
        } finally {
            setLoading(false);
        }
    };

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
                    maxWidth: 600,
                }}
            >
                <h1 style={{ fontSize: '2.5rem', marginBottom: '8px' }}>Invite a client</h1>
                <p style={{ fontSize: '1.2rem', marginBottom: '16px' }}>Enter client email to send an invite</p>

                <input
                    type="email"
                    placeholder="Client email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    style={{
                        width: '50%',
                        padding: '6px 10px',
                        fontSize: 14,
                        borderRadius: 4,
                        border: '1px solid #ccc',
                        marginBottom: 12,
                    }}
                    disabled={loading}
                />

                <button
                    onClick={handleSendInvite}
                    disabled={loading}
                    style={{
                        padding: '10px 16px',
                        fontSize: 16,
                        cursor: loading ? 'not-allowed' : 'pointer',
                        backgroundColor: '#1976d2',
                        color: 'white',
                        border: 'none',
                        borderRadius: 4,
                    }}
                >
                    {loading ? 'Sending...' : 'Send Invite'}
                </button>

                {message && (
                    <p style={{ marginTop: 12, color: message.type === 'error' ? 'red' : 'green' }}>
                        {message.text}
                    </p>
                )}
            </Box>
        </Box>
    );
};

export default InvitePage;
