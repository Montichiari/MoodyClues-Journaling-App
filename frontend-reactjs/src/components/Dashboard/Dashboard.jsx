import React, { useState } from "react";
import { TopBar } from "./TopBar.jsx";
import { Grid } from "./Grid.jsx";
import useDashboardData from "./useDashboardData.js";

const Dashboard = () => {
    const [rangeDays, setRangeDays] = useState(7);
    const { loading, error, windowRange, moodSeries, cardAverages, radarData, radarMax } = useDashboardData(rangeDays);
    return (
        <div className="bg-white rounded-lg shadow pb-8">
            <TopBar rangeDays={rangeDays} onChangeRange={setRangeDays} />
            <Grid
                loading={loading}
                error={error}
                windowRange={windowRange}
                moodSeries={moodSeries}
                cardAverages={cardAverages}
                radarData={radarData}
                radarMax={radarMax}
            />
        </div>
    );
};
export default Dashboard;
