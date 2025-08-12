import React, { useState } from "react";
import Sidenav from "../components/Sidenav.jsx";
import TopBarC from "../components/CDashboard/TopBarC.jsx";
import GridC from "../components/CDashboard/GridC.jsx";
import useCounsellorDashboardData from "../components/CDashboard/useCounsellorDashboardData.js";
import SidenavC from "../components/SidenavC.jsx";

const CounsellorDashboard = () => {
    const [rangeDays, setRangeDays] = useState(7);
    const {
        loading, error,
        windowRange,
        clients, selectedClientId, setSelectedClientId,
        moodSeriesMerged, radarDataMerged, radarMax, colorFor
    } = useCounsellorDashboardData(rangeDays);

    return (
        <div className="min-h-screen">
            <SidenavC />
            <main className="p-4 md:ml-[200px]">
                <div className="bg-white rounded-lg shadow pb-8">
                    <TopBarC rangeDays={rangeDays} onChangeRange={setRangeDays} />
                    <GridC
                        loading={loading}
                        error={error}
                        clients={clients}
                        selectedClientId={selectedClientId}
                        setSelectedClientId={setSelectedClientId}
                        moodSeriesMerged={moodSeriesMerged}
                        radarDataMerged={radarDataMerged}
                        radarMax={radarMax}
                        colorFor={colorFor}
                    />
                </div>
            </main>
        </div>
    );
};

export default CounsellorDashboard;
