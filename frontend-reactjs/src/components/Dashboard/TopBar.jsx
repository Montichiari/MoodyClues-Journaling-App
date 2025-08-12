// TopBar.jsx
import React, { useMemo, useState } from "react";
import { Button, Menu, MenuItem } from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

const OPTIONS = [
    { label: "Last 7 days", value: 7 },
    { label: "Last 14 days", value: 14 },
    { label: "Last 30 days", value: 30 },
];

function formatToday() {
    return new Date().toLocaleDateString("en-SG", { day: "numeric", month: "short", year: "numeric" });
}

export const TopBar = ({ rangeDays, onChangeRange }) => {
    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);
    const selected = useMemo(() => OPTIONS.find(o => o.value === rangeDays) ?? OPTIONS[0], [rangeDays]);

    return (
        <div className="border-b px-5 pt-4 pb-3 border-stone-200 mb-4">
            <div className="flex items-center justify-between gap-3">
                <div className="text-3xl font-extrabold tracking-tight">Today is {formatToday()}</div>

                <Button
                    variant="outlined"
                    onClick={(e) => setAnchorEl(e.currentTarget)}
                    endIcon={<ExpandMoreIcon />}
                    sx={{
                        textTransform: "none",
                        fontSize: "0.95rem",
                        px: 1.5,
                        py: 0.75,
                        borderRadius: 2,
                    }}
                >
                    {selected.label}
                </Button>

                <Menu
                    anchorEl={anchorEl}
                    open={open}
                    onClose={() => setAnchorEl(null)}
                    anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
                    transformOrigin={{ vertical: "top", horizontal: "right" }}
                >
                    {OPTIONS.map(o => (
                        <MenuItem
                            key={o.value}
                            selected={o.value === rangeDays}
                            onClick={() => { onChangeRange?.(o.value); setAnchorEl(null); }}
                        >
                            {o.label}
                        </MenuItem>
                    ))}
                </Menu>
            </div>
        </div>
    );
};
