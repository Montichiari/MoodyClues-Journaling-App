import { Box, Snackbar, Alert, Toolbar } from '@mui/material';
import { useState } from 'react';
import axios from 'axios';
import SidenavC from "../components/SidenavC.jsx";

const drawerWidth = 200;
const API_BASE = 'http://122.248.243.60:8080';

const CounsellorInviteClients = () => {
    const [email, setEmail] = useState('');
    const [errMsg, setErrMsg] = useState('');
    const [loading, setLoading] = useState(false);
    const [successOpen, setSuccessOpen] = useState(false);

    const counsellorId = localStorage.getItem('counsellorId');

    const handleInvite = async (e) => {
        e.preventDefault();
        const trimmed = email.trim().toLowerCase();
        const emailOk = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmed);
        if (!emailOk) return setErrMsg('Please enter a valid email address.');
        if (!counsellorId) return setErrMsg('Missing counsellorId. Please log in again.');

        setErrMsg('');
        setLoading(true);
        try {
            const res = await axios.post(
                `${API_BASE}/api/linkrequest/${counsellorId}`,
                { clientEmail: trimmed },
                { validateStatus: () => true, withCredentials: false }
            );

            if (res.status === 200) {
                setSuccessOpen(true);
                setEmail('');
            } else if (res.status === 404) {
                setErrMsg('No user found with that email.');
            } else if (res.status === 409) {
                setErrMsg('A link request already exists for this client.');
            } else if (res.status === 400) {
                setErrMsg(res.data?.message || 'Invalid request.');
            } else {
                setErrMsg('Failed to send invite. Please try again.');
            }
        } catch {
            setErrMsg('Network error. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <SidenavC />
            <Box
                component="main"
                sx={{ flexGrow: 1, mr: `${drawerWidth}px`, p: 2 }}
                className="px-16 py-10"
            >

                <Toolbar />

                <section className="max-w-2xl">
                    <h1 className="text-3xl font-semibold mb-6">Invite New Clients To Link</h1>

                    <form onSubmit={handleInvite} className="space-y-5">
                        <div>
                            <label htmlFor="email" className="block mb-2 text-gray-700">
                                Client Email
                            </label>
                            <input
                                id="email"
                                type="email"
                                value={email}
                                onChange={(e) => { setEmail(e.target.value); if (errMsg) setErrMsg(''); }}
                                placeholder="client@example.com"
                                className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                                autoComplete="email"
                            />
                        </div>

                        {errMsg && (
                            <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded p-2">
                                {errMsg}
                            </div>
                        )}

                        <button
                            type="submit"
                            disabled={loading || !email.trim()}
                            className="border px-6 py-2 rounded bg-blue-600 text-white hover:bg-blue-700 transition disabled:opacity-60"
                        >
                            {loading ? 'Inviting…' : 'Invite'}
                        </button>
                    </form>
                </section>

                <Snackbar
                    open={successOpen}
                    autoHideDuration={3000}
                    onClose={() => setSuccessOpen(false)}
                    anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
                >
                    <Alert severity="success" variant="filled" onClose={() => setSuccessOpen(false)}>
                        Request sent successfully
                    </Alert>
                </Snackbar>
            </Box>
        </Box>
    );
};

export default CounsellorInviteClients;
