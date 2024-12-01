import axios from "axios";
import VARIABLE from "./VARIABLES";

class AuthService {
  static BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/auth/public`;
  static async signin(username, password) {
    try {
      const response = await axios.post(`${AuthService.BASE_URL}/signin`, {
        username,
        password,
      });
      console.log(response);

      // Setup in local storage if success return true else false
      if (response.data.httpStatus == 200) {
        await this.setToken(response.data);
        return response;
      } else {
        return response;
      }
    } catch (err) {
      console.log(err);

      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }
  static async signup(username, email, password, roles = "GUEST") {
    try {
      const response = await axios.post(`${AuthService.BASE_URL}/signup`, {
        username,
        email,
        password,
        role: roles,
      });
      console.log(response);
      console.log(response.status);

      return response;
    } catch (err) {
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }

  static async getMeRefreshToken() {
    try {
      console.log("Refreshing API");

      const refreshToken = localStorage.getItem("refreshToken");
      const response = await axios.post(
        `${AuthService.BASE_URL}/refresh-token`,
        {
          refreshToken,
        }
      );
      console.log(response.data.httpStatus === 200);
      // Setup in local storage if success return true else false
      if (response.data.httpStatus === 200) {
        await this.setToken(response.data);
        return true;
      } else {
        console.log(response.data.message);
        return "else";
      }
    } catch (err) {
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }

  static async refreshTokenIfNeeded() {
    const accessToken = localStorage.getItem("accessToken");
    const expirationTime = localStorage.getItem("signOutTime");
    if (
      new Date(localStorage.getItem("refreshTokenExpirationTime")) < new Date()
    ) {
      localStorage.removeItem("signedIn");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("username");
      localStorage.removeItem("role");
      localStorage.removeItem("signOutTime");
      localStorage.removeItem("refreshTokenExpirationTime");
      console.log("Signing out");
      setIsSigned(false);
    } else {
      if (accessToken && new Date(expirationTime) < new Date()) {
        return await this.getMeRefreshToken();
      } else {
        return;
      }
    }
  }

  static async setToken(userdata) {
    localStorage.setItem("accessToken", userdata.accessToken);
    localStorage.setItem("refreshToken", userdata.refreshToken);
    localStorage.setItem("username", userdata.username);
    localStorage.setItem("role", userdata.roles);
    localStorage.setItem("signedIn", true);
    const now = new Date(); // Current time
    localStorage.setItem(
      "signOutTime",
      new Date(now.getTime() + 30 * 60 * 1000)
    );
    if (!localStorage.getItem("refreshTokenExpirationTime"))
      localStorage.setItem(
        "refreshTokenExpirationTime",
        new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000)
      );
  }
  static async forgotPassword(email) {
    try {
      const response = await axios.post(
        `${AuthService.BASE_URL}/forgot-password?email=${email}`,
        {}
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
}

export default AuthService;
