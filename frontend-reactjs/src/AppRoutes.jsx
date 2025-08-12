import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Habits from "./pages/journal/Habits";
import Mood from "./pages/journal/Mood";
import Reflections from "./pages/journal/Reflections";
import Dashboard from "./components/Dashboard/Dashboard.jsx";
import UserDashboard from "./pages/UserDashboard.jsx";
import CounsellorLogin from "./pages/CounsellorLogin.jsx";
import Home from "./pages/Home.jsx";
import ApiSmokeTest from "./pages/AppSmokeTest.jsx";
import Logout from "./pages/Logout.jsx";

const AppRoutes = () => {
    return (
        <Router>
            <Routes>

                {/*Journal user routes*/}
                <Route path="/" element={<Navigate to="/journal/mood" />} />
                <Route path="/ApiSmokeTest" element={<ApiSmokeTest />} />
                <Route path="/login" element={<Login />} />
                <Route path="/home" element={<Home />} />
                <Route path="/journal/mood" element={<Mood />} />
                <Route path="/journal/habits" element={<Habits />} />
                <Route path="/journal/reflections" element={<Reflections />} />
                <Route path="/dashboard" element={<UserDashboard />} />
                <Route path="/logout" element={<Logout />} />

                {/*Counsellor routes*/}
                <Route path="/counsellor/login" element={<CounsellorLogin />} />


            </Routes>
        </Router>
    );
};

export default AppRoutes;