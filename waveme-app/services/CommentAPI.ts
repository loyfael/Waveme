import axios from "axios"
import { refreshAuthIfNeeded } from "./AuthToken"
import { COMMENT_URL, POST_URL, REPLIES_URL, THREAD_URL } from "@/constants/API"
import { NewMessage } from "@/types"
import { payloadToFormData } from "@/utils/api"

export async function addComment(payload: NewMessage, postId: string | number) {
  const formData = payloadToFormData(payload)
  return refreshAuthIfNeeded(() => {
    return axios.post(`${COMMENT_URL}/${postId}`, formData)
  })
}

export async function addReply(payload: NewMessage, commentId: string | number) {
  const formData = payloadToFormData(payload)
  return refreshAuthIfNeeded(() => {
    return axios.post(`${REPLIES_URL}/${commentId}`, formData)
  })
}

export async function loadComments() {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${THREAD_URL}`)
  })
}

export async function loadReplies(commentId: number) {
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
