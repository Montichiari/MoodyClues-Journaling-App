// src/utils/datetime.js
// Consistent SG display + same-day checks, with a +8h correction for NAÏVE strings.

const ZONE = "Asia/Singapore";
const FIXED_OFFSET_MIN = 8 * 60; // +08:00

/** detect if a datetime string lacks timezone info (no Z and no ±hh:mm) */
function isNaiveISO(s = "") {
  return typeof s === "string" && s && !(/[zZ]|[+\-]\d{2}:?\d{2}$/.test(s.trim()));
}

/** add minutes to a Date (returns a new Date) */
function addMinutes(d, mins) {
  const t = new Date(d.getTime());
  t.setMinutes(t.getMinutes() + mins);
  return t;
}

/**
 * Parse ANY input into a Date that represents the correct instant:
 * - If string has timezone (Z or ±hh:mm): parse as is (no correction).
 * - If string is NAÏVE (no tz): treat it as UTC wall-clock, then add +8h.
 * - If it's already a Date: use it as-is (no correction).
 */
function parseWithSgCorrection(input) {
  if (!input) return null;

  // Already a Date instance
  if (input instanceof Date) return isNaN(input) ? null : input;

  // Normalize string (support "YYYY-MM-DD HH:mm:ss")
  const s = String(input).trim().replace(" ", "T");

  // If it already includes timezone, let JS handle it
  if (!isNaiveISO(s)) {
    const d = new Date(s);
    return isNaN(d) ? null : d;
  }

  // NAÏVE string → interpret as UTC wall-clock
  // Supports: YYYY-MM-DDTHH:mm[:ss[.SSS]]
  const m = s.match(
    /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})(?::(\d{2})(?:\.(\d{1,3}))?)?$/
  );
  if (!m) {
    // Fallback to native parsing if format is unexpected
    const d = new Date(s);
    return isNaN(d) ? null : addMinutes(d, FIXED_OFFSET_MIN);
  }

  const [_, y, mo, d, hh, mi, ss = "0", ms = "0"] = m;
  const naiveUtc = new Date(
    Date.UTC(+y, +mo - 1, +d, +hh, +mi, +ss, +`${ms}`.padEnd(3, "0"))
  );

  // Apply the fixed +8h correction ONLY for naïve backend strings
  return addMinutes(naiveUtc, FIXED_OFFSET_MIN);
}

/** Format date-time in SG, e.g. "12 Aug 2025, 21:05" */
export function formatLocalDateTimeSG(input) {
  const d = parseWithSgCorrection(input);
  if (!d || isNaN(d)) return "—";
  return new Intl.DateTimeFormat("en-GB", {
    timeZone: ZONE,
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  }).format(d);
}

/** Format date only in SG, e.g. "12 August 2025" */
export function formatLocalDateSG(input) {
  const d = parseWithSgCorrection(input);
  if (!d || isNaN(d)) return "—";
  return new Intl.DateTimeFormat("en-GB", {
    timeZone: ZONE,
    day: "numeric",
    month: "long",
    year: "numeric",
  }).format(d);
}

/** "YYYY-M-D" key in SG for same-day comparisons */
export function ymdSG(input) {
  const d = parseWithSgCorrection(input);
  if (!d || isNaN(d)) return "";
  const parts = new Intl.DateTimeFormat("en-GB", {
    timeZone: ZONE,
    year: "numeric",
    month: "numeric",
    day: "numeric",
  }).formatToParts(d);
  const get = (t) => parts.find((p) => p.type === t)?.value;
  return `${get("year")}-${get("month")}-${get("day")}`;
}

export const sameDaySG = (a, b) => ymdSG(a) === ymdSG(b);

/** Ordinal date in SG, e.g. "12th August 2025" */
export function formatOrdinalDateSG(input) {
  const d = parseWithSgCorrection(input);
  if (!d || isNaN(d)) return "—";
  const parts = new Intl.DateTimeFormat("en-GB", {
    timeZone: ZONE,
    year: "numeric",
    month: "long",
    day: "numeric",
  }).formatToParts(d);
  const dayNum = Number(parts.find((p) => p.type === "day")?.value || 0);
  const month = parts.find((p) => p.type === "month")?.value || "";
  const year = parts.find((p) => p.type === "year")?.value || "";
  const ord =
    dayNum % 10 === 1 && dayNum !== 11
      ? "st"
      : dayNum % 10 === 2 && dayNum !== 12
      ? "nd"
      : dayNum % 10 === 3 && dayNum !== 13
      ? "rd"
      : "th";
  return `${dayNum}${ord} ${month} ${year}`;
}
