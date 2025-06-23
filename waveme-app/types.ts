import { JwtPayload } from "jwt-decode"

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

export type Post = {
  postUniqueId: number,
  description: string | null,
  imageUrl: string,
  createdAt: string,
  upVote: number,
  downVote: number,
  voteSum: number,
  user: UserInfoLesser,
}

// Lighter version of UserInfo, typically used in other object types (Post, Comment, etc.)
export type UserInfoLesser = {
  id: string,
  pseudo: string,
  profileImg: string | null,
}

export type UserInfo = {
  authId: number,
  id: number,
  pseudo: string,
  email: string,
  profileImg: string | null,
  totalUpVote: number,
  totalPosts: number,
  totalComments: number,
  createdAt: string,
  updatedAt: string,
}

export type Message = {
  // An instance of this object can have either commentUniqueId OR replyUniqueId as a key
  [K in "commentUniqueId" | "replyUniqueId"]: number;
} & {
  id: string,
  content: string,
  userInfo: UserInfoLesser,
  upVote: number,
  downVote: number,
  author: string,
  createdAt: string,
}

export type Comment = Message & {
  // hasReplies: boolean,
  replies: Message[],
}

export type JwtType = JwtPayload & {
  id: string,
}

export type NewPost = {
  description: string,
  file: any,
  bucket: string,
}

export type NewMessage = {
  content: string,
}

// We separate each posted content type to avoid overlapping ids
export type UserProfilePictures = {
  post: string,
  comments: {
    [key: string]: string,
  },
}

export type ReportedContent = "post" | "comment" | "reply" | "profile"

export enum ReasonValues {
  SPAM = "SPAM",
  HARASSMENT = "HARASSMENT",
  INAPPROPRIATE_CONTENT = "INAPPROPRIATE_CONTENT",
  IMPERSONATION = "IMPERSONATION",
  OTHER = "OTHER",
}

export type SimplePost = {
  id: string,
  postUniqueId: number,
  imageUrl: string,
  description: string | null,
  upVote: number,
  downVote: number,
  voteSum: number,
  createdAt: string,
}

export type SimpleComment = {
  id: string,
  commentUniqueId: number,
  postId: number,
  content: string,
  createdAt: string,
  upVote: number,
  downVote: number,
  voteSum: number,
}
