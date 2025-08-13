import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import Sidenav from "../components/Sidenav.jsx";
import { Box, Toolbar } from "@mui/material";
import axios from "axios";
import { formatLocalDateTimeSG } from "../utils/datetime";

const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";
const DEBUG = import.meta.env.MODE === "development";

/** Robust server → UI status mapper */
function deriveStatus(r) {
  const statusStr = (r?.status || r?.state || "").toString().trim().toLowerCase();
  if (statusStr) {
    if (["approved", "accept", "accepted", "true", "ok", "success"].includes(statusStr)) return "approved";
    if (["rejected", "declined", "decline", "false"].includes(statusStr)) return "rejected";
    if (["pending", "new", "open"].includes(statusStr)) return "pending";
  }
  const v = r?.approved;
  if (v === true || v === "True" || v === "true" || v === 1 || v === "1") return "approved";
  if (v === false || v === "False" || v === "false" || v === 0 || v === "0") return "rejected";
  return "pending";
}

export default function Invites() {
  const navigate = useNavigate();
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");
  const [q, setQ] = useState("");
  const [busyId, setBusyId] = useState(null);

  // route guard
  useEffect(() => {
    const loggedIn = localStorage.getItem("isLoggedIn") === "true";
    const uid = localStorage.getItem("userId");
    if (!loggedIn || !uid) navigate("/login", { replace: true });
  }, [navigate]);

  async function refetchList(uid) {
    const url = `${API_BASE}/api/linkrequest/journal/all-link-requests/${encodeURIComponent(uid)}`;
    const res = await axios.get(url, { withCredentials: true });
    const list = Array.isArray(res.data) ? res.data : [];
    const toTime = (r) => new Date(r.requestedAt || r.createdAt || 0).getTime();
    list.sort((a, b) => toTime(b) - toTime(a));
    setRows(list);
    return list;
  }

  // initial fetch
  useEffect(() => {
    const uid = localStorage.getItem("userId");
    if (!uid) return;
    (async () => {
      try {
        setLoading(true);
        setErr("");
        await refetchList(uid);
      } catch (e) {
        if (e?.response?.status === 401 || e?.response?.status === 403) {
          navigate("/login", { replace: true });
          return;
        }
        console.error("Load invites error:", e.response?.data || e.message);
        setErr("Failed to load invites.");
      } finally {
        setLoading(false);
      }
    })();
  }, [API_BASE, navigate]);

  // search by counsellor name or email
  const view = useMemo(() => {
    const s = q.trim().toLowerCase();
    if (!s) return rows;
    return rows.filter((r) => {
      const c = r?.counsellorUser || {};
      const name = `${c.firstName || ""} ${c.lastName || ""}`.trim().toLowerCase();
      const email = (c.email || "").toLowerCase();
      return name.includes(s) || email.includes(s);
    });
  }, [rows, q]);

  async function decide(row, approve) {
    const uid = localStorage.getItem("userId");
    if (!uid) return navigate("/login", { replace: true });
    if (loading || busyId) return;

    setBusyId(row.id);
    try {
      // Preflight: avoid acting on stale "pending"
      const fresh = await refetchList(uid);
      const latest = fresh.find(r => r.id === row.id) || row;
      const statusNow = deriveStatus(latest);
      if (statusNow !== "pending") {
        alert(`This invite has already been ${statusNow}.`);
        return;
      }

      const label = approve ? "accept" : "decline";
      if (!window.confirm(`Are you sure you want to ${label} this request?`)) return;

      const body = { approved: approve ? "True" : "False" };
      const url = `${API_BASE}/api/linkrequest/${encodeURIComponent(row.id)}/decision/${encodeURIComponent(uid)}`;
      if (DEBUG) console.log("POST", url, body);
      await axios.post(url, body, {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
        timeout: 10000,
      });

      // Postflight: show server truth
      await refetchList(uid);
    } catch (e) {
      const status = e?.response?.status;
      if (status === 401 || status === 403) {
        navigate("/login", { replace: true });
        return;
      }
      if (status === 409) {
        try { await refetchList(uid); } catch {}
        alert("This invite has already been decided.");
      } else {
        alert(e.response?.data?.message || "Failed to submit decision.");
      }
    } finally {
      setBusyId(null);
    }
  }

  function StatusPill({ status }) {
    const base = "inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium";
    if (status === "approved") return <span className={`${base} bg-green-100 text-green-700`}>Accepted</span>;
    if (status === "rejected") return <span className={`${base} bg-rose-100 text-rose-700`}>Declined</span>;
    return <span className={`${base} bg-gray-100 text-gray-700`}>Pending</span>;
  }

  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <div className="max-w-4xl">
          <button onClick={() => navigate(-1)} className="text-sm text-gray-600 hover:underline mb-4">← Back</button>

          <div className="flex items-center justify-between">
            <h1 className="text-2xl sm:text-3xl font-semibold text-gray-900">Invites</h1>
          </div>

          <div className="mt-4">
            <input
              type="text"
              value={q}
              onChange={(e) => setQ(e.target.value)}
              placeholder="Search by counsellor name or email…"
              className="w-full max-w-lg border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
          </div>

          <div className="mt-6 border-t border-gray-200">
            <table className="w-full table-auto">
              {/* keep colgroup on one line to avoid hydration warning */}
              <colgroup><col style={{ width: "220px" }}/><col/><col style={{ width: "260px" }}/><col style={{ width: "140px" }}/><col style={{ width: "220px" }}/></colgroup>

              <thead>
                <tr className="text-sm font-medium text-gray-500">
                  <th className="py-3 px-2 text-left">Received</th>
                  <th className="py-3 px-2 text-left">Counsellor</th>
                  <th className="py-3 px-2 text-left">Email</th>
                  <th className="py-3 px-2 text-left">Status</th>
                  <th className="py-3 px-2 text-left">Action</th>
                </tr>
              </thead>

              <tbody className="divide-y divide-gray-200">
                {loading && (
                  <tr><td className="py-6 px-2 text-sm text-gray-500" colSpan={5}>Loading…</td></tr>
                )}

                {!loading && !err && view.length === 0 && (
                  <tr><td className="py-6 px-2 text-sm text-gray-500" colSpan={5}>No invites.</td></tr>
                )}

                {!loading && err && (
                  <tr><td className="py-6 px-2 text-sm text-rose-600" colSpan={5}>{err}</td></tr>
                )}

                {!loading && !err && view.map((r) => {
                  const c = r?.counsellorUser || {};
                  const name = [c.firstName, c.lastName].filter(Boolean).join(" ") || "—";
                  const email = c.email || "—";
                  const receivedRaw = r.requestedAt || r.createdAt || null;
                  const received = receivedRaw ? formatLocalDateTimeSG(receivedRaw) : "—";
                  const status = deriveStatus(r);
                  const isPending = status === "pending";

                  return (
                    <tr key={r.id}>
                      <td className="py-3 px-2 text-gray-700">{received}</td>
                      <td className="py-3 px-2 text-gray-900">{name}</td>
                      <td className="py-3 px-2 text-gray-700">{email}</td>
                      <td className="py-3 px-2"><StatusPill status={status} /></td>
                      <td className="py-3 px-2">
                        <div className="flex items-center gap-2">
                          <button
                            onClick={() => decide(r, true)}
                            disabled={loading || busyId === r.id || !isPending}
                            className={`text-sm px-3 py-1.5 rounded-md border ${
                              loading || busyId === r.id || !isPending
                                ? "border-gray-300 text-gray-400 cursor-not-allowed"
                                : "border-green-300 text-green-700 hover:bg-green-50"
                            }`}
                            title={!isPending ? "Already decided" : undefined}
                          >
                            Accept
                          </button>
                          <button
                            onClick={() => decide(r, false)}
                            disabled={loading || busyId === r.id || !isPending}
                            className={`text-sm px-3 py-1.5 rounded-md border ${
                              loading || busyId === r.id || !isPending
                                ? "border-gray-300 text-gray-400 cursor-not-allowed"
                                : "border-rose-300 text-rose-700 hover:bg-rose-50"
                            }`}
                            title={!isPending ? "Already decided" : undefined}
                          >
                            Decline
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </Box>
    </Box>
  );
}
