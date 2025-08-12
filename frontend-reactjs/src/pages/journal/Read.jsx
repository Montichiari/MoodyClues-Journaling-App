import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidenav from "../../components/Sidenav.jsx";
import { Box, Toolbar } from "@mui/material";
import axios from "axios";

// e.g. "8 Aug 2025, 14:15"
function formatDT(d) {
  const dt = new Date(d);
  if (Number.isNaN(dt.getTime())) return "—";
  const day = dt.getDate();
  const mon = dt.toLocaleString("en-GB", { month: "short" });
  const yr  = dt.getFullYear();
  const hh  = String(dt.getHours()).padStart(2, "0");
  const mm  = String(dt.getMinutes()).padStart(2, "0");
  return `${day} ${mon} ${yr}, ${hh}:${mm}`;
}

export default function Read() {
  const navigate = useNavigate();
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");
  const [q, setQ] = useState("");

  const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

  // route guard
  useEffect(() => {
    const loggedIn = localStorage.getItem("isLoggedIn") === "true";
    const uid = localStorage.getItem("userId");
    if (!loggedIn || !uid) {
      navigate("/login", { replace: true });
    }
  }, [navigate]);

  // load entries
  useEffect(() => {
    const uid = localStorage.getItem("userId");
    if (!uid) return;

    const controller = new AbortController();

    (async () => {
      try {
        setLoading(true);
        setErr("");

        // /api/journal/all/{userId}
        const res = await axios.get(`${API_BASE}/api/journal/all/${uid}`, {
          withCredentials: true,
          signal: controller.signal,
        });

        const rows = Array.isArray(res.data) ? res.data : [];
        setEntries(rows);
      } catch (e) {
        if (axios.isCancel?.(e)) return;
        console.error("Load journals error:", e.response?.data || e.message);
        if (e.response?.status === 401 || e.response?.status === 403) {
          navigate("/login", { replace: true });
          return;
        }
        setErr("Failed to load your journal.");
      } finally {
        setLoading(false);
      }
    })();

    return () => controller.abort();
  }, [API_BASE, navigate]);

  // search + chronological sort (newest first)
  const view = useMemo(() => {
    const s = q.trim().toLowerCase();
    const filtered = s
      ? entries.filter(e => (e.entryTitle || e.title || "").toLowerCase().includes(s))
      : entries;

    // sort by createdAt/date DESC
    return [...filtered].sort((a, b) => {
      const ta = new Date(a.createdAt || a.date).getTime();
      const tb = new Date(b.createdAt || b.date).getTime();
      const A = Number.isNaN(ta) ? -Infinity : ta;
      const B = Number.isNaN(tb) ? -Infinity : tb;
      return B - A;
    });
  }, [entries, q]);

  
  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />

        <div className="max-w-3xl">
          {/* Header with View Habits button */}
          <div className="flex items-center justify-between">
            <h1 className="text-2xl sm:text-3xl font-semibold text-gray-900">Your Journal</h1>
            <button
              onClick={() => navigate("/journal/habits/records")}
              className="px-3 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 text-sm"
            >
              View Habits
            </button>
          </div>

          {/* Search */}
          <div className="mt-4">
            <input
              type="text"
              value={q}
              onChange={(e) => setQ(e.target.value)}
              placeholder="Search title..."
              className="w-full max-w-lg border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>

          {/* Table */}
          <div className="mt-6 border-t border-gray-200">
            <table className="w-full table-auto">
              <colgroup>
                <col style={{ width: "200px" }}/>{/* Date+time */}
                <col/>{/* Title */}
              </colgroup>

              <thead>
                <tr className="text-sm font-medium text-gray-500">
                  <th className="py-3 px-2 text-left">Date</th>
                  <th className="py-3 px-2 text-left">Title</th>
                </tr>
              </thead>

              <tbody className="divide-y divide-gray-200">
                {loading && (
                  <tr>
                    <td className="py-6 px-2 text-sm text-gray-500 text-left" colSpan={2}>
                      Loading…
                    </td>
                  </tr>
                )}

                {!loading && view.length === 0 && !err && (
                  <tr>
                    <td className="py-6 px-2 text-sm text-gray-500 text-left" colSpan={2}>
                      No entries found.
                    </td>
                  </tr>
                )}

                {!loading && view.map((e) => {
                  const entryId = e.entryId ?? e.id; // key + route id
                  return (
                    <tr
                      key={entryId ?? `row-${Math.random()}`}
                      className="hover:bg-gray-50 cursor-pointer"
                      onClick={() => {
                        if (!entryId) return;
                        navigate(`/read/${encodeURIComponent(String(entryId).trim())}`);
                      }}
                    >
                      <td className="py-4 px-2 text-gray-500 text-left">
                        {formatDT(e.createdAt || e.date)}
                      </td>
                      <td className="py-4 px-2 text-gray-900 text-left">
                        {e.entryTitle || e.title || "(Untitled)"}
                      </td>
                    </tr>
                  );
                })}

                {err && (
                  <tr>
                    <td className="py-3 px-2 text-sm text-rose-600 text-left" colSpan={2}>
                      {err}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </Box>
    </Box>
  );


}
