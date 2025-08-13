import React, { useEffect, useState } from 'react';
import { Box, Typography } from '@mui/material';
import Sidenav from '../../components/Sidenav';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import SidenavC from "../../components/SidenavC.jsx";

const drawerWidth = 200;

const getMoodText = (mood) => {
    switch (mood) {
        case 1: return 'Very Bad';
        case 2: return 'Bad';
        case 3: return 'Neutral';
        case 4: return 'Good';
        case 5: return 'Very Good';
        default: return 'Unknown';
    }
};

const JournalDetailPage = () => {
    const { entryId, userId } = useParams();
    const [journalEntry, setJournalEntry] = useState(null);
    const [habitsEntry, setHabitsEntry] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchData() {
            setLoading(true);
            setError(null);

            const baseUrl = 'http://122.248.243.60:8080';

            try {
                const journalRes = await axios.get(
                    `${baseUrl}/api/journal/${entryId}/${userId}`,
                    { withCredentials: true }
                );
                setJournalEntry(journalRes.data);

                const habitsRes = await axios.get(
                    `${baseUrl}/api/habits/all/${userId}`,
                    { withCredentials: true }
                );
                const habitsList = habitsRes.data;

                const journalDate = new Date(journalRes.data.createdAt).toDateString();
                const matchedHabits = habitsList.find(h => new Date(h.createdAt).toDateString() === journalDate);
                setHabitsEntry(matchedHabits || null);

            } catch (err) {
                console.error(err);
                setError(err.response?.data?.message || err.message || 'Unknown error');
                setJournalEntry(null);
                setHabitsEntry(null);
            } finally {
                setLoading(false);
            }
        }

        fetchData();
    }, [userId, entryId]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>Error: {error}</div>;
    }

    if (!journalEntry) {
        return <div>No journal entry found.</div>;
    }

    return (
        <Box sx={{ display: 'flex' }}>
            <SidenavC />
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    ml: `${drawerWidth}px`,
                    p: 4,
                    maxWidth: 600,
                }}
            >
                <Typography variant="h3" component="h1" gutterBottom>
                    Your Journal
                </Typography>

                <Typography variant="h4" component="h2" gutterBottom>
                    {journalEntry.entryTitle}
                </Typography>

                <Typography variant="body2" color="text.secondary" gutterBottom sx={{ mb: 3 }}>
                    {new Date(journalEntry.createdAt).toLocaleString()}
                </Typography>

                {/* 显示时间段的心情文字 */}
                <Typography variant="body1" gutterBottom sx={{ fontSize: '1.2rem', mb: 3 }}>
                    Mood: {getMoodText(journalEntry.mood)}
                </Typography>


                {habitsEntry ? (
                    <Box sx={{ ml: 3, mb: 3, fontSize: '1.1rem' }}>
                        <Typography>Sleep hours: {habitsEntry.sleep}</Typography>
                        <Typography>Water (liters): {habitsEntry.water}</Typography>
                        <Typography>Work hours: {habitsEntry.workHours}</Typography>
                    </Box>
                ) : (
                    <Typography sx={{ ml: 3, mb: 3, fontStyle: 'italic' }}>
                        No habits data for this date.
                    </Typography>
                )}

                <hr />

                <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap', mt: 3, border: '1px solid #ccc', p: 2 }}>
                    {journalEntry.entryText}
                </Typography>
            </Box>
        </Box>
    );
};

export default JournalDetailPage;
