import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import logo from "../assets/moodyclues-logo.png";

const PASSWORD_REGEX =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

export const CounsellorRegistration = () => {
    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [password, setPassword] = useState("");
    const [confirm, setConfirm] = useState("");
    const [errMsg, setErrMsg] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const emailTrim = email.trim();
        const firstTrim = firstName.trim();
        const lastTrim = lastName.trim();
        const passTrim = password.trim();
        const confirmTrim = confirm.trim();

        if (!emailTrim || !firstTrim || !lastTrim || !passTrim || !confirmTrim) {
            setErrMsg("All fields are required.");
            return;
        }
        if (!PASSWORD_REGEX.test(passTrim)) {
            setErrMsg(
                "Password must be ≥8 chars and include upper & lower case, a number, and a special character."
            );
            return;
        }
        if (passTrim !== confirmTrim) {
            setErrMsg("Passwords do not match.");
            return;
        }

        setErrMsg("");
        setLoading(true);

        try {
            const res = await axios.post(
                "http://122.248.243.60:8080/api/counsellor/register",
                {
                    email: emailTrim,
                    password: passTrim,
                    firstName: firstTrim,
                    lastName: lastTrim,
                },
                { withCredentials: true, validateStatus: () => true }
            );

            if (res.status === 200) {
                // Go to counsellor login after successful registration
                navigate("/counsellor/login", { replace: true, state: { registered: true } });
            } else if (res.status === 409) {
                setErrMsg("Email already registered.");
            } else if (res.status === 400) {
                const serverMsg =
                    (typeof res.data === "string" && res.data) ||
                    res.data?.message ||
                    "Invalid input. Please check your details.";
                setErrMsg(serverMsg);
            } else {
                setErrMsg("Registration failed. Please try again.");
            }
        } catch {
            setErrMsg("Registration failed. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    const goCounsellorLogin = () => navigate("/counsellor/login");
    const goJournalLogin = () => navigate("/login");

    const isDisabled =
        loading ||
        !email.trim() ||
        !firstName.trim() ||
        !lastName.trim() ||
        !password.trim() ||
        !confirm.trim();

    return (
        <div className="min-h-screen flex items-center justify-center px-4 bg-white">
            <div className="w-full max-w-md p-6 rounded-xl shadow-lg space-y-6 border border-gray-100">
                <div className="flex justify-center">
                    <img src={logo} alt="MoodyClues" className="w-64 h-auto mx-auto my-4" />
                </div>

                <h1 className="text-2xl font-semibold text-center text-gray-800">
                    Counsellor Registration
                </h1>

                <div className="flex justify-center items-center gap-3">
                    <button
                        onClick={goCounsellorLogin}
                        className="text-sm text-blue-600 hover:underline"
                    >
                        Already have an account? Log in
                    </button>
                </div>

                {errMsg && (
                    <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded p-2">
                        {errMsg}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="firstName" className="block mb-1 text-gray-700">
                            First Name
                        </label>
                        <input
                            id="firstName"
                            type="text"
                            value={firstName}
                            onChange={(e) => {
                                setFirstName(e.target.value);
                                if (errMsg) setErrMsg("");
                            }}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your first name"
                            autoComplete="given-name"
                        />
                    </div>

                    <div>
                        <label htmlFor="lastName" className="block mb-1 text-gray-700">
                            Last Name
                        </label>
                        <input
                            id="lastName"
                            type="text"
                            value={lastName}
                            onChange={(e) => {
                                setLastName(e.target.value);
                                if (errMsg) setErrMsg("");
                            }}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your last name"
                            autoComplete="family-name"
                        />
                    </div>

                    <div>
                        <label htmlFor="email" className="block mb-1 text-gray-700">
                            Work Email
                        </label>
                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(e) => {
                                setEmail(e.target.value);
                                if (errMsg) setErrMsg("");
                            }}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your email"
                            autoComplete="email"
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block mb-1 text-gray-700">
                            Password
                        </label>
                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) => {
                                setPassword(e.target.value);
                                if (errMsg) setErrMsg("");
                            }}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Create a password"
                            autoComplete="new-password"
                        />
                        <p className="text-xs text-gray-500 mt-1">
                            Must be ≥8 chars, include upper & lower case, a number, and a special character.
                        </p>
                    </div>

                    <div>
                        <label htmlFor="confirm" className="block mb-1 text-gray-700">
                            Confirm Password
                        </label>
                        <input
                            id="confirm"
                            type="password"
                            value={confirm}
                            onChange={(e) => {
                                setConfirm(e.target.value);
                                if (errMsg) setErrMsg("");
                            }}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Re-enter your password"
                            autoComplete="new-password"
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={isDisabled}
                        className="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition disabled:opacity-60"
                    >
                        {loading ? "Creating account…" : "Create account"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default CounsellorRegistration;
