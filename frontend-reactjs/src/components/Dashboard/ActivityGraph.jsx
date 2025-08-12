// ActivityGraph.jsx
"use client";
import React from "react";
import { CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

export const ActivityGraph = ({ loading, error, data }) => {
    return (
        <div className="col-span-12 lg:col-span-8 overflow-hidden rounded border border-stone-300">
            <div className="p-4">
                <h3 className="flex items-center gap-1.5 font-medium">Average Mood</h3>
            </div>

            <div className="h-64 px-4 pb-6"> {/* added pb-6 */}
                {loading ? (
                    <div className="animate-pulse h-full w-full bg-stone-100 rounded" />
                ) : error ? (
                    <div className="text-sm text-red-600 p-4">Failed to load chart.</div>
                ) : (
                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={data}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="date" tick={{ fontSize: 12 }} />
                            <YAxis domain={[1, 5]} ticks={[1,2,3,4,5]} tick={{ fontSize: 12 }} />
                            <Tooltip />
                            <Legend />
                            <Line type="monotone" dataKey="avgMood" name="Avg Mood" stroke="#8884d8" dot={false} isAnimationActive={false} />
                        </LineChart>
                    </ResponsiveContainer>
                )}
            </div>
        </div>
    );
};
