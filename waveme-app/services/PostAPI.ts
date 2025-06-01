import { NewPost } from "@/types";
import { refreshAuthIfNeeded } from "./AuthToken";
import axios from "axios";
import { API_URL, BASE_SERVER_URL, POST_URL } from "@/constants/API";
import { payloadToFormData } from "@/utils/api";

export async function createPost(payload: NewPost) {
  const formData = payloadToFormData(payload)
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/upload-image`, formData)
  })
}

export async function getFeed() {
  refreshAuthIfNeeded(() => {
    return axios.get(`${API_URL}/feed`)
  })
}

export async function getPost(postId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/get/${postId}`)
  })
}

export async function getPostImage(postUri: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${BASE_SERVER_URL}${postUri}`, {
    responseType: 'arraybuffer',
  })
  })
}

// TODO: Check to see how to change the endpoint and API call
export async function downloadImage() {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/download-image`)
  })
}

export async function upvotePost(postId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/${postId}/vote`, { upvote: true })
  })
}

export async function downvotePost(postId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/${postId}/vote`, { upvote: false })
  })
}
