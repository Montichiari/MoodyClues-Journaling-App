import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import Sidenav from "../../components/Sidenav.jsx";
import SidenavC from "../../components/SidenavC.jsx";
import { Box, Toolbar } from "@mui/material";
import axios from "axios";
import { formatOrdinalDateSG, sameDaySG } from "../../utils/datetime";

const MOOD_LABELS = {
  1: "Very Bad",
  2: "Bad",
  3: "Neutral",
  4: "Good",
  5: "Very Good",
};

// units
const fmtHours = (v) => (v == null || v === "") ? "—" : `${v} Hours`;
const fmtLitres = (v) => (v == null || v === "") ? "—" : `${v} Litres`;

export default function ReadDetails() {
  const { userId: routeUserId, entryId } = useParams(); // /read/:userId/:entryId
  const location = useLocation();
  const navigate = useNavigate();

  const [item, setItem] = useState(null);     // the journal entry
  const [habits, setHabits] = useState(null); // matched habits (same local day)
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");
  const [archiving, setArchiving] = useState(false);

  const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

  const loggedInUser = localStorage.getItem("isLoggedIn") === "true" && !!localStorage.getItem("userId");
  const loggedInCounsellor = !!localStorage.getItem("counsellorId");
  const localUserId = localStorage.getItem("userId");
  const targetUserId = routeUserId || localUserId;

  // Only the owner (journal user) can delete
  const canDelete = !!localUserId && localUserId === targetUserId && !loading && !err && !!item;

  // For journal-user UI choice (emotions pill visibility controlled by user's setting)
  const showEmotion = localStorage.getItem("showEmotion") === "true";

  // Route guard: allow EITHER a user or a counsellor
  useEffect(() => {
    if (loggedInUser || loggedInCounsellor) return;
    const wantsCounsellor = location.pathname.startsWith("/counsellor/");
    navigate(wantsCounsellor ? "/counsellor/login" : "/login", { replace: true });
  }, [navigate, location.pathname, loggedInUser, loggedInCounsellor]);

  // load journal + (strict) same-day habits
  useEffect(() => {
    if (!entryId || !targetUserId) {
      setErr("Missing entry or user id.");
      setLoading(false);
      return;
    }

    const controller = new AbortController();

    async function fetchData() {
      try {
        setLoading(true);
        setErr("");

        const entryIdEnc = encodeURIComponent(String(entryId).trim());
        const userIdEnc  = encodeURIComponent(String(targetUserId).trim());

        // 1) Journal entry
        const jr = await axios.get(
            `${API_BASE}/api/journal/${entryIdEnc}/${userIdEnc}`,
            { withCredentials: true, signal: controller.signal }
        );
        const entry = jr.data || null;
        setItem(entry);

        // 2) Habits (strict same-day): GET /api/habits/all/{userId}
        let matched = null;
        if (entry?.createdAt || entry?.date) {
          const hrAll = await axios.get(
              `${API_BASE}/api/habits/all/${userIdEnc}`,
              { withCredentials: true, signal: controller.signal }
          );
          const list = Array.isArray(hrAll.data) ? hrAll.data : [];
          const entryTs = entry.createdAt || entry.date;
          matched = list.find(h => sameDaySG(h.createdAt, entryTs)) || null;
        }

        setHabits(matched);
      } catch (e) {
        if (axios.isCancel?.(e)) return;
        if (e.response?.status === 404) {
          setErr("Journal entry not found.");
        } else if (e.response?.status === 401 || e.response?.status === 403) {
          const wantsCounsellor = location.pathname.startsWith("/counsellor/");
          navigate(wantsCounsellor ? "/counsellor/login" : "/login", { replace: true });
        } else {
          setErr("Failed to load the journal entry.");
          console.error(e);
        }
      } finally {
        setLoading(false);
      }
    }

    fetchData();
    return () => controller.abort();
  }, [API_BASE, routeUserId, entryId, navigate, location.pathname, targetUserId]);

  // Archive (delete) this journal and go back to list
  async function handleArchiveJournal() {
    const uid = localUserId;
    if (!uid || !entryId) return navigate("/login", { replace: true });
    if (!window.confirm("Delete this journal entry?")) return;

    try {
      setArchiving(true);
      const entryIdEnc = encodeURIComponent(String(entryId).trim());
      const userIdEnc  = encodeURIComponent(String(uid).trim());
      await axios.put(
          `${API_BASE}/api/journal/${entryIdEnc}/${userIdEnc}/archive`,
          null,
          { withCredentials: true, timeout: 10000 }
      );
      navigate("/journal/read", { replace: true, state: { flash: "Entry moved to archive." } });
    } catch (e) {
      if (e.response?.status === 401 || e.response?.status === 403) {
        navigate("/login", { replace: true });
        return;
      }
      alert(e.response?.data?.message || "Failed to archive entry. Please try again.");
    } finally {
      setArchiving(false);
    }
  }

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
        {loggedInCounsellor ? <SidenavC /> : <Sidenav />}
        <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
          <Toolbar />

          <div className="max-w-3xl">
            {/* Top row: Back (left) + Delete (right) */}
            <div className="flex items-center justify-between">
              <button
                  onClick={() => navigate(-1)}
                  className="text-sm text-gray-600 hover:underline"
              >
                ← Back
              </button>

              {canDelete && (
                  <button
                      onClick={handleArchiveJournal}
                      disabled={archiving}
                      className={`text-sm px-3 py-1.5 rounded-md border ${
                          archiving
                              ? "border-gray-300 text-gray-400 cursor-not-allowed"
                              : "border-rose-300 text-rose-700 hover:bg-rose-50"
                      }`}
                      title="Move to archive"
                  >
                    {archiving ? "Archiving…" : "Delete"}
                  </button>
              )}
            </div>

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
                    {formatOrdinalDateSG(item.createdAt || item.date)}
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

                  {/* Emotions (left aligned) */}
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
