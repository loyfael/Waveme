import { Colors } from "@/constants/Colors";
import { useThemeColor } from "@/hooks/useThemeColor";
import React from "react";
import { TextInput, TextInputProps, StyleSheet, Platform } from "react-native";

export type ThemedTextInputProps = TextInputProps & {
  lightColor?: string;
  darkColor?: string;
}

// Generic input type text
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
      style,
      // Ignore this error, it's lying to your face (code works as intended)
      Platform.OS === 'web' ? ({ outlineStyle: 'none' } as any) : {}
    ]}
      autoCorrect={false}
      placeholderTextColor={Colors.common.placeholderTextColor}
      returnKeyType="done"
      blurOnSubmit={true}
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
