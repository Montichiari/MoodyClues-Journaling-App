"use client";
import React from "react";
import { PolarGrid, PolarAngleAxis, RadarChart, Radar, ResponsiveContainer, Tooltip } from "recharts";

const EmotionRadarC = ({ loading, error, data, clients, selectedClientId, colorFor, max }) => {
    return (
        <div className="col-span-12 lg:col-span-4 rounded border border-stone-300 mb-2">
            <div className="p-4">
                <h3 className="flex items-center gap-1.5 font-medium">Emotions (Count)</h3>
            </div>
            <div className="h-64 px-4 pb-6">
                {loading ? (
                    <div className="animate-pulse h-full w-full bg-stone-100 rounded" />
                ) : error ? (
                    <div className="text-sm text-red-600 p-4">Failed to load radar.</div>
                ) : (
                    <ResponsiveContainer width="100%" height="100%">
                        <RadarChart data={data} outerRadius="70%" margin={{ top: 12, right: 28, bottom: 20, left: 28 }}>
                            <PolarGrid />
                            <PolarAngleAxis dataKey="emotion" tick={{ fontSize: 11 }} tickMargin={10} />
                            <Tooltip />
                            {clients.map(c => {
                                const highlighted = c.id === selectedClientId;
                                return (
                                    <Radar
                                        key={c.id}
                                        name={c.firstName}
                                        dataKey={c.id}
                                        stroke={colorFor(c.id)}
                                        fill={colorFor(c.id)}
                                        fillOpacity={highlighted ? 0.35 : 0.15}
                                        strokeOpacity={highlighted ? 1 : 0.5}
                                        isAnimationActive={false}
                                    />
                                );
                            })}
                        </RadarChart>
                    </ResponsiveContainer>
                )}
            </div>
        </div>
    );
};

export default EmotionRadarC;
