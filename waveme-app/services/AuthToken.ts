import axios from 'axios'
import {jwtDecode} from 'jwt-decode'

export async function isAuthenticatedByToken() {
    // Check if there is a token
    const token = window.localStorage.getItem('authToken')
    // Check if token has expired
    if (token) {
        const { exp: expiration } = jwtDecode(token)
        if ((expiration ?? 0) * 1000 > new Date().getTime()) {
            return true
        }
    }

    return false
}

export function getExpirationTime() {
    const token = window.localStorage.getItem('authToken')
    // Check if token has expired
    if (token) {
        const { exp: expiration } = jwtDecode(token)
        const diffTime: number = new Date((expiration ?? 0) * 1000).getTime() - new Date().getTime();
        if (diffTime <= 0) return 0
        return diffTime
    }
    return 0
}

export async function refreshAuthIfNeeded(call: Function) {
    // TODO: Check token validity
    return call().catch(async (error: any) => {
        if (error?.response?.status === 401 || error.code === 401) {
            if (await isAuthenticatedByToken()) {
                return call()
            }
            clearTokens()
            window.location.href = `/${window.location.href.split('/')[3]}/login`
            return error
        }

        throw error
    })
}

export function setAxiosToken(token: string) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

export function setupAxiosToken() {
    setAxiosToken(window.localStorage.getItem('authToken') ?? '')
}

export function clearTokens() {
    window.localStorage.removeItem('authToken')
    // delete axios.defaults.headers.Authorization
}
