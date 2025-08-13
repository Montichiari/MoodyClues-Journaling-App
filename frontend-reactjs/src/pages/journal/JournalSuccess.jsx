import { useEffect, useMemo } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Sidenav from "../../components/Sidenav.jsx";
import { Box, Toolbar } from "@mui/material";

export default function JournalSuccess() {
  const navigate = useNavigate();
  const { state } = useLocation();

  const message  = state?.message || "Journal entry submitted successfully.";
  const next     = state?.next || "/home";
  const emotions = Array.isArray(state?.emotions) ? state.emotions : [];

  // show/hide flag set at login: localStorage.setItem("showEmotion", response.data.showEmotion)
  const showEmotion = localStorage.getItem("showEmotion") === "true";

  useEffect(() => {
    const ok = localStorage.getItem("isLoggedIn") === "true";
    if (!ok) navigate("/login", { replace: true });
  }, [navigate]);

  const todayLine = useMemo(() => {
    const d = new Date();
    const day = d.getDate();
    const ord = day % 10 === 1 && day !== 11 ? "st" :
                day % 10 === 2 && day !== 12 ? "nd" :
                day % 10 === 3 && day !== 13 ? "rd" : "th";
    const month = d.toLocaleString("en-GB", { month: "long" });
    return `Today is ${day}${ord} ${month} ${d.getFullYear()}.`;
  }, []);

  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <div className="max-w-3xl">
          <h1 className="text-4xl font-extrabold tracking-tight text-gray-900">{todayLine}</h1>

          <p className="mt-4 text-green-700 font-medium">{message}</p>

          {/* Emotions preview — only if user allows AND we have some */}
          {showEmotion && emotions.length > 0 && (
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
