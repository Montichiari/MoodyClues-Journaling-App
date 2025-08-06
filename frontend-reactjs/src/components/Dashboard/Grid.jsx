import React from 'react'
import {StatCards} from "./StatCards.jsx";
import {ActivityGraph} from "./ActivityGraph.jsx";

export const Grid = () => {
    return (
        <div
        className = "px-4 grid gap-3 grid-cols-12"
        >
            <StatCards />
            <ActivityGraph />

        </div>
    )
}
