import axios from "axios"
import { refreshAuthIfNeeded } from "./AuthToken"
import { COMMENT_URL, POST_URL, REPLIES_URL, THREAD_URL } from "@/constants/API"

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

// TODO: populate these two functions
export async function loadComments() {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${THREAD_URL}`)
  })
}

export async function loadReplies() {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${THREAD_URL}`)
  })
}

export async function upvoteComment(commentId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${COMMENT_URL}/${commentId}/vote`, { upvote: true })
  })
}

export async function downvoteComment(commentId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${COMMENT_URL}/${commentId}/vote`, { upvote: false })
  })
}

export async function upvoteReply(replyId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${REPLIES_URL}/${replyId}/vote`, { upvote: true })
  })
}

export async function downvoteReply(replyId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${REPLIES_URL}/${replyId}/vote`, { upvote: false })
  })
}
