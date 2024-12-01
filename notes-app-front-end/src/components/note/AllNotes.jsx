import React, { useEffect, useState } from 'react';
import 'react-quill/dist/quill.snow.css'; // Import styles for the editor
import { Link, useNavigate } from 'react-router-dom';
import NoteService from '../../services/NoteService';
import { ToastContainer, toast } from 'react-toastify';
import BounceLoader from "react-spinners/ClipLoader";


const AllNotes = () => {
    const [notes, setNotes] = useState([]); // State to store the list of notes
    const [loading, setLoading] = useState(true); // Loading state for API call
    const [error, setError] = useState(''); // Loading state for API call
    const [isAdmin, setIsAdmin] = useState(localStorage.getItem('role') == "ROLE_SU"
        || localStorage.getItem('role') == "ROLE_ADMIN")
    const navigate = useNavigate();

    const notifyDelete = () => toast("Hi edit your note")
    const notifyLog = () => toast("Hi edit your note")


    useEffect(() => {
        setTimeout(
            async () => {
                try {
                    const response = await NoteService.getAllNotes()
                    console.log(response.data);
                    setNotes(response.data);
                    setLoading(false);
                } catch (e) {
                    console.error(e);
                    setError(e.message);
                    setLoading(false)
                }

            }, 1000);

    }, []);

    // Handle Delete action (e.g., making API call to delete the note)
    const handleDelete = async (noteId) => {
        try {
            const resp = await NoteService.deleteNoteByNoteId(noteId);
            toast("Note deleted successfully");
            setTimeout(() => {
                location.reload();
                if (isAdmin)
                    navigate("/admin/all-notes")
                else
                    navigate('/user/all-notes');
            }, 500);
        } catch (error) {
            setError(error.message)
        }
    };

    // Handle Edit action (e.g., navigate to the edit page)
    const handleEdit = (noteId) => {
        toast("Editing note");
        setTimeout(() => {
            // location.reload(); 
            if (isAdmin)
                navigate(`/admin/edit-note/${noteId}`)
            else
                navigate(`/user/edit-note/${noteId}`);  // Redirect to homepage or another page
        }, 1000);

    };

    // Handle Logs action (show logs for the note)
    const handleLogs = (noteId) => {
        toast("Checking for Logs")
        setTimeout(() => {
            if (isAdmin)
                navigate(`/admin/logs-note/${noteId}`);
            else
                navigate(`/user/logs-note/${noteId}`);
        }, 2000);
    };


    return (
        <div className="max-w-4xl mx-auto p-6 bg-gray-100">
            <ToastContainer />
            <h2 className="text-3xl font-semibold text-center mb-6">My Notes</h2>
            {/* Show error */}
            {error && <h6 className="text-3xl bg-red-100 font-semibold text-center mb-6 text-red-700">{error}</h6>
            }
            {/* Render list of notes */}
            {loading ? <div className="text-3xl font-semibold text-center mb-6"><BounceLoader
                color="hsla(217, 19%, 52%, 1)"
                cssOverride={{}}
                size={60}
                speedMultiplier={5}
            /></div> : (
                <div className="space-y-6">
                    {notes.length > 0 ? notes.map((note) => (
                        <div key={note.noteId} className="bg-white shadow-md rounded-lg p-6">
                            <h3 className="text-2xl font-semibold mb-4">{note.noteHeading}</h3>

                            {/* Render HTML content using dangerouslySetInnerHTML */}
                            <div
                                className="text-gray-700 mb-4 break-words whitespace-pre-wrap"
                                dangerouslySetInnerHTML={{ __html: note.content }}
                            />

                            <div className="text-sm text-gray-500 mb-4">
                                <p>Created: {new Date(note.createdDate).toLocaleString()}</p>
                                <p>Last Updated: {new Date(note.updatedDate).toLocaleString()}</p>
                            </div>

                            {/* Action Buttons */}
                            <div className="flex space-x-4 mt-6">
                                <button
                                    onClick={() => handleEdit(note.noteId)}
                                    className="px-4 py-2 bg-yellow-500 text-white font-semibold rounded-md hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-500"
                                >
                                    Edit
                                </button>
                                <button
                                    onClick={() => handleDelete(note.noteId)}
                                    className="px-4 py-2 bg-red-500 text-white font-semibold rounded-md hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500"
                                >
                                    Delete
                                </button>
                                <button
                                    onClick={() => handleLogs(note.noteId)}
                                    className="px-4 py-2 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                >
                                    Logs
                                </button>
                            </div>
                        </div>
                    )) :
                        <div className='text-3xl font-semibold text-center mb-6'>

                            {localStorage.getItem("role") == "ROLE_ADMIN" || localStorage.getItem("role") == "ROLE_SU" ?

                                <></>
                                :
                                <Link
                                    to="/user/add-note"
                                    className="px-4 py-2 text-white bg-blue-300 rounded hover:bg-blue-500 transition"
                                >
                                    Saved your first note.
                                </Link>
                            }
                        </div>
                    }
                </div>
            )}
        </div>
    );
};

export default AllNotes;
