import { useThemeColor } from "@/hooks/useThemeColor";
import React from "react";
import { TextInput, TextInputProps, StyleSheet } from "react-native";

export type ThemedTextInputProps = TextInputProps & {
  lightColor?: string;
  darkColor?: string;
}

export function ThemedTextInput({
  style,
  lightColor,
  darkColor,
  ...rest
}: ThemedTextInputProps) {
  const backgroundColor = useThemeColor({ light: lightColor, dark: darkColor }, 'areaBackground');

  return (
    <TextInput style={[
      { backgroundColor },
      styles.themedTextInput,
    ]}
    autoCorrect={false}
    placeholderTextColor="#666"
    {...rest}
    />
  )
}

const styles = StyleSheet.create({
  themedTextInput: {
    paddingHorizontal: 15,
    paddingVertical: 12,
    borderRadius: 15,
    fontSize: 16,
  },
})
