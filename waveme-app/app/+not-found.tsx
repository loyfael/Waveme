import { Stack, useRouter } from 'expo-router';
import { Pressable, StyleSheet } from 'react-native';

import { ThemedText } from '@/components/theme/ThemedText';
import { ThemedView } from '@/components/theme/ThemedView';
import React from 'react';
import { genericButtonStyle } from '@/constants/commonStyles';

export default function NotFoundScreen() {
  const router = useRouter()

  return (
    <>
      <Stack.Screen options={{ headerShown: false }} />
      <ThemedView style={styles.container}>
        <ThemedText type="title">Cette page n'existe pas</ThemedText>
        <Pressable style={styles.genericButton} onPress={() => { router.push('/') }}>
          <ThemedText style={styles.genericButtonText}>Retour à la page d'accueil</ThemedText>
        </Pressable>
      </ThemedView>
    </>
  );
}

const notFoundStyle = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
    gap: 50,
  },
});

const styles = { ...notFoundStyle, ...genericButtonStyle }
