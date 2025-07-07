import { Ionicons } from "@expo/vector-icons"
import React from "react"
import { View, StyleSheet, TouchableOpacity } from "react-native"
import { ThemedText } from "./theme/ThemedText"
import { useRouter } from "expo-router"
import { useThemeColor } from "@/hooks/useThemeColor"
import { useResponsive } from "@/hooks/useResponsive"

type UserActivityHeaderProps = {
  profileId: string
  title: string
}

export default function UserActivityHeader(props: UserActivityHeaderProps) {

  const router = useRouter()
  const textColor = useThemeColor({}, "text")
  const { isSmallScreen } = useResponsive()

  return (
    <View style={styles.userPostsWrapper}>
      <View style={isSmallScreen ? styles.usePostsHeaderSmallScreen : styles.userPostsHeader}>
        <TouchableOpacity style={isSmallScreen ? styles.goBackButtonSmallScreen : styles.goBackButton} onPress={() => { router.push(`/user/${props.profileId}`) }}>
          <Ionicons name="arrow-back-outline" size={32} color={textColor} />
          <ThemedText type="defaultBold">Retour</ThemedText>
        </TouchableOpacity>
        <ThemedText type="title">{props.title} de user</ThemedText>
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

  usePostsHeaderSmallScreen: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    width: "100%",
  },

  goBackButton: {
    display: "flex",
    flexDirection: "row",
    position: "absolute",
    left: 0,
    alignSelf: "center",
    alignItems: "center",
  },

  goBackButtonSmallScreen: {
    display: "flex",
    flexDirection: "row",
    alignSelf: "flex-start",
    alignItems: "center",
  },

  userPostsWrapper: {
    display: "flex",
    alignItems: "center",
    marginTop: 40,
  },
})
