export const BASE_SERVER_URL = 'http://127.0.0.1:9998'

export const API_URL = `${BASE_SERVER_URL}/api`

export const AUTH_URL = `${API_URL}/auth`
export const USER_URL = `${API_URL}/user`
export const POST_URL = `${API_URL}/posts`
export const COMMENT_URL = `${API_URL}/comments`
export const REPLIES_URL = `${API_URL}/replies`
export const REPORT_URL = `${API_URL}/report`

export const THREAD_URL = `${POST_URL}/thread`

// Utility function to build complete image URLs
export const buildImageUrl = (relativeUrl: string): string => {
  if (relativeUrl.startsWith('http')) {
    return relativeUrl; // Already a complete URL
  }
  return `${BASE_SERVER_URL}${relativeUrl}`;
}
