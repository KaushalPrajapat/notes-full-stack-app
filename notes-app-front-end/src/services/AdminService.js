import axios from "axios";
import AuthService from "./AuthService";
import VARIABLE from "./VARIABLES";

class AdminService {
  static BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/admin`;
  static async addAUser(username, email, password, role) {
    AuthService.refreshTokenIfNeeded();
    try {
      const response = await axios.post(
        `${AdminService.BASE_URL}/add-user`,
        {
          username,
          email,
          password,
          role,
        },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      return response;
    } catch (err) {
      console.log(err);
      
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }
  static async getAllUsers() {
    await AuthService.refreshTokenIfNeeded();
    try {
      const response = await axios.get(`${AdminService.BASE_URL}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      return response;
    } catch (err) {
      console.log(err);
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }
  static async updatePassword(userId, newPassword) {
    console.log(userId, newPassword);
    try {
      const response = await axios.put(
        `${AdminService.BASE_URL}/update-password?userId=${userId}&newPassword=${newPassword}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      return response;
    } catch (err) {
      console.log(err);
      return "error cought";
    }
  }
  static async updateEnabledStatus(userId, field) {
    // AuthService.refreshTokenIfNeeded()

    try {
      const response = await axios.put(
        `${AdminService.BASE_URL}/update-enabled-status?userId=${userId}&enabled=${field}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      console.log(response);

      return response;
    } catch (err) {
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }

  //   It's only possible when supper user logged in

  static async getAllUserLogs() {
    // AuthService.refreshTokenIfNeeded()

    try {
      const response = await axios.get(
        `http://${VARIABLE.IP_ADDRESS}:8080/api/su/all-logs`,
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
  //   ADMIN & SU for both
  static async getNotesAllLogs() {
    // AuthService.refreshTokenIfNeeded()

    try {
      const response = await axios.get(
        `http://${VARIABLE.IP_ADDRESS}:8080/api/notes/log/admin`,
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

export default AdminService;
