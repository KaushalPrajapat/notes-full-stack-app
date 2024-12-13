import React, { useRef, useState } from 'react';
import NoteService from '../../services/NoteService';
import ReactQuill from 'react-quill';  // Import react-quill for the rich text editor
import 'react-quill/dist/quill.snow.css'; // Import styles for the editor
import { useNavigate } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';

const AddNote = () => {
  // State for note heading and content
  const [noteHeading, setNoteHeading] = useState('');
  const [content, setContent] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate()

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    // For now, we'll just log the data to the console
    // In a real app, you'd send this data to an API or save it locally
    console.log('Note Heading:', noteHeading);
    console.log('Note Content:', content);

    try {
      setLoading(true);
      const resp = await NoteService.addNote(noteHeading, content);
      console.log(resp);

      if (resp.data != null && resp.data.noteId != null) {
        console.log(resp);
        toast("Saved a new Note !! Add More")
        setNoteHeading('');
        setContent('');
      }

    } catch (error) {
      toast.error(error.message);
      setError(error.message)
    } finally {
      setLoading(false);
    }

  };

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white shadow-md rounded-lg">

      <h2 className="text-3xl font-semibold text-center mb-6">Add a New Note</h2>
      {/* Display success or error message */}
      {error && <h6 className="text-1xl bg-red-100 font-semibold text-center mb-6 text-red-700">{error}</h6>}

      <form onSubmit={handleSubmit}>
        {/* Note Heading */}
        <div className="mb-4">
          <label htmlFor="noteHeading" className="block text-lg font-medium text-gray-700">Note Heading</label>
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
          <label htmlFor="content" className="block text-lg font-medium text-gray-700">Note Content</label>
          <ReactQuill
            value={content}
            onChange={setContent}  // Quill editor updates content state
            modules={{
              toolbar: [
                [{ header: '1' }, { header: '2' }, { header: '3' }, { font: [] }],
                [{ list: 'ordered' }, { list: 'bullet' }],
                ['bold', 'italic', 'underline'],
                [{ align: [] }],
                ['link', 'image'],
                ['blockquote', 'code-block'],
                [{ color: [] }, { background: [] }],
                ['clean'], // Adds a "clean" button to remove formatting
              ],
            }}
            placeholder="Write your note content here"
            className="w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            style={{ height: '200px' }}  // Adjust height for the editor
          />
        </div>
        {/* Submit Button */}
        <div className="text-center">
          <button
            disabled={loading}
            type="submit"
            className="px-6 py-2 mt-12 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            {loading ? <>Saving</> : <>Save Note</>}
          </button>
        </div>
      </form>
    </div>
  );
};

export default AddNote;
