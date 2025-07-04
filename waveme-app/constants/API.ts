import { Platform } from "react-native"

// Yes, this is the real server IP. But i'm not sysadmin, i'm developer, and i'm too lazy to update this just for one day
// TODO: Fix api calls to backend + (dynamic ip for mobile?)
export const BASE_SERVER_URL = Platform.OS === 'web' ? 'http://127.0.0.1:9998' : 'http://192.168.1.254:9998'

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
