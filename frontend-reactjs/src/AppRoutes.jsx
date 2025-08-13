import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Habits from "./pages/journal/Habits";
//import Mood from "./pages/journal/Mood";
//import Reflections from "./pages/journal/Reflections";
import Dashboard from "./components/Dashboard/Dashboard.jsx";
import UserDashboard from "./pages/UserDashboard.jsx";
import CounsellorLogin from "./pages/CounsellorLogin.jsx";
import Home from "./pages/Home.jsx";
import ApiSmokeTest from "./pages/AppSmokeTest.jsx";
import JournalEntry from "./pages/journal/JournalEntry.jsx";
import JournalSuccess from "./pages/journal/JournalSuccess.jsx";
import HabitsSuccess from "./pages/journal/HabitsSuccess.jsx";
import Read from "./pages/journal/Read.jsx";
import ReadDetails from "./pages/journal/ReadDetails.jsx";
import HabitsRecord from "./pages/journal/HabitsRecord.jsx";
import Invites from "./pages/Invites.jsx";



const AppRoutes = () => {
    return (
        <Router>
            <Routes>

                {/*Journal user routes*/}
                <Route path="/" element={<Navigate to="/journal/entry" />} />
                <Route path="/ApiSmokeTest" element={<ApiSmokeTest />} />
                <Route path="/login" element={<Login />} />
                <Route path="/home" element={<Home />} />
                
                <Route path="/journal/entry" element={<JournalEntry />} />
                <Route path="/journal/success" element={<JournalSuccess />} />

                <Route path="/journal/habits" element={<Habits />} />
                <Route path="/journal/habits/success" element={<HabitsSuccess />} />
                <Route path="/journal/habits/records" element={<HabitsRecord />} />

                <Route path="/read" element={<Read />} />
                <Route path="/read/:id" element={<ReadDetails />} />

                <Route path="/invites" element={<Invites />} />
                
                <Route path="/dashboard" element={<UserDashboard />} />

                {/*Counsellor routes*/}
                <Route path="/counsellor/login" element={<CounsellorLogin />} />


            </Routes>
        </Router>
    );
};

export default AppRoutes;