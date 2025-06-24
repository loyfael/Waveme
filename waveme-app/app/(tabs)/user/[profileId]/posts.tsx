import PostList from "@/components/PostList"
import UserActivityHeader from "@/components/UserActivityHeader"
import { useWebTitle } from "@/hooks/useWebTitle"
import { getUser, getUserPosts } from "@/services/UserAPI"
import { Post, UserInfo } from "@/types"
import { useLocalSearchParams } from "expo-router"
import React, { useEffect, useState } from "react"

export default function Posts() {
  const [user, setUser] = useState<UserInfo | null>()
  const [posts, setPosts] = useState<Post[]>([])
  const [isLoading, setIsLoading] = useState(true)

  useWebTitle(`Tous les posts de ${user?.pseudo ?? "l'utilisateur"}`)
  const { profileId } = useLocalSearchParams<{ profileId: string }>()

  useEffect(() => {
    const handleFetchUserAndUserPosts = async () => {
      setIsLoading(true)
      await getUserPosts(profileId)
        .catch((err) => {
          console.error(err)
        })
        .then((response) => {
          setPosts(response.data)
        })
      await getUser(profileId)
        .catch((err) => {
          console.error(err)
        })
        .then((response) => {
          setUser(response.data)
        })
      setIsLoading(false)
    }
    handleFetchUserAndUserPosts()
  }, [profileId])

  return (
    <>
      <UserActivityHeader profileId={profileId} title="Posts" />
      <PostList isLoading={isLoading} posts={posts} setPosts={setPosts} />
    </>
  )
}
