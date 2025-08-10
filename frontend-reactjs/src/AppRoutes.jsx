import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Habits from "./pages/journal/Habits";
import Mood from "./pages/journal/Mood";
import Reflections from "./pages/journal/Reflections";
import JournalSaved from "./pages/journal/JournalSaved";
import HabitsSaved from "./pages/journal/HabitsSaved";
import Emotions from "./pages/journal/Emotions";
import ReadList from "./pages/journal/ReadList";
import ReadDetail from "./pages/journal/ReadDetail";

const AppRoutes = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/journal/mood" />} />
                <Route path="/login" element={<Login />} />

                <Route path="/journal/mood" element={<Mood />} />
                <Route path="/journal/habits" element={<Habits />} />
                <Route path="/journal/reflections" element={<Reflections />} />
                <Route path="/journal/complete/journal" element={<JournalSaved />} />
                <Route path="/journal/complete/habits" element={<HabitsSaved />} />
                <Route path="/journal/emotions" element={<Emotions />} />
                <Route path="/journal/read" element={<ReadList />} />
                <Route path="/journal/read/:entryId" element={<ReadDetail />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;