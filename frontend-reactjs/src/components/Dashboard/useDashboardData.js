// useDashboardData.js
import { useEffect, useMemo, useState } from "react";
import axios from "axios";

const EMOTION_ORDER = ["angry","sad","anxious","happy","curious","confused","surprised","neutral"];

export default function useDashboardData(rangeDays = 7) {
    const [loading, setLoading] = useState(true);
    const [error, setError]     = useState(null);
    const [windowRange, setWindowRange] = useState({ from: null, to: null });
    const [journalDaily, setJournalDaily] = useState([]);
    const [summary, setSummary] = useState({});
    const [emotionCounts, setEmotionCounts] = useState([]);

    useEffect(() => {
        setLoading(true);
        axios.get("http://122.248.243.60:8080/api/dashboard/window", { params: { days: rangeDays } })
            .then(res => {
                const { window, series, summary } = res.data || {};
                setWindowRange(window || {});
                setJournalDaily(series?.journalDaily || []);
                setEmotionCounts(series?.emotionCounts || []);
                setSummary(summary || {});
            })
            .catch(setError)
            .finally(() => setLoading(false));
    }, [rangeDays]);

    // Mood line series (unchanged)
    const moodSeries = useMemo(() => {
        if (!windowRange.from || !windowRange.to) return [];
        const start = new Date(windowRange.from + "T00:00:00");
        const end   = new Date(windowRange.to   + "T00:00:00");
        const byDay = new Map((journalDaily || []).map(r => [r.day, r]));
        const out = [];
        for (let d = new Date(start); d <= end; d.setDate(d.getDate()+1)) {
            const ymd = d.toISOString().slice(0,10);
            const r = byDay.get(ymd);
            out.push({
                date: ymd,
                avgMood: r?.avgMood == null ? null : Number(Number(r.avgMood).toFixed(2)),
                entries: r?.entries ?? 0
            });
        }
        return out;
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
