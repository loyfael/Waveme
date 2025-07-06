import React, { useContext, useEffect, useState } from "react"
import { Animated, Modal, Pressable, StyleSheet, Switch, TouchableOpacity, View, Image } from "react-native"
import { ThemedView } from "./theme/ThemedView"
import { AuthContext } from "@/context/AuthContext"
import { MaterialIcons, Ionicons } from "@expo/vector-icons"
import { ThemedText } from "./theme/ThemedText"
import { redirectFromModal } from "@/utils/modals"
import dayjs from "dayjs"
import { genericButtonStyle, modalContainerStyle } from "@/constants/commonStyles"
import { logout } from "@/services/AuthAPI"
import { useRouter } from "expo-router"
import { Colors } from "@/constants/Colors"
import { useAnimatedButton } from "@/hooks/useAnimatedButton"
import { ThemeContext } from "@/context/ThemeContext"
import { useThemeColor } from "@/hooks/useThemeColor"
import { fadeButtonToClicked, fadeButtonToIdle } from "@/utils/animateButton"
import { setProfileImage } from "@/services/UserAPI"
import { createLocalUriFromBackUri, pickImage } from "@/utils/api"
import { useResponsive } from "@/hooks/useResponsive"

type ProfileModalProps = {
  visible: boolean,
  setVisible: Function,
}

export default function ProfileModal(props: ProfileModalProps) {
  const [image, setImage] = useState<any>(null)
  const [loadedProfilePicture, setLoadedProfilePicture] = useState<string>("")

  const { user, setUser, reloadUser } = useContext(AuthContext)
  const { isDarkMode, setDarkMode } = useContext(ThemeContext)
  const textColor = useThemeColor({}, 'text')
  const iconColor = useThemeColor({}, "icon")
  const backgroundColor = useThemeColor({}, 'background')
  const router = useRouter()
  const { isSmallScreen, isVerySmallScreen } = useResponsive()

  const AnimatedButton = Animated.createAnimatedComponent(Pressable)

  const { animatedButton: logoutButton, backgroundColor: logoutBackgroundColor } = useAnimatedButton({
    idleColor: backgroundColor,
    clickedColor: Colors.common.genericButtonPressed
  })

  const handleChangeProfilePicture = async () => {
    await pickImage(image, setImage)
  }

  const handleLogout = async () => {
    await logout()
    reloadUser()
    router.push("/login")
  }

  useEffect(() => {
    if (user && image) {
      setProfileImage(user.id.toString(), image.file)
        .catch((err) => {
          console.error(err)
        })
        .then((response) => {
          setUser({ ...user, profileImg: response.data })
          setImage(null)
        })
    }
  }, [image])

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
    <Modal visible={props.visible} transparent animationType="fade" onRequestClose={() => props.setVisible(false)}>
      <Pressable style={{ ...styles.centeredModalView, ...styles.modalCursorOverride }} onPress={() => props.setVisible(false)}>
        <Pressable style={styles.modalCursorOverride}>
          {user ? (
            <ThemedView
              style={isSmallScreen
                ? isVerySmallScreen
                  ? { ...styles.modalViewSmallScreen, ...styles.connectedModalSizeTinyScreen }
                  : { ...styles.modalViewSmallScreen, ...styles.connectedModalSizeSmallScreen }
                : { ...styles.modalView, ...styles.connectedModalSize }}
            >
              <Pressable onPress={() => { handleChangeProfilePicture() }}>
                {loadedProfilePicture ? (
                  <Image source={{ uri: loadedProfilePicture }} style={styles.userPfp} />
                ) : (
                  <MaterialIcons name="account-circle" size={150} color={iconColor} style={styles.userPfp} />
                )}
                <View style={styles.editPfpButton}>
                  <Ionicons name="pencil" color="white" size={12} />
                </View>
              </Pressable>
              <Pressable onPress={() => { redirectFromModal(`/user/${user.id}`, props.setVisible) }}>
                <ThemedText
                  type='title'
                  style={isSmallScreen
                    ? { ...styles.userNameSmallScreen, borderBottomColor: textColor }
                    : { ...styles.userName, borderBottomColor: textColor }}
                >
                  {user.pseudo}
                </ThemedText>
              </Pressable>
              <View style={isSmallScreen ? isVerySmallScreen ? styles.optionsTinyScreen : styles.optionsSmallScreen : styles.options}>
                <ThemedText>Date de création : {dayjs(user.createdAt).format("DD/MM/YYYY")}</ThemedText>
                <ThemedText>Total d'upvotes : {user.totalUpVote}</ThemedText>
                <ThemedText>Nombre de posts : {user.totalPosts}</ThemedText>
                <ThemedText>Nombre de commentaires : {user.totalComments}</ThemedText>
                <View style={styles.switch}>
                  <Switch value={isDarkMode} onValueChange={setDarkMode} />
                  <ThemedText style={styles.switchLabel}>Mode sombre</ThemedText>
                </View>
              </View>
              <AnimatedButton
                onPressIn={() => fadeButtonToClicked(logoutButton)}
                onPressOut={() => fadeButtonToIdle(logoutButton, 250)}
                onPress={handleLogout}
                style={isSmallScreen
                  ? { ...styles.logoutSmallScreen, backgroundColor: logoutBackgroundColor }
                  : { ...styles.logout, backgroundColor: logoutBackgroundColor }}
              >
                <MaterialIcons name="logout" size={36} color={textColor} />
              </AnimatedButton>
            </ThemedView>
          ) : (
            <ThemedView style={{ ...styles.modalView, ...styles.disconnectedModalSize }}>
              <View style={styles.connection}>
                <ThemedText type="subtitle">Envie de poster ?</ThemedText>
                <TouchableOpacity style={styles.genericButton} onPress={() => { redirectFromModal("/login", props.setVisible) }}>
                  <ThemedText type="defaultBold" style={styles.genericButtonText}>Se connecter</ThemedText>
                </TouchableOpacity>
                <TouchableOpacity style={styles.genericButton} onPress={() => { redirectFromModal("/signup", props.setVisible) }}>
                  <ThemedText type="defaultBold" style={styles.genericButtonText}>S'inscrire</ThemedText>
                </TouchableOpacity>
                <View style={styles.switch}>
                  <Switch value={isDarkMode} onValueChange={setDarkMode} />
                  <ThemedText style={styles.switchLabel}>Mode sombre</ThemedText>
                </View>
              </View>
            </ThemedView>
          )}
        </Pressable>
      </Pressable>
    </Modal>
  )
}

const localStyles = StyleSheet.create({
  modalView: {
    paddingVertical: 40,
    paddingHorizontal: 80,
    opacity: 0.97,
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: 20,
  },

  modalViewSmallScreen: {
    paddingVertical: 30,
    paddingHorizontal: 40,
    opacity: 0.97,
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: 20,
  },

  connectedModalSize: {
    width: 700,
    height: 650,
  },

  connectedModalSizeSmallScreen: {
    width: 450,
    minHeight: 600,
  },

  connectedModalSizeTinyScreen: {
    width: 300,
    minHeight: 600,
  },

  disconnectedModalSize: {
    width: 500,
    height: 300,
  },

  userPfp: {
    width: 150,
    height: 150,
    borderRadius: 75,
    overflow: 'hidden',
  },

  editPfpButton: {
    position: 'absolute',
    left: 128,
    top: 128,
    padding: 5,
    borderRadius: 10,
    backgroundColor: Colors.common.button,
  },

  userName: {
    marginTop: 35,
    borderBottomWidth: 1,
    width: 350,
    textAlign: 'center',
    paddingBottom: 15,
  },

  userNameSmallScreen: {
    marginTop: 35,
    borderBottomWidth: 1,
    width: 250,
    textAlign: 'center',
    paddingBottom: 15,
  },

  options: {
    flexDirection: 'column',
    alignSelf: 'flex-start',
    alignItems: 'flex-start',
    marginLeft: 90,
    marginTop: 20,
  },

  optionsSmallScreen: {
    flexDirection: 'column',
    alignSelf: 'flex-start',
    alignItems: 'flex-start',
    marginLeft: 45,
    marginTop: 20,
  },

  optionsTinyScreen: {
    flexDirection: 'column',
    alignSelf: 'flex-start',
    alignItems: 'flex-start',
    marginTop: 20,
  },

  switch: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 5,
  },

  switchLabel: {
    marginStart: 8,
  },

  connection: {
    display: "flex",
    alignItems: "center",
    marginTop: 40,
    gap: 10,
  },

  logout: {
    alignSelf: 'flex-end',
    marginTop: 'auto',
    position: 'relative',
    left: 40,
    padding: 5,
    borderRadius: 26,
  },

  logoutSmallScreen: {
    alignSelf: 'flex-end',
    marginTop: 'auto',
    position: 'relative',
    left: 20,
    padding: 5,
    borderRadius: 26,
  },
})

const styles = { ...localStyles, ...modalContainerStyle, ...genericButtonStyle }
