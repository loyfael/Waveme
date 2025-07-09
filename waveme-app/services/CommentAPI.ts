import axios from "axios"
import { refreshAuthIfNeeded } from "./AuthToken"
import { COMMENT_URL, REPLIES_URL, THREAD_URL } from "@/constants/API"
import { NewMessage } from "@/types"
import { payloadToFormData } from "@/utils/formData"

export async function addComment(payload: NewMessage, postId: string | number) {
  return refreshAuthIfNeeded(() => {
    console.log('Using axios params for comment');
    return axios.post(`${COMMENT_URL}/${postId}`, null, {
      params: {
        content: payload.content
      }
    })
  })
}

export async function addReply(payload: NewMessage, commentId: string | number) {
  return refreshAuthIfNeeded(() => {
    console.log('Using axios params for reply');
    return axios.post(`${REPLIES_URL}/${commentId}`, null, {
      params: {
        content: payload.content
      }
    })
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
    return axios.post(
      `${COMMENT_URL}/${commentId}/vote`,
      { upvote },
      { headers: { "Content-Type": "multipart/form-data" }},
    )
  })
}

export async function voteReply(replyId: number, upvote: boolean) {
  return refreshAuthIfNeeded(() => {
    return axios.post(
      `${REPLIES_URL}/${replyId}/vote`,
      { upvote },
      { headers: { "Content-Type": "multipart/form-data" }},
    )
  })
}
