import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Habits from "./pages/journal/Habits";
import Mood from "./pages/journal/Mood";
import Reflections from "./pages/journal/Reflections";
import Dashboard from "./components/Dashboard/Dashboard.jsx";
import Home from "./pages/Home.jsx";

const AppRoutes = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/home" />} />
                <Route path="/login" element={<Login />} />

                <Route path="/journal/mood" element={<Mood />} />
                <Route path="/journal/habits" element={<Habits />} />
                <Route path="/journal/reflections" element={<Reflections />} />
                <Route path="/home" element={<Home />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;