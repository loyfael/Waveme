import { JwtPayload } from "jwt-decode"
import { ImageSourcePropType } from "react-native"

// Add types used throughout multiple files here

export type ChildrenProps = {
  children: React.ReactNode
}

export type SignupCredentials = {
  username: string,
  email: string,
  password: string,
  confirmPassword: string,
}

export type LoginCredentials = {
  pseudo: string,
  password: string,
}

export type InvalidTooltip = {
  display: boolean,
  field: string,
  message: string,
}

// TODO: When connection with backend is established, change all ImageSourcePropType to string (URIs)
export type Post = {
  id: number,
  title: string | null,
  meme: ImageSourcePropType,
  user: UserInfo,
}

export type UserInfo = {
  id: number,
  pseudo: string,
  profileImg: string | null,
  totalUpvotes: number,
  totalPosts: number,
  updatedAt: string,
}

export type Message = {
  id: number,
  message: string,
  user: UserInfo,
}

export type Comment = Message & {
  hasReplies: boolean,
  replies: Message[],
  upvotes: number,
}

export type JwtType = JwtPayload & {
  id: string,
}

export type NewPost = {
  description: string,
  file: any,
  bucketName: string,
}
