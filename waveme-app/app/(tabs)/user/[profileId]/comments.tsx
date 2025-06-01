import { Loading } from "@/components/Loading";
import UserActivityHeader from "@/components/UserActivityHeader";
import { ThemedText } from "@/components/theme/ThemedText";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import { getUser, getUserComments } from "@/services/UserAPI";
import { SimpleComment, UserInfo } from "@/types";
import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import { StyleSheet } from "react-native";
import { TouchableOpacity } from "react-native-gesture-handler";

export default function Comments() {
  const [user, setUser] = useState<UserInfo | null>()
  const [comments, setComments] = useState<SimpleComment[]>([])
  const [isLoading, setIsLoading] = useState(true)

  useWebTitle(`Tous les commentaires de ${user?.pseudo}`)
  const router = useRouter()
  const { profileId } = useLocalSearchParams<{ profileId: string }>()

  useEffect(() => {
    setIsLoading(true)
    getUserComments(profileId)
      .catch((err) => {
        console.error(err)
      })
      .then((response) => {
        setComments(response.data)
      })
    getUser(profileId)
      .catch((err) => {
        console.error(err)
      })
      .then((response) => {
        setUser(response.data)
      })
    setIsLoading(false)
  }, [profileId])

  return (
    <>
      <UserActivityHeader profileId={profileId} title="Commentaires" />
      {!isLoading ? comments.map((comment) => (
        <TouchableOpacity key={comment.id} style={styles.commentContainer} onPress={() => { router.push(`/post/${comment.postId}`) }}>
          <ThemedText type="defaultBold" style={{ color: comment.voteSum >= 0 ? Colors.common.upvote : Colors.common.downvote }}>
            {comment.voteSum > 0 ? "+" : ""}{comment.voteSum}
          </ThemedText>
          <ThemedText>{comment.content}</ThemedText>
        </TouchableOpacity>
      )) : (<Loading />)}
    </>
  )
}

const styles = StyleSheet.create({
  commentContainer: {
    borderWidth: 1,
    borderColor: Colors.common.button,
    margin: 5,
    padding: 10,
    borderRadius: 4,
    cursor: "pointer",
  },

  repliedTo: {
    position: "relative",
    top: 5,
    display: "flex",
    flexDirection: "row",
    alignItems: "center",
  },

  repliedToText: {
    fontSize: 12,
    marginLeft: 4,
    lineHeight: 12,
  },
})
