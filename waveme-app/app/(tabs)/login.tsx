import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { useWebTitle } from "@/hooks/useWebTitle";
import React from "react";
import { StyleSheet, View } from "react-native";

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
    </>
  )
}

const styles = StyleSheet.create({
  connectionField: {
    marginTop: 15,
  },
})
