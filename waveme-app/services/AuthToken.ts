import { getLocalStorage, removeLocalStorage, setLocalStorage } from '@/utils/localStorage'
import axios from 'axios'
import { jwtDecode } from 'jwt-decode'

export async function isAuthenticatedByToken() {
  // Check if there is a token
  const token = await getLocalStorage('authToken')
  if (token) {
    // Check if token has expired
    const { exp: expiration } = jwtDecode(token)
    if ((expiration ?? 0) * 1000 > new Date().getTime()) {
      setAxiosToken(token)
      return true
    }
  }

  return false
}

// TODO: remove this function from API calls that don't require authentication
export async function refreshAuthIfNeeded(call: Function) {
  return call().catch(async (error: any) => {
    // If the token sent is invalid or absent, check if one is valid in localStorage
    // If the localStorage token is valid, set it again and retry, else clear the token and redirect to login
    // TODO: Potentially change behavior on an invalid token
    if (error?.response?.status === 401 || error.code === 401) {
      if (await isAuthenticatedByToken()) {
        return call()
      }
      clearTokens()
      return error
    }

    throw error
  })
}

export function setAxiosToken(token: string) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

export async function clearTokens() {
  await removeLocalStorage('authToken')
  axios.defaults.headers.common['Authorization'] = undefined  
}
