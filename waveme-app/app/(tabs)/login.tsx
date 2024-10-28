import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import React from "react";
import { Pressable, StyleSheet, View } from "react-native";

export default function Login() {
  useWebTitle('Connexion - Waveme')

  return (
    <>
      <View style={styles.connectionField}>
        <ThemedTextInput
          autoComplete="username"
          placeholder="Pseudo"
          autoFocus
          style={styles.connectionField}
        />
      </View>
      <View style={styles.connectionField}>
        <ThemedTextInput
          autoComplete="password"
          placeholder="Mot de passe"
          secureTextEntry
          style={styles.connectionField}
        />
      </View>
      <ThemedText type="link">Pas de compte ? Cliquez ici !</ThemedText>
      <Pressable style={styles.loginButton}>
        <ThemedText type="defaultBold" style={styles.loginButtonText}>CONNEXION</ThemedText>
      </Pressable>
    </>
  )
}

const styles = StyleSheet.create({
  connectionField: {
    marginTop: 15,
  },

  loginButton: {
    backgroundColor: Colors.common.button,
    paddingHorizontal: 25,
    paddingVertical: 8,
    borderRadius: 20,
    marginTop: 5,
  },

  loginButtonText: {
    color: 'white',
  },
})
