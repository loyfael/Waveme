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

export async function refreshAuthIfNeeded(call: Function) {
  return call().catch(async (error: any) => {
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
