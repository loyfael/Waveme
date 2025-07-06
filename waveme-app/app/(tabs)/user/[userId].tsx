import { Loading } from "@/components/Loading";
import { ThemedText } from "@/components/theme/ThemedText";
import { Colors } from "@/constants/Colors";
import { useResponsive } from "@/hooks/useResponsive";
import { useThemeColor } from "@/hooks/useThemeColor";
import { useWebTitle } from "@/hooks/useWebTitle";
import { getUser } from "@/services/UserAPI";
import { SimpleComment, SimplePost } from "@/types";
import { createLocalUriFromBackUri } from "@/utils/api";
import { MaterialIcons } from "@expo/vector-icons";
import dayjs from "dayjs";
import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import { StyleSheet, View, Image, Pressable, ImageSourcePropType, TouchableOpacity } from "react-native";

type User = {
  id: number,
  pseudo: string,
  profileImg: string | null,
  totalUpVote: number,
  totalPosts: number,
  totalComments: number,
  createdAt: string,
  updatedAt: string,
  latestPosts: SimplePost[],
  latestComments: SimpleComment[],
}

export default function UserScreen() {
  const [user, setUser] = useState<User | null>()
  const [loadedProfilePicture, setLoadedProfilePicture] = useState<string>("")

  useWebTitle(user?.pseudo ?? "Utilisateur")
  const router = useRouter()
  const { userId } = useLocalSearchParams()
  const { isSmallScreen } = useResponsive()
  const areaBackgroundColor = useThemeColor({}, "areaBackground")
  const iconColor = useThemeColor({}, "icon")

  useEffect(() => {
    const handleFetchUser = async () => {
      try {
        const response = await getUser(userId as string);
        if (response && response.data) {
          setUser(response.data);
        }
      } catch (err) {
        console.error('Error fetching user:', err);
        setUser(null); // Set to null to show error state
      }
    }
    handleFetchUser()
  }, [userId])

  useEffect(() => {
    if (user && user.profileImg) {
      const fetchProfilePicture = async () => {
        const dataUri = await createLocalUriFromBackUri(user.profileImg as string, "profile")
        if (dataUri) {
          setLoadedProfilePicture(dataUri)
        }
      }
      fetchProfilePicture()
    }
  }, [user])

  return (
    <>
      {user === undefined ? (
        <Loading />
      ) : user === null ? (
        <View style={styles.errorWrapper}>
          <ThemedText type="title">Utilisateur introuvable</ThemedText>
          <ThemedText>L'utilisateur que vous cherchez n'existe pas ou n'est plus disponible.</ThemedText>
        </View>
      ) : (
        <>
          <View style={styles.userWrapper}>
            {user?.profileImg ? (
              <Image source={{ uri: loadedProfilePicture }} style={styles.userPfp} />
            ) : (
              <MaterialIcons name="account-circle" size={200} color={iconColor} style={styles.userPfp} />
            )}
            <ThemedText type="title">{user.pseudo}</ThemedText>
            <ThemedText>Date de création : {dayjs(user.createdAt).format("DD/MM/YYYY")}</ThemedText>
            <ThemedText>Dernière activité : {dayjs(user.updatedAt).format("DD/MM/YYYY")}</ThemedText>
            <ThemedText>Score : {user.totalUpVote}</ThemedText>
            <ThemedText>Nombre de posts : {user.totalPosts}</ThemedText>
            <ThemedText>Nombre de commentaires : {user.totalComments}</ThemedText>
          </View>
          <View style={isSmallScreen ? styles.activityWrapperSmallScreen : styles.activityWrapper}>
            <Pressable
              style={isSmallScreen
                ? { ...styles.activityBlockSmallScreen, borderColor: areaBackgroundColor }
                : { ...styles.activityBlock, borderColor: areaBackgroundColor }}
              onPress={() => { router.push(`/user/${userId}/posts`) }}
            >
              <ThemedText type="subtitle">Derniers posts</ThemedText>
              {user?.latestPosts ? (
                <>
                  <View style={styles.latestActivityList}>
                    {user.latestPosts.map((post) => (
                      <TouchableOpacity key={post.id} onPress={() => { router.push(`/post/${post.postUniqueId}`) }} style={styles.latestPost}>
                        <ThemedText>
                          {post.description}
                        </ThemedText>
                        <ThemedText type="defaultBold" style={{ color: post.voteSum >= 0 ? Colors.common.upvote : Colors.common.downvote }}>
                          {post.voteSum > 0 ? "+" : ""}{post.voteSum}
                        </ThemedText>
                      </TouchableOpacity>
                    ))}
                  </View>
                  <ThemedText>Voir plus...</ThemedText>
                </>
              ) : (<ThemedText>Cet utilisateur n'a rien posté.</ThemedText>)}
            </Pressable>
            <Pressable
              style={isSmallScreen
                ? { ...styles.activityBlockSmallScreen, borderColor: areaBackgroundColor }
                : { ...styles.activityBlock, borderColor: areaBackgroundColor }}
              onPress={() => { router.push(`/user/${userId}/comments`) }}
            >
              <ThemedText type="subtitle">Derniers commentaires</ThemedText>
              {user?.latestComments ? (
                <>
                  <View style={styles.latestActivityList}>
                    {user.latestComments.map((comment) => (
                      <TouchableOpacity key={comment.id} onPress={() => { router.push(`/post/${comment.postId}`) }} style={styles.latestPost}>
                        <ThemedText>
                          {comment.content}
                        </ThemedText>
                        <ThemedText type="defaultBold" style={{ color: comment.voteSum >= 0 ? Colors.common.upvote : Colors.common.downvote }}>
                          {comment.voteSum > 0 ? "+" : ""}{comment.voteSum}
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
      )}
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

  errorWrapper: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    paddingTop: 80,
    marginBottom: 50,
    padding: 20,
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
