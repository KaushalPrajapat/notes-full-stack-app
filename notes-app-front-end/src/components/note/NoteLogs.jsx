import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import NoteLogsService from '../../services/NoteLogsService';
import { toast } from 'react-toastify';
import { DataGrid } from '@mui/x-data-grid';



const NoteLogs = () => {
    // Get noteId from URL params
    const { noteId } = useParams();

    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const truncateText = (text, length = 30) => {
        if (text && text.length > length) {
            return text.substring(0, length) + '...';
        }
        return text;
    };
    // Fetch logs based on the noteId
    useEffect(() => {
        toast.info("loading logs for your note");
        const fetchLogs = async () => {
            try {
                const response = await NoteLogsService.logsOfANote(noteId);
                if (!response.status === 200) {
                    throw new Error('Failed to fetch logs');
                }
                setLogs(response.data);
            } catch (err) {
                toast.error(err.message)
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchLogs();
    }, []);


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



    if (loading) return <div className="text-center text-lg font-semibold">Loading logs...</div>;
    if (error) return <div className="text-center text-red-500">Error: {error}</div>;

    return (
        <div className="max-w-7xl mx-auto p-6">
            <h1 className="text-3xl font-semibold mb-6">Change Logs for Note {noteId}</h1>
            <div style={{ height: 300, width: '100%' }}>
                <DataGrid rows={rows} columns={columns} getRowId={(row) => row.noteLogId || row.index}
                   
                    // pageSize={10} // Default page size
                    rowsPerPageOptions={[10, 20, 25]} // Custom page size options
                    pagination
                    paginationMode="client" />
            </div>


            {/*  
            <div className="overflow-x-auto shadow-lg rounded-lg border border-gray-200">
                <table className="min-w-full bg-white">
                    <thead className="bg-gray-100">
                        <tr>
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
                                <td colSpan="7" className="px-4 py-2 text-center text-gray-500">No logs found for this note.</td>
                            </tr>
                        ) : (
                            logs.map((log, index) => (
                                <tr key={index} className="hover:bg-gray-50">
                                    
                                    <td className="px-4 py-2 text-sm text-gray-700 font-bold">{log.oldHeading}</td>

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
                </table>
            </div>
 */}


        </div>
    );
};

export default NoteLogs;
