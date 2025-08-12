import { Box, Toolbar } from "@mui/material";
import Sidenav from "../../components/Sidenav";
import { useState, useMemo, useEffect } from "react";
import { useNavigate, useLocation, useSearchParams } from "react-router-dom";
import axios from "axios";

const DEBUG = import.meta.env.MODE === "development";
const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

function todayLine() {
  const d = new Date();
  const day = d.getDate();
  const ord =
    day % 10 === 1 && day !== 11 ? "st" :
    day % 10 === 2 && day !== 12 ? "nd" :
    day % 10 === 3 && day !== 13 ? "rd" : "th";
  const month = d.toLocaleString("en-GB", { month: "long" });
  return `Today is ${day}${ord} ${month} ${d.getFullYear()}.`;
}

const toLocalYMD = (d) => {
  const dt = new Date(d);
  return Number.isNaN(dt.getTime()) ? "" : [dt.getFullYear(), dt.getMonth(), dt.getDate()].join("-");
};
const isSameLocalDay = (a, b) => toLocalYMD(a) === toLocalYMD(b);
const fmtDT = (d) => {
  const dt = new Date(d);
  if (Number.isNaN(dt.getTime())) return "—";
  const hh = String(dt.getHours()).padStart(2, "0");
  const mm = String(dt.getMinutes()).padStart(2, "0");
  const day = dt.getDate();
  const mon = dt.toLocaleString("en-GB", { month: "short" });
  const yr  = dt.getFullYear();
  return `${day} ${mon} ${yr}, ${hh}:${mm}`;
};

export default function Habits() {
  const navigate = useNavigate();
  const location = useLocation();
  const [search] = useSearchParams();

  const [sleep, setSleep] = useState(0);
  const [water, setWater] = useState(0);
  const [work,  setWork]  = useState(0); // backend expects workHours
  const [saving, setSaving] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [createdAt, setCreatedAt] = useState(null);
  const [lastSavedAt, setLastSavedAt] = useState(null);
  const [habitId, setHabitId] = useState(null); // null = create mode

  const line = useMemo(() => todayLine(), []);
  const userId = localStorage.getItem("userId");

  // Optional incoming id to edit a specific record (from HabitsRecord page)
  const incomingId = location.state?.habitId || search.get("id") || null;

  // Route guard (keep your current behavior)
  useEffect(() => {
    const ok = localStorage.getItem("isLoggedIn") === "true";
    if (!ok) navigate("/login", { replace: true });
  }, [navigate]);

  // Load: prefer a specific id; else find today's record from list
  useEffect(() => {
    if (!userId) return;
    const controller = new AbortController();

    (async () => {
      try {
        setLoading(true);
        setError("");

        if (incomingId) {
          // Edit a specific record
          const res = await axios.get(
            `${API_BASE}/api/habits/${encodeURIComponent(incomingId)}/${encodeURIComponent(userId)}`,
            { withCredentials: true, signal: controller.signal }
          );
          const h = res.data;
          hydrate(h);
          return;
        }

        // Otherwise check today's
        const listRes = await axios.get(
          `${API_BASE}/api/habits/all/${encodeURIComponent(userId)}`,
          { withCredentials: true, signal: controller.signal }
        );
        const rows = Array.isArray(listRes.data) ? listRes.data : [];
        rows.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
        const today = rows.find(r => isSameLocalDay(r.createdAt, new Date()));

        if (today) hydrate(today);
        else {
          // create mode (empty)
          setHabitId(null);
          setCreatedAt(null);
          setLastSavedAt(null);
          setSleep(0);
          setWater(0);
          setWork(0);
        }
      } catch (e) {
        if (axios.isCancel?.(e)) return;
        console.error("Load habits error:", e.response?.data || e.message);
        setError("Failed to load habits.");
      } finally {
        setLoading(false);
      }
    })();

    function hydrate(h) {
      setHabitId(h.id || null);
      setCreatedAt(h.createdAt || null);
      setLastSavedAt(h.lastSavedAt || null);
      setSleep(typeof h.sleep === "number" ? h.sleep : parseFloat(h.sleep ?? 0) || 0);
      setWater(typeof h.water === "number" ? h.water : parseFloat(h.water ?? 0) || 0);
      setWork(typeof h.workHours === "number" ? h.workHours : parseFloat(h.workHours ?? 0) || 0);
    }

    return () => controller.abort();
  }, [API_BASE, incomingId, userId]);

  const mode = habitId ? "edit" : "create";

  const handleSubmit = async () => {
    setError("");
    const uid = localStorage.getItem("userId");
    if (!uid) return navigate("/login", { replace: true });

    // keep for later pages if you want
    sessionStorage.setItem("journal_habits", JSON.stringify({ sleep, water, work }));

    // simple validation in your existing ranges
    const vSleep = parseFloat(sleep);
    const vWater = parseFloat(water);
    const vWork  = parseFloat(work);
    if (
      Number.isNaN(vSleep) || vSleep < 0 || vSleep > 12 ||
      Number.isNaN(vWater) || vWater < 0 || vWater > 10 ||
      Number.isNaN(vWork)  || vWork  < 0 || vWork  > 16
    ) {
      setError("Please enter valid numbers (Sleep 0–12h, Water 0–10L, Work 0–16h).");
      return;
    }

    const payload = {
      userId: String(uid),
      sleep: vSleep,
      water: vWater,
      workHours: vWork, // match backend
    };

    const controller = new AbortController();

    try {
      setSaving(true);

      if (mode === "edit") {
        // Overwrite existing
        const url = `${API_BASE}/api/habits/${encodeURIComponent(habitId)}/${encodeURIComponent(uid)}/edit`;
        if (DEBUG) console.log("DEBUG PUT →", url, payload);
        await axios.put(url, payload, {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
          timeout: 10000,
          signal: controller.signal,
        });

        navigate("/journal/habits/success", {
          replace: true,
          state: { message: "Habits updated.", next: "/habits/records" },
        });
      } else {
        // Create today's
        const url = `${API_BASE}/api/habits/submit`;
        if (DEBUG) console.log("DEBUG POST →", url, payload);
        await axios.post(url, payload, {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
          timeout: 10000,
          signal: controller.signal,
        });

        navigate("/journal/habits/success", {
          replace: true,
          state: { message: "Habits entry submitted successfully.", next: "/habits/records" },
        });
      }
    } catch (e) {
      if (DEBUG) {
        console.error("Habits submit error:", e.response ? e.response.data : e.message);
        alert("DEBUG (Habits): " + (e.response?.data?.message || e.message));
      }
      setError(
        e.response?.status === 405
          ? "This endpoint does not allow this method at this path. Check the URL/method."
          : "Failed to save habits. Please try again."
      );
    } finally {
      setSaving(false);
    }
  };

  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <div className="max-w-3xl">
          <h1 className="text-4xl font-extrabold tracking-tight text-gray-900">{line}</h1>
          <p className="mt-4 text-gray-700">Log your lifestyle choices for the day.</p>

          {/* meta line */}
          {(createdAt || lastSavedAt) && (
            <div className="mt-2 text-sm text-gray-500">
              {createdAt && <>Created: {fmtDT(createdAt)}</>}
              {lastSavedAt && <span className="ml-3">Last saved: {fmtDT(lastSavedAt)}</span>}
            </div>
          )}

          <div className="mt-6 space-y-6">
            <div>
              <label className="block mb-2 font-medium text-gray-900">
                How much did you sleep the previous night? (hours)
              </label>
              <input
                type="number"
                step="0.1"
                min="0"
                max="12"
                value={sleep}
                onChange={(e) => setSleep(e.target.value)}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
                placeholder="e.g., 7.5"
              />
            </div>

            <div>
              <label className="block mb-2 font-medium text-gray-900">
                How much water did you drink today? (litres)
              </label>
              <input
                type="number"
                step="0.1"
                min="0"
                max="10"
                value={water}
                onChange={(e) => setWater(e.target.value)}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
                placeholder="e.g., 2.0"
              />
            </div>

            <div>
              <label className="block mb-2 font-medium text-gray-900">
                How many hours did you work today?
              </label>
              <input
                type="number"
                step="0.1"
                min="0"
                max="16"
                value={work}
                onChange={(e) => setWork(e.target.value)}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
                placeholder="e.g., 8.0"
              />
            </div>
          </div>

          {error && <div className="mt-3 text-sm text-rose-600">{error}</div>}

          <div className="flex justify-end pt-8">
            <button
              onClick={handleSubmit}
              disabled={saving || loading}
              className={`px-5 py-2.5 rounded-lg text-white transition ${
                saving ? "bg-gray-400 cursor-not-allowed" : "bg-black hover:bg-gray-900"
              }`}
            >
              {saving ? "Saving..." : mode === "edit" ? "Update Habits" : "Save & Submit"}
            </button>
          </div>

          {loading && <div className="mt-4 text-sm text-gray-500">Loading…</div>}
        </div>
      </Box>
    </Box>
  );
}
