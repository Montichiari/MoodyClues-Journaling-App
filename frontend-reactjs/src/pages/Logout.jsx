// Logout.jsx
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const API = 'http://122.248.243.60:8080';

const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {
        let mounted = true;

        (async () => {
            // detect who is (was) logged in
            const hadCounsellor = !!localStorage.getItem('counsellorId');
            const hadUser = localStorage.getItem('isLoggedIn') === 'true' && !!localStorage.getItem('userId');

            // try to end any server sessions (ignore errors)
            await Promise.allSettled([
                axios.get(`${API}/api/user/logout`, { withCredentials: true, timeout: 6000 }),
                axios.get(`${API}/api/counsellor/logout`, { withCredentials: true, timeout: 6000 }),
            ]);

            // clear client state
            localStorage.removeItem('isLoggedIn');
            localStorage.removeItem('userId');
            localStorage.removeItem('showEmotion');
            localStorage.removeItem('counsellorId');

            if (!mounted) return;

            const dest = hadCounsellor ? '/counsellor/login' : '/login';
            navigate(dest, { replace: true });
        })();

        return () => { mounted = false; };
    }, [navigate]);

    return <div className="px-6 py-4">Logging out…</div>;
};

export default Logout;
