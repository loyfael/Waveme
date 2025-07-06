import { Platform } from "react-native"

// Yes, this is the real server IP. But i'm not sysadmin, i'm developer, and i'm too lazy to update this just for one day
// NOTE: The second url is a temporary ngrok tunnel url, run `ngrok http 9998` and replace by the URL given on the 'Forwarding' line
export const BASE_SERVER_URL = Platform.OS === 'web' ? 'http://127.0.0.1:9998' : 'https://e692-2001-861-208-2e10-19a2-a9e3-a7c9-aa94.ngrok-free.app'

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
