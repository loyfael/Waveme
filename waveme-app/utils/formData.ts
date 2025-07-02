type FormDataPayload = {
  [key: string]: string | Blob
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
