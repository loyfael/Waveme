import React from "react";
import { StyleSheet } from "react-native"
import { ThemedText } from "./theme/ThemedText";

export function Loading() {
  return (<ThemedText type="title" style={styles.loading}>Chargement...</ThemedText>)
}

export const styles = StyleSheet.create({
  loading: {
    textAlign: 'center',
    marginTop: 75,
  },
})
