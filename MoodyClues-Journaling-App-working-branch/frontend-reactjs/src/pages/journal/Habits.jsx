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



const Habits = () => {
    const [sleep, setSleep] = useState(0);
    const [water, setWater] = useState(0);
    const [work, setWork] = useState(0);

    const navigate = useNavigate();

    const today = formatDate(new Date());

    const handleNext = () => {
        const habitsData = {
            sleep,
            water,
            work
        };

        sessionStorage.setItem("journal_habits", JSON.stringify(habitsData));
        navigate("/journal/reflections");
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav />
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    ml: `${drawerWidth}px`,
                    px: 8,
                    py: 10,
                }}
            >
                <Toolbar />
                <div className="max-w-2xl">
                    <h1 className="text-3xl font-semibold mb-4">Today is {today}.</h1>

                    <div className="text-lg mb-6">Log your lifestyle choices for the day.</div>

                    <div className="space-y-6">
                        <div>
                            <label className="block mb-2">How much did you sleep the previous night?</label>
                            <input
                                type="number"
                                step="0.1"
                                min="0"
                                max="12"
                                value={sleep}
                                onChange={(e) => setSleep(Number(e.target.value))}
                                className="w-full border px-3 py-2 rounded"
                            />
                        </div>

                        <div>
                            <label className="block mb-2">How much water did you drink today (litres)?</label>
                            <input
                                type="number"
                                step="0.1"
                                min="0"
                                max="10"
                                value={water}
                                onChange={(e) => setWater(Number(e.target.value))}
                                className="w-full border px-3 py-2 rounded"
                            />
                        </div>

                        <div>
                            <label className="block mb-2">How many hours did I work today?</label>
                            <input
                                type="number"
                                step="0.1"
                                min="0"
                                max="16"
                                value={work}
                                onChange={(e) => setWork(Number(e.target.value))}
                                className="w-full border px-3 py-2 rounded"
                            />
                        </div>
                    </div>

                    <div className="mt-10">
                        <button className="border px-4 py-2 rounded"
                        onClick={handleNext}>Continue</button>
                    </div>
                </div>
            </Box>
        </Box>
    );
};

export default Habits;
