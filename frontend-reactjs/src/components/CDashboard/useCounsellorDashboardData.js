import { useEffect, useMemo, useState } from "react";
import axios from "axios";

const EMOTION_ORDER = ["angry","sad","anxious","happy","curious","confused","surprised","neutral"];
const COLORS = ["#6366F1","#10B981","#F59E0B","#EF4444","#06B6D4","#A855F7","#84CC16","#F97316"];


const toUtcDate = (ymd) => { const [y,m,d]=ymd.split("-").map(Number); return new Date(Date.UTC(y,m-1,d,0,0,0)); };
const ymdUTC = (date) => date.toISOString().slice(0,10);
const addUtcDays = (date, n) => new Date(date.getTime() + n * 86400000);

const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

export default function useCounsellorDashboardData(rangeDays = 7) {
    const [loading, setLoading] = useState(true);
    const [error, setError]     = useState(null);

    const [windowRange, setWindowRange] = useState({ from: null, to: null });
    const [clients, setClients] = useState([]);                     // [{id, firstName}]
    const [selectedClientId, setSelectedClientId] = useState(null);
    const [seriesByClient, setSeriesByClient] = useState({});       // id -> { journalDaily, emotionCounts }


    const colorFor = (id) => {
        const idx = clients.findIndex(c => c.id === id);
        return COLORS[(idx >= 0 ? idx : 0) % COLORS.length];
    };

    useEffect(() => {
        let cancelled = false;
        setLoading(true);

        const counsellorId = localStorage.getItem("counsellorId");
        axios.get(`${API_BASE}/api/counsellor/dashboard/window`, {
            params: { days: rangeDays, counsellorId },
            withCredentials: false
        })
            .then(res => {
                if (cancelled) return;
                const { window, clients, seriesByClient } = res.data || {};
                setWindowRange(window || {});
                setClients(Array.isArray(clients) ? clients : []);
                setSeriesByClient(seriesByClient || {});
                if (!selectedClientId && clients?.length) {
                    setSelectedClientId(clients[0].id);
                }
            })
            .catch(err => { if (!cancelled) setError(err); })
            .finally(() => { if (!cancelled) setLoading(false); });

        return () => { cancelled = true; };
    }, [rangeDays]); // eslint-disable-line

    // Merge mood series: one row per day, columns are client IDs
    const moodSeriesMerged = useMemo(() => {
        if (!windowRange.from || !windowRange.to || !clients.length) return [];
        const start = toUtcDate(windowRange.from);
        const end   = toUtcDate(windowRange.to);

        const byClientDay = {};
        for (const c of clients) {
            const jd = seriesByClient[c.id]?.journalDaily ?? [];
            byClientDay[c.id] = new Map(jd.map(r => [r.day, r.avgMood == null ? null : Number(Number(r.avgMood).toFixed(2))]));
        }

        const rows = [];
        for (let d = new Date(start); d.getTime() <= end.getTime(); d = addUtcDays(d, 1)) {
            const ymd = ymdUTC(d);
            const row = { date: ymd };
            for (const c of clients) {
                row[c.id] = byClientDay[c.id].has(ymd) ? byClientDay[c.id].get(ymd) : null;
            }
            rows.push(row);
        }
        return rows;
    }, [windowRange, clients, seriesByClient]);

    // Merge radar data: one row per emotion, columns are client IDs with counts
    const radarDataMerged = useMemo(() => {
        const base = EMOTION_ORDER.map(em => {
            const row = { emotion: em };
            for (const c of clients) {
                const list = seriesByClient[c.id]?.emotionCounts ?? [];
                const found = list.find(x => String(x.emotion).toLowerCase() === em);
                row[c.id] = found ? Number(found.cnt || 0) : 0;
            }
            return row;
        });
        return base;
    }, [clients, seriesByClient]);

    const radarMax = useMemo(() => {
        let m = 1;
        for (const row of radarDataMerged) {
            for (const c of clients) m = Math.max(m, row[c.id] ?? 0);
        }
        return m;
    }, [radarDataMerged, clients]);

    return {
        loading, error,
        windowRange,
        clients, selectedClientId, setSelectedClientId,
        moodSeriesMerged,
        radarDataMerged, radarMax,
        colorFor
    };
}
