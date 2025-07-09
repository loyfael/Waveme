import { Platform } from 'react-native';

type FormDataPayload = {
  [key: string]: string | Blob | null
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

export const payloadToFormData = (payload: FormDataPayload) => {
  const formData = new FormData()
  
  console.log('Creating FormData from payload:', payload);
  
  Object.keys(payload).forEach((key) => {
    const value = payload[key]
    
    console.log(`Processing key: ${key}, value:`, value, 'type:', typeof value);
    
    if (value === null || value === undefined) {
      console.log(`Skipping ${key} - null/undefined`);
      return; // Skip null/undefined values
    }
    
    if (key === 'file' && typeof value === 'string') {
      if (Platform.OS === 'web' && value.startsWith('data:')) {
        // Web: Convert base64 data URL to Blob
        console.log('Converting data URL to Blob for web');
        const blob = dataURLToBlob(value)
        formData.append(key, blob, 'image.png')
      } else if (Platform.OS !== 'web' && value.startsWith('file://')) {
        // React Native: Handle local file URI
        console.log('Handling file URI for React Native');
        const fileExtension = value.split('.').pop() || 'jpg';
        const mimeType = `image/${fileExtension === 'jpg' ? 'jpeg' : fileExtension}`;
        
        formData.append(key, {
          uri: value,
          type: mimeType,
          name: `image.${fileExtension}`,
        } as any);
      } else {
        // Fallback for other cases
        console.log('Using fallback for file');
        formData.append(key, value)
      }
    } else {
      console.log(`Appending text value: ${key} = ${value}`);
      formData.append(key, value as string)
    }
  })
  
  console.log('FormData created for platform:', Platform.OS);
  
  // Note: FormData.entries() n'est pas disponible sur React Native
  // mais les données sont bien ajoutées au FormData
  
  return formData
}
