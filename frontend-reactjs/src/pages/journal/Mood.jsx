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

    const [morning, setMorning] = useState(0);
    const [afternoon, setAfternoon] = useState(0);
    const [evening, setEvening] = useState(0);

    const handleNext = () => {
        const moodData = { morning, afternoon, evening };
        sessionStorage.setItem('journal_mood', JSON.stringify(moodData));
        navigate('/journal/complete/journal');
    };

    const ratingButtons = (selected, setSelected) => (
        <div className="flex gap-2 mt-2">
            {moodOptions.map(({ label, value }) => (
                <button
                    key={label}
                    onClick={() => setSelected(value)}
                    style={{
                        padding: '0.5rem 1rem',
                        backgroundColor: selected === value ? '#4acaa8' : '#f0f0f0',
                        color: selected === value ? 'white' : 'black',
                        border: '1px solid #ccc',
                        borderRadius: '4px',
                        cursor: 'pointer',
                    }}
                >
                    {label}
                </button>
            ))}
        </div>
    );

    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav />
            <Box component="main" sx={{ flexGrow: 1, p: 3, ml: `${drawerWidth}px` }} className="px-16 py-10">
                <Toolbar />
                <h1 className="text-3xl font-semibold mb-6">Today is {today}.</h1>

                <div className="text-lg mb-8">Rate how you felt throughout the day.</div>

                <div className="space-y-8">
                    <div>
                        <label className="block mb-1">Morning:</label>
                        {ratingButtons(morning, setMorning)}
                    </div>
                    <div>
                        <label className="block mb-1">Afternoon:</label>
                        {ratingButtons(afternoon, setAfternoon)}
                    </div>
                    <div>
                        <label className="block mb-1">Evening:</label>
                        {ratingButtons(evening, setEvening)}
                    </div>
                </div>

                <div className="mt-12">
                    <button onClick={handleNext} className="border px-5 py-2 rounded">Continue</button>
                </div>
            </Box>
        </Box>
    );
};

export default Mood;
