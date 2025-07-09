import axios from "axios";
import { refreshAuthIfNeeded } from "./AuthToken";
import { API_URL, USER_URL } from "@/constants/API";
import { payloadToFormData } from "@/utils/formData";

export async function getCurrentUser() {
  return refreshAuthIfNeeded(() => {    
    return axios.get(`${USER_URL}/me`)
  })
}

export async function setProfileImage(userId: string, file: any) {
  console.log('=== SET PROFILE IMAGE DEBUG ===');
  console.log('User ID:', userId);
  console.log('File URI:', file);
  
  const formData = payloadToFormData({
    userId,
    file,
    bucket: "waveme"
  })
  
  console.log('FormData created for profile image, attempting upload...');
  
  return refreshAuthIfNeeded(() => {
    return axios.post(`${USER_URL}/profile-image`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      timeout: 30000, // 30 second timeout
    })
  }).then((response) => {
    console.log('Profile image upload successful:', response.status);
    return response;
  }).catch((error) => {
    console.error('Profile image upload failed:', {
      message: error.message,
      code: error.code,
      status: error.response?.status,
      statusText: error.response?.statusText,
      data: error.response?.data
    });
    throw error;
  })
}

export async function getUser(userId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${USER_URL}/${userId}`)
  })
}

export async function getUserPosts(userId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${USER_URL}/${userId}/posts`)
  })
}

export async function getUserComments(userId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${USER_URL}/${userId}/comments`)
  })
}

export async function getProfileImage(imageId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${API_URL}${imageId}`, {
    responseType: 'arraybuffer',
  })
  })
}

// Get basic user info for feed posts
export async function getUserBasicInfo(userId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${USER_URL}/${userId}`)
  })
}

// Get multiple users basic info in batch (if backend supports it)
export async function getUsersBasicInfo(userIds: string[]) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${USER_URL}/batch`, { userIds })
  })
}
