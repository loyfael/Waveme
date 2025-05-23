import { Loading } from "@/components/Loading";
import { ThemedText } from "@/components/theme/ThemedText";
import { Colors } from "@/constants/Colors";
import { useThemeColor } from "@/hooks/useThemeColor";
import { useWebTitle } from "@/hooks/useWebTitle";
import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import { StyleSheet, View, Image, Pressable, ImageSourcePropType, TouchableOpacity } from "react-native";
import { useMediaQuery } from "react-responsive";

export default function UserScreen() {
  useWebTitle('user')

  const [user, setUser] = useState<User | null>()

  type SimplePost = {
    id: number,
    title: string | null,
    upvotes: number,
  }

  type SimpleComment = {
    id: number,
    message: string,
    upvotes: number,
  }

  type User = {
    name: string,
    pfp: ImageSourcePropType | undefined,
    upvotes: number,
    latestPosts: SimplePost[],
    latestComments: SimpleComment[],
  }

  const router = useRouter()
  const { userId } = useLocalSearchParams()
  const isSmallScreen = useMediaQuery({ query: '(max-width: 1200px)' })
  const areaBackgroundColor = useThemeColor({}, "areaBackground")

  useEffect(() => {
    // TODO: API call    
    setUser({
      name: "Beuteu34",
      pfp: require('@/assets/images/pfp.png'),
      upvotes: 50,
      latestPosts: [{
        id: 34,
        title: "meme",
        upvotes: 47,
      },
      {
        id: 35,
        title: "meme (nul)",
        upvotes: -512,
      }],
      latestComments: [{
        id: 111,
        message: "ouais c'est marrant",
        upvotes: 3,
      }],
    })
  }, [userId])

  return (
    <>
      {user ? (
        <>
          <View style={styles.userWrapper}>
            <Image source={user.pfp} style={styles.userPfp} />
            <ThemedText type="title">{user.name}</ThemedText>
            <ThemedText>Score : {user.upvotes}</ThemedText>
          </View>
          <View style={isSmallScreen ? styles.activityWrapperSmallScreen : styles.activityWrapper}>
            {/* Don't mind that error, the linter must get confused by dynamic values */}
            {/* If you don't see an error, ignore these comments as well */}
            <Pressable style={{ ...styles.activityBlock, borderColor: areaBackgroundColor }} onPress={() => { router.push(`/user/${userId}/posts`) }}>
              <ThemedText type="subtitle">Derniers posts</ThemedText>
              {typeof user === "object" ? (
                <>
                  <View style={styles.latestActivityList}>
                    {user.latestPosts.map((post) => (
                      <TouchableOpacity key={post.id} onPress={() => { router.push(`/post/${"11"/*TODO: Replace with postId*/}`) }} style={styles.latestPost}>
                        <ThemedText>
                          {post.title}
                        </ThemedText>
                        <ThemedText type="defaultBold" style={{ color: post.upvotes > 0 ? Colors.common.upvote : Colors.common.downvote }}>
                          {post.upvotes > 0 ? "+" : ""}{post.upvotes}
                        </ThemedText>
                      </TouchableOpacity>
                    ))}
                  </View>
                  <ThemedText>Voir plus...</ThemedText>
                </>
              ) : (<ThemedText>Cet utilisateur n'a rien posté.</ThemedText>)}
            </Pressable>
            {/* Same here */}
            <Pressable style={{ ...styles.activityBlock, borderColor: areaBackgroundColor }} onPress={() => { router.push(`/user/${userId}/comments`) }}>
              <ThemedText type="subtitle">Derniers commentaires</ThemedText>
              {typeof user === "object" ? (
                <>
                  <View style={styles.latestActivityList}>
                    {user.latestComments.map((comment) => (
                      <TouchableOpacity key={comment.id} onPress={() => { router.push(`/post/${"11"/*TODO: Replace with postId*/}`) }} style={styles.latestPost}>
                        <ThemedText>
                          {comment.message}
                        </ThemedText>
                        <ThemedText type="defaultBold" style={{ color: comment.upvotes >= 0 ? Colors.common.upvote : Colors.common.downvote }}>
                          {comment.upvotes > 0 ? "+" : ""}{comment.upvotes}
                        </ThemedText>
                      </TouchableOpacity>
                    ))}
                  </View>
                  <ThemedText>Voir plus...</ThemedText>
                </>
              ) : (<ThemedText>Cet utilisateur n'a pas commenté.</ThemedText>)}
            </Pressable>
          </View>
        </>
      ) : (<Loading />)}
    </>
  )
}

const localStyles = StyleSheet.create({
  userWrapper: {
    display: "flex",
    alignItems: "center",
    paddingTop: 80,
    marginBottom: 50,
  },

  userPfp: {
    width: 200,
    height: 200,
    borderRadius: 100,
    marginBottom: 20,
  },

  activityWrapper: {
    display: "flex",
    flexDirection: "row",
  },

  activityWrapperSmallScreen: {
    display: "flex",
    flexDirection: "column",
  },

  activityBlock: {
    flex: 1,
    display: "flex",
    alignItems: "center",
    margin: 15,
    borderWidth: 1,
    borderRadius: 5,
    padding: 10,
  },

  activityBlockSmallScreen: {
    display: "flex",
    alignItems: "center",
    margin: 15,
    borderWidth: 1,
    borderRadius: 5,
    padding: 10,
  },

  latestActivityList: {
    width: "100%",
  },

  latestPost: {
    display: "flex",
    alignItems: "center",
    borderWidth: 1,
    borderColor: Colors.common.button,
    margin: 5,
    padding: 10,
    flex: 1,
    borderRadius: 4,
    cursor: "pointer",
  },
})

const styles = { ...localStyles }
