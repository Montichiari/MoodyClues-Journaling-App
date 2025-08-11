import React, { useState, useEffect } from "react";
import { Box, Toolbar, TextField } from '@mui/material';
import Sidenav from '../../components/Sidenav';
import { Divider } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const drawerWidth = 200;

const ClientsPage = () => {
    const navigate = useNavigate();
    const [searchTerm, setSearchTerm] = useState("");
    const [clients, setClients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 从API获取客户数据
    useEffect(() => {
        const fetchClients = async () => {
            try {
                const response = await fetch('/api/counsellor/all-link-requests', {
                    credentials: 'include'
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch clients');
                }

                const data = await response.json();
                setClients(data.map(request => ({
                    id: request.journalUserId,
                    dateLinked: new Date(request.createdAt).toLocaleDateString('en-GB'),
                    name: request.journalUserName || request.journalUserEmail
                })));
            } catch (err) {
                setError(err.message);
                console.error('Error fetching clients:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchClients();
    }, []);

    // 过滤客户数据
    const filteredClients = clients.filter(client =>
        client.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // 查看Journal
    const handleViewJournal = (clientId) => {
        navigate(`/journal/${clientId}`);
    };

    // 查看Dashboard
    const handleViewDashboard = (clientId) => {
        navigate(`/dashboard/${clientId}`);
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav />
            <Box
                component="main"
                sx={{ flexGrow: 1, p: 3, ml: `${drawerWidth}px` }}
            >
                <Toolbar />
                <h1 className="text-2xl font-bold mb-6">MoodyClues</h1>

                <div className="space-y-8">
                    {/* Quick Start 部分 */}
                    <div>
                        <h2 className="text-xl font-semibold mb-4">Your Linked Clients</h2>
                        <div className="space-y-2 mb-6">
                            <p className="font-semibold">Quick Start</p>
                            <ul className="list-disc pl-5 space-y-1">
                                <li>Home</li>
                                <li>Clients</li>
                                <li>Pending Invites</li>
                            </ul>
                        </div>
                    </div>

                    {/* 搜索和客户列表 */}
                    <div>
                        <TextField
                            label="Search..."
                            variant="outlined"
                            fullWidth
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            sx={{ mb: 3 }}
                        />

                        <div className="mb-4">
                            <div className="flex font-semibold">
                                <span className="w-1/4">Date linked</span>
                                <span className="w-1/4">Name</span>
                                <span className="w-1/2 text-right">Actions</span>
                            </div>
                            <Divider sx={{ my: 1 }} />

                            {loading ? (
                                <p className="py-4 text-center">Loading clients...</p>
                            ) : error ? (
                                <p className="py-4 text-center text-red-500">{error}</p>
                            ) : filteredClients.length === 0 ? (
                                <p className="py-4 text-center">
                                    {searchTerm ? 'No matching clients found' : 'No clients linked yet'}
                                </p>
                            ) : (
                                filteredClients.map(client => (
                                    <div key={client.id} className="flex items-center py-2">
                                        <span className="w-1/4">{client.dateLinked}</span>
                                        <span className="w-1/4">{client.name}</span>
                                        <div className="w-1/2 text-right space-x-2">
                                            <button
                                                className="text-blue-600 hover:underline"
                                                onClick={() => handleViewJournal(client.id)}
                                            >
                                                Journal
                                            </button>
                                            <button
                                                className="text-blue-600 hover:underline"
                                                onClick={() => handleViewDashboard(client.id)}
                                            >
                                                Dashboard
                                            </button>
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>
                    </div>

                    {/* 底部操作 */}
                    <div className="flex justify-between items-center">
                        <button className="text-blue-600 hover:underline">
                            (w) Edit Profile
                        </button>
                        <button
                            className="text-red-600 hover:underline font-semibold"
                            onClick={() => navigate('/logout')}
                        >
                            Logout
                        </button>
                    </div>
                </div>
            </Box>
        </Box>
    );
};

export default ;