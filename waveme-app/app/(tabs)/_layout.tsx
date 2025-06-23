import React, { useContext, useEffect, useState } from 'react';
import { StyleSheet, Image, ScrollView, Pressable, TouchableOpacity } from 'react-native';
import { Slot, usePathname, useRouter } from 'expo-router';
import { ThemedView } from '@/components/theme/ThemedView';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { Colors } from '@/constants/Colors';
import { ThemedText } from '@/components/theme/ThemedText';
import { useThemeColor } from '@/hooks/useThemeColor';
import { genericButtonStyle, modalContainerStyle } from '@/constants/commonStyles';
import { AuthContext } from '@/context/AuthContext';
import { Ionicons } from '@expo/vector-icons';
import { useMediaQuery } from 'react-responsive';
import { createLocalUriFromBackUri } from '@/utils/api';
import ProfileModal from '@/components/ProfileModal';

export default function TabLayout() {
  const [showProfileModal, setShowProfileModal] = useState(false)
  const [loadedProfilePicture, setLoadedProfilePicture] = useState<string>("")

  const textColor = useThemeColor({}, 'text')
  const iconColor = useThemeColor({}, "icon")
  const { user } = useContext(AuthContext)
  const router = useRouter()
  const pathname = usePathname()
  const isSmallScreen = useMediaQuery({ query: '(max-width: 1200px)' })
  const connectionRoutes = ['/login', '/signup']

  useEffect(() => {
    if (user && user.profileImg) {
      const fetchProfilePicture = async () => {
        const dataUri = await createLocalUriFromBackUri(user.profileImg as string, "profile")
        setLoadedProfilePicture(dataUri)
      }
      fetchProfilePicture()
    }
  }, [user])

  if (connectionRoutes.includes(pathname)) {
    return (
      <ThemedView style={styles.loginWrapper}>
        <TouchableOpacity style={styles.backToHomepageButton} onPress={() => router.push("/")}>
          <Ionicons name="arrow-back-outline" size={32} color={textColor} />
          <ThemedText type="defaultBold">{isSmallScreen ? "Retour à l'accueil" : "Retourner à la page d'accueil"}</ThemedText>
        </TouchableOpacity>
        <Image source={require('@/assets/images/waveme.png')} style={styles.loginLogo} />
        <Slot />
      </ThemedView>
    )
  }
  return (
    <ThemedView style={styles.wrapper}>
      <ThemedView style={isSmallScreen ? styles.leftColumnSmallScreen : styles.leftColumn}>
        <Pressable style={isSmallScreen ? styles.logoSmallScreen : styles.logo} onPress={() => { router.push("/") }}>
          <Image source={require('@/assets/images/waveme.png')} style={isSmallScreen ? styles.logoSmallScreen : styles.logo} />
        </Pressable>
      </ThemedView>
      <ScrollView style={styles.main} showsVerticalScrollIndicator={false}>
        <Slot />
      </ScrollView>
      <ThemedView style={isSmallScreen ? styles.rightColumnSmallScreen : styles.rightColumn}>
        <TouchableOpacity onPress={() => setShowProfileModal(true)}>
          {loadedProfilePicture ? (
            <Image source={{ uri: loadedProfilePicture }} style={styles.account} />
          ) : (
            <MaterialIcons name="account-circle" size={70} color={iconColor} style={styles.account} />
          )}
        </TouchableOpacity>
      </ThemedView>

      <ProfileModal
        visible={showProfileModal}
        setVisible={setShowProfileModal}
        loadedProfilePicture={loadedProfilePicture}
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
    marginLeft: 50,
  },

  leftColumnSmallScreen: {
    flex: 1,
    marginBottom: 30,
    marginLeft: 30,
    justifyContent: "flex-end"
  },

  logo: {
    width: 90,
    height: 90,
  },

  logoSmallScreen: {
    width: 75,
    height: 75,
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

  rightColumnSmallScreen: {
    flex: 1,
    alignItems: "flex-end",
    justifyContent: "flex-end",
    marginBottom: 30,
    marginEnd: 30,
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
})

const styles = { ...localStyles, ...modalContainerStyle, ...genericButtonStyle }
