import React, { useContext, useEffect, useState } from 'react';
import { StyleSheet, Image, ScrollView, Pressable, Modal, ImageSourcePropType, View, Switch, Animated, TouchableOpacity } from 'react-native';
import { Slot, usePathname, useRouter } from 'expo-router';
import { ThemedView } from '@/components/theme/ThemedView';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { PencilFill } from 'react-bootstrap-icons';
import { Colors } from '@/constants/Colors';
import { ThemedText } from '@/components/theme/ThemedText';
import { useThemeColor } from '@/hooks/useThemeColor';
import { ThemeContext } from '@/context/ThemeContext';
import { fadeButtonToClicked, fadeButtonToIdle } from '@/utils/animateButton';
import { useAnimatedButton } from '@/hooks/useAnimatedButton';
import { genericButtonStyle, modalContainerStyle } from '@/constants/commonStyles';
import { AuthContext } from '@/context/AuthContext';
import { logout } from '@/services/AuthAPI';
import { redirectFromModal } from '@/utils/modals';
import { Ionicons } from '@expo/vector-icons';

export default function TabLayout() {
  const [showProfileModal, setShowProfileModal] = useState(false)

  const { isDarkMode, setDarkMode } = useContext(ThemeContext)
  const textColor = useThemeColor({}, 'text')
  const backgroundColor = useThemeColor({}, 'background')
  const { user, reloadUser } = useContext(AuthContext)
  const router = useRouter()
  const pathname = usePathname()
  const connectionRoutes = ['/login', '/signup']

  const handleLogout = async () => {
    await logout()
    reloadUser()
  }

  const AnimatedButton = Animated.createAnimatedComponent(Pressable)

  const { animatedButton: logoutButton, backgroundColor: logoutBackgroundColor } = useAnimatedButton({
    idleColor: backgroundColor,
    clickedColor: Colors.common.genericButtonPressed
  })

  if (connectionRoutes.includes(pathname)) {
    return (
      <ThemedView style={styles.loginWrapper}>
        <TouchableOpacity style={styles.backToHomepageButton} onPress={() => router.push("/")}>
          <Ionicons name="arrow-back-outline" size={32} color={textColor} />
          <ThemedText type="defaultBold">Retourner à la page d'accueil</ThemedText>
        </TouchableOpacity>
        <Image source={require('@/assets/images/waveme.png')} style={styles.loginLogo} />
        <Slot />
      </ThemedView>
    )
  }
  return (
    <ThemedView style={styles.wrapper}>
      <ThemedView style={styles.leftColumn}>
        <Pressable style={styles.logo} onPress={() => { router.push("/") }}>
          <Image source={require('@/assets/images/waveme.png')} style={styles.logo} />
        </Pressable>
      </ThemedView>
      <ScrollView style={styles.main} showsVerticalScrollIndicator={false}>
        <Slot />
      </ScrollView>
      <ThemedView style={styles.rightColumn}>
        <TouchableOpacity onPress={() => setShowProfileModal(true)}>
          {user?.profileImg ? (
            <Image source={{ uri: user?.profileImg }} style={styles.account} />
          ) : (
            <MaterialIcons name="account-circle" size={70} color="black" style={styles.account} />
          )}
        </TouchableOpacity>
      </ThemedView>

      {/* Profile modal */}
      <Modal visible={showProfileModal} transparent animationType="fade" onRequestClose={() => setShowProfileModal(false)}>
        <Pressable style={{ ...styles.centeredModalView, ...styles.modalCursorOverride }} onPress={() => setShowProfileModal(false)}>
          <Pressable style={styles.modalCursorOverride}>
            {user ? (
              <ThemedView style={{ ...styles.modalView, ...styles.connectedModalSize }}>
                <Pressable onPress={() => { }}>
                  {user.profileImg ? (
                    <Image source={{ uri: user.profileImg }} style={styles.userPfp} />
                  ) : (
                    <MaterialIcons name="account-circle" size={150} color="black" style={styles.userPfp} />
                  )}
                  <View style={styles.editPfpButton}>
                    <PencilFill color="white" size={12} />
                  </View>
                </Pressable>
                <ThemedText type='title' style={{ ...styles.userName, borderBottomColor: textColor }}>
                  {user.pseudo}
                </ThemedText>
                <View style={styles.options}>
                  <ThemedText>Total d'upvotes : {user.totalUpvotes}</ThemedText>
                  <ThemedText>Nombre de posts : {user.totalPosts}</ThemedText>
                  <View style={styles.switch}>
                    <Switch value={isDarkMode} onValueChange={setDarkMode} />
                    <ThemedText style={styles.switchLabel}>Mode sombre</ThemedText>
                  </View>
                </View>
                <AnimatedButton
                  onPressIn={() => fadeButtonToClicked(logoutButton)}
                  onPressOut={() => fadeButtonToIdle(logoutButton, 250)}
                  onPress={handleLogout}
                  style={{ ...styles.logout, backgroundColor: logoutBackgroundColor }}>
                  <MaterialIcons name="logout" size={36} color={textColor} />
                </AnimatedButton>
              </ThemedView>
            ) : (
              <ThemedView style={{ ...styles.modalView, ...styles.disconnectedModalSize }}>
                <View style={styles.connection}>
                  <ThemedText type="subtitle">Envie de poster ?</ThemedText>
                  <TouchableOpacity style={styles.genericButton} onPress={() => { redirectFromModal("/login", setShowProfileModal) }}>
                    <ThemedText type="defaultBold" style={styles.genericButtonText}>Se connecter</ThemedText>
                  </TouchableOpacity>
                  <TouchableOpacity style={styles.genericButton} onPress={() => { redirectFromModal("/signup", setShowProfileModal) }}>
                    <ThemedText type="defaultBold" style={styles.genericButtonText}>S'inscrire</ThemedText>
                  </TouchableOpacity>
                </View>
              </ThemedView>
            )}
          </Pressable>
        </Pressable>
      </Modal>
    </ThemedView>
  );
}

const localStyles = StyleSheet.create({
  wrapper: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
  },

  loginWrapper: {
    flex: 1,
    flexDirection: 'column',
    alignItems: 'center',
  },

  backToHomepageButton: {
    display: "flex",
    flexDirection: "row",
    alignItems: "center",
    position: "absolute",
    top: 80,
    left: 60,
  },

  leftColumn: {
    flex: 1,
    marginTop: 50,
    marginStart: 50,
  },

  logo: {
    width: 90,
    height: 90,
  },

  loginLogo: {
    width: 200,
    height: 200,
    marginTop: 80,
    marginBottom: 50,
  },

  rightColumn: {
    flex: 1,
    alignItems: 'flex-end',
    marginTop: 35,
    marginEnd: 35,
  },

  account: {
    width: 70,
    height: 70,
    borderRadius: 35,
    overflow: 'hidden',
  },

  main: {
    flex: 6,
    flexGrow: 6,
  },

  modalView: {
    paddingVertical: 40,
    paddingHorizontal: 80,
    opacity: 0.97,
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: 20,
  },

  connectedModalSize: {
    width: 700,
    height: 650,
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

  options: {
    flexDirection: 'column',
    alignSelf: 'flex-start',
    alignItems: 'flex-start',
    marginStart: 90,
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
})

const styles = { ...localStyles, ...modalContainerStyle, ...genericButtonStyle }
