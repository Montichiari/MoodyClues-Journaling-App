import React from "react";
import Sidenav from "../components/Sidenav.jsx";
import { Box } from "@mui/material";
import { Link } from "react-router-dom";   // added


const Home = () => {
    const currentDate = new Date().toLocaleDateString("en-GB", {
        day: "numeric",
        month: "long",
        year: "numeric",
    });

    return (
        <Box sx={{ display: 'flex' }}>
        <Sidenav />
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
                {/* Write Card */}
                <Link to="/journal/entry" className="rounded-md overflow-hidden shadow hover:shadow-lg transition duration-300 cursor-pointer block">
                  <img
                    src="https://images.unsplash.com/photo-1434030216411-0b793f4b4173?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                    alt="Write"
                    className="w-full h-40 object-cover"
                  />
                  <div className="p-4">
                    <h3 className="text-lg font-semibold">Write To Journal</h3>
                    <p className="text-sm text-gray-500">Record today</p>
                  </div>
                </Link>

                {/* Read Card */}
                <div className="rounded-md overflow-hidden shadow hover:shadow-lg transition duration-300 cursor-pointer">
                    <img
                        src="https://images.unsplash.com/photo-1491309055486-24ae511c15c7?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                        alt="Read"
                        className="w-full h-40 object-cover"
                    />
                    <div className="p-4">
                        <h3 className="text-lg font-semibold">Read Your Journal</h3>
                        <p className="text-sm text-gray-500">Have a look at your past</p>
                    </div>
                </div>

                {/* Track Card */}
                <div className="rounded-md overflow-hidden shadow hover:shadow-lg transition duration-300 cursor-pointer">
                    <img
                        src="https://images.unsplash.com/photo-1551836022-d5d88e9218df"
                        alt="Track"
                        className="w-full h-40 object-cover"
                    />
                    <div className="p-4">
                        <h3 className="text-lg font-semibold">Dashboards</h3>
                        <p className="text-sm text-gray-500">
                            Track trends on your mood and emotions, and see the future
                        </p>
                    </div>
                </div>
            </div>
        </div>
        </Box>
    );
};

export default Home;
