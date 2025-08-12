import { Box, Toolbar } from "@mui/material";
import Sidenav from "../../components/Sidenav";
import { useState, useMemo, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const DEBUG = import.meta.env.MODE === "development";

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

export default function Habits() {
  const navigate = useNavigate();
  const [sleep, setSleep] = useState(0);
  const [water, setWater] = useState(0);
  const [work, setWork] = useState(0); // local field; backend expects workHours
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  const line = useMemo(() => todayLine(), []);
  const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

  // Route guard (enable if needed)
  useEffect(() => {
    // const ok = localStorage.getItem("isLoggedIn") === "true";
    // if (!ok) navigate("/login", { replace: true });
  }, [navigate]);

  const handleSubmit = async () => {
    setError("");
    const userId = localStorage.getItem("userId");
    if (!userId) return navigate("/login", { replace: true });

    // optional: keep for later pages
    sessionStorage.setItem("journal_habits", JSON.stringify({ sleep, water, work }));

    const payload = {
      userId: String(userId),
      sleep: parseFloat(sleep),
      water: parseFloat(water),
      workHours: parseFloat(work), // <-- match Postman key
    };

    try {
      setSaving(true);

      const url = `${API_BASE}/api/habits/submit`; // <-- match Postman URL
      if (DEBUG) console.log("DEBUG (Habits payload) →", url, payload);

      const res = await axios.post(url, payload, {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
        timeout: 10000,
      });

      if (![200, 201].includes(res.status)) {
        throw new Error(`Backend returned ${res.status}`);
      }

      let msg = "Habits entry submitted successfully.";
      if (typeof res.data === "string") msg = res.data;
      else if (res.data?.message) msg = res.data.message;

      navigate("/journal/habits/success", {
        replace: true,
        state: { message: msg, next: "/journal/reflections" },
      });
    } catch (e) {
      if (DEBUG) {
        console.error("Habits submit error:", e.response ? e.response.data : e.message);
        alert("DEBUG (Habits): " + (e.response?.data?.message || e.message));
      }
      setError(
        e.response?.status === 405
          ? "This endpoint does not allow POST at this path. Check the URL/method."
          : "Failed to submit habits. Please try again."
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
              disabled={saving}
              className={`px-5 py-2.5 rounded-lg text-white transition ${
                saving ? "bg-gray-400 cursor-not-allowed" : "bg-black hover:bg-gray-900"
              }`}
            >
              {saving ? "Saving..." : "Save & Submit"}
            </button>
          </div>
        </div>
      </Box>
    </Box>
  );
}
