import axios from "axios";
import { refreshAuthIfNeeded } from "./AuthToken";
import { USER_URL } from "@/constants/API";

export async function getCurrentUser() {
  return refreshAuthIfNeeded(() => {    
    return axios.get(`${USER_URL}/me`)
  })
}

// TODO: potentially replace with correct route when the endpoint is in place and remove one of the two parameter types
export async function getUser(userId: number | string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${USER_URL}/${userId}`)
  })
}
