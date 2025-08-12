import React from "react";
import Sidenav from "../components/Sidenav.jsx";
import { Box } from "@mui/material";
import HomeCard from "../components/Home/HomeCard.jsx";
import SidenavC from "../components/SidenavC.jsx";


const CounsellorHome = () => {
    const currentDate = new Date().toLocaleDateString("en-GB", {
        day: "numeric",
        month: "long",
        year: "numeric",
    });

    return (
        <Box sx={{ display: 'flex' }}>
            <SidenavC />
            <div className="p-6">
                <div className="flex justify-end mb-4">
                    <div className="bg-black text-white px-4 py-1 rounded-md text-sm font-medium">
                        Today is {currentDate}
                    </div>
                </div>

                <div className="mb-6">
                    <h1 className="text-3xl font-semibold">Welcome back</h1>
                    <p className="text-gray-500">Get started</p>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">

                    <HomeCard
                        to="/counsellor/home"
                        imgSrc="https://images.unsplash.com/photo-1434030216411-0b793f4b4173?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                        imgAlt="View Link Requests"
                        title="View Link Requests"
                        subtitle="View all requests"
                    />

                    <HomeCard
                        to="/counsellor/home"
                        imgSrc="https://images.unsplash.com/photo-1491309055486-24ae511c15c7?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                        imgAlt="View Clients"
                        title="View Clients"
                        subtitle="Access your clients entries"
                    />

                    <HomeCard
                        to="/counsellor/dashboard"
                        imgSrc="https://images.unsplash.com/photo-1551836022-d5d88e9218df"
                        imgAlt="Dashboard"
                        title="Dashboard"
                        subtitle="Track your clients' trends"
                    />

                </div>
            </div>
        </Box>
    );
};

export default CounsellorHome;
