import { NewPost } from "@/types";
import { refreshAuthIfNeeded } from "./AuthToken";
import axios from "axios";
import { API_URL, BASE_SERVER_URL, POST_URL } from "@/constants/API";
import { payloadToFormData } from "@/utils/formData";
import { PostPageDto } from "../types/index";

export async function createPost(payload: NewPost) {
  console.log('=== CREATE POST DEBUG ===');
  console.log('Payload:', payload);
  console.log('File URI:', payload.file);
  
  const formData = payloadToFormData(payload)
  
  console.log('FormData created, attempting upload...');
  
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/upload-image`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      timeout: 30000, // 30 second timeout
    })
  }).then((response) => {
    console.log('Upload successful:', response.status);
    return response;
  }).catch((error) => {
    console.error('Upload failed:', {
      message: error.message,
      code: error.code,
      status: error.response?.status,
      statusText: error.response?.statusText,
      data: error.response?.data
    });
    throw error;
  })
}

export async function getFeed() {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${API_URL}/feed`)
  })
}

export async function getPost(postId: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/get/${postId}`)
  })
}

export async function getPostVotes(postId: number) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/${postId}/get-user-votes`)
  })
}

export async function getPostImage(postUri: string) {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${BASE_SERVER_URL}${postUri}`, {
    responseType: 'arraybuffer',
  })
  })
}

// TODO: Check to see how to change the endpoint and API call
export async function downloadImage() {
  return refreshAuthIfNeeded(() => {
    return axios.get(`${POST_URL}/download-image`)
  })
}

export async function votePost(postId: number | string, upvote: boolean) {
  return refreshAuthIfNeeded(() => {
    return axios.post(`${POST_URL}/${postId}/vote?upvote=${upvote}`)
  })
}

export const getFeedPage = async (page: number): Promise<PostPageDto> => {
  const response = await refreshAuthIfNeeded(() => {
    return axios.get(`${API_URL}/feed?page=${page}`);
  });
  
  if (!response) {
    // Handle the case where refreshAuthIfNeeded returns undefined (401 redirect)
    throw new Error('Authentication required');
  }
  
  return response.data;
};

// Enhanced feed function that fetches user data for each post
export const getFeedPageWithUsers = async (page: number): Promise<PostPageDto & { usersData: { [userId: string]: any } }> => {
  const feedData = await getFeedPage(page);
  
  // Extract unique user IDs from posts (if available in the backend response)
  // Since PostMetadataDto might not include authorId, we'll need to check the actual API response
  // For now, we'll return the feed data as-is and handle user fetching in the component
  
  return {
    ...feedData,
    usersData: {}
  };
};
