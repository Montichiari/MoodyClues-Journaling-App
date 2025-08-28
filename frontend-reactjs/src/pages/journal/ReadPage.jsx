import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, TextField, Table, TableHead, TableRow, TableCell, TableBody, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Sidenav from '../../components/Sidenav';

const drawerWidth = 200;

const ReadPage = () => {
    const { userId: routeUserId } = useParams(); // Get user ID from URL params
    const loggedInUserId = localStorage.getItem("userId"); // Get logged-in user's ID from localStorage
    const targetUserId = routeUserId || loggedInUserId; // If routeUserId exists, use it; otherwise use logged-in user ID

    // test
    console.log("routeUserId:", routeUserId);
    console.log("loggedInUserId:", loggedInUserId);
    console.log("targetUserId:", targetUserId);


    const [entries, setEntries] = useState([]);
    const [filteredEntries, setFilteredEntries] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    // Fetch entries when targetUserId changes
    useEffect(() => {

        if (!targetUserId) {
            setError("User ID not found, please login.");
            return;
        }

        const fetchEntries = async () => {
            setLoading(true);
            setError(null);
            try {
                //test
                const baseUrl = "http://localhost:8080";

                const url = `${baseUrl}/api/journal/all/${targetUserId}`;
                const res = await fetch(url, { credentials: 'include' });
                if (!res.ok) throw new Error(`Error ${res.status}`);
                const data = await res.json();
                console.log("Fetched Entries:", data);  // Log fetched data for debugging
                setEntries(data);
                setFilteredEntries(data);
            } catch (err) {
                console.error("Fetch error:", err);  // Log error for debugging
                setError(err.message || "Failed to load entries");
                setEntries([]);
                setFilteredEntries([]);
            } finally {
                setLoading(false);
            }
        };

        fetchEntries();
    }, [targetUserId]);

    // Handle search input and filter entries by title
    const handleSearchChange = (e) => {
        const value = e.target.value;
        setSearchTerm(value);
        const filtered = entries.filter(entry =>
            entry.entryTitle && entry.entryTitle.toLowerCase().includes(value.toLowerCase())
        );
        setFilteredEntries(filtered);
    };

    // Handle title click to navigate to detailed page
    const handleTitleClick = (entry) => {
        navigate(`/read/detail/${targetUserId}/${entry.id}`);
    };

    // Handle rendering the table rows for journal entries
    const renderEntries = () => {
        return filteredEntries.map(entry => (
            <TableRow key={entry.id || entry.entryId} hover>
                <TableCell>
                    {new Date(entry.dateCreated || entry.createdAt || entry.date || '').toLocaleDateString()}
                </TableCell>
                <TableCell
                    onClick={() => handleTitleClick(entry)}
                    sx={{ cursor: 'pointer', color: 'primary.main', textDecoration: 'underline' }}
                >
                    {entry.entryTitle}
                </TableCell>
            </TableRow>
        ));
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
                }}
            />
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    ml: `${drawerWidth}px`,
                    p: 4,
                    maxWidth: 700,
                }}
            >
                <Typography variant="h4" gutterBottom>
                    Journal Entries
                </Typography>

                <TextField
                    label="Search by Title"
                    variant="outlined"
                    size="small"
                    value={searchTerm}
                    onChange={handleSearchChange}
                    sx={{ mb: 3, width: 400 }}
                    disabled={loading}
                />

                {error && <Typography color="error" mb={2}>{error}</Typography>}

                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Date</TableCell>
                            <TableCell>Title</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {loading ? (
                            <TableRow>
                                <TableCell colSpan={2} align="center">
                                    Loading...
                                </TableCell>
                            </TableRow>
                        ) : filteredEntries.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={2} align="center">
                                    No entries available for this user.
                                </TableCell>
                            </TableRow>
                        ) : (
                            renderEntries()
                        )}
                    </TableBody>
                </Table>
            </Box>
        </Box>
    );
};

export default ReadPage;
