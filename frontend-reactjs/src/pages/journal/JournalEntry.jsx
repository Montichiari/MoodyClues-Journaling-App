import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Sidenav from "../../components/Sidenav.jsx";
import { Box, Toolbar } from "@mui/material";

const MOODS = ["Very Good", "Good", "Neutral", "Bad", "Very Bad"];
const MOOD_MAP = {
  "Very Good": 5,
  "Good": 4,
  "Neutral": 3,
  "Bad": 2,
  "Very Bad": 1,
};

const TITLE_MAX = 120;



function parseEmotions(data) {
  if (!Array.isArray(data)) return [];
  const out = [];

  (function walk(a) {
    for (const v of a) {
      if (Array.isArray(v)) {
        walk(v);
      } else if (typeof v === "string") {
        const trimmed = v.trim();
        if (trimmed && !out.includes(trimmed)) {
          out.push(trimmed);
        }
      }
    }
  })(data);

  return out;
}



export default function JournalEntry() {
  const navigate = useNavigate();
  const [mood, setMood] = useState("Neutral");
  const [entryTitle, setEntryTitle] = useState("");
  const [entryText, setEntryText] = useState("");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [submitted, setSubmitted] = useState(false);


  const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";
  const ML_BASE  = import.meta?.env?.VITE_ML_BASE_URL  || "http://18.141.76.63:5000";

  // (Optional) Route guard
  useEffect(() => {
    const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
    if (!isLoggedIn) navigate("/login", { replace: true });
  }, [navigate]);

  // "Today is 23rd July 2025."
  const todayLine = useMemo(() => {
    const d = new Date();
    const day = d.getDate();
    const ord = day % 10 === 1 && day !== 11 ? "st"
              : day % 10 === 2 && day !== 12 ? "nd"
              : day % 10 === 3 && day !== 13 ? "rd" : "th";
    const month = d.toLocaleString("en-GB", { month: "long" });
    return `Today is ${day}${ord} ${month} ${d.getFullYear()}.`;
  }, []);

  const titleError =
    submitted && !entryTitle.trim()
      ? "Title is required."
      : entryTitle.length > TITLE_MAX
      ? `Title must be ≤ ${TITLE_MAX} characters.`
      : "";

  const entryError =
    submitted && !entryText.trim()
      ? "Entry is required."
      : "";


  async function onSubmit(e) {
    e.preventDefault();
    setSubmitted(true);
    setError("");

    const userId = localStorage.getItem("userId");
    if (!userId) return navigate("/login", { replace: true });

    if (!entryTitle.trim() || !entryText.trim()) return;

    try {
      setSaving(true);

      // 1) ML emotions (required for navigation in this flow)
      let mlEmotions = []; // <-- declare OUTSIDE inner try so it's in scope later
      try {
        const mlRes = await axios.post(
          `${ML_BASE}/predict`,
          { text: entryText },
          {
            headers: { "Content-Type": "application/json" },
            timeout: 8000,
            withCredentials: false, // no cookies to ML
          }
        );

        mlEmotions = parseEmotions(mlRes.data?.top_emotions ?? []);


        // Optional: treat empty emotions as error
        // if (!mlEmotions.length) throw new Error("No emotions returned from ML model.");
      } catch (mlErr) {
        if (DEBUG) {
          console.error("ML API error:", mlErr.response ? mlErr.response.data : mlErr.message);
          alert("DEBUG (ML): " + (mlErr.response?.data?.message || mlErr.message));
        }
        setError("Emotion analysis failed. Please try again.");
        return; // don't submit to backend if ML fails (your chosen behavior)
      }

      // 2) Build payload immediately using mlEmotions
      const payload = {
        userId: String(userId),
        mood: MOOD_MAP[mood], // number 1..5
        entryTitle,
        entryText,
        emotions: mlEmotions, // <-- now correctly populated
      };

      console.log("Submit payload (snapshot):", JSON.stringify(payload));

      // 3) Submit to backend
      const submitRes = await axios.post(`${API_BASE}/api/journal/submit`, payload, {
        headers: { "Content-Type": "application/json" },
        withCredentials: true, // if your Spring API uses cookies/sessions
        timeout: 10000,
      });

      if (![200, 201].includes(submitRes.status)) {
        throw new Error(`Backend returned ${submitRes.status}`);
      }

      let successMsg = "Journal entry submitted successfully.";
      if (typeof submitRes.data === "string") {
        successMsg = submitRes.data;
      } else if (submitRes.data?.message) {
        successMsg = submitRes.data.message;
      }

      // Store emotions in sessionStorage for JournalSuccess fallback
      sessionStorage.setItem("last_journal_emotions", JSON.stringify(mlEmotions));


      navigate("/journal/entry/success", {
        replace: true,
        state: {
          message: successMsg,
          next: "/home",
          emotions: mlEmotions,   // ← pass the predicted emotions array
        },
      });




    } catch (err) {
      if (DEBUG) {
        console.error("Journal submit error:", err.response ? err.response.data : err.message);
        alert("DEBUG (Submit): " + (err.response?.data?.message || err.message));
      }
      setError("Failed to save entry. Please try again.");
    } finally {
      setSaving(false);
    }
  }



  const moodBtn = (label) => {
    const active = mood === label;
    return (
      <button
        key={label}
        type="button"
        onClick={() => setMood(label)}
        className={[
          "px-3 py-1.5 rounded-full border text-sm transition",
          active
            ? (label === "Very Bad"
                ? "bg-rose-100 border-rose-300 text-rose-700"
                : "bg-gray-900 border-gray-900 text-white")
            : "bg-white border-gray-300 text-gray-700 hover:bg-gray-50"
        ].join(" ")}
      >
        {label}
      </button>
    );
  };

  return (
    <Box sx={{ display: "flex" }}>
      <Sidenav />
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <div className="max-w-3xl">
          <h1 className="text-4xl font-semibold mb-6">{todayLine}</h1>
          <p className="mt-4 text-gray-700">
            Write into your journal, and log how you feel right now.
          </p>

          {/* Mood selector */}
          <div className="mt-6">
            <p className="font-medium text-gray-900 mb-2">How do you feel?</p>
            <div className="flex gap-2 flex-wrap">{MOODS.map(moodBtn)}</div>
          </div>

          <form onSubmit={onSubmit} className="mt-6 space-y-4">
            {/* Title */}
            <div>
              <textarea
                rows={2}
                value={entryTitle}
                onChange={(e) => setEntryTitle(e.target.value)}
                placeholder="Start with a title to remember this day."
                className={[
                  "w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2",
                  titleError
                    ? "border-rose-400 focus:ring-rose-300"
                    : "border-gray-300 focus:ring-blue-400"
                ].join(" ")}
                maxLength={TITLE_MAX + 1}
              />
              <div className="mt-1 flex justify-between text-xs">
                <span className="text-rose-600">{titleError}</span>
                <span className="text-gray-500">{entryTitle.length}/{TITLE_MAX}</span>
              </div>
            </div>

            {/* Entry */}
            <div>
              <textarea
                rows={12}
                value={entryText}
                onChange={(e) => setEntryText(e.target.value)}
                placeholder="Today, I..."
                className={[
                  "w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2",
                  entryError
                    ? "border-rose-400 focus:ring-rose-300"
                    : "border-gray-300 focus:ring-blue-400"
                ].join(" ")}
              />
              <div className="mt-1 text-xs text-rose-600">{entryError}</div>
            </div>


            {error && <div className="text-rose-600 text-sm">{error}</div>}

            <div className="flex justify-end pt-2">
              <button
                type="submit"
                disabled={saving}
                className={`px-5 py-2.5 rounded-lg text-white transition ${
                  saving ? "bg-gray-400 cursor-not-allowed" : "bg-black hover:bg-gray-900"
                }`}
              >
                {saving ? "Saving..." : "Save & Continue"}
              </button>
            </div>
          </form>
        </div>
      </Box>
    </Box>
  );
}
