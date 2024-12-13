import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import AdminService from '../../services/AdminService';
import BounceLoader from "react-spinners/ClipLoader";
import { DataGrid } from '@mui/x-data-grid';

const AllNotesAllLogs = () => {
    // Get noteId from URL params
    const { noteId } = useParams();

    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Fetch logs based on the noteId
    useEffect(() => {
        const fetchLogs = async () => {
            try {
                const response = await AdminService.getNotesAllLogs();
                // console.log(response);

                if (!response.status === 200) {
                    throw new Error('Failed to fetch logs');
                }
                setLogs(response.data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchLogs();
    }, [noteId]); // Re-fetch if noteId changes
    const truncateText = (text, length = 30) => {
        if (text && text.length > length) {
            return text.substring(0, length) + '...';
        }
        return text;
    };

    const rows = logs.map((item) => {
        return {
            noteLogId: item.noteLogId,
            oldHeading: item.oldHeading,
            newHeading: item.newHeading,
            oldContent: item.oldContent,
            newContent: item.newContent,
            modifiedBy: item.changedBy,
            Owner: item.noteOwner,
            modifiedAt: item.createdDate,
        }
    })


    const columns = [
        { field: 'noteLogId', headerName: 'Log Id', width: 10 },
        { field: 'oldHeading', headerName: 'Old Heading', width: 150 },
        { field: 'newHeading', headerName: 'New Heading', width: 150 },
        {
            field: 'oldContent',
            headerName: 'Old Content',
            width: 150,
            renderCell: (params) => (
                <div
                    dangerouslySetInnerHTML={{ __html: truncateText(params.value) }}
                />
            ),
        },
        {
            field: 'newContent',
            headerName: 'New Content',
            width: 150,
            renderCell: (params) => (
                <div
                    dangerouslySetInnerHTML={{ __html: truncateText(params.value) }}
                />
            ),
        },
        { field: 'modifiedBy', headerName: 'Changed By', width: 150 },
        { field: 'Owner', headerName: 'Owner', width: 150 },
        { field: 'modifiedAt', headerName: 'Log Time', width: 200 }
    ];
    const CustomPagination = () => (
        <GridFooter rowsPerPageOptions={[10, 20, 25]} />
    );


    return (
        <div className="max-w-7xl mx-auto p-6">
            <h1 className="text-3xl font-semibold text-center mb-6">Change Logs for All Note</h1>

            {loading ? <div className="text-3xl font-semibold text-center mb-6"><BounceLoader
                color="hsla(217, 19%, 52%, 1)"
                cssOverride={{}}
                size={60}
                speedMultiplier={5}
            /></div> : (
                <>
                    {error ?
                        <h6 className="text-1xl bg-red-100 font-semibold text-center mb-6 text-red-700">{error}</h6> : (<div className="overflow-x-auto shadow-lg rounded-lg border border-gray-200">
                            {/* <table className="min-w-full bg-white">
                            <thead className="bg-gray-100">
                                <tr>
                                    <th className="px-4 py-2 text-left text-sm font-medium text-gray-600">Log Id</th>
                                    <th className="px-4 py-2 text-left text-sm font-medium text-gray-600">Old Heading</th>
                                    <th className="px-4 py-2 text-left text-sm font-medium text-gray-600">New Heading</th>
                                    <th className="px-4 py-2 text-left text-sm font-medium text-gray-600">Old Content</th>
                                    <th className="px-4 py-2 text-left text-sm font-medium text-gray-600">New Content</th>
                                    <th className="px-4 py-2 text-left text-sm font-medium text-gray-600">Changed By</th>
                                    <th className="px-4 py-2 text-left text-sm font-medium text-gray-600">Note Owner</th>
                                    <th className="px-4 py-2 text-left text-sm font-medium text-gray-600">Created Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                {logs.length === 0 ? (
                                    <tr>
                                        <td colSpan="7" className="px-4 py-2 text-center text-gray-500">No logs exists.</td>
                                    </tr>
                                ) : (
                                    logs.map((log, index) => (
                                        <tr key={index} className="hover:bg-gray-50">
                                            <td className="px-4 py-2 text-sm text-gray-700">{log.noteLogId}</td>
                                            
                                            <td className="px-4 py-2 text-sm text-gray-700 font-serif">{log.oldHeading}</td>

                                            <td className="px-4 py-2 text-sm text-gray-700">{log.newHeading}</td>

                                            <td className="px-4 py-2 text-sm text-gray-700">
                                                <div
                                                    className="text-gray-700"
                                                    dangerouslySetInnerHTML={{ __html: truncateText(log.oldContent) }}
                                                />
                                            </td>

                                            <td className="px-4 py-2 text-sm text-gray-700">
                                                <div
                                                    className="text-gray-700"
                                                    dangerouslySetInnerHTML={{ __html: truncateText(log.newContent) }}
                                                />
                                            </td>

                                            <td className="px-4 py-2 text-sm text-gray-700">{log.changedBy}</td>

                                            <td className="px-4 py-2 text-sm text-gray-700">{log.noteOwner}</td>

                                            <td className="px-4 py-2 text-sm text-gray-700">{log.createdDate}</td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table> */}

                            <div style={{ height: '60vh', width: '100%' }}>
                                <DataGrid rows={rows} columns={columns} getRowId={(row) => row.noteLogId || row.index}
                                    pageSize={10} // Default page size
                                    pagination
                                    paginationMode="client"
                                    components={{
                                        Pagination: CustomPagination,
                                    }} />
                            </div>
                        </div>)

                    }
                </>

            )}



        </div>
    );
};

export default AllNotesAllLogs;
