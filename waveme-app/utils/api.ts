import { getPostImage } from "@/services/PostAPI";
import { getProfileImage } from "@/services/UserAPI";
import * as ImagePicker from 'expo-image-picker';
import * as FileSystem from "expo-file-system"
import { Platform } from "react-native";

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

export const blobToDataUri = (blob: Blob): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = reject;
    reader.readAsDataURL(blob);
  });
}

const uint8ArrayToBase64 = (uint8Array: Uint8Array): string => {
  const chunkSize = 8192; // Process in chunks of 8KB
  let result = '';

  for (let i = 0; i < uint8Array.length; i += chunkSize) {
    const chunk = uint8Array.slice(i, i + chunkSize);
    result += String.fromCharCode.apply(null, Array.from(chunk));
  }

  return btoa(result);
};

// Make an API call to get the image, convert to blob and create an URI on front
export const createLocalUriFromBackUri = async (imageUrl: string, imageSource: "post" | "profile") => {
  if (Platform.OS === "web") {
    let response: any

    if (imageSource === "post") {
      response = await getPostImage(imageUrl)
    } else {
      response = await getProfileImage(imageUrl)
    }

    const imageType = detectImageType(response.data)
    const blob = new Blob([response.data], { type: imageType })
    return await blobToDataUri(blob)
  } else if (Platform.OS === "android") {
    try {
      let response: any
      if (imageSource === "post") {
        response = await getPostImage(imageUrl)
      } else {
        response = await getProfileImage(imageUrl)
      }

      let uint8Array: Uint8Array;
      if (response.data instanceof Uint8Array) {
        uint8Array = response.data;
      } else if (response.data instanceof ArrayBuffer) {
        uint8Array = new Uint8Array(response.data);
      } else {
        throw new Error(`Unexpected data type: ${typeof response.data}`);
      }

      // Convert to base64 using chunked approach
      const base64String = uint8ArrayToBase64(uint8Array);

      // Create a local file path with correct extension
      const imageType = detectImageType(response.data)
      const extension = imageType.split('/')[1]; // extracts 'jpeg', 'png', 'gif', or 'webp'
      const filename = `${imageSource}_${Date.now()}.${extension}`;
      const localUri = `${FileSystem.documentDirectory}${filename}`;

      await FileSystem.writeAsStringAsync(localUri, base64String, {
        encoding: FileSystem.EncodingType.Base64,
      });

      return localUri;
    } catch (error) {
      console.error('Error in createLocalUriFromBackUri:', error);
      throw error;
    }
  } else {
    console.error("Image loading: device unsupported")
  }
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
