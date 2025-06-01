import { InvalidFieldTooltip } from "@/components/InvalidFieldTooltip";
import { ThemedText } from "@/components/theme/ThemedText";
import { ThemedTextInput } from "@/components/theme/ThemedTextInput";
import { authStyle, genericButtonStyle } from "@/constants/commonStyles";
import { AuthContext } from "@/context/AuthContext";
import { useWebTitle } from "@/hooks/useWebTitle";
import { authenticate } from "@/services/AuthAPI";
import { InvalidTooltip, LoginCredentials } from "@/types";
import { getMissingFields } from "@/utils/formChecks";
import { useRouter } from "expo-router";
import React, { useContext, useState } from "react";
import { Pressable, TouchableOpacity, View } from "react-native";

export default function Login() {
  const [credentials, setCredentials] = useState<LoginCredentials>({ pseudo: '', password: '' })
  const [invalidTooltip, setInvalidTooltip] = useState<InvalidTooltip>({ display: false, field: '', message: '' })
  const [invalidMessage, setInvalidMessage] = useState("")
  
  useWebTitle('Connexion')
  const router = useRouter()
  const { reloadUser } = useContext(AuthContext)

  const handleLogin = async () => {
    const missingFields = getMissingFields(credentials)
    if (missingFields.length) {
      setInvalidTooltip({ display: true, field: missingFields[0], message: 'Vous devez remplir ce champ.' })
      return
    }

    await authenticate(credentials)
      .then(() => {
        reloadUser()
        router.push('/')
      })
      .catch((error) => setInvalidMessage(error.data.message))
  }

  return (
    <>
      <View style={styles.connectionField}>
        <InvalidFieldTooltip field="username" tooltip={invalidTooltip} setTooltip={setInvalidTooltip}>
          <ThemedTextInput
            value={credentials.pseudo}
            onChangeText={(value) => { setCredentials({ ...credentials, pseudo: value }) }}
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
      <TouchableOpacity style={styles.genericButton} onPress={handleLogin}>
        <ThemedText type="defaultBold" style={styles.genericButtonText}>CONNEXION</ThemedText>
      </TouchableOpacity>
      {invalidMessage && (
        <ThemedText style={styles.invalidMessage}>{invalidMessage}</ThemedText>
      )}
    </>
  )
}

const styles = { ...authStyle, ...genericButtonStyle }
