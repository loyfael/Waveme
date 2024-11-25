import { InvalidFieldTooltip } from "@/components/InvalidFieldTooltip";
import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import { authenticate } from "@/services/AuthAPI";
import { InvalidTooltip, LoginCredentials } from "@/types";
import { getMissingFields } from "@/utils/formChecks";
import { useRouter } from "expo-router";
import React, { useState } from "react";
import { Pressable, StyleSheet, View } from "react-native";

export default function Login() {
  useWebTitle('Connexion - Waveme')

  const router = useRouter()

  const [credentials, setCredentials] = useState<LoginCredentials>({ username: '', password: '' })
  const [invalidTooltip, setInvalidTooltip] = useState<InvalidTooltip>({ display: false, field: '', message: '' })

  const handleLogin = (): void => {
    const missingFields = getMissingFields(credentials)
    if (missingFields.length) {
      setInvalidTooltip({ display: true, field: missingFields[0], message: 'Vous devez remplir ce champ.' })
      return
    }

    const response = authenticate(credentials)
  }

  return (
    <>
      <View style={styles.connectionField}>
        <InvalidFieldTooltip field="username" tooltip={invalidTooltip} setTooltip={setInvalidTooltip}>
          <ThemedTextInput
            value={credentials.username}
            onChangeText={(value) => { setCredentials({ ...credentials, username: value }) }}
            autoComplete="username"
            placeholder="Pseudo"
            autoFocus
            style={styles.connectionField}
          />
        </InvalidFieldTooltip>
      </View>
      <View style={styles.connectionField}>
        <InvalidFieldTooltip field="password" tooltip={invalidTooltip} setTooltip={setInvalidTooltip}>
          <ThemedTextInput
            value={credentials.password}
            onChangeText={(value) => { setCredentials({ ...credentials, password: value }) }}
            autoComplete="password"
            placeholder="Mot de passe"
            secureTextEntry
            style={styles.connectionField}
          />
        </InvalidFieldTooltip>
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
