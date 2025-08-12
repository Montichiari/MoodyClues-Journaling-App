import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidenav from "../../components/Sidenav.jsx";
import { Box, Toolbar } from "@mui/material";
import axios from "axios";

const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

const fmtDT = (d) => {
  const dt = new Date(d);
  if (Number.isNaN(dt.getTime())) return "—";
  const day = dt.getDate();
  const mon = dt.toLocaleString("en-GB", { month: "short" });
  const yr  = dt.getFullYear();
  const hh  = String(dt.getHours()).padStart(2, "0");
  const mm  = String(dt.getMinutes()).padStart(2, "0");
  return `${day} ${mon} ${yr}, ${hh}:${mm}`;
};

export default function HabitsRecord() {
  const navigate = useNavigate();
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");

  // route guard (keep consistent with your app)
  useEffect(() => {
    const loggedIn = localStorage.getItem("isLoggedIn") === "true";
    const uid = localStorage.getItem("userId");
    if (!loggedIn || !uid) navigate("/login", { replace: true });
  }, [navigate]);

  useEffect(() => {
    const uid = localStorage.getItem("userId");
    if (!uid) return;

    const controller = new AbortController();
    (async () => {
      try {
        setLoading(true);
        setErr("");
        const res = await axios.get(`${API_BASE}/api/habits/all/${encodeURIComponent(uid)}`, {
          withCredentials: true,
          signal: controller.signal,
        });
        const list = Array.isArray(res.data) ? res.data : [];
        // newest first by createdAt
        list.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
        setRows(list);
      } catch (e) {
        if (axios.isCancel?.(e)) return;
        console.error("Load habits records error:", e.response?.data || e.message);
        setErr("Failed to load habits records.");
      } finally {
        setLoading(false);
      }
    })();
    return () => controller.abort();
  }, []);

  const data = useMemo(() => rows, [rows]);

  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <div className="max-w-4xl">
          <div className="flex items-center justify-between">
            <h1 className="text-2xl sm:text-3xl font-semibold text-gray-900">Habits Records</h1>
            <div className="flex items-center gap-2">
              <button
                onClick={() => navigate("/journal/habits")}
                className="px-3 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 text-sm"
              >
                Add/Update Today’s Habits
              </button>
            </div>
          </div>

          <div className="mt-6 border-t border-gray-200">
            <table className="w-full table-auto">
              <colgroup>
                <col style={{ width: "220px" }}/>{/* Created at */}
                <col style={{ width: "120px" }}/>{/* Sleep */}
                <col style={{ width: "120px" }}/>{/* Water */}
                <col style={{ width: "120px" }}/>{/* Work */}
                <col style={{ width: "180px" }}/>{/* Updated at */}
                <col style={{ width: "120px" }}/>{/* Action */}
              </colgroup>

              <thead>
                <tr className="text-sm font-medium text-gray-500">
                  <th className="py-3 px-2 text-left">Date</th>
                  <th className="py-3 px-2 text-left">Sleep (Hours)</th>
                  <th className="py-3 px-2 text-left">Water (Litres)</th>
                  <th className="py-3 px-2 text-left">Work (Hours)</th>
                  <th className="py-3 px-2 text-left">Updated At</th>
                  <th className="py-3 px-2 text-left">Action</th>
                </tr>
              </thead>

              <tbody className="divide-y divide-gray-200">
                {loading && (
                  <tr>
                    <td className="py-6 px-2 text-sm text-gray-500" colSpan={6}>Loading…</td>
                  </tr>
                )}

                {!loading && !err && data.length === 0 && (
                  <tr>
                    <td className="py-6 px-2 text-sm text-gray-500" colSpan={6}>No habits recorded yet.</td>
                  </tr>
                )}

                {!loading && err && (
                  <tr>
                    <td className="py-6 px-2 text-sm text-rose-600" colSpan={6}>{err}</td>
                  </tr>
                )}

                {!loading && !err && data.map((r) => (
                  <tr key={r.id}>
                    <td className="py-3 px-2 text-gray-700">{fmtDT(r.createdAt)}</td>
                    <td className="py-3 px-2 text-gray-900">{r.sleep ?? "—"}</td>
                    <td className="py-3 px-2 text-gray-900">{r.water ?? "—"}</td>
                    <td className="py-3 px-2 text-gray-900">{r.workHours ?? "—"}</td>
                    <td className="py-3 px-2 text-gray-700">{r.lastSavedAt ? fmtDT(r.lastSavedAt) : "—"}</td>
                    <td className="py-3 px-2">
                      <button
                        onClick={() => navigate("/journal/habits", { state: { habitId: r.id, from: "records" } })}
                        className="text-sm px-3 py-1.5 rounded-md border border-gray-300 hover:bg-gray-50 text-gray-700"
                      >
                        Edit
                      </button>
                    </td>
                  </tr>
                ))}

              </tbody>
            </table>
          </div>
        </div>
      </Box>
    </Box>
  );
}
