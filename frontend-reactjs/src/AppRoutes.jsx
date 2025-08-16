import { BrowserRouter as Router, Routes, Route, Navigate, Outlet, useLocation } from "react-router-dom";
import Login from "./pages/Login";
import Habits from "./pages/journal/Habits";
import UserDashboard from "./pages/UserDashboard.jsx";
import CounsellorLogin from "./pages/CounsellorLogin.jsx";
import Home from "./pages/Home.jsx";
import Logout from "./pages/Logout.jsx";
import CounsellorDashboard from "./pages/CounsellorDashboard.jsx";
import CounsellorHome from "./pages/CounsellorHome.jsx";
import CounsellorInviteClients from "./pages/CounsellorInviteClients.jsx";
import NotFound from "./pages/NotFound.jsx";
import ClientsPage from "./pages/counsellor/ClientsPage.jsx";
import PendingInvitesPage from "./pages/counsellor/PendingInvitesPage.jsx";
import JournalEntry from "./pages/journal/JournalEntry.jsx";
import HabitsSuccess from "./pages/journal/HabitsSuccess.jsx";
import HabitsRecord from "./pages/journal/HabitsRecord.jsx";
import Read from "./pages/journal/Read.jsx";
import ReadDetails from "./pages/journal/ReadDetails.jsx";
import JournalSuccess from "./pages/journal/JournalSuccess.jsx";
import Invites from "./pages/Invites.jsx";
import Register from "./pages/Register.jsx";
import CounsellorRegistration from "./pages/CounsellorRegistration.jsx";

const RequireUserAuth = () => {
    const location = useLocation();
    const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
    const userId = localStorage.getItem("userId");
    return (isLoggedIn && userId)
        ? <Outlet />
        : <Navigate to="/login" replace state={{ from: location }} />;
};

const RequireCounsellorAuth = () => {
    const location = useLocation();
    const counsellorId = localStorage.getItem("counsellorId");
    return counsellorId
        ? <Outlet />
        : <Navigate to="/counsellor/login" replace state={{ from: location }} />;
};

const RedirectIfAuthed = () => {
    const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
    const userId = localStorage.getItem("userId");
    const counsellorId = localStorage.getItem("counsellorId");
    if (isLoggedIn && userId) return <Navigate to="/home" replace />;
    if (counsellorId) return <Navigate to="/counsellor/home" replace />;
    return <Outlet />;
};




const AppRoutes = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login" />} />

                {/* login pages */}
                <Route element={<RedirectIfAuthed />}>
                    <Route path="/login" element={<Login />} />
                    <Route path="/counsellor/login" element={<CounsellorLogin />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/counsellor/register" element={<CounsellorRegistration />} />

                </Route>

                {/* user-protected */}
                <Route element={<RequireUserAuth />}>
                    <Route path="/home" element={<Home />} />
                    <Route path="/journal/entry" element={<JournalEntry />} />
                    <Route path="/journal/entry/success" element={<JournalSuccess />} />

                    <Route path="/journal/habits" element={<Habits />} />
                    <Route path="/journal/habits/success" element={<HabitsSuccess />} />
                    <Route path="/journal/habits/records" element={<HabitsRecord />} />

                    <Route path="/journal/read" element={<Read />} />
                    <Route path="/journal/read/:userId/:entryId" element={<ReadDetails />} />

                    <Route path="/invites" element={<Invites />} />

                    <Route path="/dashboard" element={<UserDashboard />} />
                </Route>

                {/* counsellor-protected */}
                <Route element={<RequireCounsellorAuth />}>
                    <Route path="/counsellor/home" element={<CounsellorHome />} />
                    <Route path="/counsellor/dashboard" element={<CounsellorDashboard />} />
                    <Route path="/counsellor/invite" element={<CounsellorInviteClients />} />
                    <Route path="/counsellor/clients" element={<ClientsPage />} />
                    <Route path="/counsellor/requests" element={<PendingInvitesPage />} />
                    <Route path="/counsellor/read/:userId" element={<Read />} />
                    <Route path="/counsellor/read/:userId/:entryId" element={<ReadDetails />} />

                </Route>

                <Route path="/logout" element={<Logout />} />

                {/* 404 */}
                <Route path="*" element={<NotFound />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;
