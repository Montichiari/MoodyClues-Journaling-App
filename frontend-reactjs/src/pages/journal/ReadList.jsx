import React, { useEffect, useState } from "react";
import { Box, TextField, Table, TableHead, TableBody, TableRow, TableCell } from '@mui/material';
import Sidenav from '../../components/Sidenav';
import { useNavigate } from "react-router-dom";
import axios from "axios";

const drawerWidth = 240;

function ReadList() {
    const [entries, setEntries] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const userId = sessionStorage.getItem("userId");
        if (!userId) {
            console.error("userId can't find!");
            return;
        }
        axios.get(`/api/journal/all?userId=${userId}`)
            .then(res => setEntries(res.data))
            .catch(err => console.error(err));
    }, []);

    return (
        <Box sx={{ display: 'flex', height: '100vh' }}>
            <Sidenav />

            {/* 这里主内容区域取消所有 marginLeft、paddingLeft */}
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    width: `calc(100% - ${drawerWidth}px)`,
                    paddingTop: 3,
                    paddingBottom: 3,
                    paddingLeft: 0,      // 关键：无左内边距
                    paddingRight: 3,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'flex-start',
                    overflowY: 'auto',
                    boxSizing: 'border-box',
                    textAlign: 'left',
                }}
            >

                {/* 去掉 Toolbar，顶部不留空白 */}

                <h1 style={{ margin: 0, marginBottom: 16, fontWeight: 'normal', width: '100%' }}>
                    Your Journal
                </h1>

                <TextField
                    placeholder="Search title..."
                    variant="outlined"
                    size="small"
                    fullWidth
                    sx={{
                        marginBottom: 3,
                        maxWidth: 400,
                        '& .MuiOutlinedInput-root': {
                            borderRadius: 1,
                            backgroundColor: '#fff'
                        }
                    }}
                />

                <Box
                    sx={{
                        border: '1px solid #e0e0e0',
                        borderRadius: 1,
                        overflow: 'hidden',
                        width: '100%',
                        maxWidth: 700,
                    }}
                >
                    {entries.length === 0 ? (
                        <Box sx={{ p: 3, color: 'text.secondary', textAlign: 'center' }}>
                            暂无日记，快去写点内容吧！
                        </Box>
                    ) : (
                        <Table>
                            <TableHead>
                                <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                                    <TableCell>Date</TableCell>
                                    <TableCell>Title</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {entries.map(entry => (
                                    <TableRow
                                        key={entry.id}
                                        hover
                                        onClick={() => navigate(`/journal/read/${entry.id}`)}
                                        sx={{ cursor: 'pointer' }}
                                    >
                                        <TableCell sx={{ color: 'text.secondary', width: 140 }}>
                                            {new Date(entry.date).toLocaleDateString()}
                                        </TableCell>
                                        <TableCell>{entry.title}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    )}
                </Box>
            </Box>
        </Box>
    );
}

export default ReadList;
