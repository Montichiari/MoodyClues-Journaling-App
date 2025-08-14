import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import logo from "../assets/moodyclues-logo.png";

export const CounsellorLogin = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errMsg, setErrMsg] = useState('');
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const emailTrim = email.trim();
        const passTrim = password.trim();
        if (!emailTrim || !passTrim) {
            setErrMsg('Email and password are required.');
            return;
        }

        setErrMsg('');
        setLoading(true);

        try {
            const res = await axios.post(
                'http://122.248.243.60:8080/api/counsellor/login',
                { email: emailTrim, password: passTrim },
                { withCredentials: true, validateStatus: () => true }
            );

            if (res.status === 200 && res.data?.userId) {
                localStorage.setItem("isLoggedIn", "true");
                localStorage.setItem("counsellorId", res.data.userId);
                localStorage.setItem("showEmotion", String(res.data.showEmotion));
                navigate("/counsellor/home", { replace: true });
            } else {
                localStorage.removeItem("isLoggedIn");
                localStorage.removeItem("counsellorId");
                localStorage.removeItem("showEmotion");
                setErrMsg('Login failed. Check your email or password.');
            }
        } catch {
            setErrMsg('Login failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const handleJournalLogin = () => navigate("/login");
    const handleRegister = () => navigate("/counsellor/register");

    return (
        <div className="min-h-screen flex items-center justify-center px-4 bg-white">
            <div className="w-full max-w-md p-6 rounded-xl shadow-lg space-y-6 border border-gray-100">
                <div className="flex justify-center">
                    <img src={logo} alt="MoodyClues" className="w-64 h-auto mx-auto my-4" />
                </div>

                <h1 className="text-2xl font-semibold text-center text-gray-800">
                    Counsellor Login
                </h1>

                <div className="flex justify-center">
                    <button onClick={handleJournalLogin} className="text-sm text-blue-600 hover:underline">
                        Journal User Login
                    </button>
                </div>

                {errMsg && (
                    <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded p-2">
                        {errMsg}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="email" className="block mb-1 text-gray-700">Email</label>
                        <input
                            id="email"
                            type="text"
                            value={email}
                            onChange={(e) => { setEmail(e.target.value); if (errMsg) setErrMsg(''); }}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your email"
                            autoComplete="username"
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block mb-1 text-gray-700">Password</label>
                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) => { setPassword(e.target.value); if (errMsg) setErrMsg(''); }}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your password"
                            autoComplete="current-password"
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading || !email.trim() || !password.trim()}
                        className="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition disabled:opacity-60"
                    >
                        {loading ? 'Logging in…' : 'Login'}
                    </button>

                    <p className="text-sm text-center text-gray-600">
                        Don’t have a counsellor account?{" "}
                        <button
                            type="button"
                            onClick={handleRegister}
                            className="text-blue-600 hover:underline"
                        >
                            Create one
                        </button>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default CounsellorLogin;
