import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";

export const Login = () => {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    const emotions = [Happy, Sad]

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('http://localhost:8080/user/login', {
                email: email,
                password: password
            });
            console.log(response.data);

            localStorage.setItem("isLoggedIn", "true");

            navigate("/journal/mood")

        } catch (err) {
            console.log(err)
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center px-4">
            <form onSubmit={handleSubmit} className="w-full max-w-sm space-y-4">
                <h1 className="text-xl text-center">Simple Login</h1>

                <div>
                    <label htmlFor="email" className="block mb-1">Email:</label>
                    <input
                        id="email"
                        type="text"
                        name="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="w-full border px-3 py-2 rounded"
                    />
                </div>

                <div>
                    <label htmlFor="password" className="block mb-1">Password:</label>
                    <input
                        id="password"
                        type="password"
                        name="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="w-full border px-3 py-2 rounded"
                    />
                </div>

                <button type="submit" className="w-full border px-3 py-2 rounded">
                    Login
                </button>
            </form>
        </div>
    );
};

export default Login;