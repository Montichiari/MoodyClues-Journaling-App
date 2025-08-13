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

const Reflections = () => {
    const navigate = useNavigate();
    const today = formatDate(new Date());

    const [title, setTitle] = useState('');
    const [body, setBody] = useState('');

    const handleNext = () => {
        const reflectionData = { title, body };
        sessionStorage.setItem('journal_reflections', JSON.stringify(reflectionData));
        navigate('/journal/emotions');
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
                <h1 className="text-3xl font-semibold mb-6">Today is {today}.</h1>

                <div className="text-lg mb-8">Write about the day’s events and feelings.</div>

                <div className="space-y-6 max-w-xl">
          <textarea
              style={{ marginLeft: '100px' }}
              placeholder="Start with a title to remember this day."
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full border rounded px-3 py-2"
              rows={2}
          />
                    <textarea
                        style={{ marginLeft: '100px' }}
                        placeholder="Today, I..."
                        value={body}
                        onChange={(e) => setBody(e.target.value)}
                        className="w-full border rounded px-3 py-4"
                        rows={10}
                    />
                </div>

                <div className="mt-10">
                    <button
                        onClick={handleNext}
                        className="border px-5 py-2 rounded"
                    >
                        Save & Continue
                    </button>
                </div>
            </Box>
        </Box>
    );
};

export default Reflections;
