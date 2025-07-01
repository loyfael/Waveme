// Yes, this is the real server IP. But i'm not sysadmin, i'm developer, and i'm too lazy to update this just for one day
export const BASE_SERVER_URL = 'https://45.140.164.224'

export const API_URL = `${BASE_SERVER_URL}/api`

export const AUTH_URL = `${API_URL}/auth`
export const USER_URL = `${API_URL}/user`
export const POST_URL = `${API_URL}/posts`
export const COMMENT_URL = `${API_URL}/comments`
export const REPLIES_URL = `${API_URL}/replies`
export const REPORT_URL = `${API_URL}/report`

export const THREAD_URL = `${POST_URL}/thread`
