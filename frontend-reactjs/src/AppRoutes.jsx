import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Habits from "./pages/journal/Habits";
import Mood from "./pages/journal/Mood";
import Reflections from "./pages/journal/Reflections";

import JournalSaved from "./pages/journal/JournalSaved";
import HabitsSaved from "./pages/journal/HabitsSaved";
import Emotions from "./pages/journal/Emotions";
import JournalPage from "./pages/journal/JournalPage";
import ReadPage from "./pages/journal/ReadPage";
import JournalDetailPage from "./pages/journal/JournalDetailPage";
import ClientsPage from "./pages/counsellor/ClientsPage";
import InvitePage from "./pages/counsellor/InvitePage";
import PendingInvitesPage from "./pages/counsellor/PendingInvitesPage";


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
                <Route path="/clients" element={<ClientsPage />} />
                <Route path="/journal/:clientId" element={<JournalPage />} />
                <Route path="/invites/sendinvites" element={<InvitePage />} />
                <Route path="/invites" element={<PendingInvitesPage />} />
                <Route path="/read/detail/:journalUserId/:entryId" element={<JournalDetailPage />} />
                <Route path="/counsellor/login" element={<CounsellorLogin />} />
                <Route path="/read/:userId" element={<ReadPage />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;