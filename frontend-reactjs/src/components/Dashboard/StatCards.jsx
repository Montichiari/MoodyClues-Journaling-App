import React from "react";

const fmt = (v, digits = 2) => (v == null || Number.isNaN(v) ? "-" : Number(v).toFixed(digits));

export const StatCards = ({ loading, error, averages }) => {
    const items = [
        { title: "Average Sleep", value: fmt(averages?.sleep), unit: "hrs/day" },
        { title: "Average Water", value: fmt(averages?.water), unit: "L/day" },
        { title: "Average Work",  value: fmt(averages?.work),  unit: "hrs/day" },
    ];

    return (
        <>
            {items.map((it) => (
                <div key={it.title} className="col-span-12 sm:col-span-6 lg:col-span-4 p-4 border border-stone-200 rounded-lg">
                    <div className="text-xs text-stone-500 mb-1">{it.title}</div>
                    {loading ? (
                        <div className="animate-pulse h-7 w-24 bg-stone-200 rounded" />
                    ) : error ? (
                        <div className="text-sm text-red-600">Failed to load</div>
                    ) : (
                        <div className="flex items-baseline gap-2">
                            <div className="text-2xl font-semibold">{it.value}</div>
                            <div className="text-xs text-stone-500">{it.unit}</div>
                        </div>
                    )}
                </div>
            ))}
        </>
    );
};
