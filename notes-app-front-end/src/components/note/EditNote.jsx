import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css'; // Import Quill styles
import NoteService from '../../services/NoteService';
import { toast, ToastContainer } from 'react-toastify';
import ClipLoader from "react-spinners/ClipLoader";


const EditNote = () => {
    const { noteId } = useParams(); // Get the noteId from the URL
    const navigate = useNavigate();

    const [noteHeading, setNoteHeading] = useState('');
    const [content, setContent] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Fetch the note data when the component mounts
    useEffect(() => {
        setTimeout(() => {
            toast("Hi edit your note " + noteId)

        }, 100);
        const fetchNoteData = async () => {
            try {
                const response = await NoteService.getNoteById(noteId); // Use the NoteService to get the note by ID
                const data = response.data;
                setNoteHeading(data.noteHeading);
                setContent(data.content); // Set rich text content
                setLoading(false);
            } catch (error) {
                setError(error.message);
                setLoading(false);
            }
        };

        fetchNoteData();
    }, [noteId]); // Fetch the note data whenever noteId changes

    // Handle the note update
    const handleUpdate = async (e) => {
        e.preventDefault();

        const updatedNote = {
            noteHeading,
            content,
        };

        try {
            const response = await NoteService.updateNote(noteId, updatedNote.noteHeading, updatedNote.content); // Update note using NoteService
            console.log(response);



            if (response.data.noteId != null) {
                toast("Note updated successFully")
                setTimeout(() => {
                    if (localStorage.getItem('role') === "ROLE_ADMIN" || localStorage.getItem('role') === "ROLE_SU") {
                        navigate("/admin/all-notes");
                        location.reload()
                    }
                    else {
                        location.reload()
                        navigate("/user/all-notes")
                    }
                }, 1500);
            }
        } catch (error) {
            toast("Note updated failed, Try later " + noteId)
            setTimeout(() => {
                setError(error.message);
            }, 1300);
        }
    };

    if (error) {
        return <h2 className="text-3xl font-semibold text-center mb-6 text-red-700">{error}</h2>;
    }

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white shadow-md rounded-lg">
            <ToastContainer />
            <h2 className="text-3xl font-semibold text-center mb-6">Edit Note</h2>
            {(loading) &&
                <div className='text-3xl font-semibold text-center mb-6'><ClipLoader /></div>
            }
            <form onSubmit={handleUpdate}>
                {/* Note Heading */}
                <div className="mb-4">
                    <label htmlFor="noteHeading" className="block text-lg font-medium text-gray-700">
                        Note Heading
                    </label>
                    <input
                        type="text"
                        id="noteHeading"
                        name="noteHeading"
                        value={noteHeading}
                        onChange={(e) => setNoteHeading(e.target.value)}
                        placeholder="Enter the heading for your note"
                        className="w-full p-3 mt-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>

                {/* Rich Text Editor for Note Content */}
                <div className="mb-6">
                    <label htmlFor="content" className="block text-lg font-medium text-gray-700">
                        Note Content
                    </label>
                    <ReactQuill
                        value={content}
                        onChange={setContent}
                        modules={{
                            toolbar: [
                                [{ header: '1' }, { header: '2' }, { font: [] }],
                                [{ list: 'ordered' }, { list: 'bullet' }],
                                ['bold', 'italic', 'underline'],
                                [{ align: [] }],
                                ['link', 'image'],
                                ['blockquote', 'code-block'],
                                [{ color: [] }, { background: [] }],
                                ['clean'],
                            ],
                        }}
                        placeholder="Write your note content here"
                        className="w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        style={{ height: '200px' }}
                    />
                </div>

                {/* Submit Button */}
                <div className="text-center">
                    <button
                        type="submit"
                        className="px-6 py-2 mt-12 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        Save Changes
                    </button>
                </div>
            </form>
        </div>
    );
};

export default EditNote;
