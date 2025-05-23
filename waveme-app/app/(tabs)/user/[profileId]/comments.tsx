import { Loading } from "@/components/Loading";
import UserActivityHeader from "@/components/UserActivityHeader";
import { ThemedText } from "@/components/theme/ThemedText";
import { Colors } from "@/constants/Colors";
import { useThemeColor } from "@/hooks/useThemeColor";
import { useWebTitle } from "@/hooks/useWebTitle";
import { Comment } from "@/types";
import { Octicons } from "@expo/vector-icons";
import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import { View, StyleSheet } from "react-native";
import { TouchableOpacity } from "react-native-gesture-handler";

export default function Comments() {
  useWebTitle("Tous les commentaires de user")

  const router = useRouter()
  const { profileId } = useLocalSearchParams<{ profileId: string }>()
  const [comments, setComments] = useState<Comment[]>([])
  const [isLoading, setIsLoading] = useState(true)

  const textColor = useThemeColor({}, "text")

  useEffect(() => {
    setIsLoading(true)
    // TODO: API call
    setComments([
      {
        id: 1,
        message: "J'avoue",
        user: {
          id: 2,
          userName: "heyeah",
          userPfp: require('@/assets/images/pfp.png'),
        },
        hasReplies: true,
        replies: [],
        upvotes: 12,
      },
      {
        id: 2,
        message: "J'avoue pas",
        user: {
          id: 3,
          userName: "heyeah2",
          userPfp: require('@/assets/images/pfp.png'),
        },
        hasReplies: false,
        replies: [],
        upvotes: -5,
      },
      {
        id: 3,
        message: "J'avoue peut-être",
        user: {
          id: 4,
          userName: "heyeah3",
          userPfp: require('@/assets/images/pfp.png'),
        },
        hasReplies: true,
        replies: [
          {
            id: 1,
            message: "Tu as tort",
            user: {
              id: 5,
              userName: "hater",
              userPfp: require('@/assets/images/pfp.png'),
            },
          },
          {
            id: 2,
            message: "Tu as taure",
            user: {
              id: 6,
              userName: "hater2",
              userPfp: require('@/assets/images/pfp.png'),
            },
          }
        ],
        upvotes: 0,
      }
    ])
    setIsLoading(false)
  }, [profileId])

  return (
    <>
      <UserActivityHeader profileId={profileId} title="Commentaires" />
      {!isLoading ? comments.map((comment) => (
        <TouchableOpacity key={comment.id} style={styles.commentContainer} onPress={() => {router.push(`/post/${"11"/*TODO: Replace with postId*/}`)}}>
          <ThemedText type="defaultBold" style={{ color: comment.upvotes >= 0 ? Colors.common.upvote : Colors.common.downvote }}>
            {comment.upvotes > 0 ? "+" : ""}{comment.upvotes}
          </ThemedText>
          {!comment.hasReplies && (
            <View style={styles.repliedTo}>
              <Octicons name="reply" size={12} color={textColor} />
              <ThemedText style={styles.repliedToText}>En réponse à {/*TODO: Display original comment or original commenter*/}</ThemedText>
            </View>
          )}
          <ThemedText>{comment.message}</ThemedText>
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
