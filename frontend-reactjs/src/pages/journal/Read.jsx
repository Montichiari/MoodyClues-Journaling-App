import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import Sidenav from "../../components/Sidenav.jsx";
import SidenavC from "../../components/SidenavC.jsx";
import { Box, Toolbar } from "@mui/material";
import axios from "axios";
import { formatLocalDateTimeSG } from "../../utils/datetime";

export default function Read() {
  const { userId: routeUserId } = useParams(); // userId from route when counsellor views
  const location = useLocation();
  const navigate = useNavigate();

  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");
  const [q, setQ] = useState("");

  const loggedInUser = localStorage.getItem("isLoggedIn") === "true" && !!localStorage.getItem("userId");
  const loggedInCounsellor = !!localStorage.getItem("counsellorId");
  const hasUserId = !!localStorage.getItem("userId"); // for user-only UI like "View Habits"

  const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

  // ---- SG-aware ms extractor (handles naive strings as UTC + 8h) ----
  function toMsSG(input) {
    if (!input) return -Infinity;
    const s = String(input).trim().replace(" ", "T");
    const hasTZ = /[zZ]|[+\-]\d{2}:?\d{2}$/.test(s);
    let d;

    if (hasTZ) {
      d = new Date(s);
    } else {
      const m = s.match(
          /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})(?::(\d{2})(?:\.(\d{1,3}))?)?$/
      );
      if (m) {
        const [_, y, mo, dd, hh, mi, ss = "0", ms = "0"] = m;
        const utc = Date.UTC(+y, +mo - 1, +dd, +hh, +mi, +ss, +`${ms}`.padEnd(3, "0"));
        d = new Date(utc + 8 * 60 * 60 * 1000); // shift to SG
      } else {
        d = new Date(s);
      }
    }
    return Number.isNaN(d?.getTime()) ? -Infinity : d.getTime();
  }

  // Route guard: allow EITHER a user or a counsellor.
  useEffect(() => {
    if (loggedInUser || loggedInCounsellor) return;
    const wantsCounsellor = location.pathname.startsWith("/counsellor/");
    navigate(wantsCounsellor ? "/counsellor/login" : "/login", { replace: true });
  }, [navigate, location.pathname, loggedInUser, loggedInCounsellor]);

  // Load entries for target user (route param for counsellor, LS for user)
  useEffect(() => {
    const uid = localStorage.getItem("userId");
    const targetUserId = routeUserId || uid;

    if (!targetUserId) {
      setErr("Missing user id.");
      setLoading(false);
      return;
    }

    const controller = new AbortController();

    (async () => {
      try {
        setLoading(true);
        setErr("");

        const res = await axios.get(
            `${API_BASE}/api/journal/all/${encodeURIComponent(String(targetUserId).trim())}`,
            { withCredentials: true, signal: controller.signal }
        );

        const rows = Array.isArray(res.data) ? res.data : [];
        setEntries(rows);
      } catch (e) {
        if (axios.isCancel?.(e)) return;
        if (e.response?.status === 401 || e.response?.status === 403) {
          const wantsCounsellor = location.pathname.startsWith("/counsellor/");
          navigate(wantsCounsellor ? "/counsellor/login" : "/login", { replace: true });
          return;
        }
        setErr("Failed to load your journal.");
        console.error("Load journals error:", e.response?.data || e.message);
      } finally {
        setLoading(false);
      }
    })();

    return () => controller.abort();
  }, [API_BASE, navigate, location.pathname, routeUserId]);

  // search + chronological sort (newest first), SG-aware
  const view = useMemo(() => {
    const s = q.trim().toLowerCase();
    const filtered = s
        ? entries.filter(e => (e.entryTitle || e.title || "").toLowerCase().includes(s))
        : entries;

    return [...filtered].sort((a, b) => {
      const ta = toMsSG(a.createdAt || a.date);
      const tb = toMsSG(b.createdAt || b.date);
      return tb - ta;
    });
  }, [entries, q]);

  // Choose correct base path for details depending on who is logged in
  const detailBase = loggedInCounsellor ? "/counsellor/read" : "/journal/read";

  return (
      <Box sx={{ display: "flex" }}>
        {loggedInCounsellor ? <SidenavC /> : <Sidenav />}
        <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
          <Toolbar />

          <div className="max-w-3xl">
            {/* Header with View Habits button (users only) */}
            <div className="flex items-center justify-between">
              <h1 className="text-2xl sm:text-3xl font-semibold text-gray-900">Your Journal</h1>

              {hasUserId && (
                  <button
                      onClick={() => navigate("/journal/habits/records")}
                      className="px-3 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 text-sm"
                  >
                    View Habits
                  </button>
              )}
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
                  <col style={{ width: "200px" }} /> {/* Date+time */}
                  <col /> {/* Title */}
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

                {!loading &&
                    view.map((e) => {
                      const entryId = e.entryId ?? e.id; // key + route id
                      const uid = localStorage.getItem("userId");
                      const userIdForRoute = routeUserId || uid;

                      return (
                          <tr
                              key={entryId ?? `row-${Math.random()}`}
                              className="hover:bg-gray-50 cursor-pointer"
                              onClick={() => {
                                navigate(
                                    `${detailBase}/${userIdForRoute}/${encodeURIComponent(
                                        String(entryId).trim()
                                    )}`
                                );
                              }}
                          >
                            <td className="py-4 px-2 text-gray-500 text-left">
                              {formatLocalDateTimeSG(e.createdAt || e.date)}
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
