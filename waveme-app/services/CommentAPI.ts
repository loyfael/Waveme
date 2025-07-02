import axios from "axios"
import { refreshAuthIfNeeded } from "./AuthToken"
import { COMMENT_URL, REPLIES_URL, THREAD_URL } from "@/constants/API"
import { NewMessage } from "@/types"
import { payloadToFormData } from "@/utils/formData"

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

export async function getCommentVotes(commentId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${COMMENT_URL}/${commentId}/get-user-votes`)
  })
}

export async function getReplyVotes(replyId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${REPLIES_URL}/${replyId}/get-user-votes`)
  })
}

export async function voteComment(commentId: number, upvote: boolean) {
  return refreshAuthIfNeeded(() => {
    // We use the form-data header instead of the FormData class instance because the latter doesn't support boolean values
    return axios.post(
      `${COMMENT_URL}/${commentId}/vote`,
      { upvote },
      { headers: { "Content-Type": "multipart/form-data" }},
    )
  })
}

export async function voteReply(replyId: number, upvote: boolean) {
  return refreshAuthIfNeeded(() => {
    // We use the form-data header instead of the FormData class instance because the latter doesn't support boolean values
    return axios.post(
      `${REPLIES_URL}/${replyId}/vote`,
      { upvote },
      { headers: { "Content-Type": "multipart/form-data" }},
    )
  })
}
