import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidenav from "../../components/Sidenav.jsx";
import { Box, Toolbar } from "@mui/material";
import axios from "axios";
import {
  formatLocalDateTimeSG,
  formatLocalDateSG,
  sameDaySG,
} from "../../utils/datetime";

const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";
const DEBUG = import.meta.env.MODE === "development";

export default function HabitsRecord() {
  const navigate = useNavigate();
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");
  const [busyId, setBusyId] = useState(null); // row being archived

  // route guard
  useEffect(() => {
    const loggedIn = localStorage.getItem("isLoggedIn") === "true";
    const uid = localStorage.getItem("userId");
    if (!loggedIn || !uid) navigate("/login", { replace: true });
  }, [navigate]);

  // load all habits
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
        // newest first
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

  // quick memo (you may add search/filter later)
  const data = useMemo(() => rows, [rows]);

  // archive (delete) a row
  async function handleArchive(row) {
    const uid = localStorage.getItem("userId");
    if (!uid) return navigate("/login", { replace: true });
    if (!window.confirm("Delete this habits record?")) return;

    try {
      setBusyId(row.id);
      const url = `${API_BASE}/api/habits/${encodeURIComponent(row.id)}/${encodeURIComponent(uid)}/archive`;
      if (DEBUG) console.log("ARCHIVE →", url);
      await axios.put(url, null, { withCredentials: true, timeout: 10000 });

      // remove from list locally
      setRows(prev => prev.filter(r => r.id !== row.id));

      // if it's today's record, clear banner marker and notify
      if (sameDaySG(row.createdAt, new Date())) {
        localStorage.removeItem("habits_last_saved_ymd");
        window.dispatchEvent(new Event("habits:updated"));
      }
    } catch (e) {
      if (e.response?.status === 401 || e.response?.status === 403) {
        navigate("/login", { replace: true });
        return;
      }
      alert(e.response?.data?.message || "Failed to delete record.");
    } finally {
      setBusyId(null);
    }
  }

  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <div className="max-w-4xl">
          {/* Back button */}
          <button
            onClick={() => navigate(-1)}
            className="text-sm text-gray-600 hover:underline mb-4"
          >
            ← Back
          </button>

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
                <col style={{ width: "220px" }}/>{/* Action */}
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
                    <td className="py-3 px-2 text-gray-700">{formatLocalDateSG(r.createdAt)}</td>
                    <td className="py-3 px-2 text-gray-900">{r.sleep ?? "—"}</td>
                    <td className="py-3 px-2 text-gray-900">{r.water ?? "—"}</td>
                    <td className="py-3 px-2 text-gray-900">{r.workHours ?? "—"}</td>
                    <td className="py-3 px-2 text-gray-700">
                      {r.lastSavedAt ? formatLocalDateTimeSG(r.lastSavedAt) : "—"}
                    </td>
                    <td className="py-3 px-2">
                      <div className="flex items-center gap-2">
                        <button
                          onClick={() => navigate("/journal/habits", { state: { entryId: r.id, from: "records" } })}
                          className="text-sm px-3 py-1.5 rounded-md border border-gray-300 hover:bg-gray-50 text-gray-700"
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => handleArchive(r)}
                          disabled={busyId === r.id}
                          className={`text-sm px-3 py-1.5 rounded-md border ${
                            busyId === r.id
                              ? "border-gray-300 text-gray-400 cursor-not-allowed"
                              : "border-rose-300 text-rose-700 hover:bg-rose-50"
                          }`}
                          title="Delete"
                        >
                          {busyId === r.id ? "Deleting…" : "Delete"}
                        </button>
                      </div>
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
