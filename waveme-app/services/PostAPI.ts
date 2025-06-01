import { NewPost } from "@/types";
import { refreshAuthIfNeeded } from "./AuthToken";
import axios from "axios";
import { API_URL, POST_URL } from "@/constants/API";

export async function createPost(payload: NewPost) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/upload-image`, payload)
  })
}

export async function getFeed() {
  refreshAuthIfNeeded(() => {
    return axios.get(`${API_URL}/feed`)
  })
}

export async function getUserPosts(userId: number | string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/all/${userId}`)
  })
}

export async function getPost(postId: number | string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/${postId}`)
  })
}

// TODO: Check to see how to change the endpoint and API call
export async function downloadImage() {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/download-image`)
  })
}

export async function upvotePost(postId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/${postId}/vote`, { upvote: true })
  })
}

export async function downvotePost(postId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/${postId}/vote`, { upvote: false })
  })
}
