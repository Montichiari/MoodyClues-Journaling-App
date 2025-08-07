import React from 'react'
import Dashboard from "../components/Dashboard/Dashboard.jsx";
import Sidenav from "../components/Sidenav.jsx";

export const UserDashboard = () => {
    return (
        <div className = "p-4 grid-cols-[220px,_1fr]">

            <Sidenav />
            <Dashboard />

        </div>
    )
}
export default UserDashboard;