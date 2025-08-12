// EmotionRadar.jsx
"use client";
import React from "react";
import {
    PolarGrid,
    PolarAngleAxis,
    RadarChart,
    Radar,
    ResponsiveContainer,
    Tooltip,
} from "recharts";

export const EmotionRadar = ({ loading, error, data, max }) => {
    return (
        <div className="col-span-12 lg:col-span-4 rounded border border-stone-300"> {/* no overflow-hidden */}
            <div className="p-4">
                <h3 className="flex items-center gap-1.5 font-medium">Emotions (Count)</h3>
            </div>

            <div className="h-64 px-4 pb-6"> {/* bottom padding added */}
                {loading ? (
                    <div className="animate-pulse h-full w-full bg-stone-100 rounded" />
                ) : error ? (
                    <div className="text-sm text-red-600 p-4">Failed to load radar.</div>
                ) : (
                    <ResponsiveContainer width="100%" height="100%">
                        <RadarChart
                            data={data}
                            outerRadius="70%"
                            margin={{ top: 12, right: 28, bottom: 20, left: 28 }}
                        >
                            <PolarGrid />
                            <PolarAngleAxis dataKey="emotion" tick={{ fontSize: 11 }} tickMargin={10} />
                            {/* Removed PolarRadiusAxis to hide the number scale */}
                            <Tooltip />
                            <Radar
                                name="Count"
                                dataKey="count"
                                stroke="#8884d8"
                                fill="#8884d8"
                                fillOpacity={0.4}
                                isAnimationActive={false}
                            />
                        </RadarChart>
                    </ResponsiveContainer>
                )}
            </div>
        </div>
    );
};
