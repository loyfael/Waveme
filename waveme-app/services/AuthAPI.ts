import { AUTH_URL } from "@/constants/API";
import { LoginCredentials, SignupCredentials } from "@/types";
import axios from "axios";
import { setAxiosToken } from "./AuthToken";

export async function signup(data: SignupCredentials) {
  return axios.post(`${AUTH_URL}/register`, {
    pseudo: data.username,
    email: data.email,
    password: data.password,
    role: "ROLE_USER", // TODO: /!\ REMOVE AS SOON AS POSSIBLE /!\
  }, {
    withCredentials: true,
  })
}

export async function authenticate(credentials: LoginCredentials) {
  return axios
    .post(`${AUTH_URL}/login`, credentials, {
      validateStatus(status) {
        return status === 200 || status === 401
      },
      withCredentials: true,
    })
    .then((response) => {
      if (response.status !== 200) {
        throw response
      }
      console.log(response);
      // We stock the token on local storage
      window.localStorage.setItem('authToken', response.data.token)
      // We add token to axios header
      setAxiosToken(response.data.token)
      return response.data.token
    })
}
