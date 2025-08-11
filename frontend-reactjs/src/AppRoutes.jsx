import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Habits from "./pages/journal/Habits";
import Mood from "./pages/journal/Mood";
import Reflections from "./pages/journal/Reflections";

import JournalSaved from "./pages/journal/JournalSaved";
import HabitsSaved from "./pages/journal/HabitsSaved";
import Emotions from "./pages/journal/Emotions";

import Dashboard from "./components/Dashboard/Dashboard.jsx";
import UserDashboard from "./pages/UserDashboard.jsx";
import CounsellorLogin from "./pages/CounsellorLogin.jsx";
import Home from "./pages/Home.jsx";
import ApiSmokeTest from "./pages/AppSmokeTest.jsx";


const AppRoutes = () => {
    return (
        <Router>
            <Routes>

                <Route path="/" element={<Navigate to="/journal/mood" />} />


                {/*Journal user routes*/}
                <Route path="/" element={<Navigate to="/ApiSmokeTest" />} />
                <Route path="/ApiSmokeTest" element={<ApiSmokeTest />} />

                <Route path="/login" element={<Login />} />

                <Route path="/journal/mood" element={<Mood />} />
                <Route path="/journal/habits" element={<Habits />} />
                <Route path="/journal/reflections" element={<Reflections />} />
                <Route path="/journal/complete/journal" element={<JournalSaved />} />
                <Route path="/journal/complete/habits" element={<HabitsSaved />} />
                <Route path="/journal/emotions" element={<Emotions />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;