import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import { useRouter } from "expo-router";
import React, { useState } from "react";
import { Pressable, StyleSheet, View } from 'react-native'

export default function Signup() {
  useWebTitle('Inscription - Waveme')

  const router = useRouter()

  type SignupCredentials = {
    username: string,
    email: string,
    password: string,
    confirmPassword: string,
  }

  const [credentials, setCredentials] = useState<SignupCredentials>({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  })

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
          value={credentials.email}
          onChangeText={(value) => { setCredentials({ ...credentials, email: value }) }}
          autoComplete="email"
          placeholder="Adresse email"
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
      <View style={styles.connectionField}>
        <ThemedTextInput
          value={credentials.confirmPassword}
          onChangeText={(value) => { setCredentials({ ...credentials, confirmPassword: value }) }}
          autoComplete="password"
          placeholder="Confirmer le mot de passe"
          secureTextEntry
          style={styles.connectionField}
        />
      </View>
      <Pressable onPress={() => { router.push('/login') }}>
        <ThemedText type="link">Déjà un compte ? Cliquez ici !</ThemedText>
      </Pressable>
      <Pressable style={styles.loginButton}>
        <ThemedText type="defaultBold" style={styles.loginButtonText}>INSCRIPTION</ThemedText>
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
