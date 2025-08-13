// src/hooks/useHabitsToday.js
import { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { ymdSG } from "../utils/datetime";

export const HABITS_TODAY_MARKER = "habits_last_saved_ymd"; // <<— use this key everywhere

const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://122.248.243.60:8080";

export default function useHabitsToday() {
  const [hasToday, setHasToday] = useState(false);
  const [loading, setLoading] = useState(true);

  const computeHasToday = useCallback((list) => {
    const todayKey = ymdSG(new Date());
    const apiHas = (Array.isArray(list) ? list : []).some(h => ymdSG(h.createdAt) === todayKey);

    // localStorage override: if we recently saved, hide banner immediately
    const marker = localStorage.getItem(HABITS_TODAY_MARKER);
    const markerHas = marker ? (ymdSG(marker) === todayKey) : false;

    return apiHas || markerHas;
  }, []);

  const refetch = useCallback(async (signal) => {
    const uid = localStorage.getItem("userId");
    if (!uid) { setHasToday(false); setLoading(false); return; }
    try {
      setLoading(true);
      const res = await axios.get(`${API_BASE}/api/habits/all/${encodeURIComponent(uid)}`, {
        withCredentials: true, signal
      });
      setHasToday(computeHasToday(res.data));
    } catch (e) {
      if (!axios.isCancel?.(e) && import.meta.env.MODE === "development") {
        console.warn("useHabitsToday error:", e.response?.data || e.message);
      }
      // fall back to local marker so UX isn't stuck
      setHasToday(computeHasToday([]));
    } finally {
      setLoading(false);
    }
  }, [computeHasToday]);

  useEffect(() => {
    const controller = new AbortController();
    refetch(controller.signal);

    const onFocus   = () => refetch();
    const onVis     = () => { if (document.visibilityState === "visible") refetch(); };
    const onUpdated = () => refetch();

    window.addEventListener("focus", onFocus);
    document.addEventListener("visibilitychange", onVis);
    window.addEventListener("habits:updated", onUpdated);
    return () => {
      controller.abort();
      window.removeEventListener("focus", onFocus);
      document.removeEventListener("visibilitychange", onVis);
      window.removeEventListener("habits:updated", onUpdated);
    };
  }, [refetch]);

  return { hasToday, loading, refetch };
}
