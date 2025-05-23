import axios from "axios";
import { refreshAuthIfNeeded } from "./AuthToken";
import { jwtDecode } from "jwt-decode";
import { USER_URL } from "@/constants/API";
import { JwtType } from "@/types";

export async function getCurrentUser() {
  return refreshAuthIfNeeded(() => {    
    const { id: id } = jwtDecode<JwtType>(localStorage.getItem("authToken") ?? "")    
    return axios.get(`${USER_URL}/me`)
  })
}
