import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Sidenav from "../../components/Sidenav.jsx";
import { Box, Toolbar } from "@mui/material";
import axios from "axios";

const MOOD_LABELS = {
  1: "Very Bad",
  2: "Bad",
  3: "Neutral",
  4: "Good",
  5: "Very Good",
};

function formatOrdinalDate(d) {
  const dt = new Date(d);
  if (Number.isNaN(dt.getTime())) return "—";
  const day = dt.getDate();
  const ord =
    day % 10 === 1 && day !== 11 ? "st" :
    day % 10 === 2 && day !== 12 ? "nd" :
    day % 10 === 3 && day !== 13 ? "rd" : "th";
  const month = dt.toLocaleString("en-GB", { month: "long" });
  return `${day}${ord} ${month} ${dt.getFullYear()}`;
}

// units
const fmtHours = (v) => (v == null || v === "") ? "—" : `${v} Hours`;
const fmtLitres = (v) => (v == null || v === "") ? "—" : `${v} Litres`;

// strict same-day match helpers (local timezone)
const toLocalYMD = (d) => {
  const dt = new Date(d);
  return Number.isNaN(dt.getTime())
    ? ""
    : [dt.getFullYear(), dt.getMonth(), dt.getDate()].join("-");
};
const sameLocalDay = (a, b) => toLocalYMD(a) === toLocalYMD(b);

export default function ReadDetails() {
  const { id } = useParams(); // /read/:id (entryId)
  const navigate = useNavigate();

  const [item, setItem] = useState(null);     // the journal entry
  const [habits, setHabits] = useState(null); // matched habits (same local day)
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");

  const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";
  const showEmotion = localStorage.getItem("showEmotion") === "true";

  // route guard
  useEffect(() => {
    const loggedIn = localStorage.getItem("isLoggedIn") === "true";
    const uid = localStorage.getItem("userId");
    if (!loggedIn || !uid) navigate("/login", { replace: true });
  }, [navigate]);

  // load journal + (strict) same-day habits
  useEffect(() => {
    const uid = localStorage.getItem("userId");
    if (!id || !uid) return;

    const controller = new AbortController();

    async function fetchData() {
      try {
        setLoading(true);
        setErr("");

        const entryId = encodeURIComponent(String(id).trim());
        const userId  = encodeURIComponent(String(uid).trim());

        // 1) Journal entry
        const jr = await axios.get(
          `${API_BASE}/api/journal/${entryId}/${userId}`,
          { withCredentials: true, signal: controller.signal }
        );
        const entry = jr.data || null;
        setItem(entry);

        // 2) Habits (strict same-day): GET /api/habits/all/{userId}
        let matched = null;
        if (entry?.createdAt || entry?.date) {
          const hrAll = await axios.get(
            `${API_BASE}/api/habits/all/${userId}`,
            { withCredentials: true, signal: controller.signal }
          );
          const list = Array.isArray(hrAll.data) ? hrAll.data : [];
          matched = list.find(h => sameLocalDay(h.createdAt, entry.createdAt || entry.date)) || null;
        }

        setHabits(matched);
      } catch (e) {
        if (axios.isCancel?.(e)) return;
        if (e.response?.status === 404) {
          setErr("Journal entry not found.");
        } else if (e.response?.status === 401 || e.response?.status === 403) {
          navigate("/login", { replace: true });
        } else {
          setErr("Failed to load the journal entry.");
        }
      } finally {
        setLoading(false);
      }
    }

    fetchData();
    return () => controller.abort();
  }, [API_BASE, id, navigate]);

  // normalize emotions (["happy", ...] or [{emotionLabel: "happy"}])
  const emotions = useMemo(() => {
    if (!item?.emotions) return [];
    if (Array.isArray(item.emotions)) {
      return item.emotions
        .map(e => (typeof e === "string" ? e : e?.emotionLabel))
        .filter(Boolean);
    }
    return [];
  }, [item]);

  
  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />

        <div className="max-w-3xl">
          <button
            onClick={() => navigate(-1)}
            className="text-sm text-gray-600 hover:underline"
          >
            ← Back
          </button>

          {/* Centered section heading */}
          <h2 className="mt-2 text-lg font-medium text-gray-900 text-center">Your Journal</h2>

          {loading && <div className="mt-6 text-gray-500 text-sm">Loading…</div>}
          {!loading && err && <div className="mt-6 text-sm text-rose-600">{err}</div>}

          {!loading && !err && item && (
            <>
              {/* Title — centered */}
              <h1 className="mt-4 text-3xl font-semibold text-gray-900 text-center">
                {item.entryTitle || "(Untitled)"}
              </h1>

              {/* Date — centered */}
              <div className="mt-2 text-sm text-gray-500 text-center">
                {formatOrdinalDate(item.createdAt || item.date)}
              </div>

              {/* Mood (left aligned) */}
              <div className="mt-8 text-gray-900 text-left">
                <span className="font-medium">Mood:</span>{" "}
                <span>{MOOD_LABELS[item.mood] || "—"}</span>
              </div>

              {/* Sleep / Water / Work (left aligned) */}
              <div className="mt-2 flex flex-wrap gap-x-8 gap-y-2 text-gray-900 text-left">
                <div>
                  <span className="font-medium">Sleep:</span>{" "}
                  <span>{fmtHours(habits?.sleep ?? habits?.sleepHours)}</span>
                </div>
                <div>
                  <span className="font-medium">Water:</span>{" "}
                  <span>{fmtLitres(habits?.water ?? habits?.waterLitres)}</span>
                </div>
                <div>
                  <span className="font-medium">Work:</span>{" "}
                  <span>{fmtHours(habits?.workHours ?? habits?.work)}</span>
                </div>
              </div>

              {/* Optional: no habits message (left aligned) */}
              {!habits && (
                <div className="mt-2 text-sm text-gray-500 text-left">
                  No habits logged for this day.
                </div>
              )}

              {/* Emotions (left aligned, inline to the right of the label) */}
              {showEmotion && emotions.length > 0 && (
                <div className="mt-6 flex flex-wrap items-center gap-3 text-left">
                  <div className="font-medium text-gray-900">You felt:</div>
                  <div className="flex flex-wrap gap-3">
                    {emotions.map((e) => (
                      <span
                        key={e}
                        className="inline-flex items-center gap-1 px-3 py-1 rounded-full border border-gray-300 text-sm"
                      >
                        {e}
                      </span>
                    ))}
                  </div>
                </div>
              )}

              {/* Entry text (read-only) */}
              <div className="mt-6">
                <textarea
                  readOnly
                  value={item.entryText || ""}
                  rows={8}
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none bg-white"
                />
              </div>
            </>
          )}
        </div>
      </Box>
    </Box>
  );


}
