import React from 'react';
import { StyleSheet, Image } from 'react-native';
import { useColorScheme } from '@/hooks/useColorScheme';
import { Slot } from 'expo-router';
import { ThemedView } from '@/components/ThemedView';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';

export default function TabLayout() {
  const colorScheme = useColorScheme();

  return (
    <ThemedView style={styles.wrapper}>
      <ThemedView style={styles.leftColumn}>
        <Image source={require('@/assets/images/waveme.png')} style={styles.logo} />
      </ThemedView>
      <ThemedView style={styles.main}>
        <Slot />
      </ThemedView>
      <ThemedView style={styles.rightColumn}>
        <MaterialIcons name="account-circle" size={70} color="black" style={styles.account} />
      </ThemedView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
  },

  leftColumn: {
    flex: 1,
    marginTop: 50,
    marginStart: 50,
  },

  logo: {
    width: 90,
    height: 90,
  },

  rightColumn: {
    flex: 1,
    alignItems: 'flex-end',
    marginTop: 35,
    marginEnd: 35,
  },

  // Unused but will probably be used soon
  account: {
    width: 70,
    height: 70,
  },

  main: {
    flex: 5,
  },
})
