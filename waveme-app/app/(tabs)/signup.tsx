import { InvalidFieldTooltip } from "@/components/InvalidFieldTooltip";
import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { Colors } from "@/constants/Colors";
import { useWebTitle } from "@/hooks/useWebTitle";
import { signup } from "@/services/AuthAPI";
import { InvalidTooltip, SignupCredentials } from "@/types";
import { getMissingFields } from "@/utils/formChecks";
import { validate } from "email-validator";
import { useRouter } from "expo-router";
import React, { useState } from "react";
import { Pressable, StyleSheet, View, Text } from 'react-native'

export default function Signup() {
  useWebTitle('Inscription - Waveme')

  const router = useRouter()

  const [credentials, setCredentials] = useState<SignupCredentials>({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  })
  const [invalidTooltip, setInvalidTooltip] = useState<InvalidTooltip>({ display: false, field: '', message: '' })

  const handleSubmit = async (): Promise<void> => {
    const missingFields = getMissingFields(credentials)
    // Form validation
    if (missingFields.length) {
      setInvalidTooltip({ display: true, field: missingFields[0], message: 'Vous devez remplir ce champ.' })
      return
    }
    if (!validate(credentials.email)) {
      setInvalidTooltip({ display: true, field: 'email', message: 'Adresse email non valide.' })
      return
    }
    if (credentials.password !== credentials.confirmPassword) {
      setInvalidTooltip({ display: true, field: 'confirmPassword', message: 'Le mot de passe n\'est pas identique.' })
      return
    }

    const response = await signup(credentials).catch((error) => console.log(error))
    console.log(response);
  }

  const handleSignup = () => { }

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
        <InvalidFieldTooltip field="email" tooltip={invalidTooltip} setTooltip={setInvalidTooltip}>
          <ThemedTextInput
            value={credentials.email}
            onChangeText={(value) => { setCredentials({ ...credentials, email: value }) }}
            autoComplete="email"
            placeholder="Adresse email"
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
      <View style={styles.connectionField}>
        <InvalidFieldTooltip field="confirmPassword" tooltip={invalidTooltip} setTooltip={setInvalidTooltip}>
          <ThemedTextInput
            value={credentials.confirmPassword}
            onChangeText={(value) => { setCredentials({ ...credentials, confirmPassword: value }) }}
            autoComplete="password"
            placeholder="Confirmer le mot de passe"
            secureTextEntry
            style={styles.connectionField}
          />
        </InvalidFieldTooltip>
      </View>
      <Pressable onPress={() => { router.push('/login') }}>
        <ThemedText type="link">Déjà un compte ? Cliquez ici !</ThemedText>
      </Pressable>
      <Pressable style={styles.loginButton} onPress={handleSubmit}>
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
