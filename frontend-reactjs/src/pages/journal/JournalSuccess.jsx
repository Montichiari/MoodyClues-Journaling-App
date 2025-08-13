// src/pages/journal/JournalSuccess.jsx
import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Sidenav from "../../components/Sidenav.jsx";
import { Box, Toolbar, Switch, FormControlLabel } from "@mui/material";
import axios from "axios";

const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

export default function JournalSuccess() {
  const navigate = useNavigate();
  const { state } = useLocation();

  // message / navigation
  const message  = state?.message || "Journal entry submitted successfully.";
  const next     = state?.next || "/home";

  // emotions passed via navigation or (fallback) sessionStorage set by JournalEntry
  const navEmotions = Array.isArray(state?.emotions) ? state.emotions : null;
  const ssEmotions  = (() => {
    try {
      const raw = sessionStorage.getItem("last_journal_emotions");
      const arr = JSON.parse(raw || "[]");
      return Array.isArray(arr) ? arr : [];
    } catch { return []; }
  })();
  const emotions = navEmotions ?? ssEmotions;

  // current visibility preference (mirrors backend, cached in localStorage)
  const [showEmotion, setShowEmotion] = useState(localStorage.getItem("showEmotion") === "true");
  const [toggling, setToggling] = useState(false);

  // guard
  useEffect(() => {
    const ok = localStorage.getItem("isLoggedIn") === "true";
    if (!ok) navigate("/login", { replace: true });
  }, [navigate]);

  const todayLine = useMemo(() => {
    const d = new Date();
    const day = d.getDate();
    const ord = day % 10 === 1 && day !== 11 ? "st"
              : day % 10 === 2 && day !== 12 ? "nd"
              : day % 10 === 3 && day !== 13 ? "rd" : "th";
    const month = d.toLocaleString("en-GB", { month: "long" });
    return `Today is ${day}${ord} ${month} ${d.getFullYear()}.`;
  }, []);

  async function handleToggle() {
    const uid = localStorage.getItem("userId");
    if (!uid) { navigate("/login", { replace: true }); return; }

    try {
      setToggling(true);
      // Hit backend toggle endpoint (no body)
      await axios.put(
        `${API_BASE}/api/user/toggle-emotion/${encodeURIComponent(uid)}`,
        null,
        { withCredentials: true, timeout: 10000 }
      );
      // Flip local state + cache so rest of session reflects new preference
      const nextVal = !showEmotion;
      setShowEmotion(nextVal);
      localStorage.setItem("showEmotion", String(nextVal));
    } catch (e) {
      alert(e?.response?.data?.message || "Failed to update preference.");
    } finally {
      setToggling(false);
    }
  }

  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <div className="max-w-3xl">
          <h1 className="text-4xl font-extrabold tracking-tight text-gray-900">{todayLine}</h1>

          <p className="mt-4 text-green-700 font-medium">{message}</p>

          {/* Toggle (Material UI) */}
          <div className="mt-3">
            <FormControlLabel
              control={
                <Switch
                  checked={showEmotion}
                  onChange={handleToggle}
                  disabled={toggling}
                />
              }
              label={showEmotion ? "Emotions Visible" : "Emotions Hidden"}
            />
          </div>

          {/* Emotions preview — only if allowed AND present */}
          {showEmotion && Array.isArray(emotions) && emotions.length > 0 && (
            <div className="mt-3 text-sm text-gray-700">
              It seems like you felt:&nbsp;
              {emotions.map((e) => (
                <span
                  key={e}
                  className="inline-block px-2 py-1 mr-2 mb-2 rounded-full border border-gray-300"
                >
                  {e}
                </span>
              ))}
            </div>
          )}

          <div className="flex justify-end pt-6">
            <button
              onClick={() => navigate(next, { replace: true })}
              className="px-5 py-2.5 rounded-lg text-white bg-black hover:bg-gray-900 transition"
            >
              Go Home
            </button>
          </div>
        </div>
      </Box>
    </Box>
  );
}
