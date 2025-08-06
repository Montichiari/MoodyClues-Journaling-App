import { Box, Toolbar } from '@mui/material';
import Sidenav from '../../components/Sidenav';
import { useNavigate } from 'react-router-dom';

const drawerWidth = 200;

const HabitsSaved = () => {
    const navigate = useNavigate();
    const handleContinue = () => {
        navigate("/journal/reflections");
    };
    return (
        <Box sx={{ display: 'flex' }}>
            <Sidenav />
            <Box component="main" sx={{ flexGrow: 1, p: 3, ml: `${drawerWidth}px` }} className="px-16 py-10">
                <Toolbar />
                <h1 className="text-3xl font-semibold mb-6">Today is {new Date().toDateString()}.</h1>
                <p>Your habits have been successfully logged.</p>
                <div className="mt-8">
                    <button
                        className="border px-4 py-2 rounded"
                        onClick={handleContinue}
                    >
                        Save & Continue
                    </button>
                </div>
            </Box>
        </Box>
    );
};

export default HabitsSaved;
