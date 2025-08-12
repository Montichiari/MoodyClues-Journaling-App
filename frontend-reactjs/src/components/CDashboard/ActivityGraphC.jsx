"use client";
import React from "react";
import { CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

const MOOD_TICKS = { 1:"Very Bad", 2:"Bad", 3:"Neutral", 4:"Good", 5:"Very Good" };

const ActivityGraphC = ({ loading, error, data, clients, selectedClientId, colorFor }) => {
    return (
        <div className="col-span-12 lg:col-span-8 overflow-hidden rounded border border-stone-300 mb-2">
            <div className="p-4">
                <h3 className="flex items-center gap-1.5 font-medium">Average Mood</h3>
            </div>
            <div className="h-64 px-4 pb-6">
                {loading ? (
                    <div className="animate-pulse h-full w-full bg-stone-100 rounded" />
                ) : error ? (
                    <div className="text-sm text-red-600 p-4">Failed to load chart.</div>
                ) : (
                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={data}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="date" tick={{ fontSize: 12 }} />
                            <YAxis domain={[1,5]} ticks={[1,2,3,4,5]} tickFormatter={v => MOOD_TICKS[v] ?? v} tick={{ fontSize: 12 }} width={90} />
                            <Tooltip />
                            <Legend />
                            {clients.map((c) => {
                                const highlighted = c.id === selectedClientId;
                                return (
                                    <Line
                                        key={c.id}
                                        type="monotone"
                                        dataKey={c.id}           // y-value per client
                                        name={c.firstName}
                                        stroke={colorFor(c.id)}
                                        strokeOpacity={highlighted ? 1 : 0.35}
                                        strokeWidth={highlighted ? 3 : 2}
                                        dot={false}
                                        connectNulls={false}
                                        isAnimationActive={false}
                                    />
                                );
                            })}
                        </LineChart>
                    </ResponsiveContainer>
                )}
            </div>
        </div>
    );
};

export default ActivityGraphC;
