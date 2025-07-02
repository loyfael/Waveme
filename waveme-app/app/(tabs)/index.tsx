import { ThemedText } from "@/components/theme/ThemedText";
import { genericButtonStyle } from "@/constants/commonStyles";
import { AuthContext } from "@/context/AuthContext";
import { useWebTitle } from "@/hooks/useWebTitle";
import { useRouter } from "expo-router";
import React, { useContext, useEffect } from "react";
import { View, Image, StyleSheet, TouchableOpacity } from "react-native";

export default function HomeScreen() {
  useWebTitle("Bienvenue")
  const router = useRouter()
  const { user } = useContext(AuthContext)

  // Redirection automatique si l'utilisateur est déjà connecté
  useEffect(() => {
    if (user) {
      router.replace('/feed')
    }
  }, [user, router])

  return (
    <>
      <Image source={require('@/assets/images/waveme.png')} style={styles.homeLogo} />
      <View style={styles.homeTextContainer}>
        <ThemedText type="title">WAVEME</ThemedText>
        <ThemedText>Une place pour les créateurs de memes</ThemedText>
        <TouchableOpacity style={styles.genericButton} onPress={() => { router.push("/login") }}>
          <ThemedText type="defaultBold" style={styles.genericButtonText}>NOUS REJOINDRE</ThemedText>
        </TouchableOpacity>
      </View>
    </>
  )
}

const localStyles = StyleSheet.create({
  homeLogo: {
    width: 240,
    height: 240,
  },

  homeTextContainer: {
    alignItems: "center",
    gap: 10,
  },
})

const styles = { ...localStyles, ...genericButtonStyle }
