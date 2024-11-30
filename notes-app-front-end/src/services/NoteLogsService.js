import axios from "axios";
import AuthService from "./AuthService";
import VARIABLE from "./VARIABLES";

class NoteLogsService {
  static BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/notes/log`;
  static isAdmin() {
    if (
      localStorage.getItem("role") == "ROLE_SU" ||
      localStorage.getItem("role") == "ROLE_ADMIN"
    ) {
      this.BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/notes/log/admin`;
    }
  }
  static async logsOfANote(noteId) {
    AuthService.refreshTokenIfNeeded();
    this.isAdmin();
    try {
      const response = await axios.get(
        `${NoteLogsService.BASE_URL}/${noteId}`,
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
}

export default NoteLogsService;
