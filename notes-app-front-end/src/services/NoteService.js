import axios from "axios";
import AuthService from "./AuthService";
import VARIABLE from "./VARIABLES";

class NoteService {
  static BASE_URL;

  static isAdmin() {
    // console.log(localStorage.getItem("role") == "ROLE_SU");

    if (
      localStorage.getItem("role") == "ROLE_SU" ||
      localStorage.getItem("role") == "ROLE_ADMIN"
    ) {
      this.BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/admin/note`;
    } else if (localStorage.getItem("role") == "ROLE_GUEST") {
      this.BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/guest/note`;
    } else {
      this.BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/user/note`;
    }
  }

  // static BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/note`;
  static async addNote(noteHeading, content) {
    this.isAdmin();
    AuthService.refreshTokenIfNeeded();
    try {
      const response = await axios.post(
        `${NoteService.BASE_URL}`,
        {
          noteHeading,
          content,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );

      if (response.data.noteId != null) {
        return response;
      }
      return null;
    } catch (err) {
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }
  static async updateNote(noteId, noteHeading, content) {
    this.isAdmin();
    AuthService.refreshTokenIfNeeded();
    try {
      const response = await axios.put(
        `${NoteService.BASE_URL}/${noteId}`,
        {
          noteHeading,
          content,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );

      return response;
    } catch (err) {
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }
  static async getAllNotes() {
    this.isAdmin();
    // console.log(this.BASE_URL);
    AuthService.refreshTokenIfNeeded();
    try {
      const response = await axios.get(`${NoteService.BASE_URL}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      if (response.data.includes("!DOCTYPE")) {
        return null;
      } else {
        return response;
      }
    } catch (err) { 
      if(err.response)
        throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later")
      }
    }
  }
  static async getNoteById(noteId) {
    this.isAdmin();

    // AuthService.refreshTokenIfNeeded()
    // console.log(this.BASE_URL);
    try {
      const response = await axios.get(`${NoteService.BASE_URL}/${noteId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      // console.log(response.data);

      return response;
    } catch (err) {
      // console.log(err);
      
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }
  static async deleteNoteByNoteId(noteId) {
    this.isAdmin();
    AuthService.refreshTokenIfNeeded();
    try {
      const response = await axios.delete(`${NoteService.BASE_URL}/${noteId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      return response;
    } catch (err) {
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }
}

export default NoteService;
