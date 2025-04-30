import PostList from "@/components/PostList"
import UserActivityHeader from "@/components/UserActivityHeader"
import { useWebTitle } from "@/hooks/useWebTitle"
import { Post } from "@/types"
import { useLocalSearchParams, useRouter } from "expo-router"
import React, { useEffect, useState } from "react"

export default function Posts() {
  useWebTitle("Tous les posts de user")

  const { profileId } = useLocalSearchParams<{ profileId: string }>()
  const [posts, setPosts] = useState<Post[]>([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    setIsLoading(true)
    // TODO: API call
    setPosts([{
      id: 32,
      title: 'Hilarant.',
      meme: require('@/assets/images/meme.png'),
      user: {
        id: 1,
        userName: 'Beiten34',
        userPfp: require('@/assets/images/pfp.png'),
      }
    },
    {
      id: 33,
      title: 'Repost si tu trouves ça hilarant hahaha trodrol quoi terrible quest-ce que je fais de ma vie jecris littéralement un gros texte pour tester ce que ça donne avec un gros texte et jai la flemme de mettre des backslash du coup je mets juste pas dapostrophe et jai limpression de pas savoir écrire lolool',
      meme: require('@/assets/images/meme.png'),
      user: {
        id: 2,
        userName: 'Beiten34',
        userPfp: require('@/assets/images/pfp.png'),
      }
    },
    {
      id: 34,
      title: 'Hilarant.',
      meme: require('@/assets/images/meme.png'),
      user: {
        id: 3,
        userName: 'Beiten34',
        userPfp: require('@/assets/images/pfp.png'),
      }
    }])
    setIsLoading(false)
  }, [profileId])

  return (
    <>
      <UserActivityHeader profileId={profileId} />
      <PostList isLoading={isLoading} posts={posts} />
    </>
  )
}
