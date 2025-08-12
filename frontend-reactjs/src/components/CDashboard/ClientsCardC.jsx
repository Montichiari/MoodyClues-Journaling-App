import React from "react";

const ClientsCardC = ({ clients, selectedClientId, setSelectedClientId }) => {
    return (
        <div className="col-span-12 p-4 border border-stone-200 rounded-lg">
            <div className="text-xs text-stone-500 mb-2">Clients</div>
            {clients.length === 0 ? (
                <div className="text-sm text-stone-500">No linked clients.</div>
            ) : (
                <div className="flex flex-wrap gap-2">
                    {clients.map(c => {
                        const selected = c.id === selectedClientId;
                        return (
                            <button
                                key={c.id}
                                onClick={() => setSelectedClientId(c.id)}
                                className={[
                                    "px-3 py-1.5 rounded border transition",
                                    selected
                                        ? "border-violet-500 text-violet-700 bg-violet-50"
                                        : "border-stone-300 text-stone-700 hover:bg-stone-100"
                                ].join(" ")}
                            >
                                {c.firstName}
                            </button>
                        );
                    })}
                </div>
            )}
        </div>
    );
};

export default ClientsCardC;
