// Grid.jsx
import React from "react";
import { StatCards } from "./StatCards.jsx";
import { ActivityGraph } from "./ActivityGraph.jsx";
import { EmotionRadar } from "./EmotionRadar.jsx";

export const Grid = ({ loading, error, windowRange, moodSeries, cardAverages, radarData, radarMax }) => {
    return (
        <div className="px-4 mt-4 pb-6 grid gap-5 grid-cols-12">
            <StatCards loading={loading} error={error} averages={cardAverages} />
            <ActivityGraph loading={loading} error={error} data={moodSeries} />
            <EmotionRadar loading={loading} error={error} data={radarData} max={radarMax} />
        </div>
    );
};
