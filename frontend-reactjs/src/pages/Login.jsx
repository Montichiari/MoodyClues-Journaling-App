import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import logo from "../assets/moodyclues-logo.png";

export const Login = () => {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('http://localhost:8080/api/user/login', {
                email: email,
                password: password
            }, {
                withCredentials: true
            });

            localStorage.setItem("isLoggedIn", "true");
            localStorage.setItem("userId", response.data.userId);
            localStorage.setItem("showEmotion", response.data.showEmotion);

            navigate("/home");

        } catch (err) {
            console.log(err);
        }
    };

    const handleCounsellorLogin = () => {
        navigate("/counsellor/login");
    };

    return (
        <div className="min-h-screen flex items-center justify-center px-4 bg-white">
            <div className="w-full max-w-md p-6 rounded-xl shadow-lg space-y-6 border border-gray-100">

                {/* Logo */}
                <div className="flex justify-center">
                    <img src={logo} alt="MoodyClues" className="w-64 h-auto mx-auto my-4" />
                </div>

                {/* Heading */}
                <h1 className="text-2xl font-semibold text-center text-gray-800">
                    Journal User Login
                </h1>

                {/* Counsellor Login Button */}
                <div className="flex justify-center">
                    <button
                        onClick={handleCounsellorLogin}
                        className="text-sm text-blue-600 hover:underline"
                    >
                        Counsellor Login
                    </button>
                </div>

                {/* Form */}
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="email" className="block mb-1 text-gray-700">Email</label>
                        <input
                            id="email"
                            type="text"
                            name="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your email"
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block mb-1 text-gray-700">Password</label>
                        <input
                            id="password"
                            type="password"
                            name="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full border border-gray-300 px-3 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                            placeholder="Enter your password"
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
                    >
                        Login
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Login;
