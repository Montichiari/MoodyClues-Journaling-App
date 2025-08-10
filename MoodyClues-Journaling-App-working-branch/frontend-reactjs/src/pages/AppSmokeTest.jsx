// src/ApiSmokeTest.jsx
import React, { useState } from "react";
import axios from "axios";

axios.defaults.withCredentials = true;           // keep JSESSIONID
axios.defaults.validateStatus = () => true;      // show bodies even on 4xx/5xx

// ==== EDIT THESE TWO CONSTANTS IN YOUR IDE ====
const BASE_URL = "http://122.248.243.60:8080";        // e.g. "http://122.248.243.60:8080"
const REQUEST = {
    method: "GET",                                // "GET" | "POST" | "PUT" | "PATCH" | "DELETE"
    url: "122.248.243.60:8080/api/journal/all?userId=0656cb9f-73ba-11f0-8809-06ee56a6265f",                        // path only; BASE_URL is prepended
    headers: { "Content-Type": "application/json" },
    data: {                                        // ignored for GET
        email: "alice.johnson@email.com",
        password: "password",
    },
};
// ==============================================

export default function ApiSmokeTest() {
    const [status, setStatus] = useState("");
    const [timeMs, setTimeMs] = useState(null);
    const [resp, setResp] = useState(null);
    const [err, setErr] = useState("");

    async function send() {
        setErr("");
        setResp(null);
        setStatus("");
        const t0 = performance.now();
        try {
            const res = await axios({
                baseURL: BASE_URL,
                method: REQUEST.method,
                url: REQUEST.url,
                headers: REQUEST.headers,
                data: REQUEST.data,
            });
            setStatus(`${res.status} ${res.statusText}`);
            setResp(res.data);
            console.log("Response headers:", res.headers);
            console.log("Response body:", res.data);
        } catch (e) {
            setErr(e?.message || String(e));
            console.error(e);
        } finally {
            setTimeMs(Math.round(performance.now() - t0));
        }
    }

    return (
        <div style={{ fontFamily: "system-ui, sans-serif", padding: 16 }}>
            <h1 style={{ marginBottom: 8 }}>API Smoke Test</h1>
            <p style={{ margin: 0, fontSize: 14, opacity: 0.7 }}>
                Edit <code>BASE_URL</code> and <code>REQUEST</code> in <code>ApiSmokeTest.jsx</code>, save, then click Send.
            </p>

            <div style={{ marginTop: 12 }}>
                <button onClick={send} style={{ padding: "8px 14px" }}>Send</button>
                <span style={{ marginLeft: 12, fontSize: 14, opacity: 0.8 }}>
          {status && <b>Status: {status}</b>} {timeMs !== null && <> · {timeMs} ms</>}
        </span>
            </div>

            <div style={{ marginTop: 16 }}>
                <h3 style={{ marginBottom: 6 }}>Response</h3>
                <pre style={{ background: "#f6f8fa", padding: 12, borderRadius: 8, overflow: "auto" }}>
{resp ? JSON.stringify(resp, null, 2) : (err || "—")}
        </pre>
            </div>

            <div style={{ marginTop: 16 }}>
                <h3 style={{ marginBottom: 6 }}>Cookies (this origin)</h3>
                <code style={{ background: "#f6f8fa", padding: 8, borderRadius: 8, display: "block" }}>
                    {document.cookie || "—"}
                </code>
            </div>
        </div>
    );
}
