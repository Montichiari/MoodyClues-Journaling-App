import { Box, Toolbar } from '@mui/material';
import Sidenav from '../../components/Sidenav';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const drawerWidth = 200;

const formatDate = (date) => {
    const day = date.getDate();
    const month = date.toLocaleString('default', { month: 'long' });
    const year = date.getFullYear();
    return `${day} ${month} ${year}`;
};

const moodOptions = [
    { label: 'Very Bad', value: 1 },
    { label: 'Bad', value: 2 },
    { label: 'Neutral', value: 3 },
    { label: 'Good', value: 4 },
    { label: 'Very Good', value: 5 },
];

const Mood = () => {
    const navigate = useNavigate();
    const today = formatDate(new Date());

    const [mood, setMood] = useState(0);

    const handleNext = () => {
        sessionStorage.setItem('journal_mood', JSON.stringify({ mood }));
        navigate('/journal/complete/journal');
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav />
            <Box
                component="main"
                sx={{ flexGrow: 1, p: 3, ml: `${drawerWidth}px` }}
                className="px-16 py-10"
            >
                <Toolbar />
                <h1 className="text-2xl font-semibold mb-4">Today is {today}.</h1>
                <p className="mb-4 text-lg">
                    Write into your journal, and log how you feel right now.
                </p>
                <p className="mb-6 text-md">How do you feel?</p>

                <div className="flex gap-2 " style={{ marginLeft: '150px' }}>
                    {moodOptions.map(({ label, value }) => (
                        <button
                            key={label}
                            onClick={() => setMood(value)}
                            style={{
                                padding: '0.5rem 1rem',
                                backgroundColor: mood === value ? '#fca5a5' : '#f0f0f0',
                                color: mood === value ? 'white' : 'black',
                                border: '1px solid #ccc',
                                borderRadius: '4px',
                                cursor: 'pointer',
                            }}
                        >
                            {label}
                        </button>
                    ))}
                </div>


                <button
                    onClick={handleNext}
                    className="border px-5 py-2 rounded"
                    style={{ marginTop: '20px' }}
                >
                    Continue
                </button>
            </Box>
        </Box>
    );
};

export default Mood;
