import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import { useRouter } from "expo-router";
import React, { useState } from "react";
import { Pressable, StyleSheet, View } from "react-native";

export default function Login() {
  useWebTitle('Connexion - Waveme')

  const router = useRouter()

  type LoginCredentials = {
    username: string,
    password: string,
  }

  const [credentials, setCredentials] = useState<LoginCredentials>({ username: '', password: '' })

  const handleLogin = (): void => {
    // TODO: Request to back
  }

  return (
    <>
      <View style={styles.connectionField}>
        <ThemedTextInput
          value={credentials.username}
          onChangeText={(value) => { setCredentials({ ...credentials, username: value }) }}
          autoComplete="username"
          placeholder="Pseudo"
          autoFocus
          style={styles.connectionField}
        />
      </View>
      <View style={styles.connectionField}>
        <ThemedTextInput
          value={credentials.password}
          onChangeText={(value) => { setCredentials({ ...credentials, password: value }) }}
          autoComplete="password"
          placeholder="Mot de passe"
          secureTextEntry
          style={styles.connectionField}
        />
      </View>
      <Pressable onPress={() => { router.push('/signup') }}>
        <ThemedText type="link">Pas de compte ? Cliquez ici !</ThemedText>
      </Pressable>
      <Pressable style={styles.loginButton} onPress={handleLogin}>
        <ThemedText type="defaultBold" style={styles.loginButtonText}>CONNEXION</ThemedText>
      </Pressable>
    </>
  )
}

const styles = StyleSheet.create({
  connectionField: {
    marginTop: 15,
    width: 250,
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
