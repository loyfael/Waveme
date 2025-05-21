import { NewPost } from "@/types";
import { refreshAuthIfNeeded } from "./AuthToken";
import axios from "axios";
import { POST_URL } from "@/constants/API";

export async function createPost(payload: NewPost) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/upload-image`, payload)
  })
}

// TODO: Check to see how to change the endpoint and API call
export async function downloadImage() {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/download-image`)
  })
}

export async function addComment(content: string, postId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/${postId}/comments`, { content })
  })
}

export async function addReply(content: string, commentId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/comments/${commentId}/reply`, { content })
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

export async function upvoteComment(commentId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/comments/${commentId}/vote`, { upvote: true })
  })
}

export async function downvoteComment(commentId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/comments/${commentId}/vote`, { upvote: false })
  })
}

export async function upvoteReply(replyId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/replies/${replyId}/vote`, { upvote: true })
  })
}

export async function downvoteReply(replyId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/replies/${replyId}/vote`, { upvote: false })
  })
}
