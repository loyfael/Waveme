import { getPostImage } from "@/services/PostAPI";
import { getProfileImage } from "@/services/UserAPI";
import * as ImagePicker from 'expo-image-picker';

type FormDataPayload = {
  [key: string]: string | Blob
}

export const detectImageType = (data: any): string => {
  let bytes: Uint8Array = new Uint8Array(data);
  
  // Check magic bytes
  if (bytes[0] === 0xFF && bytes[1] === 0xD8 && bytes[2] === 0xFF) {
    return 'image/jpeg';
  }
  if (bytes[0] === 0x89 && bytes[1] === 0x50 && bytes[2] === 0x4E && bytes[3] === 0x47) {
    return 'image/png';
  }
  if (bytes[0] === 0x47 && bytes[1] === 0x49 && bytes[2] === 0x46) {
    return 'image/gif';
  }
  if (bytes[0] === 0x52 && bytes[1] === 0x49 && bytes[2] === 0x46 && bytes[3] === 0x46) {
    // Check for WEBP
    if (bytes[8] === 0x57 && bytes[9] === 0x45 && bytes[10] === 0x42 && bytes[11] === 0x50) {
      return 'image/webp';
    }
  }
  
  // Default fallback
  return 'image/jpeg';
}

export const dataURLToBlob = (dataURL: string): Blob => {
  const arr = dataURL.split(',')
  const mime = arr[0].match(/:(.*?);/)?.[1] || 'image/png'
  const bstr = atob(arr[1])
  let n = bstr.length
  const u8arr = new Uint8Array(n)
  
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n)
  }
  
  return new Blob([u8arr], { type: mime })
}

export const blobToDataUri = (blob: Blob): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = reject;
    reader.readAsDataURL(blob);
  });
}

export const payloadToFormData = (payload: FormDataPayload) => {
  const formData = new FormData()
  Object.keys(payload).forEach((key) => {
    const value = payload[key]
    if (typeof value === 'string' && value.startsWith('data:')) {
      // Convert base64 data URL to Blob
      const blob = dataURLToBlob(value)
      formData.append(key, blob, 'image.png') // Add filename
    } else {
      formData.append(key, value)
    }
  })
  return formData
}

// Make an API call to get the image, convert to blob and create an URI on front
export const createLocalUriFromBackUri = async (imageUrl: string, imageSource: "post" | "profile") => {
  let response: any
  if (imageSource === "post") {
    response = await getPostImage(imageUrl)
  } else {
    response = await getProfileImage(imageUrl)
  }
  const imageType = detectImageType(response.data)
  const blob = new Blob([response.data], { type: imageType })
  return await blobToDataUri(blob)
}

export const pickImage = async (state: any, setState: Function) => {
  // No permissions request is necessary for launching the image library
  let result = await ImagePicker.launchImageLibraryAsync({
    mediaTypes: ['images'], // Possible to add videos in the future
    allowsEditing: true,
    aspect: [4, 3],
    quality: 1,
  });

  if (!result.canceled) {
    setState({ ...state, file: result.assets[0].uri });
  }
};
