import { Box, Toolbar } from '@mui/material';
import Sidenav from '../../components/Sidenav';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const drawerWidth = 200;

const formatDate = (date) => {
    const day = date.getDate();
    const month = date.toLocaleString('default', { month: 'long' });
    const year = date.getFullYear();
    return `${day} ${month} ${year}`;
};

const Emotions = () => {
    const navigate = useNavigate();
    const today = formatDate(new Date());

    const [emotion, setEmotion] = useState("");
    const [quote, setQuote] = useState("");

    useEffect(() => {
        const mood = JSON.parse(sessionStorage.getItem("journal_mood")) || {};
        const avgMood = (mood.morning + mood.afternoon + mood.evening) / 3;
        const felt = getEmotion(avgMood);
        setEmotion(felt);
        setQuote(getMotivationalQuote(felt));
    }, []);

    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav />
            <Box component="main" sx={{ flexGrow: 1, p: 3, ml: `${drawerWidth}px` }} className="px-16 py-10">
                <Toolbar />
                <h1 className="text-3xl font-semibold mb-4">Today is {today}.</h1>
                <div className="mb-8">Today, it seems like you felt...</div>
                <div className="text-xl font-medium mb-4">{emotion}</div>
                <div className="text-md italic text-gray-700">"{quote}"</div>
                <div className="mt-12">
                    <button onClick={() => navigate("/journal/mood")} className="border px-5 py-2 rounded">Go Home</button>
                </div>
            </Box>
        </Box>
    );
};

export default Emotions;
