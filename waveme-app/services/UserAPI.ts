import axios from "axios";
import { refreshAuthIfNeeded } from "./AuthToken";
import { JwtPayload, jwtDecode } from "jwt-decode";
import { USER_URL } from "@/constants/API";

type JwtType = JwtPayload & {
  id: string,
}

export async function getCurrentUser() {
  return refreshAuthIfNeeded(() => {    
    const { id: id } = jwtDecode<JwtType>(localStorage.getItem("authToken") ?? "")    
    return axios.get(`${USER_URL}/${id}`)
  })
}
