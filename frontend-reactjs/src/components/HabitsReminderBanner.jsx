// src/components/HabitsReminderBanner.jsx
import { useNavigate } from "react-router-dom";
import useHabitsToday from "../hooks/useHabitsToday";

/**
 * Props:
 *  - hasLoggedToday?: boolean   // optional override (defaults to hook value)
 *  - onLogNow?: () => void      // optional custom handler (defaults to navigate to /journal/habits)
 *  - hideWhenLogged?: boolean   // if true, banner is hidden once logged (default: false)
 *  - className?: string         // optional extra classes
 */
export default function HabitsReminderBanner({
  hasLoggedToday,
  onLogNow,
  hideWhenLogged = false,
  className = "",
}) {
  const navigate = useNavigate();
  const { hasToday, loading } = useHabitsToday();

  // prefer explicit prop if provided; else use hook
  const logged = typeof hasLoggedToday === "boolean" ? hasLoggedToday : hasToday;
  const handleClick = onLogNow || (() => navigate("/journal/habits"));

  if (loading) return null;
  if (hideWhenLogged && logged) return null; // completely hide if requested

  return (
    <div
      className={
        "mt-4 mb-2 rounded-lg border border-gray-200 bg-gray-50 px-4 py-3 flex items-center justify-between " +
        className
      }
      role="status"
      aria-live="polite"
    >
      <span className="text-sm text-gray-800">
        {logged
          ? "✅ You have logged today’s habits."
          : "You haven’t logged today’s habits yet."}
      </span>

      {!logged && (
        <button
          onClick={handleClick}
          className="text-sm px-3 py-1.5 rounded-md border border-gray-300 hover:bg-white text-gray-800"
        >
          Log now
        </button>
      )}
    </div>
  );
}
