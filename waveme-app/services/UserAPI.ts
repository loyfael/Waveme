import axios from "axios";
import { refreshAuthIfNeeded } from "./AuthToken";
import { API_URL, USER_URL } from "@/constants/API";

export async function getCurrentUser() {
  return refreshAuthIfNeeded(() => {    
    return axios.get(`${USER_URL}/me`)
  })
}

export async function getUser(userId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${USER_URL}/${userId}`)
  })
}

export async function getUserPosts(userId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${USER_URL}/${userId}/posts`)
  })
}

export async function getUserComments(userId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${USER_URL}/${userId}/comments`)
  })
}

export async function getProfileImage(imageId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${API_URL}${imageId}`, {
    responseType: 'arraybuffer',
  })
  })
}
