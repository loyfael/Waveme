import { Ionicons } from "@expo/vector-icons"
import React from "react"
import { Pressable, View, StyleSheet } from "react-native"
import { ThemedText } from "./theme/ThemedText"
import { useRouter } from "expo-router"
import { useThemeColor } from "@/hooks/useThemeColor"

type UserActivityHeaderProps = {
  profileId: string
}

export default function UserActivityHeader(props: UserActivityHeaderProps) {
  const router = useRouter()

  const textColor = useThemeColor({}, "text")

  return (
    <View style={styles.userPostsWrapper}>
      <View style={styles.userPostsHeader}>
        <Pressable style={styles.goBackButton} onPress={() => { router.push(`/user/${props.profileId}`) }}>
          <Ionicons name="arrow-back-outline" size={32} color={textColor} />
          <ThemedText type="defaultBold">Retour</ThemedText>
        </Pressable>
        <ThemedText type="title">Posts de user</ThemedText>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  userPostsHeader: {
    display: "flex",
    flexDirection: "row",
    justifyContent: "center",
    width: "100%",
    flex: 1,
  },

  goBackButton: {
    display: "flex",
    flexDirection: "row",
    position: "absolute",
    left: 0,
    alignSelf: "center",
    alignItems: "center",
  },

  userPostsWrapper: {
    display: "flex",
    alignItems: "center",
    marginTop: 40,
  },
})
