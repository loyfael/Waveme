import { getLocalStorage, removeLocalStorage } from '@/utils/localStorage'
import axios from 'axios'
import { router } from 'expo-router'
import { jwtDecode } from 'jwt-decode'

export async function isAuthenticatedByToken() {
  // Check if there is a token
  const token = await getLocalStorage('authToken')
  console.log('Checking auth token:', token ? 'Token exists' : 'No token found')
  
  if (token) {
    // Check if token has expired
    try {
      const decoded = jwtDecode(token)
      const { exp: expiration } = decoded
      const isValid = (expiration ?? 0) * 1000 > new Date().getTime()
      
      console.log('Token validation:', {
        expiration: new Date((expiration ?? 0) * 1000),
        currentTime: new Date(),
        isValid
      })
      
      if (isValid) {
        setAxiosToken(token)
        return true
      } else {
        console.log('Token expired, clearing...')
        await clearTokens()
      }
    } catch (error) {
      console.error('Error decoding token:', error)
      await clearTokens()
    }
  }

  return false
}

// TODO: remove this function from API calls that don't require authentication
export async function refreshAuthIfNeeded(call: Function) {
  return call().catch(async (error: any) => {
    console.error('API call failed:', {
      status: error?.response?.status,
      code: error.code,
      message: error.message,
      url: error?.config?.url
    })
    
    // If the token sent is invalid or absent, check if one is valid in localStorage
    // If the localStorage token is valid, set it again and retry, else clear the token and redirect to login
    if (error?.response?.status === 401 || error.code === 401) {
      console.log('401 error detected, checking auth...')
      
      if (await isAuthenticatedByToken()) {
        console.log('Retrying API call with refreshed token...')
        return call()
      }
      
      console.log('Authentication failed, redirecting to login...')
      clearTokens()
      router.push('/login')
      return
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
