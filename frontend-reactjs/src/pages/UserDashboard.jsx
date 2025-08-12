import React from "react";
import Dashboard from "../components/Dashboard/Dashboard.jsx";
import Sidenav from "../components/Sidenav.jsx";

export const UserDashboard = () => {
    return (
        <div className="min-h-screen bg-stone-50">
            <Sidenav />
            <main className="p-4 md:ml-[200px]">
                <Dashboard />
            </main>
        </div>
    );
};
export default UserDashboard;
