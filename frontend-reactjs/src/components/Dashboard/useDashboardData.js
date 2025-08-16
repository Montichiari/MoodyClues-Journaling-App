// useDashboardData.js
import { useEffect, useMemo, useState } from "react";
import axios from "axios";

const EMOTION_ORDER = ["angry","sad","anxious","happy","curious","confused","surprised","neutral"];


const toUtcDate = (ymd) => {
    const [y, m, d] = ymd.split("-").map(Number);
    return new Date(Date.UTC(y, m - 1, d, 0, 0, 0));
};
const ymdUTC = (date) => date.toISOString().slice(0, 10);
const addUtcDays = (date, n) => new Date(date.getTime() + n * 86400000);

const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

// Fill every day in [from..to] inclusive, no timezone drift
function fillDaysUTC(fromYmd, toYmd, raw, mapRow) {
    const byDay = new Map((raw || []).map(r => [r.day, r])); // r.day is "YYYY-MM-DD"
    const out = [];
    let d = toUtcDate(fromYmd);
    const end = toUtcDate(toYmd);
    while (d.getTime() <= end.getTime()) {
        const ymd = ymdUTC(d);
        const r = byDay.get(ymd);
        out.push({ date: ymd, ...mapRow(r) });
        d = addUtcDays(d, 1);
    }
    return out;
}


export default function useDashboardData(rangeDays = 7) {
    const [loading, setLoading] = useState(true);
    const [error, setError]     = useState(null);
    const [windowRange, setWindowRange] = useState({ from: null, to: null });
    const [journalDaily, setJournalDaily] = useState([]);
    const [summary, setSummary] = useState({});
    const [emotionCounts, setEmotionCounts] = useState([]);

    useEffect(() => {
        let cancelled = false;
        setLoading(true);
        const userId = localStorage.getItem("userId");

        axios.get(`${API_BASE}/api/dashboard/window`, {
            params: { days: rangeDays, userId },
            withCredentials: false
        })
            .then(res => {
                if (cancelled) return;
                const { window, series, summary } = res.data || {};
                setWindowRange(window || {});
                setJournalDaily(series?.journalDaily || []);
                setEmotionCounts(series?.emotionCounts || []);
                setSummary(summary || {});
            })
            .catch(err => { if (!cancelled) setError(err); })
            .finally(() => { if (!cancelled) setLoading(false); });

        return () => { cancelled = true; };
    }, [rangeDays]);

    // moodSeries built with UTC-safe filler (fixes missing first day)
    const moodSeries = useMemo(() => {
        if (!windowRange.from || !windowRange.to) return [];
        return fillDaysUTC(windowRange.from, windowRange.to, journalDaily, (r) => ({
            avgMood: r?.avgMood == null ? null : Number(Number(r.avgMood).toFixed(2)),
            entries: r?.entries ?? 0,
        }));
    }, [journalDaily, windowRange]);

    // Cards
    const cardAverages = useMemo(() => ({
        sleep: summary?.avgSleepHoursSelected ?? null,
        water: summary?.avgWaterLitresSelected ?? null,
        work : summary?.avgWorkHoursSelected ?? null,
    }), [summary]);

    // Radar data: ensure all 8 emotions exist and are ordered consistently
    const radarData = useMemo(() => {
        const map = new Map((emotionCounts || []).map(e => [String(e.emotion).toLowerCase(), Number(e.cnt || 0)]));
        return EMOTION_ORDER.map(label => ({
            emotion: label,
            count: map.get(label) ?? 0
        }));
    }, [emotionCounts]);

    const radarMax = useMemo(
        () => Math.max(1, ...radarData.map(d => d.count)),
        [radarData]
    );

    return { loading, error, windowRange, moodSeries, cardAverages, radarData, radarMax };
}
