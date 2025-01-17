// Remove when no longer broken
const isBroken = true
export const BASE_SERVER_URL = isBroken ? 'http://127.0.0.1:8080' : 'http://127.0.0.1:9080'

export const API_URL = `${BASE_SERVER_URL}/api`
export const AUTH_URL = `${API_URL}/auth`
