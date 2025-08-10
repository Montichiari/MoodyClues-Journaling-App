import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

export default function ReadDetail() {
    const { entryId } = useParams();
    const [entry, setEntry] = useState(null);

    useEffect(() => {
        axios.get(`/api/journal/${entryId}`)
            .then(res => setEntry(res.data))
            .catch(err => console.error(err));
    }, [entryId]);

    if (!entry) return <div>Loading...</div>;

    return (
        <div>
            <h1>{entry.title}</h1>
            <p>{new Date(entry.date).toLocaleDateString()}</p>
            <p>Morning: {entry.morningMood}</p>
            <p>Afternoon: {entry.afternoonMood}</p>
            <p>Evening: {entry.eveningMood}</p>
            <p>Sleep: {entry.sleepHours} Hours</p>
            <p>Water: {entry.waterLitres} Litres</p>
            <p>Work: {entry.workHours} Hours</p>
            <p>You felt: {entry.feelings?.join(", ")}</p>
            <textarea readOnly value={entry.content}></textarea>
        </div>
    );
}
