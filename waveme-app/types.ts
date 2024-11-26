import { ImageSourcePropType } from "react-native"

export type SignupCredentials = {
  username: string,
  email: string,
  password: string,
  confirmPassword: string,
}

export type LoginCredentials = {
  username: string,
  password: string,
}

export type InvalidTooltip = {
  display: boolean,
  field: string,
  message: string,
}

// NOTE: When connection with backend is established, change all ImageSourcePropType to string (URIs)
export type Post = {
  title: string | null,
  meme: ImageSourcePropType,
  user: UserInfo,
}

export type UserInfo = {
  userName: string,
  userPfp: ImageSourcePropType | undefined,
}
