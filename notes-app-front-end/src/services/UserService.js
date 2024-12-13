import axios from "axios";
import AuthService from "./AuthService";
import VARIABLE from "./VARIABLES";

class UserService {
  static BASE_URL = `http://${VARIABLE.IP_ADDRESS}:8080/api/user`;
  static async updatePassword(newPassword) {
    // console.log(
    //   newPassword,
    //   `${UserService.BASE_URL}/update-password?newPassword=${newPassword}`
    // );

    AuthService.refreshTokenIfNeeded();
    try {
      const response = await axios.put(
        `${UserService.BASE_URL}/update-password?newPassword=${newPassword}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      // console.log(response.status + " status");
      return response;
    } catch (err) {
      if (err.response) throw new Error(err.response.data.message);
      if (err.message) {
        throw new Error(err.message + " Check Internet or Try Later");
      }
    }
  }
}

export default UserService;
