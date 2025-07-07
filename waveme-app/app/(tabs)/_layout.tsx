import React, { useContext, useEffect, useState } from 'react';
import { StyleSheet, Image, ScrollView, Pressable, TouchableOpacity } from 'react-native';
import { Slot, usePathname, useRouter } from 'expo-router';
import { ThemedView } from '@/components/theme/ThemedView';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { ThemedText } from '@/components/theme/ThemedText';
import { useThemeColor } from '@/hooks/useThemeColor';
import { genericButtonStyle, modalContainerStyle } from '@/constants/commonStyles';
import { AuthContext } from '@/context/AuthContext';
import { Ionicons } from '@expo/vector-icons';
import { createLocalUriFromBackUri } from '@/utils/api';
import ProfileModal from '@/components/ProfileModal';
import { useResponsive } from '@/hooks/useResponsive';

export default function TabLayout() {
  const [showProfileModal, setShowProfileModal] = useState(false)
  const [loadedProfilePicture, setLoadedProfilePicture] = useState<string>("")

  const textColor = useThemeColor({}, 'text')
  const iconColor = useThemeColor({}, "icon")
  const { user } = useContext(AuthContext)
  const router = useRouter()
  const pathname = usePathname()
  const { isSmallScreen, isVerySmallScreen } = useResponsive()
  const connectionRoutes = ['/login', '/signup']

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

  if (pathname === '/') {
    return (
      <ThemedView style={isVerySmallScreen ? styles.homeWrapperSmallScreen : styles.homeWrapper}>
        <Image
          source={require("@/assets/images/homeLines.svg")}
          tintColor={textColor}
          style={isVerySmallScreen ? styles.homeLinesSmallScreen : styles.homeLines}
        />
        <Slot />
      </ThemedView>
    )
  }
  if (connectionRoutes.includes(pathname)) {
    return (
      <ThemedView style={styles.loginWrapper}>
        <TouchableOpacity style={isSmallScreen ? styles.backToHomepageButtonSmallScreen : styles.backToHomepageButton} onPress={() => router.push("/")}>
          <Ionicons name="arrow-back-outline" size={32} color={textColor} />
          <ThemedText type="defaultBold">{isVerySmallScreen ? "Retour à l'accueil" : "Retourner à la page d'accueil"}</ThemedText>
        </TouchableOpacity>
        <Image source={require('@/assets/images/waveme.png')} style={isSmallScreen ? styles.loginLogoSmallScreen : styles.loginLogo} />
        <Slot />
      </ThemedView>
    )
  }
  return (
    <ThemedView style={styles.wrapper}>
      <ThemedView
        style={isSmallScreen ? isVerySmallScreen ? styles.leftColumnTinyScreen : styles.leftColumnSmallScreen : styles.leftColumn}
      >
        <Pressable
          style={isSmallScreen ? isVerySmallScreen ? styles.logoTinyScreen : styles.logoSmallScreen : styles.logo}
          onPress={() => { router.push("/feed") }}
        >
          <Image
            source={require('@/assets/images/waveme.png')}
            style={isSmallScreen ? isVerySmallScreen ? styles.logoTinyScreen : styles.logoSmallScreen : styles.logo}
          />
        </Pressable>
      </ThemedView>
      <ScrollView style={styles.main} showsVerticalScrollIndicator={false}>
        <Slot />
      </ScrollView>
      <ThemedView
        style={isSmallScreen ? isVerySmallScreen ? styles.rightColumnTinyScreen : styles.rightColumnSmallScreen : styles.rightColumn}
      >
        <TouchableOpacity onPress={() => setShowProfileModal(true)}>
          {loadedProfilePicture ? (
            <Image
              source={{ uri: loadedProfilePicture }}
              style={isSmallScreen ? isVerySmallScreen ? styles.accountTinyScreen : styles.accountSmallScreen : styles.account}
            />
          ) : (
            <MaterialIcons
              name="account-circle"
              size={isSmallScreen ? isVerySmallScreen ? 36 : 50 : 70}
              color={iconColor}
              style={isSmallScreen ? isVerySmallScreen ? styles.accountTinyScreen : styles.accountSmallScreen : styles.account}
            />
          )}
        </TouchableOpacity>
      </ThemedView>

      <ProfileModal
        visible={showProfileModal}
        setVisible={setShowProfileModal}
      />
    </ThemedView>
  );
}

const localStyles = StyleSheet.create({
  wrapper: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
  },

  homeWrapper: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 50,
  },

  homeWrapperSmallScreen: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    gap: 20,
  },

  homeLines: {
    position: "absolute",
    width: "100%",
    height: "100%",
  },

  // NOTE: This SVG won't show up on the actual mobile version for limitation reasons and I can't be bothered fixing it
  homeLinesSmallScreen: {
    position: "absolute",
    height: "100%",
    width: "100%",
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

  backToHomepageButtonSmallScreen: {
    display: "flex",
    flexDirection: "row",
    alignItems: "center",
    position: "absolute",
    top: 60,
    left: 40,
  },

  leftColumn: {
    flex: 1,
    marginTop: 50,
    marginLeft: 50,
  },

  leftColumnSmallScreen: {
    flex: 1,
    marginBottom: 50,
    marginLeft: 15,
    justifyContent: "flex-end"
  },

  leftColumnTinyScreen: {
    flex: 1,
    marginBottom: 50,
    marginLeft: 5,
    justifyContent: "flex-end",
  },

  logo: {
    width: 90,
    height: 90,
  },

  logoSmallScreen: {
    width: 50,
    height: 50,
  },

  logoTinyScreen: {
    width: 36,
    height: 36,
  },

  loginLogo: {
    width: 200,
    height: 200,
    marginTop: 80,
    marginBottom: 50,
  },

  loginLogoSmallScreen: {
    width: 160,
    height: 160,
    marginTop: 100,
    marginBottom: 10,
  },

  rightColumn: {
    flex: 1,
    alignItems: 'flex-end',
    marginTop: 35,
    marginRight: 35,
  },

  rightColumnSmallScreen: {
    flex: 1,
    alignItems: "flex-end",
    justifyContent: "flex-end",
    marginBottom: 50,
    marginRight: 15,
  },

  rightColumnTinyScreen: {
    flex: 1,
    alignItems: "flex-end",
    justifyContent: "flex-end",
    marginBottom: 50,
    marginRight: 5,
  },

  account: {
    width: 70,
    height: 70,
    borderRadius: 35,
    overflow: 'hidden',
  },

  accountSmallScreen: {
    width: 50,
    height: 50,
    borderRadius: 25,
    overflow: "hidden",
  },

  accountTinyScreen: {
    width: 36,
    height: 36,
    borderRadius: 18,
    overflow: "hidden",
  },

  main: {
    flex: 6,
    flexGrow: 6,
  },
})

const styles = { ...localStyles, ...modalContainerStyle, ...genericButtonStyle }
