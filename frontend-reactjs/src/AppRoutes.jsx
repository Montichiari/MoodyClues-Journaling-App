import { BrowserRouter as Router, Routes, Route, Navigate, Outlet, useLocation } from "react-router-dom";
import Login from "./pages/Login";
import Habits from "./pages/journal/Habits";
import Mood from "./pages/journal/Mood";
import Reflections from "./pages/journal/Reflections";
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
                </Route>

                {/* user-protected */}
                <Route element={<RequireUserAuth />}>
                    <Route path="/home" element={<Home />} />
                    <Route path="/journal/mood" element={<Mood />} />
                    <Route path="/journal/habits" element={<Habits />} />
                    <Route path="/journal/reflections" element={<Reflections />} />
                    <Route path="/dashboard" element={<UserDashboard />} />
                </Route>

                {/* counsellor-protected */}
                <Route element={<RequireCounsellorAuth />}>
                    <Route path="/counsellor/home" element={<CounsellorHome />} />
                    <Route path="/counsellor/dashboard" element={<CounsellorDashboard />} />
                    <Route path="/counsellor/invite" element={<CounsellorInviteClients />} />
                    <Route path="/counsellor/clients" element={<ClientsPage />} />
                    <Route path="/counsellor/requests" element={<PendingInvitesPage />} />
                </Route>

                <Route path="/logout" element={<Logout />} />

                {/* 404 */}
                <Route path="*" element={<NotFound />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;
