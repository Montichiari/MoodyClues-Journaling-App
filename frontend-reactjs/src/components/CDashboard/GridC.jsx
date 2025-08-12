import React from "react";
import ClientsCardC from "./ClientsCardC.jsx";
import ActivityGraphC from "./ActivityGraphC.jsx";
import EmotionRadarC from "./EmotionRadarC.jsx";

const GridC = ({
                   loading, error,
                   clients, selectedClientId, setSelectedClientId,
                   moodSeriesMerged, radarDataMerged, radarMax, colorFor
               }) => {
    return (
        <div className="px-4 mt-4 pb-6 grid gap-5 grid-cols-12">
            <ClientsCardC
                clients={clients}
                selectedClientId={selectedClientId}
                setSelectedClientId={setSelectedClientId}
            />
            <ActivityGraphC
                loading={loading}
                error={error}
                data={moodSeriesMerged}
                clients={clients}
                selectedClientId={selectedClientId}
                colorFor={colorFor}
            />
            <EmotionRadarC
                loading={loading}
                error={error}
                data={radarDataMerged}
                clients={clients}
                selectedClientId={selectedClientId}
                colorFor={colorFor}
                max={radarMax}
            />
        </div>
    );
};

export default GridC;
