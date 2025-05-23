import { AUTH_URL } from "@/constants/API";
import { LoginCredentials, SignupCredentials } from "@/types";
import axios from "axios";
import { clearTokens, setAxiosToken } from "./AuthToken";
import { setLocalStorage } from "@/utils/localStorage";

export async function signup(data: SignupCredentials) {
  return axios.post(`${AUTH_URL}/register`, {
    pseudo: data.username,
    email: data.email,
    password: data.password,
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
      // We stock the token on local storage
      setLocalStorage('authToken', response.data.jwtCookie)
      // We add token to axios header
      setAxiosToken(response.data.jwtCookie)
      return response.data.token
    })
}

export async function logout() {
  return axios
    .post(`${AUTH_URL}/signout`)
    .then(async (response) => {
      if (response.status !== 200) {
        throw response
      }
      await clearTokens()      
    })
}
