import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import NoteLogsService from '../../services/NoteLogsService';

const NoteLogs = () => {
    // Get noteId from URL params
    const { noteId } = useParams();

    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Fetch logs based on the noteId
    useEffect(() => {
        const fetchLogs = async () => {
            try {
                const response = await NoteLogsService.logsOfANote(noteId);
                console.log(response);

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
    if (loading) return <div className="text-center text-lg font-semibold">Loading logs...</div>;
    if (error) return <div className="text-center text-red-500">Error: {error}</div>;

    return (
        <div className="max-w-7xl mx-auto p-6">
            <h1 className="text-3xl font-semibold mb-6">Change Logs for Note {noteId}</h1>

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
                                    {/* Old Heading in Bold */}
                                    <td className="px-4 py-2 text-sm text-gray-700 font-bold">{log.oldHeading}</td>

                                    {/* New Heading */}
                                    <td className="px-4 py-2 text-sm text-gray-700">{log.newHeading}</td>

                                    {/* Old Content with Rich Text & Truncation */}
                                    <td className="px-4 py-2 text-sm text-gray-700">
                                        <div
                                            className="text-gray-700"
                                            dangerouslySetInnerHTML={{ __html: truncateText(log.oldContent) }}
                                        />
                                    </td>

                                    {/* New Content with Rich Text & Truncation */}
                                    <td className="px-4 py-2 text-sm text-gray-700">
                                        <div
                                            className="text-gray-700"
                                            dangerouslySetInnerHTML={{ __html: truncateText(log.newContent) }}
                                        />
                                    </td>

                                    {/* Changed By */}
                                    <td className="px-4 py-2 text-sm text-gray-700">{log.changedBy}</td>

                                    {/* Note Owner */}
                                    <td className="px-4 py-2 text-sm text-gray-700">{log.noteOwner}</td>

                                    {/* Created Date */}
                                    <td className="px-4 py-2 text-sm text-gray-700">{log.createdDate}</td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default NoteLogs;
